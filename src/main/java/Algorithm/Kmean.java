/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import QueryDB.SimpleQuery;
import QueryDB.getData.Comparation;
import QueryDB.getData.Vector;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hanh Nguyen
 */
public class Kmean {
//Number of Clusters. This metric should be related to the number of points

    private final int NUM_CLUSTERS = 3;
    private List<Point> points;
    private final List<Cluster> clusters;

    public Kmean() {
        this.points = new ArrayList();
        this.clusters = new ArrayList();
    }

    public void init(Cursor cursor) {

        points = Point.getPoints(cursor);

        //Create Clusters
        //Set Centroids
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = points.get(i);
            cluster.setCentroid(centroid);
            clusters.add(cluster);

        }

        plotClusters();
        System.out.println("----------------------------------");
        System.out.println("init");
        System.out.println("----------------------------------");
    }

    private void plotClusters() {
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster c = clusters.get(i);
            c.plotCluster();
        }
    } //ok!

    public void calculate() {
        boolean finish = false;
        int iteration = 0;

        // Add in new data, one at a time, recalculating centroids with each new one. 
        while (!finish) {
            //Clear cluster state
            clearClusters();

            List<Point> lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateCentroids();

            iteration++;

            List<Point> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            float distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance = Point.distance(lastCentroids.get(i), currentCentroids.get(i));

                System.out.println("distance" + distance);
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);

            plotClusters();

            if (distance == 4) {
                finish = true;
                // creating table centroid
                CollectionCentroid db = new CollectionCentroid();
                db.insertCentroid(clusters);
            }
        }

        plotClusters();
    }

    private void clearClusters() {
        clusters.forEach((cluster) -> {
            cluster.clear();
        });
    } //ok!

    private List getCentroids() {
        List centroids = new ArrayList(NUM_CLUSTERS);
        for (Cluster cluster : clusters) {
            Point aux = cluster.getCentroid();
            Point point = new Point(aux.getGoal(), aux.getTask(), aux.getQuality(), aux.getResource());
            centroids.add(point);
        }
        return centroids;
    }//ok!

    private void assignCluster() {
        float max;

        int cluster = 0;
        float distance = 0;

        for (Point point : points) {
            int count = 0;
            max = -8;
            for (int i = 0; i < NUM_CLUSTERS; i++) {
                Cluster c = clusters.get(i);
                System.out.println("point" + "[" + count + "]:" + point + "\n");
                System.out.println("centroid" + "[" + i + "]:" + c.getCentroid() + "\n");

                distance = Point.distance(point, c.getCentroid());

                System.out.println("distance" + "[" + i + "]:" + distance);
                if (distance >= max) {
                    max = distance;
                    cluster = i;

                    System.out.println("max:" + max + "\n");
                }

            }

            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);

        }
        plotClusters();

    }

    private void calculateCentroids() {
        Comparation sml = new Comparation();
        List emp = new ArrayList();
        for (Cluster cluster : clusters) {
            List<Point> list = cluster.getPoints();
            Representation r = new Representation();
            emp = r.represent(list, sml);

            Point centroid = cluster.getCentroid();

            centroid.setGoal(emp.get(0).toString());
            centroid.setTask(emp.get(1).toString());
            centroid.setQuality(emp.get(2).toString());
            centroid.setResource(emp.get(3).toString());
        }

    }

    public static void main(String[] args) {
        Vector vector = new Vector();
        vector.createCollectionVector();

        Cursor cursor = vector.loadModel(MyConstants.VECTOR_COLLECTION_NAME);
        Kmean k = new Kmean();
        k.init(cursor);

        k.calculate();

       
    }
}

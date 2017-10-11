/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
import QueryDB.getData.Comparation;
import QueryDB.getData.Vector;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Kmean {
//Number of Clusters. This metric should be related to the number of points

    private final int NUM_CLUSTERS_MEANING = 3;

    private List<Point> meaning_points;

    private final List<Cluster> meaning_clusters;

    public Kmean() {
        this.meaning_points = new ArrayList();
        this.meaning_clusters = new ArrayList();
    }

    public Point chooseCentroid(List<Point> points, List<Cluster> clusters, Comparation sml, int id) {
        Point centroid = null;
        float min = Float.MAX_VALUE;

        if (clusters.isEmpty()) {

            centroid = meaning_points.get(id);
            return centroid;

        }
int j = 1;
        for (Point point : points) {
            int i = 1;
            System.out.println("point " + j + ":" + j);
            float sum = 0;
            for (Cluster cluster : clusters) {
                sum += sml.compare(cluster.getMeaningCentroid().getGoal(), point.getGoal()) + sml.compare(cluster.getMeaningCentroid().getQuality(), point.getQuality())
                        + sml.compare(cluster.getMeaningCentroid().getResource(), point.getResource()) + sml.compare(cluster.getMeaningCentroid().getTask(), point.getTask());
                System.out.println("sum " + i + ": "+ sum);
                i++;
                
            }
            if (sum < min) {
                    min = sum;
                    
                    System.out.println("min " + min);
                    centroid = point;
                }
            j++;
        }

        return centroid;
    }
//init meaning points
    Comparation sml = new Comparation();

    public void initMeaningPoints(Cursor cursor) {

        meaning_points = Point.getMeaningPoints(cursor);

        //Create Clusters
        //Set Centroids
        for (int i = 0; i < NUM_CLUSTERS_MEANING; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = chooseCentroid(meaning_points, meaning_clusters, sml, i);
            cluster.setMeaningCentroid(centroid);
            meaning_clusters.add(cluster);

        }

        plotMeaningClusters();
        System.out.println("----------------------------------");
        System.out.println("init");
        System.out.println("----------------------------------");
    }

    private void plotMeaningClusters() {
        for (int i = 0; i < NUM_CLUSTERS_MEANING; i++) {
            Cluster c = meaning_clusters.get(i);
            c.plotMeaningCluster();
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
            calculateMeaningCentroids();

            plotMeaningClusters();
            iteration++;

            List<Point> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            float distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += Point.distanceMeaning(lastCentroids.get(i), currentCentroids.get(i));

                System.out.println("distance" + distance);
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);

            if (distance == lastCentroids.size() * 4) {
                finish = true;
                // creating table centroid
                CollectionCentroid db = new CollectionCentroid();
                db.insertMeaningCentroid(meaning_clusters);
            }
        }

        plotMeaningClusters();
    }

    private void clearClusters() {
        meaning_clusters.forEach((cluster) -> {
            cluster.clearMeaning();
        });
    } //ok!

    private List getCentroids() {
        List centroids = new ArrayList(NUM_CLUSTERS_MEANING);
        for (Cluster cluster : meaning_clusters) {
            Point aux = cluster.getMeaningCentroid();
            Point point = new Point(aux.getGoal(), aux.getTask(), aux.getQuality(), aux.getResource());
            centroids.add(point);
        }
        return centroids;
    }//ok!

    private void assignCluster() {
        float max;

        int cluster = 0;
        float distance = 0;
        int count = 0;
        for (Point point : meaning_points) {

            max = -4;
            for (int i = 0; i < NUM_CLUSTERS_MEANING; i++) {
                Cluster c = meaning_clusters.get(i);
                System.out.println("point" + "[" + count + "]:" + point + "\n");
                System.out.println("centroid" + "[" + i + "]:" + c.getMeaningCentroid() + "\n");

                distance = Point.distanceMeaning(point, c.getMeaningCentroid());

                System.out.println("distance" + "[" + i + "]:" + distance);
                if (distance >= max) {
                    max = distance;
                    cluster = i;

                    System.out.println("max:" + max + "\n");
                }

            }
            count++;
            point.setCluster(cluster);
            meaning_clusters.get(cluster).addMeaningPoints(point);

        }
        plotMeaningClusters();

    }

    private void calculateMeaningCentroids() {
        Comparation sml = new Comparation();
        List emp = new ArrayList();

        int i = 0;
        for (Cluster cluster : meaning_clusters) {
            List<Point> list = cluster.getMeaningPoints();

            System.out.println("listPoint-before----------- " + i + list);
            Representation r = new Representation();
            emp = r.represent(list, sml);

            cluster.setMeaningCentroid(new Point(emp.get(0).toString(), emp.get(1).toString(), emp.get(2).toString(), emp.get(3).toString()));

            System.out.println("centroi----------- " + cluster.meaning_centroid);
            System.out.println("listPoint-mid----------- " + i + list);

            System.out.println("listPoint-after----------- " + i + list);
            i++;
        }

    }

    public void insertMeaning_id(List<Cluster> meaning_clusters) {
        ConnectionDB connect = new ConnectionDB();
        DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);

        int j = 0;
        for (Cluster cluster : meaning_clusters) {

            for (int i = 0; i < cluster.getMeaningPoints().size(); i++) {
                j++;
                BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
                whereVector.push("meaning_vector");
                whereVector.add("goal", cluster.meaning_points.get(i).getGoal());
                whereVector.add("task", cluster.meaning_points.get(i).getTask());
                whereVector.add("quality", cluster.meaning_points.get(i).getQuality());
                whereVector.add("resource", cluster.meaning_points.get(i).getResource());

                //insert field meaning_id into vector collection
//                BasicDBObject obj = (BasicDBObject) vector.find(whereVector.get()).next();
//                vector.update(obj, obj.append("meaning_id", cluster.getMeaningId() + 1));
                DBCursor cursor = vector.find(whereVector.get());

                while (cursor.hasNext()) {
                    BasicDBObject obj = (BasicDBObject) cursor.next();
                    obj.append("meaning_id", cluster.getMeaningId() + 1);
                    vector.save(obj);
                }

                System.out.println("update " + j);

//                
                System.out.println("-------------------------");
            }
        }
    }

// clustering level 1 based on meaning
    public void execute1() {
        Vector vector = new Vector();
        vector.createCollectionVector();

        Cursor cursorMeaning = vector.loadModel(MyConstants.VECTOR_COLLECTION_NAME);

        initMeaningPoints(cursorMeaning);
        calculate();

        insertMeaning_id(meaning_clusters);
    }

    public static void main(String[] args) {
        Kmean k = new Kmean();
        k.execute1();
//
//        ConnectionDB connect = new ConnectionDB();
//        DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);
//
//        FrequencyKMean a = new FrequencyKMean();
//        a.execute2(k.meaning_clusters, vector);
    }
}

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
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Kmean {
//Number of Clusters. This metric should be related to the number of points

    private final int NUM_CLUSTERS = 4;
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

            plotClusters();
            iteration++;

            List<Point> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            float distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += Point.distance(lastCentroids.get(i), currentCentroids.get(i));

                System.out.println("distance" + distance);
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);

            if (distance == lastCentroids.size() * 4) {
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
        int count = 0;
        for (Point point : points) {

            max = -4;
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
            count++;
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);

        }
        plotClusters();

    }

    private void calculateCentroids() {
        Comparation sml = new Comparation();
        List emp = new ArrayList();

        int i = 0;
        for (Cluster cluster : clusters) {
            List<Point> list = cluster.getPoints();

            System.out.println("listPoint-before----------- " + i + list);
            Representation r = new Representation();
            emp = r.represent(list, sml);

            cluster.setCentroid(new Point(emp.get(0).toString(), emp.get(1).toString(), emp.get(2).toString(), emp.get(3).toString()));

            System.out.println("centroi----------- " + cluster.centroid);
            System.out.println("listPoint-mid----------- " + i + list);

            System.out.println("listPoint-after----------- " + i + list);
            i++;
        }

    }

   

    public void insertClusterCol(List<Cluster> clusters) {
        ConnectionDB connect = new ConnectionDB();
        DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);
        DBCollection model = connect.connect(MyConstants.ORIGINAL_MODEL_NAME);
        DBCollection clusterCol = connect.connect(MyConstants.CLUSTER_COLLECTION_NAME);

        int j = 0;
        for (Cluster cluster : clusters) {
            j++;
            System.out.println("----------------------- " + j);

            for (int i = 0; i < cluster.getPoints().size(); i++) {
                BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
                BasicDBObjectBuilder whereModel = BasicDBObjectBuilder.start();

                whereVector.push("meaning_vector");
                whereVector.add("goal", cluster.points.get(i).getGoal());
                whereVector.add("task", cluster.points.get(i).getTask());
                whereVector.add("quality", cluster.points.get(i).getQuality());
                whereVector.add("resource", cluster.points.get(i).getResource());

                DBCursor cursor1 = vector.find(whereVector.get());

                whereModel.add("id_model", cursor1.next().get("id_model"));

                DBCursor cursor2 = model.find(whereModel.get());

                while (cursor2.hasNext()) {
                    BasicDBObject values = (BasicDBObject) cursor2.next();
                    values.append("cluster_id", cluster.getId() + 1);

                    clusterCol.insert(values);
                }

            }

        }

//        while (cursorVector.hasNext()) {
//            DBObject dbObj = cursorVector.next();
//
//            for (Cluster cluster : clusters) {
//                for (int i = 0; i < cluster.getPoints().size(); i++) {
//                    BasicDBObject point = new BasicDBObject();
//                    point.append("goal", cluster.points.get(i).getGoal()).append("task", cluster.points.get(i).getTask())
//                            .append("quality", cluster.points.get(i).getQuality()).append("resource", cluster.points.get(i).getResource());
//                    if (point.equals(dbObj.get("vector"))) {
//                        System.out.println("trued");
//                        System.out.println("---------------------------");
//                        System.out.println("db_id_model" + dbObj.get("id_model"));
//                    }
//                }
//            }
//
//        }
    }

    public void execute() {
        insertClusterCol(clusters);
    }

    public static void main(String[] args) {
        Vector vector = new Vector();
        vector.createCollectionVector();

        Cursor cursor = vector.loadModel(MyConstants.VECTOR_COLLECTION_NAME);
        Kmean k = new Kmean();
        k.init(cursor);
        k.calculate();

        k.execute();
    }
}

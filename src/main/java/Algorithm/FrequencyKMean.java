/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
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
public class FrequencyKMean {

    private final int NUM_CLUSTERS_FREQUENCY = 2;
    private List<Point> frequency_points;
    public final List<Cluster> frequency_clusters;

    public FrequencyKMean() {
        this.frequency_clusters = new ArrayList();
        this.frequency_points = new ArrayList();
    }
    
    
    // init frequency points
    private void initFrequencyPoints(Cursor cursor) {
        frequency_points = Point.getFrequencyPoints(cursor);

        frequency_clusters.clear();
        for (int i = 0; i < NUM_CLUSTERS_FREQUENCY; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = frequency_points.get(i);

            cluster.setFrequencyCentroid(centroid);
            frequency_clusters.add(cluster);
        }

    }

    private void calculate() {
        boolean finish = false;
        int iteration = 0;
        while (!finish) {
            //Clear cluster state
            clearClusters();

            List<Point> lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateFrequencyCentroids();

            plotFrequencyClusters();
            iteration++;

            List<Point> currentCentroids = getCentroids();

            double distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += Point.distanceFrequency(lastCentroids.get(i), currentCentroids.get(i));

                System.out.println("distance" + distance);
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);

            if (distance == 0) {
                finish = true;

            }
        }
        plotFrequencyClusters();
    }

    private List getCentroids() {
        List centroids = new ArrayList(NUM_CLUSTERS_FREQUENCY);
        for (Cluster cluster : frequency_clusters) {
            Point aux = cluster.getFrequencyCentroid();
            Point point = new Point(aux.getNumGoal(), aux.getNumTask(), aux.getNumQuality(), aux.getNumResource(), aux.getNumLink());
            centroids.add(point);
        }
        return centroids;
    }

    private void clearClusters() {
        frequency_clusters.forEach((cluster) -> {
            cluster.clearFrequency();
        });
    } //ok!

    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for (Point point : frequency_points) {
            min = max;
            for (int i = 0; i < NUM_CLUSTERS_FREQUENCY; i++) {
                Cluster c = frequency_clusters.get(i);
                distance = Point.distanceFrequency(point, c.getFrequencyCentroid());
                if (distance < min) {
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            frequency_clusters.get(cluster).addFrequencyPoints(point);
        }
    }

    private void calculateFrequencyCentroids() {
        for (Cluster cluster : frequency_clusters) {

            double SumGoal = 0;
            double SumTask = 0;
            double SumQuality = 0;
            double SumResource = 0;
            double SumLink = 0;

            List<Point> list = cluster.getFrequencyPoints();

            int n_points = list.size();

            for (Point p : list) {
                SumGoal += p.getNumGoal();
                SumTask += p.getNumTask();
                SumQuality += p.getNumQuality();
                SumResource += p.getNumResource();
                SumLink += p.getNumLink();
            }

            if (n_points > 0) {
                double newGoal = SumGoal / n_points;
                double newTask = SumTask / n_points;
                double newQuality = SumQuality / n_points;
                double newResource = SumResource / n_points;
                double newLink = SumLink / n_points;

                cluster.setFrequencyCentroid(new Point(newGoal, newTask, newQuality, newResource, newLink));

            }
            System.out.println("List============ " + list);
        }

    }

    private void plotFrequencyClusters() {
        for (int i = 0; i < NUM_CLUSTERS_FREQUENCY; i++) {
            Cluster c = frequency_clusters.get(i);
            c.plotFrequencyCluster();
        }
    } //ok!

    public void insertFrequency_id(List<Cluster> frequency_clusters, Cluster meaning_cluster) {
        ConnectionDB connect = new ConnectionDB();
        DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);

        for (Cluster cluster : frequency_clusters) {
            BasicDBObjectBuilder whereVector1 = BasicDBObjectBuilder.start();
            whereVector1.add("meaning_id", meaning_cluster.meaning_id + 1);
            for (int i = 0; i < cluster.getFrequencyPoints().size(); i++) {

                BasicDBObjectBuilder whereVector2 = BasicDBObjectBuilder.start();
                
                whereVector2.add("numGoal", cluster.frequency_points.get(i).getNumGoal());
                whereVector2.add("numTask", cluster.frequency_points.get(i).getNumTask());
                whereVector2.add("numQuality", cluster.frequency_points.get(i).getNumQuality());
                whereVector2.add("numResource", cluster.frequency_points.get(i).getNumResource());
                whereVector2.add("numLink", cluster.frequency_points.get(i).getNumLink());
                
                whereVector1.add("frequency_vector", whereVector2.get());

               
                //insert field frequency_id into vector collection
                
                DBCursor cursor = vector.find(whereVector1.get());
                while(cursor.hasNext()) {
                    BasicDBObject obj = (BasicDBObject) cursor.next();
                    obj.append("frequency_id", cluster.getFrequencyId() + 1);
                    vector.save(obj);
                }
                
//
//                vector.update(vector.find(whereVector1.get()).next(), obj.append("frequency_id", cluster.getFrequencyId() + 1));

            }
             
        }

    }

    public void insertClusterCol(Cluster meaning_cluster, Cluster frequency_cluster,DBCursor cursor) {
        ConnectionDB connect = new ConnectionDB();
        DBCollection model = connect.connect(MyConstants.ORIGINAL_MODEL_NAME);
        DBCollection clusterCol = connect.connect(MyConstants.CLUSTER_COLLECTION_NAME);

        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();

        // find model which has id_model the same field into vector collection
        while (cursor.hasNext()) {
            BasicDBObjectBuilder whereModel = BasicDBObjectBuilder.start();
            whereModel.add("id_model", cursor.next().get("id_model"));
            DBCursor cursor2 = model.find(whereModel.get());
           
            
            BasicDBObject values = (BasicDBObject) cursor2.next();
            values.append("meaning_id", meaning_cluster.meaning_id + 1).append("frequency_id", frequency_cluster.frequency_id+1 );
            clusterCol.insert(values);
            
        }


    }
// clustering level 2 based on frequency
    public void execute2(List<Cluster> meaning_clusters, DBCollection vector) {

        CollectionCentroid db = new CollectionCentroid();

        for (Cluster cluster : meaning_clusters) {
            BasicDBObjectBuilder whereVector1 = BasicDBObjectBuilder.start();
            DBCursor cursor = vector.find(whereVector1.add("meaning_id", cluster.meaning_id + 1).get());

            initFrequencyPoints(cursor);
            calculate();

            insertFrequency_id(frequency_clusters, cluster);
            db.insertFrequencyCentroid(cluster, frequency_clusters);

            BasicDBObjectBuilder whereVector2 = BasicDBObjectBuilder.start();
            whereVector2.add("meaning_id", cluster.meaning_id + 1);
            for(int i = 0; i < frequency_clusters.size(); i++) {
                
                whereVector2.add("frequency_id", frequency_clusters.get(i).frequency_id+1);
                
                DBCursor cursorVector = vector.find(whereVector2.get());
                
                insertClusterCol(cluster, frequency_clusters.get(i), cursorVector);
               
            }
           
        }

    }
}

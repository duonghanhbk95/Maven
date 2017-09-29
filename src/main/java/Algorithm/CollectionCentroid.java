/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.List;

/**
 * insert cluster into DB
 *
 * @author Hanh Nguyen
 */
public class CollectionCentroid {

    public void insertCentroid(List<Cluster> clusters) {
        ConnectionDB connect = new ConnectionDB();
        
        DBCollection cluster = connect.connect("Centroid");

        for (int i = 0; i < clusters.size(); i++) {
            BasicDBObject dbObject = new BasicDBObject("cluster_id", i+1);
            BasicDBObject centroid = new BasicDBObject();
            centroid.append("goal",clusters.get(i).getCentroid().getGoal());
            centroid.append("task",clusters.get(i).getCentroid().getTask());
            centroid.append("quality",clusters.get(i).getCentroid().getQuality());
            centroid.append("resource",clusters.get(i).getCentroid().getResource());
            dbObject.append("centroid", centroid);
            
            cluster.insert(dbObject);

        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * insert cluster into DB
 *
 * @author Hanh Nguyen
 */
public class InsertClusterDB {

    public void insertCluster(List<Cluster> clusters) {
        MongoClient mongoClient;
        mongoClient = null;
        try {
            mongoClient = MongoUtils.getMongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(InsertClusterDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        DB db = mongoClient.getDB(MyConstants.DB_NAME);

        DBCollection cluster = db.getCollection("Centroid");

        for (int i = 0; i < clusters.size(); i++) {
            BasicDBObject dbObject = new BasicDBObject("cluster_id", i+1).append("centroid", clusters.get(i).getCentroid().toString());
            cluster.insert(dbObject);

        }
    }
}

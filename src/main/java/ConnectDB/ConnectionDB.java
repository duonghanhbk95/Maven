/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectDB;

import Algorithm.CollectionCentroid;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hanh Nguyen
 */
public class ConnectionDB {
    public DBCollection connect(String collection_name) {
        MongoClient mongoClient;
        mongoClient = null;
        try {
            mongoClient = MongoUtils.getMongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(CollectionCentroid.class.getName()).log(Level.SEVERE, null, ex);
        }
        DB db = mongoClient.getDB(MyConstants.DB_NAME);

        DBCollection dbCollection = db.getCollection(collection_name);
        
        return dbCollection;
    }
}

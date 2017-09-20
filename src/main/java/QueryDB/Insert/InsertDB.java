/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.Insert;

import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import ReadFile.ReadJSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import org.json.simple.JSONArray;

/**
 *
 * @author Hanh Nguyen
 */
public class InsertDB {

    public void insert() throws UnknownHostException {
        MongoClient mongoClient = MongoUtils.getMongoClient();
        DB db = mongoClient.getDB(MyConstants.DB_NAME);
        DBCollection dept = db.getCollection("Department");

       
        // Insert Document 1
        ReadJSON js = new ReadJSON();
        JSONArray array = js.readFolder("E://Study//TTTN//piStar//iStar2005_done");
        for (Object obj : array) {
            DBObject dbObject = (DBObject) JSON.parse(obj.toString());
            System.out.println("dbObject:" + dbObject);
            
            
            dept.insert(dbObject);
            
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        InsertDB is = new InsertDB();
        is.insert();
    }
}

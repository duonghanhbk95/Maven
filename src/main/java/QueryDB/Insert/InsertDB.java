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
        JSONArray array = js.readFolder("E://Study//TTTN//piStar//iStar2010_done");
        
        int i = 1;
        for (Object obj : array) {
            BasicDBObject dbObject = (BasicDBObject) JSON.parse(obj.toString());
            dbObject.append("id_model", i);
            dept.insert(dbObject);
            
            
            i++;
            
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        InsertDB is = new InsertDB();
        is.insert();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB;

import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hanh Nguyen
 */
public class SimpleQuery {
     public static List l = new ArrayList();
     public static void main(String[] args) {

        // Kết nối tới MongoDB.
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoUtils.getMongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        DB db = mongoClient.getDB(MyConstants.DB_NAME);

        DBCollection dept = db.getCollection("Department");

        // Truy vấn tất cả các object trong Collection.
        BasicDBObject allQuery = new BasicDBObject();
        BasicDBObject field = new BasicDBObject();

       
//        field.put("_id", 0);

        Grouping a = new Grouping();
        
        
        DBCursor cursor = dept.find();
        
        int i = 1;
        while (cursor.hasNext()) {
            System.out.println("i" + i);
            
            cursor.next();
            
//            
//            BasicDBList List1 = (BasicDBList) obj.get("actors");
//            Iterator in_1 = List1.iterator();
//            
//            DBObject objNodes = (DBObject) in_1.next();
//            
//            BasicDBList List2 = (BasicDBList) objNodes.get("nodes");
//            
//            Iterator in_2 = List2.iterator();
//            
//            DBObject obj2 = (DBObject) in_2.next();
//            
//            String el = (String) obj2.get("text");
//            
//            System.out.println("List2" + el);
            
            i++;
        }
    }
}

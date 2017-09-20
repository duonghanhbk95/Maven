/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import QueryDB.SimpleQuery;
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
public class LoadModel {
    public DBCursor loadModel() {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoUtils.getMongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        DB db = mongoClient.getDB(MyConstants.DB_NAME);
        DBCollection dept = db.getCollection(MyConstants.COL_NAME);

        DBCursor cursor = dept.find();
        
        return cursor;

    }
    
    public static void main(String[] args) {
        List c = new ArrayList();
        
        Grouping b = new Grouping();
        
        int i = 1;
        
        LoadModel a = new LoadModel();
        DBCursor cursor = a.loadModel();
        while(cursor.hasNext()) {
            System.out.println("i:" + i);
            i++;
            
            b.getGroup(cursor, c, "istar.Goal");
            
            System.out.println("list" + c);
            
        }
        
        System.out.println("Last:" + c);
    }
}

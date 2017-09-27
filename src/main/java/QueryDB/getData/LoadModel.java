/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import QueryDB.SimpleQuery;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;

/**
 *
 * @author Hanh Nguyen
 */
public class LoadModel {

    private static List types;
    private static List<Grouping> groups;
    private static DB db;
    public LoadModel() {
        LoadModel.groups = new ArrayList();
        LoadModel.types = new ArrayList();
    }

    public DBCursor loadModel() {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoUtils.getMongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        db = mongoClient.getDB(MyConstants.DB_NAME);
        DBCollection dept = db.getCollection(MyConstants.COL_NAME);

        DBCursor cursor = dept.find();

        return cursor;

    }
    
    private void insertGroup(List group) {
//        db.createCollection("Group");
    }
    public static void main(String[] args) {

        LoadModel load = new LoadModel();
        DBCursor iCursor = load.loadModel();
        
        
        
        Type t = new Type();
        
        int i = 0;
        while(iCursor.hasNext()) {
            Grouping group = new Grouping(i);
            
            DBObject dbObj = (DBObject) iCursor.next();
            
            t.setGoal(t.getGroup(dbObj, "istar.Goal"));
            t.setTask(t.getGroup(dbObj, "istar.Task"));
            t.setQuality(t.getGroup(dbObj, "istar.Quality"));
            t.setResource(t.getGroup(dbObj, "istar.Resource"));
            t.setGroup_id(i);
            
            
            types.add(t.getGoal());
            types.add(t.getTask());
            types.add(t.getQuality());
            types.add(t.getResource());
            
//            
//            group.setType(types);
//            groups.get(i).addType(t);
//            
           
            group.setType(types);
            groups.add(group);
            
            group.setType(types);
            groups.add(group);
            
            group.setType(types);
            groups.add(group);
            
            System.out.println("group :" + groups.get(i).types);
          
            
//            gr.setType(t.getTask());
//            types.add(gr);
//            
//            gr.setType(t.getQuality());
//            types.add(gr);
//            
//            gr.setType(t.getResource());
//            types.add(gr);
            
            System.out.println("------------------------------------");
            System.out.println("goal" + "[" + i + "]" + t.getGoal());
            System.out.println("task" + "[" + i + "]" + t.getTask());
            System.out.println("quality" + "[" + i + "]" + t.getQuality());
            System.out.println("resource" + "[" + i + "]" + t.getResource());
            
            
           
            i++;
        }
       
       

        
        

    }
    
    

}

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

    private static List<Type> types;
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

        int i = 1;
        
        Grouping group = new Grouping();
        
        Type type = new Type();
        while (iCursor.hasNext()) {
            List listType = new ArrayList();
            
            DBObject dbObj = (DBObject) iCursor.next();
            
            System.out.println("goal: " + i + ":" + type.getGroup(dbObj, "istar.Goal"));
            
            listType = group.getVector(type.getGroup(dbObj, "istar.Goal"), type.getGroup(dbObj, "istar.Task"),
                    type.getGroup(dbObj, "istar.Quality"), type.getGroup(dbObj, "istar.Resource"));

            BasicDBObject doc = new BasicDBObject();
            doc.put("id_model", i);
            BasicDBObject vector = new BasicDBObject();
            vector.append("goal", listType.get(0)).append("task", listType.get(1));
            vector.append("quality", listType.get(2)).append("resource", listType.get(3));
            doc.put("vector", vector);
            DBCollection insert = db.getCollection("vector2");
            insert.insert(doc);

            i++;
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB;

import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import com.mongodb.BasicDBList;
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
public class Grouping {
    private final String ACTOR = "actors";
    private final String NODE = "nodes";
    private Boolean checkGoal(String type) {
        return "istar.Goal".equals(type);
    }

    private Boolean checkTask(String type) {
        return "istar.Task".equals(type);
    }

    private Boolean checkQuality(String type) {
        return "istar.Quality".equals(type);
    }

    private Boolean checkResource(String type) {
        return "istar.Resource".equals(type);
    }

    public List getGroup(List emp,String type) {
        // Kết nối tới MongoDB.
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoUtils.getMongoClient();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        DB db = mongoClient.getDB(MyConstants.DB_NAME);
        DBCollection dept = db.getCollection(MyConstants.COL_NAME);

        DBCursor cursor = dept.find();
        
        
        DBObject dbObj = (DBObject) cursor.next();

        BasicDBList actor= (BasicDBList) dbObj.get(ACTOR);

        Iterator in_1 = actor.iterator();
        DBObject nodeObj = (DBObject) in_1.next();
        
        BasicDBList nodes = (BasicDBList) nodeObj.get(NODE);
        
        Iterator in_2 = nodes.iterator();

        while (in_2.hasNext()) {

            DBObject obj = (DBObject) in_2.next();

            String elm = (String) obj.get("text");
            
            if(checkGoal(type)) {
                emp.add(elm);
            }
            else if(checkTask(type)) {
                emp.add(elm);
            }
            else if(checkQuality(type)) {
                emp.add(elm);
            }
            else if(checkResource(type)) {
                emp.add(elm);
            }
        }
        
        return emp;
    }
}

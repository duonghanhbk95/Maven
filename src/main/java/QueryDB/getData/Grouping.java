/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Grouping {

    private final String ACTOR = "actors";
    private final String NODE = "nodes";
    private final String GOAL = "istar.Goal";
    private final String TASK = "istar.Task";
    private final String QUALITY = "Quality";
    private final String RESOURCE = "Resource";
    
    private Boolean checkType(String type1,String type2) {
        return type1.equals(type2);
    }
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
// grouping
    public List getGroup(DBCursor cursor,List emp, String type) {

//        MongoClient mongoClient = null;
//        try {
//            mongoClient = MongoUtils.getMongoClient();
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(SimpleQuery.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        DB db = mongoClient.getDB(MyConstants.DB_NAME);
//        DBCollection dept = db.getCollection(MyConstants.COL_NAME);
//
//        DBCursor cursor = dept.find();
//        DBObject dbObj = (DBObject) cursor.next();
        
        DBObject dbObj = (DBObject) cursor.next();
        BasicDBList actor = (BasicDBList) dbObj.get(ACTOR);

        Iterator in_1 = actor.iterator();
        DBObject nodeObj = (DBObject) in_1.next();

        BasicDBList nodes = (BasicDBList) nodeObj.get(NODE);

        Iterator in_2 = nodes.iterator();
        while (in_2.hasNext()) {

            DBObject obj = (DBObject) in_2.next();
            if(checkType(obj.get("type").toString(),type)) {
                emp.add(obj.get("text"));
            }
        }
        return emp;
    }
}

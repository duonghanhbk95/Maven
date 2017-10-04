/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Type {

    private final String ACTOR = "actors";
    private final String NODE = "nodes";
    private final String LINK = "links";
    public int group_id;

    public List goal;
    public List task;
    public List quality;
    public List resource;
    public int link;

    public List getQuality() {
        return quality;
    }

    public void setQuality(List quality) {
        this.quality = quality;
    }

    public List getGoal() {
        return goal;
    }

    public void setGoal(List goal) {
        this.goal = goal;
    }

    public List getTask() {
        return task;
    }

    public void setTask(List task) {
        this.task = task;
    }

    public List getResource() {
        return resource;
    }

    public void setResource(List resource) {
        this.resource = resource;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    private Boolean checkType(String type1, String type2) {
        return type1.equals(type2);
    }

// grouping
    public int Link(DBObject dbObj) {
        BasicDBList links = (BasicDBList) dbObj.get(LINK);
        return links.size();

    }

    public List<Type> getGroup(DBObject dbObj, String type) {
        List emp = new ArrayList();
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

        BasicDBList actor = (BasicDBList) dbObj.get(ACTOR);

        Iterator in_1 = actor.iterator();
        DBObject nodeObj = (DBObject) in_1.next();

        BasicDBList nodes = (BasicDBList) nodeObj.get(NODE);

        Iterator in_2 = nodes.iterator();
        while (in_2.hasNext()) {

            DBObject obj = (DBObject) in_2.next();
            if (checkType(obj.get("type").toString(), type)) {
                emp.add(obj.get("text"));
            }

        }
        return emp;
    }

}

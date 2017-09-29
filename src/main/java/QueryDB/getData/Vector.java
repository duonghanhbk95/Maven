/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import ConnectDB.ConnectionDB;
import ConnectDB.MongoUtils;
import ConnectDB.MyConstants;
import QueryDB.SimpleQuery;
import com.mongodb.BasicDBObject;
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
public class Vector {

    private static List<Type> types;
    private static List<Grouping> groups;
    private static DB db;

    public Vector() {
        Vector.groups = new ArrayList();
        Vector.types = new ArrayList();
    }

    public DBCursor loadModel(String Collection_name) {
        ConnectionDB connect = new ConnectionDB();
        DBCollection model = connect.connect(Collection_name);

        DBCursor cursor = model.find();

        return cursor;

    }
    public void createCollectionVector() {
        
        ConnectionDB connect = new ConnectionDB();
        
        DBCursor iCursor = loadModel(MyConstants.ORIGINAL_MODEL_NAME);

        int i = 1;

        Grouping group = new Grouping();

        Type type = new Type();
        while (iCursor.hasNext()) {
            List listType = new ArrayList();

            DBObject dbObj = (DBObject) iCursor.next();

            listType = group.getVector(type.getGroup(dbObj, "istar.Goal"), type.getGroup(dbObj, "istar.Task"),
                    type.getGroup(dbObj, "istar.Quality"), type.getGroup(dbObj, "istar.Resource"));

            System.out.println("list type:" + i + listType);

            BasicDBObject doc = new BasicDBObject();
            doc.put("id_model", i);
            doc.put("cluster_id", "");
//            doc.put("vector", listType.toString());
            BasicDBObject vector = new BasicDBObject();
            vector.append("goal", listType.get(0)).append("task", listType.get(1));
            vector.append("quality", listType.get(2)).append("resource", listType.get(3));
            doc.put("vector", vector);
            DBCollection insert = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);
            insert.insert(doc);

            i++;
        }

    }
}
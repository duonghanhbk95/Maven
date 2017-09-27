/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Grouping {
    public int id;
    public List<Type> types;

    public Grouping(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public List getType() {
        return types;
    }

    public void setType(List type) {
        this.types = type;
    }
     

    public void addType(Type type) {
        types.add(type);
    }

    
}

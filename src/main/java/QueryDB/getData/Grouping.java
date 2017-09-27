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
    public List getVector(List goal, List task, List quality, List resource) {
        List emp = new ArrayList();
        emp.add(goal.get(0));
        
        System.out.println("goal:" + goal.get(0));
        emp.add(goal.get(0));
        emp.add(goal.get(0));
        emp.add(goal.get(0));
        
        return emp;
    }
    
}

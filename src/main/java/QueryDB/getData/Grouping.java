/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Grouping {

    public List getVector(List goal, List task, List quality, List resource) {
       
        List emp = new ArrayList();
        Api api = new Api();

        String data_goal = "";
        String data_task = "";
        String data_quality = "";
        String data_resource = "";

        if (goal.isEmpty()) {
            emp.add("");
        } else if (goal.size() == 1) {
            emp.add(goal.get(0));
        } else {
            for (int i = 0; i < goal.size(); i++) {
                String element = String.format("goal=%s", ((String) goal.get(i)).replace(" ", "_").toLowerCase());
                //System.out.println(element);
                String temp = new StringBuffer(data_goal).append(element).append("&").toString();
                data_goal = temp;
            }
            
            emp.add(Api.getApi(data_goal));
        }
        if (task.isEmpty()) {
            emp.add("");
        } else if (task.size() == 1) {
            emp.add(task.get(0));
        } else {
            for (int i = 0; i < task.size(); i++) {
                String element = String.format("goal=%s", ((String) task.get(i)).replace(" ", "_").toLowerCase());
                String temp = new StringBuffer(data_task).append(element).append("&").toString();
                data_task = temp;
            }
            emp.add(Api.getApi(data_task));
        }

        if (quality.isEmpty()) {
            emp.add("");
        } else if (quality.size() == 1) {
            emp.add(quality.get(0));
        } else {
            for (int i = 0; i < quality.size(); i++) {
                String element = String.format("goal=%s", ((String) quality.get(i)).replace(" ", "_").toLowerCase());
                String temp = new StringBuffer(data_quality).append(element).append("&").toString();
                data_quality = temp;
            }
            emp.add(Api.getApi(data_quality));
        }
        if (resource.isEmpty()) {
            emp.add("");
        } else if (resource.size() == 1) {
            emp.add(resource.get(0));
        } else {
            for (int i = 0; i < resource.size(); i++) {
                String element = String.format("goal=%s", ((String) resource.get(i)).replace(" ", "_").toLowerCase());
                String temp = new StringBuffer(data_resource).append(element).append("&").toString();
                data_resource = temp;
            }
            emp.add(Api.getApi(data_resource));
        }
        return emp;
    }

}

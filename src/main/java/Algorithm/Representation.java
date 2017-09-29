/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import QueryDB.getData.Comparation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Representation {

    public List represent(List<Point> points, Comparation sml) {
        
        List emp = new ArrayList();
        float sumGoal = 0;
        float sumTask = 0;
        float sumQuality = 0;
        float sumResource = 0;
        float maxGoal = sumGoal;
        float maxTask = sumTask;
        float maxQuality = sumQuality;
        float maxResource = sumResource;
        String goal = "";
        String task = "";
        String quality = "";
        String resource = "";
        for (Point point1 : points) {
            sumGoal = 0;
            sumTask = 0;
            sumQuality = 0;
            sumResource = 0;
            for (Point point2 : points) {
                System.out.println("----------------------------------------");
                System.out.println("point1: " + point1);
                System.out.println("point2 :" + point2);
                
                
                sumGoal += sml.compare(point1.getGoal(), point2.getGoal());
                sumTask += sml.compare(point1.getTask(), point2.getTask());
                sumQuality += sml.compare(point1.getQuality(), point2.getQuality());
                sumResource += sml.compare(point1.getResource(), point2.getResource());

                System.out.println("sumGoal :" + sumGoal);
                System.out.println("sumTask :" + sumTask);
                System.out.println("sumQuality :" + sumQuality);
                System.out.println("sumResource :" + sumResource);
                System.out.println("----------------------------------------");
            }
            if (sumGoal > maxGoal) {
                maxGoal = sumGoal;
                goal = point1.getGoal();
                System.out.println("!--------------------------------------!");
                System.out.println("maxGoal :" + maxGoal);
            }
            if (sumTask > maxTask) {
                maxTask = sumTask;
                task = point1.getTask();
                System.out.println("!--------------------------------------!");
                System.out.println("maxTask :" + maxTask);
            }
            if (sumQuality > maxQuality) {
                maxQuality = sumQuality;
                quality = point1.getQuality();
                System.out.println("!--------------------------------------!");
                System.out.println("maxQuality :" + maxQuality);
            }
            if (sumResource > maxResource) {
                maxResource = sumResource;
                resource = point1.getResource();
                System.out.println("!--------------------------------------!");
                System.out.println("maxResource :" + maxResource);
            }
        }
        
        emp.add(goal);
        emp.add(task);
        emp.add(quality);
        emp.add(resource);
        System.out.println("goal:-------------: " + goal);
        System.out.println("task:-------------: " + task);
        System.out.println("quality:-------------: " + quality);
        System.out.println("resource:-------------: " + resource);
        
        
        return emp;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import QueryDB.getData.Comparation;
import com.mongodb.Cursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Point {

    private String goal;
    private String task;
    private String quality;
    private String resource;
    private int cluster_number = 0;

    public Point(String goal, String task, String quality, String resource) {
        this.goal = goal;
        this.task = task;
        this.quality = quality;
        this.resource = resource;
    }

    public String getGoal() {
        return this.goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTask() {
        return this.task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getQuality() {
        return this.quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setCluster(int n) {
        this.cluster_number = n;
    }

    public int getCluster() {
        return this.cluster_number;
    }

    //Calculates the distance between two points.
    static Comparation sml = new Comparation();

    protected static Float distance(Point p, Point centroid) {
        
        float distance = 0;
        distance = sml.compare(p.goal, centroid.goal) + sml.compare(p.task, centroid.task) + sml.compare(p.quality, centroid.quality)
                + sml.compare(p.resource, centroid.resource);
        
        return distance;
    }

    protected static Point createPoint(DBObject dbObject) {

        DBObject db1 = (DBObject) dbObject.get("vector");
        
        
        String str1 = db1.get("goal").toString();
        String str2 = db1.get("task").toString();
        String str3 = db1.get("quality").toString();
        String str4 = db1.get("resource").toString();

        return new Point(str1, str2, str3, str4);
    }

    protected static List getPoints(Cursor cursor) {
        List points = new ArrayList();

        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();

            points.add(createPoint(dbObject));
        }

        return points;
    }

    @Override
    public String toString() {
        return "(" + goal + "," + task + "," + quality + "," + resource + ")";
    }

}

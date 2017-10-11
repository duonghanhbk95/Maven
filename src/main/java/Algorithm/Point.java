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

    private double numGoal;
    private double numTask;
    private double numQuality;
    private double numResource;
    private double numLink;

    private static final String MEANING_VECTOR = "meaning_vector";
    private static final String FREQUENCY_VECTOR = "frequency_vector";
    private int cluster_number = 0;

  
    public Point(String goal, String task, String quality, String resource) {
        this.goal = goal;
        this.task = task;
        this.quality = quality;
        this.resource = resource;
    }

    public Point(double numGoal, double numTask, double numQuality, double numResource, double numlink) {
        this.numGoal = numGoal;
        this.numTask = numTask;
        this.numQuality = numQuality;
        this.numResource = numResource;
        this.numLink = numlink;
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

    public double getNumGoal() {
        return numGoal;
    }

    public void setNumGoal(double numGoal) {
        this.numGoal = numGoal;
    }

    public double getNumTask() {
        return numTask;
    }

    public void setNumTask(double numTask) {
        this.numTask = numTask;
    }

    public double getNumQuality() {
        return numQuality;
    }

    public void setNumQuality(double numQuality) {
        this.numQuality = numQuality;
    }

    public double getNumResource() {
        return numResource;
    }

    public void setNumResource(double numResource) {
        this.numResource = numResource;
    }

    public double getNumLink() {
        return numLink;
    }

    public void setNumLink(double numlink) {
        this.numLink = numlink;
    }

    public void setCluster(int n) {
        this.cluster_number = n;
    }

    public int getCluster() {
        return this.cluster_number;
    }

    //Calculates the distance between two points belong to meaning
    static Comparation sml = new Comparation();

    protected static Float distanceMeaning(Point p, Point centroid) {

        float distance = 0;
        distance = sml.compare(p.goal, centroid.goal) + sml.compare(p.task, centroid.task) + sml.compare(p.quality, centroid.quality)
                + sml.compare(p.resource, centroid.resource);

        return distance;
    }

    protected static Point createMeaningPoint(DBObject dbObject, String vector) {

        DBObject db1 = (DBObject) dbObject.get(vector);

        String str1 = db1.get("goal").toString();
        String str2 = db1.get("task").toString();
        String str3 = db1.get("quality").toString();
        String str4 = db1.get("resource").toString();
     
        return new Point(str1, str2, str3, str4);
    }

    protected static List getMeaningPoints(Cursor cursor) {
        List points = new ArrayList();

        while (cursor.hasNext()) {

            DBObject dbObject = cursor.next();

            points.add(createMeaningPoint(dbObject, MEANING_VECTOR));
        }

        return points;
    }

    //calculates the distance between two points belong to frequency
    /**
     *
     * @param p
     * @param centroid
     * @return
     */
    protected static double distanceFrequency(Point p, Point centroid) {
        double distance = Math.sqrt(Math.pow((centroid.getNumGoal() - p.getNumGoal()), 2) + Math.pow((centroid.getNumTask() - p.getNumTask()), 2)
                + Math.pow((centroid.getNumQuality() - p.getNumQuality()), 2) + Math.pow((centroid.getNumResource() - p.getNumResource()), 2)
                + Math.pow((centroid.getNumLink() - p.getNumLink()), 2));

        return distance;
    }

    protected static Point createFrequencyPoint(DBObject dbObject, String vector) {
        DBObject db1 = (DBObject) dbObject.get(vector);

        int numGoal = Integer.parseInt(db1.get("numGoal").toString());
        int numTask = Integer.parseInt(db1.get("numTask").toString());
        int numQuality = Integer.parseInt(db1.get("numQuality").toString());
        int numResource = Integer.parseInt(db1.get("numResource").toString());
        int numlink = Integer.parseInt(db1.get("numLink").toString());

        return new Point(numGoal, numTask, numQuality, numResource, numlink);
    }

    protected static List getFrequencyPoints(Cursor cursor) {
        List points = new ArrayList();
        while (cursor.hasNext()) {
            DBObject dbObj = cursor.next();

            points.add(createFrequencyPoint(dbObj, FREQUENCY_VECTOR));
        }

        return points;
    }
//    @Override
//    public String toString() {
//        return "(" + numGoal + "," + numTask + "," + numQuality + "," + numResource + "," + numlink + ")";
//    }

    public String toString() {
        return "(" + goal + "," + task + "," + quality + "," + resource + ")";
    }
}

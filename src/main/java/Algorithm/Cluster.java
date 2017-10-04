/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Cluster {

    public List<Point> meaning_points;
    public Point meaning_centroid;

    public List<Point> frequency_points;
    public Point frequency_centroid;

    public int meaning_id;
    public int frequency_id;

    //Creates a new Cluster
    public Cluster(int id) {
        this.meaning_id = id;
        this.frequency_id = id;
        this.meaning_points = new ArrayList();
        this.meaning_centroid = null;
        this.frequency_points = new ArrayList();
        this.frequency_centroid = null;
    }
    // set meaning point

    public List getMeaningPoints() {
        return meaning_points;
    }

    public void addMeaningPoints(Point point) {
        meaning_points.add(point);
    }

    public void setMeaningPoints(List points) {
        this.meaning_points = points;
    }

    public Point getMeaningCentroid() {
        return meaning_centroid;
    }

    public void setMeaningCentroid(Point centroid) {
        this.meaning_centroid = centroid;
    }
    // end set meaning points

    //set meaning_id
    public int getMeaningId() {
        return meaning_id;
    }
    // end set meaning_id

    public int getFrequencyId() {
        return frequency_id;
    }

    //  set frequency points
    public List getFrequencyPoints() {
        return frequency_points;
    }

    public void addFrequencyPoints(Point point) {
        frequency_points.add(point);
    }

    public void setFrequencyPoints(List points) {
        this.frequency_points = points;
    }

    public Point getFrequencyCentroid() {
        return frequency_centroid;
    }

    public void setFrequencyCentroid(Point centroid) {
        this.frequency_centroid = centroid;
    }

    // end set frequency points
    public void clearMeaning() {
        meaning_points.clear();
    }

    public void clearFrequency() {
        frequency_points.clear();
    }

    public void plotMeaningCluster() {
        System.out.println("[Cluster: " + meaning_id + "]");
        System.out.println("[Centroid: " + meaning_centroid + "]");
        System.out.println("[Points: \n");
        meaning_points.forEach((p) -> {
            System.out.println(p);
        });
        System.out.println("]");
    }

    public void plotFrequencyCluster() {
        System.out.println("[Cluster: " + frequency_id + "]");
        System.out.println("[Centroid: " + frequency_centroid + "]");
        System.out.println("[Points: \n");
        frequency_points.forEach((p) -> {
            System.out.println(p);
        });
        System.out.println("]");
    }

}

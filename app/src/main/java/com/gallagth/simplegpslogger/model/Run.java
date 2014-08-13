package com.gallagth.simplegpslogger.model;

import java.util.LinkedList;

/**
 * Created by Thomas on 13/08/2014.
 */
public class Run {

    private String name;
    private long creationTime;
    private LinkedList<StampedPoint> points;

    public Run(String name, long creationDate) {
        this.name = name;
        this.creationTime = creationDate;
        this.points = new LinkedList<StampedPoint>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public LinkedList<StampedPoint> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<StampedPoint> points) {
        this.points = points;
    }

    public boolean appendPoint(StampedPoint point) {
        return points.add(point);
    }

    public String generateFileName() {
        return String.format("%d_%s", getCreationTime(), getName());
    }
}

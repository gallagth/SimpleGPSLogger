package com.gallagth.simplegpslogger.model;

import android.location.Location;

import java.util.LinkedList;

/**
 * Created by Thomas on 13/08/2014.
 */
public class Run {

    private String name;
    private long creationTime;
    private LinkedList<Location> locations;

    public Run(String name, long creationDate) {
        this.name = name;
        this.creationTime = creationDate;
        this.locations = new LinkedList<Location>();
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

    public LinkedList<Location> getLocations() {
        return locations;
    }

    public void setLocations(LinkedList<Location> locations) {
        this.locations = locations;
    }

    public boolean appendLocation(Location point) {
        return locations.add(point);
    }

    public String generateFileName() {
        return String.format("%d_%s", getCreationTime(), getName());
    }
}

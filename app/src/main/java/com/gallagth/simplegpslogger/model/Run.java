package com.gallagth.simplegpslogger.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

/**
 * Created by Thomas on 13/08/2014.
 */
@org.parceler.Parcel
public class Run {

    String name;
    long creationTime;
    double length;
    LinkedList<Location> locations;

    public Run() {
        //Required by Parceler
    }

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
        length = calculateLength(locations);
        this.locations = locations;
    }

    public boolean appendLocation(Location point) {
        length += locations.getLast().distanceTo(point);
        return locations.add(point);
    }

    public String generateFileName() {
        return String.format("%d_%s", getCreationTime(), getName());
    }

    public double getLength() {
        return length;
    }

    private double calculateLength(LinkedList<Location> locations) {
        if (locations.size() < 2) {
            return 0d;
        }
        double length = 0;
        for (int i = 1; i < locations.size(); i++) {
            length += locations.get(i-1).distanceTo(locations.get(i));
        }
        return length;
    }
}

package com.gallagth.simplegpslogger.model;

/**
 * Created by Thomas on 13/08/2014.
 */
public class StampedPoint {

    private long timestamp;
    private GeoPoint point;

    public StampedPoint(long timestamp, GeoPoint point) {
        this.timestamp = timestamp;
        this.point = point;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }
}

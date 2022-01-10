package com.github.ayan.gpsprojectnexis;

import java.util.Date;

public class LocationAndTime {

    private double longitude;
    private double latitude;
    private Date date;
    private long elapsedTime;

    public LocationAndTime(double longitude, double latitude, Date date, long elapsedTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.elapsedTime = elapsedTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }


}

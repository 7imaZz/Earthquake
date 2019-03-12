package com.example.myapplication;

public class Earthquake {
    Double mag;
    String place;
    long date;
    String url;

    public String getUrl() {
        return url;
    }

    public Earthquake(Double mag, String place, long date, String url) {
        this.mag = mag;
        this.place = place;
        this.date = date;
        this.url = url;
    }

    public Earthquake(Double mag, String place, long date) {
        this.mag = mag;
        this.place = place;
        this.date = date;
    }

    public Double getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public long getDate() {
        return date;
    }
}

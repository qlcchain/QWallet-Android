package com.stratagile.qlink.entity;

public class Location extends BaseBack {

    /**
     * currentTimeMillis : 1586397760344
     * location : domestic
     */

    private long currentTimeMillis;
    private String location;

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

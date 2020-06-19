package com.stratagile.qlink.entity.defi;

public class ActiveDataBean {
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ActiveDataBean(long time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double value;
}

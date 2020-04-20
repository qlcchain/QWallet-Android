package com.stratagile.qlink.ui.adapter.my;

public class OutSideBean {
    private int day;
    private boolean isDone;

    public int getDay() {
        return day;
    }

    public OutSideBean(int day, boolean isDone) {
        this.day = day;
        this.isDone = isDone;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}

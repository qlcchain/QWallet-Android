package com.stratagile.qlink.entity;

public class RedPoint extends BaseBack {

    /**
     * dailyIncomePoint : 1
     * gzbdPoint : 0
     * tradeMiningPoint : 0
     * gzbdFocusInterrupt : 1
     * invitePoint : 0
     */

    private int dailyIncomePoint;
    private int gzbdPoint;
    private int tradeMiningPoint;
    private int gzbdFocusInterrupt;
    private int invitePoint;

    public int getDailyIncomePoint() {
        return dailyIncomePoint;
    }

    public void setDailyIncomePoint(int dailyIncomePoint) {
        this.dailyIncomePoint = dailyIncomePoint;
    }

    public int getGzbdPoint() {
        return gzbdPoint;
    }

    public void setGzbdPoint(int gzbdPoint) {
        this.gzbdPoint = gzbdPoint;
    }

    public int getTradeMiningPoint() {
        return tradeMiningPoint;
    }

    public void setTradeMiningPoint(int tradeMiningPoint) {
        this.tradeMiningPoint = tradeMiningPoint;
    }

    public int getGzbdFocusInterrupt() {
        return gzbdFocusInterrupt;
    }

    public void setGzbdFocusInterrupt(int gzbdFocusInterrupt) {
        this.gzbdFocusInterrupt = gzbdFocusInterrupt;
    }

    public int getInvitePoint() {
        return invitePoint;
    }

    public void setInvitePoint(int invitePoint) {
        this.invitePoint = invitePoint;
    }
}

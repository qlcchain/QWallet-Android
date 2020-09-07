package com.today.step.net;

public class CreateRecord extends BaseBack {


    @Override
    public String toString() {
        return "CreateRecord{" +
                "pledgeQlcDays=" + pledgeQlcDays +
                ", rewardQlcAmount=" + rewardQlcAmount +
                ", subsidised='" + subsidised + '\'' +
                ", pledgeQlcBase=" + pledgeQlcBase +
                ", rewardQlcDays=" + rewardQlcDays +
                ", isolationDays=" + isolationDays +
                ", claimedQgas=" + claimedQgas +
                '}';
    }

    /**
     * pledgeQlcDays : 15
     * rewardQlcAmount : 100
     * subsidised : 1
     * pledgeQlcBase : 100
     * rewardQlcDays : 14
     * isolationDays : 14
     * claimedQgas : 0.01012844
     */

    private int pledgeQlcDays;
    private int rewardQlcAmount;
    private String subsidised;
    private int pledgeQlcBase;
    private int rewardQlcDays;
    private int isolationDays;

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    private double claimedQgas;
    private boolean isCreate;

    public int getPledgeQlcDays() {
        return pledgeQlcDays;
    }

    public void setPledgeQlcDays(int pledgeQlcDays) {
        this.pledgeQlcDays = pledgeQlcDays;
    }

    public int getRewardQlcAmount() {
        return rewardQlcAmount;
    }

    public void setRewardQlcAmount(int rewardQlcAmount) {
        this.rewardQlcAmount = rewardQlcAmount;
    }

    public String getSubsidised() {
        return subsidised;
    }

    public void setSubsidised(String subsidised) {
        this.subsidised = subsidised;
    }

    public int getPledgeQlcBase() {
        return pledgeQlcBase;
    }

    public void setPledgeQlcBase(int pledgeQlcBase) {
        this.pledgeQlcBase = pledgeQlcBase;
    }

    public int getRewardQlcDays() {
        return rewardQlcDays;
    }

    public void setRewardQlcDays(int rewardQlcDays) {
        this.rewardQlcDays = rewardQlcDays;
    }

    public int getIsolationDays() {
        return isolationDays;
    }

    public void setIsolationDays(int isolationDays) {
        this.isolationDays = isolationDays;
    }

    public double getClaimedQgas() {
        return claimedQgas;
    }

    public void setClaimedQgas(double claimedQgas) {
        this.claimedQgas = claimedQgas;
    }
}

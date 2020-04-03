package com.stratagile.qlink.entity;

public class SwitchToOtc {
    private boolean toSell;

    public boolean isToSell() {
        return toSell;
    }

    public SwitchToOtc(boolean toSell) {
        this.toSell = toSell;
    }

    public SwitchToOtc() {
    }

    public void setToSell(boolean toSell) {
        this.toSell = toSell;
    }
}

package com.stratagile.qlink.entity.eventbus;

public class ShowMiningAct {
    public boolean isShow() {
        return show;
    }

    public ShowMiningAct(boolean show) {
        this.show = show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    private boolean show;
    private String count;

    public ShowMiningAct(boolean show, String count) {
        this.show = show;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

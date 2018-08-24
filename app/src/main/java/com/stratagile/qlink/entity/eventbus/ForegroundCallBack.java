package com.stratagile.qlink.entity.eventbus;

/**
 * Created by huzhipeng on 2018/2/27.
 */

public class ForegroundCallBack {
    private boolean foreground;

    public ForegroundCallBack(boolean foreground) {
        this.foreground = foreground;
    }

    public boolean isForeground() {

        return foreground;
    }

    public void setForeground(boolean foreground) {
        this.foreground = foreground;
    }
}

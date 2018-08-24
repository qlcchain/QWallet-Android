package com.stratagile.qlink.entity.eventbus;

/**
 * Created by huzhipeng on 2018/1/20.
 */

public class ChangeViewpager {
    private int position;

    public int getPosition() {
        return position;
    }

    public ChangeViewpager(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

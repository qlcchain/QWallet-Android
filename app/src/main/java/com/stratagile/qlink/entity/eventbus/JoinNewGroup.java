package com.stratagile.qlink.entity.eventbus;

/**
 * Created by huzhipeng on 2018/3/20.
 */

public class JoinNewGroup {
    int groupNum;

    public int getGroupNum() {
        return groupNum;
    }

    public JoinNewGroup(int groupNum) {
        this.groupNum = groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }
}

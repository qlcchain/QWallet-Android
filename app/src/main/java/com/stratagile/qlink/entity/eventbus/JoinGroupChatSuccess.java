package com.stratagile.qlink.entity.eventbus;

/**
 * Created by huzhipeng on 2018/3/20.
 */

public class JoinGroupChatSuccess {
    private int groupNum;

    public int getGroupNum() {
        return groupNum;
    }

    public JoinGroupChatSuccess(int groupNum) {
        this.groupNum = groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;

    }
}

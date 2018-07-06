package com.stratagile.qlink.entity.im;

/**
 * Created by huzhipeng on 2018/3/20.
 */

public class InviteToGroupChatRsp {
    int groupNum;

    public InviteToGroupChatRsp(int groupNum) {
        this.groupNum = groupNum;
    }

    public int getGroupNum() {
        return groupNum;

    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }
}

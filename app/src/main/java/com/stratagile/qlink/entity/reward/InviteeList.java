package com.stratagile.qlink.entity.reward;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class InviteeList extends BaseBack {

    private ArrayList<InviteeListBean> inviteeList;

    public ArrayList<InviteeListBean> getInviteeList() {
        return inviteeList;
    }

    public void setInviteeList(ArrayList<InviteeListBean> inviteeList) {
        this.inviteeList = inviteeList;
    }

    public static class InviteeListBean {
        /**
         * head :
         * nickname : 123@qq.com
         * status : NO_AWARD
         * createDate : 2019-11-04 10:39:32
         */

        private String head;
        private String nickname;
        private String status;
        private String createDate;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}

package com.stratagile.qlink.entity.topup;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class SalePartner extends BaseBack {

    private ArrayList<UserListBean> userList;

    public ArrayList<UserListBean> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<UserListBean> userList) {
        this.userList = userList;
    }

    public static class UserListBean {
        /**
         * head : /data/dapp/head/a8d08590d6e64f37983faf68c7593320.png
         * sequence : 1
         * totalReward : 4.0
         * name : Jelly
         * id : 89d3b950953044ec8314ec0f15722110
         */

        private String head;
        private int sequence;
        private double totalReward;
        private String name;
        private String id;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        private int level;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public double getTotalReward() {
            return totalReward;
        }

        public void setTotalReward(double totalReward) {
            this.totalReward = totalReward;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

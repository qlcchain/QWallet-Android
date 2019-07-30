package com.stratagile.qlink.entity.finance;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class MyRanking extends BaseBack<List<MyRanking.DataBean>> {

//    private List<DataBean> data;
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * head : /data/dapp/head/fab2931e6a344943a6713ffa9c8dccf7.png
         * sequence : 1
         * name : Jelly
         * totalInvite : 1
         * id : 06812349
         */

        private String head;
        private int sequence;
        private String name;
        private int totalInvite;
        private String id;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalInvite() {
            return totalInvite;
        }

        public void setTotalInvite(int totalInvite) {
            this.totalInvite = totalInvite;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

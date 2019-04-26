package com.stratagile.qlink.entity.finance;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class EarnRank extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * head : /data/dapp/head/1b2375c0388d45a5997631da94f20103.jpg
         * sequence : 1
         * totalBuy : 27
         * name : 18670819116
         * id : 19528463
         * totalRevenue : 0.12
         */

        private String head;
        private int sequence;
        private int totalBuy;
        private String name;
        private String id;
        private double totalRevenue;

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

        public int getTotalBuy() {
            return totalBuy;
        }

        public void setTotalBuy(int totalBuy) {
            this.totalBuy = totalBuy;
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

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }
}

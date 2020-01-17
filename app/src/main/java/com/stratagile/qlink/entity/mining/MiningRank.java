package com.stratagile.qlink.entity.mining;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class MiningRank extends BaseBack<MiningRank.ListBean> {

    private ArrayList<ListBean> list;

    public ArrayList<ListBean> getList() {
        return list;
    }

    public void setList(ArrayList<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * head : /data/dapp/head/bafbc90f5a9c47fe80f92367897b4e8d.png
         * sequence : 1
         * totalReward : 1.5776E-4
         * name : sketch
         * id : 8155b81f47384b788d8e015ea7cace4e
         */

        private String head;
        private int sequence;
        private double totalReward;
        private String name;
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

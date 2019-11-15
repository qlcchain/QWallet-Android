package com.stratagile.qlink.entity.newwinq;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class MiningAct extends BaseBack {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * totalRewardAmount : 100000.0
         * imgPath :
         * name : 交易挖矿
         * tradeToken : QGAS
         * startTime : 2019-11-13 00:00:00
         * id : 5ae726f14199460086cced309f76b57y
         * rewardToken : QLC
         * endTime : 2019-11-22 23:59:59
         */

        private double totalRewardAmount;
        private String imgPath;
        private String name;
        private String tradeToken;
        private String startTime;
        private String id;
        private String rewardToken;
        private String endTime;

        public double getTotalRewardAmount() {
            return totalRewardAmount;
        }

        public void setTotalRewardAmount(double totalRewardAmount) {
            this.totalRewardAmount = totalRewardAmount;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTradeToken() {
            return tradeToken;
        }

        public void setTradeToken(String tradeToken) {
            this.tradeToken = tradeToken;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRewardToken() {
            return rewardToken;
        }

        public void setRewardToken(String rewardToken) {
            this.rewardToken = rewardToken;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}

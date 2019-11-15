package com.stratagile.qlink.entity.mining;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class MiningRewardList extends BaseBack<MiningRewardList.ListBean> {

    private ArrayList<ListBean> list;

    public ArrayList<ListBean> getList() {
        return list;
    }

    public void setList(ArrayList<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * timeStamp : 2019-11-01
         * symbol : QLC
         * id : 27188db006614d6290fe4bfd7ce89eab
         * rewardAmount : 0.78977472
         * rewardDate : 2019-11-02 14:45:03
         * userId : 7060628a65e4450690976bf56c127787
         * status : NO_AWARD
         */

        private String timeStamp;
        private String symbol;
        private String id;
        private double rewardAmount;
        private String rewardDate;
        private String userId;
        private String status;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getRewardAmount() {
            return rewardAmount;
        }

        public void setRewardAmount(double rewardAmount) {
            this.rewardAmount = rewardAmount;
        }

        public String getRewardDate() {
            return rewardDate;
        }

        public void setRewardDate(String rewardDate) {
            this.rewardDate = rewardDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

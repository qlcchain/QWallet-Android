package com.stratagile.qlink.entity.reward;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class RewardList extends BaseBack<RewardList.RewardListBean> {

    private ArrayList<RewardListBean> rewardList;

    public ArrayList<RewardListBean> getRewardList() {
        return rewardList;
    }

    public void setRewardList(ArrayList<RewardListBean> rewardList) {
        this.rewardList = rewardList;
    }

    public static class RewardListBean {
        /**
         * timeStamp : 2019-10-10
         * txid :
         * id : b38103507fc94dd6ada4dc7455f99597
         * rewardAmount : 0.17035704
         * rewardDate : 2019-10-10 14:22:53
         * type : REGISTER
         * userId : 7060628a65e4450690976bf56c127787
         * toAddress :
         * status : NEW
         */

        private String timeStamp;
        private String txid;
        private String id;
        private double rewardAmount;
        private String rewardDate;
        private String type;
        private String userId;
        private String toAddress;
        private String status;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

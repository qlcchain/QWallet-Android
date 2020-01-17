package com.stratagile.qlink.entity.topup;

import com.stratagile.qlink.entity.BaseBack;

public class CreateGroup extends BaseBack {

    /**
     * group : {"joined":"","discount":0.9,"payFiatMoney":3.68,"deductionToken":"QGAS","payTokenAmount":3.14,"deductionTokenAmount":4.089,"duration":180,"id":"ed8acf44868946a68f317cd1037166f8","payTokenPrice":1,"numberOfPeople":3,"status":"PROCESSING","payToken":"QLC","createDate":"2020-01-14 16:29:05"}
     */

    private GroupBean group;

    public GroupBean getGroup() {
        return group;
    }

    public void setGroup(GroupBean group) {
        this.group = group;
    }

    public static class GroupBean {
        /**
         * joined :
         * discount : 0.9
         * payFiatMoney : 3.68
         * deductionToken : QGAS
         * payTokenAmount : 3.14
         * deductionTokenAmount : 4.089
         * duration : 180
         * id : ed8acf44868946a68f317cd1037166f8
         * payTokenPrice : 1.0
         * numberOfPeople : 3
         * status : PROCESSING
         * payToken : QLC
         * createDate : 2020-01-14 16:29:05
         */

        private String joined;
        private double discount;
        private double payFiatMoney;
        private String deductionToken;
        private double payTokenAmount;
        private double deductionTokenAmount;
        private int duration;
        private String id;
        private double payTokenPrice;
        private int numberOfPeople;
        private String status;
        private String payToken;
        private String createDate;

        public String getJoined() {
            return joined;
        }

        public void setJoined(String joined) {
            this.joined = joined;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getPayFiatMoney() {
            return payFiatMoney;
        }

        public void setPayFiatMoney(double payFiatMoney) {
            this.payFiatMoney = payFiatMoney;
        }

        public String getDeductionToken() {
            return deductionToken;
        }

        public void setDeductionToken(String deductionToken) {
            this.deductionToken = deductionToken;
        }

        public double getPayTokenAmount() {
            return payTokenAmount;
        }

        public void setPayTokenAmount(double payTokenAmount) {
            this.payTokenAmount = payTokenAmount;
        }

        public double getDeductionTokenAmount() {
            return deductionTokenAmount;
        }

        public void setDeductionTokenAmount(double deductionTokenAmount) {
            this.deductionTokenAmount = deductionTokenAmount;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getPayTokenPrice() {
            return payTokenPrice;
        }

        public void setPayTokenPrice(double payTokenPrice) {
            this.payTokenPrice = payTokenPrice;
        }

        public int getNumberOfPeople() {
            return numberOfPeople;
        }

        public void setNumberOfPeople(int numberOfPeople) {
            this.numberOfPeople = numberOfPeople;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}

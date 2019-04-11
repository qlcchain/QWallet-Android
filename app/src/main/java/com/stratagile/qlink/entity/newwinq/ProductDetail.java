package com.stratagile.qlink.entity.newwinq;

import com.stratagile.qlink.entity.BaseBack;

public class ProductDetail extends BaseBack {

    /**
     * data : {"timeLimit":30,"annualIncomeRate":0.5,"name":"超级回馈","leastAmount":5000,"point":"高收益","status":"ON_SALE"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timeLimit : 30
         * annualIncomeRate : 0.5
         * name : 超级回馈
         * leastAmount : 5000
         * point : 高收益
         * status : ON_SALE
         */

        private int timeLimit;
        private double annualIncomeRate;
        private String name;
        private int leastAmount;
        private String point;
        private String status;

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public double getAnnualIncomeRate() {
            return annualIncomeRate;
        }

        public void setAnnualIncomeRate(double annualIncomeRate) {
            this.annualIncomeRate = annualIncomeRate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLeastAmount() {
            return leastAmount;
        }

        public void setLeastAmount(int leastAmount) {
            this.leastAmount = leastAmount;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

package com.stratagile.qlink.entity.newwinq;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class Product extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timeLimit : 30
         * annualIncomeRate : 0.2
         * name : QLC锁仓30天
         * id : a721b33c572742d097e36c75f0089ce4
         * leastAmount : 100
         * status : ON_SALE
         */

        private int timeLimit;
        private double annualIncomeRate;
        private String name;
        private String id;
        private int leastAmount;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getLeastAmount() {
            return leastAmount;
        }

        public void setLeastAmount(int leastAmount) {
            this.leastAmount = leastAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

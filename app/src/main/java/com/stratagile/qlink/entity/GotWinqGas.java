package com.stratagile.qlink.entity;

public class GotWinqGas extends BaseBack {

    /**
     * data : {"receiveNum":0.01347419,"tips":"Receive success,It takes 1-2 minutes to arrive."}
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
         * receiveNum : 0.01347419
         * tips : Receive success,It takes 1-2 minutes to arrive.
         */

        private double receiveNum;
        private String tips;

        public double getReceiveNum() {
            return receiveNum;
        }

        public void setReceiveNum(double receiveNum) {
            this.receiveNum = receiveNum;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }
}

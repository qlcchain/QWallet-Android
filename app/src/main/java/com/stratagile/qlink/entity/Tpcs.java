package com.stratagile.qlink.entity;

import java.util.List;

public class Tpcs extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * symbol : QLC
         * lowPrice : 0.00000836
         * highPrice : 0.00000930
         * coinVal : 0.0
         * priceChangePercent : -0.355
         * lastPrice : 0.00000841
         */

        private String symbol;
        private String lowPrice;
        private String highPrice;
        private double coinVal;
        private String priceChangePercent;
        private String lastPrice;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getLowPrice() {
            return lowPrice;
        }

        public void setLowPrice(String lowPrice) {
            this.lowPrice = lowPrice;
        }

        public String getHighPrice() {
            return highPrice;
        }

        public void setHighPrice(String highPrice) {
            this.highPrice = highPrice;
        }

        public double getCoinVal() {
            return coinVal;
        }

        public void setCoinVal(double coinVal) {
            this.coinVal = coinVal;
        }

        public String getPriceChangePercent() {
            return priceChangePercent;
        }

        public void setPriceChangePercent(String priceChangePercent) {
            this.priceChangePercent = priceChangePercent;
        }

        public String getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(String lastPrice) {
            this.lastPrice = lastPrice;
        }
    }
}

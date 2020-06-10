package com.stratagile.qlink.entity.defi;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class DefiStateList extends BaseBack {

    private List<HistoricalStatsListBean> historicalStatsList;

    public List<HistoricalStatsListBean> getHistoricalStatsList() {
        return historicalStatsList;
    }

    public void setHistoricalStatsList(List<HistoricalStatsListBean> historicalStatsList) {
        this.historicalStatsList = historicalStatsList;
    }

    public static class HistoricalStatsListBean {
        /**
         * btc : 1124.7951353
         * statsDate : 2020-05-21 08:00:00
         * usdPoor : 4222622.0
         * eth : 2001454.284678123
         * tvlEth : 0.0
         * ethPoor : 0.0
         * dai : 1.1790774694731107E7
         * tvlUsd : 4.58023606E8
         */

        private double btc;
        private String statsDate;
        private double usdPoor;
        private double eth;
        private double tvlEth;
        private double ethPoor;
        private double dai;
        private double tvlUsd;

        public double getBtc() {
            return btc;
        }

        public void setBtc(double btc) {
            this.btc = btc;
        }

        public String getStatsDate() {
            return statsDate;
        }

        public void setStatsDate(String statsDate) {
            this.statsDate = statsDate;
        }

        public double getUsdPoor() {
            return usdPoor;
        }

        public void setUsdPoor(double usdPoor) {
            this.usdPoor = usdPoor;
        }

        public double getEth() {
            return eth;
        }

        public void setEth(double eth) {
            this.eth = eth;
        }

        public double getTvlEth() {
            return tvlEth;
        }

        public void setTvlEth(double tvlEth) {
            this.tvlEth = tvlEth;
        }

        public double getEthPoor() {
            return ethPoor;
        }

        public void setEthPoor(double ethPoor) {
            this.ethPoor = ethPoor;
        }

        public double getDai() {
            return dai;
        }

        public void setDai(double dai) {
            this.dai = dai;
        }

        public double getTvlUsd() {
            return tvlUsd;
        }

        public void setTvlUsd(double tvlUsd) {
            this.tvlUsd = tvlUsd;
        }
    }
}

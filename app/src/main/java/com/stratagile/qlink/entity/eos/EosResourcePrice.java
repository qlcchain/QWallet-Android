package com.stratagile.qlink.entity.eos;

import com.stratagile.qlink.entity.BaseBack;

public class EosResourcePrice extends BaseBack {

    /**
     * data : {"cpuPrice":1.2415,"ramPrice":0.0637,"cpuPriceUnit":"EOS/ms/Day","netPriceUnit":"EOS/KB/Day","ramPriceUnit":"EOS/KB","netPrice":4.0E-4}
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
         * cpuPrice : 1.2415
         * ramPrice : 0.0637
         * cpuPriceUnit : EOS/ms/Day
         * netPriceUnit : EOS/KB/Day
         * ramPriceUnit : EOS/KB
         * netPrice : 4.0E-4
         */

        private double cpuPrice;
        private double ramPrice;
        private String cpuPriceUnit;
        private String netPriceUnit;
        private String ramPriceUnit;
        private double netPrice;

        public double getCpuPrice() {
            return cpuPrice;
        }

        public void setCpuPrice(double cpuPrice) {
            this.cpuPrice = cpuPrice;
        }

        public double getRamPrice() {
            return ramPrice;
        }

        public void setRamPrice(double ramPrice) {
            this.ramPrice = ramPrice;
        }

        public String getCpuPriceUnit() {
            return cpuPriceUnit;
        }

        public void setCpuPriceUnit(String cpuPriceUnit) {
            this.cpuPriceUnit = cpuPriceUnit;
        }

        public String getNetPriceUnit() {
            return netPriceUnit;
        }

        public void setNetPriceUnit(String netPriceUnit) {
            this.netPriceUnit = netPriceUnit;
        }

        public String getRamPriceUnit() {
            return ramPriceUnit;
        }

        public void setRamPriceUnit(String ramPriceUnit) {
            this.ramPriceUnit = ramPriceUnit;
        }

        public double getNetPrice() {
            return netPrice;
        }

        public void setNetPrice(double netPrice) {
            this.netPrice = netPrice;
        }
    }
}

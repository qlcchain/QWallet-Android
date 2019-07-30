package com.stratagile.qlink.entity;

public class WinqGasBack extends BaseBack<WinqGasBack.DataBean> {

    /**
     * data : {"winqGas":0}
     */

//    private DataBean data;
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * winqGas : 0
         */

        private double winqGas;

        public double getWinqGas() {
            return winqGas;
        }

        public void setWinqGas(double winqGas) {
            this.winqGas = winqGas;
        }
    }
}

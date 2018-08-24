package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class Balance extends BaseBack {

    /**
     * data : {"QLC":"0","NEO":1,"GAS":0.5}
     *
     *
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
         * QLC : 0
         * NEO : 1
         * GAS : 0.5
         */

        private double QLC;
        private int NEO;
        private double GAS;

        public double getQLC() {
            return QLC;
        }

        public void setQLC(double QLC) {
            this.QLC = QLC;
        }

        public int getNEO() {
            return NEO;
        }

        public void setNEO(int NEO) {
            this.NEO = NEO;
        }

        public double getGAS() {
            return GAS;
        }

        public void setGAS(double GAS) {
            this.GAS = GAS;
        }
    }
}

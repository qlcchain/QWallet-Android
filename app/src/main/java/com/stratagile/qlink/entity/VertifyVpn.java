package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class VertifyVpn extends BaseBack<VertifyVpn.DataBean> {

    /**
     * data : {"isExist":true,"qlc":0}
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
         * isExist : true
         * qlc : 0.0
         */

        private boolean isExist;
        private double qlc;

        public boolean isIsExist() {
            return isExist;
        }

        public void setIsExist(boolean isExist) {
            this.isExist = isExist;
        }

        public double getQlc() {
            return qlc;
        }

        public void setQlc(double qlc) {
            this.qlc = qlc;
        }
    }
}

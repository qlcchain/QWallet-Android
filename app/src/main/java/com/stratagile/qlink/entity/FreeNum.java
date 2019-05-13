package com.stratagile.qlink.entity;

public class FreeNum extends BaseBack<FreeNum.DataBean> {

    /**
     * data : {"freeNum":3}
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
         * freeNum : 3
         */

        private int freeNum;

        public int getFreeNum() {
            return freeNum;
        }

        public void setFreeNum(int freeNum) {
            this.freeNum = freeNum;
        }
    }
}

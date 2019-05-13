package com.stratagile.qlink.entity;

public class ServerTime extends BaseBack<ServerTime.DataBean> {

    /**
     * data : {"sysTime":"2018-06-12 11:00:33"}
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
         * sysTime : 2018-06-12 11:00:33
         */

        private String sysTime;

        public String getSysTime() {
            return sysTime;
        }

        public void setSysTime(String sysTime) {
            this.sysTime = sysTime;
        }
    }
}

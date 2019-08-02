package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

public class SysTime extends BaseBack<SysTime.DataBean> {

    /**
     * data : {"sysTime":"2019-08-02 11:59:23"}
     */


    public static class DataBean {
        /**
         * sysTime : 2019-08-02 11:59:23
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

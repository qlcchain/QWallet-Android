package com.stratagile.qlink.entity;


import com.google.gson.annotations.SerializedName;

public class AppVersion extends BaseBack<AppVersion.DataBean> {

    /**
     * data : {"version_time":"2019-08-12 13:52:03","forced_updates":"false","version_number":"1#1.1.1","version_des":".......................................................................rrtrtrtrtrtrtrtrtrtrtrtrtrtrt"}
     */

    public static class DataBean {
        /**
         * version_time : 2019-08-12 13:52:03
         * forced_updates : false
         * version_number : 1#1.1.1
         * version_des : .......................................................................rrtrtrtrtrtrtrtrtrtrtrtrtrtrt
         */

        private String version_time;
        private String forced_updates;
        private String version_number;
        private String version_des;

        public String getVersion_time() {
            return version_time;
        }

        public void setVersion_time(String version_time) {
            this.version_time = version_time;
        }

        public String getForced_updates() {
            return forced_updates;
        }

        public void setForced_updates(String forced_updates) {
            this.forced_updates = forced_updates;
        }

        public String getVersion_number() {
            return version_number;
        }

        public void setVersion_number(String version_number) {
            this.version_number = version_number;
        }

        public String getVersion_des() {
            return version_des;
        }

        public void setVersion_des(String version_des) {
            this.version_des = version_des;
        }
    }
}

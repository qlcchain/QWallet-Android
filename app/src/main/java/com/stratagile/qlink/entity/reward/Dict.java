package com.stratagile.qlink.entity.reward;

import com.google.gson.annotations.SerializedName;
import com.stratagile.qlink.entity.BaseBack;

public class Dict extends BaseBack<Dict.DataBean> {

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    /**
     * data : {"value":"1500"}
     */

    private long currentTimeMillis;
    public static class DataBean {
        /**
         * value : 1500
         */

        private String value;
        private String topopGroupEndDate;
        private String topupGroupStartDate;

        public String getTopopGroupEndDate() {
            return topopGroupEndDate;
        }

        public void setTopopGroupEndDate(String topopGroupEndDate) {
            this.topopGroupEndDate = topopGroupEndDate;
        }

        public String getTopupGroupStartDate() {
            return topupGroupStartDate;
        }

        public void setTopupGroupStartDate(String topupGroupStartDate) {
            this.topupGroupStartDate = topupGroupStartDate;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

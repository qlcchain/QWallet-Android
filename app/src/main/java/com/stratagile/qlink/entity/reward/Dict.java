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
        private String burnQgasVoteStartDate;

        public String getBurnQgasVoteStartDate() {
            return burnQgasVoteStartDate;
        }

        public void setBurnQgasVoteStartDate(String burnQgasVoteStartDate) {
            this.burnQgasVoteStartDate = burnQgasVoteStartDate;
        }

        public String getBurnQgasVoteEndDate() {
            return burnQgasVoteEndDate;
        }

        public void setBurnQgasVoteEndDate(String burnQgasVoteEndDate) {
            this.burnQgasVoteEndDate = burnQgasVoteEndDate;
        }

        private String burnQgasVoteEndDate;

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

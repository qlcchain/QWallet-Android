package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/2/11.
 */

public class RecordVpn extends BaseBack {

    /**
     * data : {"result":true,"recordId":"a8c06e0f28ce4756a8727225d3250ae5"}
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
         * result : true
         * recordId : a8c06e0f28ce4756a8727225d3250ae5
         */

        private boolean result;
        private String recordId;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }
    }
}

package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/1/26.
 */

public class BuyQlc extends BaseBack {

    /**
     * data : {"result":true,"txid":"78897871dd6e8f79c557bf2a2f65c0b36938df0d4efb8e0ee49ac2c7a3f186f0"}
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
         * txid : 78897871dd6e8f79c557bf2a2f65c0b36938df0d4efb8e0ee49ac2c7a3f186f0
         */

        private boolean result;
        private String txid;
        private String recordId;

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }
    }
}

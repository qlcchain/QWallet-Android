package com.stratagile.qlink.entity;

public class TransactionResult extends BaseBack<TransactionResult.DataBean> {

    /**
     * data : {"recordId":"bc986634644e4fc5810a7ee5539e3847","operationResult":true}
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
         * recordId : bc986634644e4fc5810a7ee5539e3847
         * operationResult : true
         */

        private String recordId;
        private boolean operationResult;

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public boolean isOperationResult() {
            return operationResult;
        }

        public void setOperationResult(boolean operationResult) {
            this.operationResult = operationResult;
        }
    }
}

package com.stratagile.qlink.entity;

public class NeoTransfer extends BaseBack<NeoTransfer.DataBean> {

    /**
     * data : {"transferResult":true}
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
         * transferResult : true
         */

        private boolean transferResult;

        public boolean isTransferResult() {
            return transferResult;
        }

        public void setTransferResult(boolean transferResult) {
            this.transferResult = transferResult;
        }
    }
}

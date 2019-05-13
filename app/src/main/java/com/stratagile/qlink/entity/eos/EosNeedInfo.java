package com.stratagile.qlink.entity.eos;

import com.stratagile.qlink.entity.BaseBack;

public class EosNeedInfo extends BaseBack<EosNeedInfo.DataBean> {

    /**
     * data : {"ethAmount":"0.015"}
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
         * ethAmount : 0.015
         */

        private String ethAmount;

        public String getEthAddress() {
            return ethAddress;
        }

        public void setEthAddress(String ethAddress) {
            this.ethAddress = ethAddress;
        }

        private String ethAddress;

        public String getEthAmount() {
            return ethAmount;
        }

        public void setEthAmount(String ethAmount) {
            this.ethAmount = ethAmount;
        }
    }
}

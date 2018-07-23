package com.stratagile.qlink.entity;

import java.util.List;

public class FreeRecord extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fromP2pId : 2BF63D03CA68AA5B8BD75B80319DE58066A7251B8A6E14A0A1B5718247BC5F47FF5A275DBF35
         * toP2pId :
         * toAddress :
         * assetName : Rewards for Newcomer
         * freeType : 1
         * time : 2018-07-18 14:08:47
         */

        private String fromP2pId;
        private String toP2pId;
        private String toAddress;
        private String assetName;
        private int freeType;
        private String time;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;

        public String getFromP2pId() {
            return fromP2pId;
        }

        public void setFromP2pId(String fromP2pId) {
            this.fromP2pId = fromP2pId;
        }

        public String getToP2pId() {
            return toP2pId;
        }

        public void setToP2pId(String toP2pId) {
            this.toP2pId = toP2pId;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public int getFreeType() {
            return freeType;
        }

        public void setFreeType(int freeType) {
            this.freeType = freeType;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}

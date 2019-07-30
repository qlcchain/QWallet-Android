package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/2/2.
 */

public class Reward extends BaseBack<Reward.DataBean> {

    /**
     * data : {"toP2pId":"6B3B83D9C99DB40778012E414377DAE2041C1C0D2392AE7E96A8BC1EE45A916E03AF89FD1A80","recordId":"afc6ad74839e4409a1c87fd11f778b63","time":"2018-02-02 16:04:45","qlc":3.9,"formP2pId":"B773C104F30BE7875A85D08DAE0F39E1B81448776D3B38BD96BEB877D1484B078238BCC9508F","addressFrom":"AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo","addressTo":"AMYHqUaN5odrZKwXssoqmGtfKeS16uEy7M","isGratuity":"rewardSuccess"}
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
         * toP2pId : 6B3B83D9C99DB40778012E414377DAE2041C1C0D2392AE7E96A8BC1EE45A916E03AF89FD1A80
         * recordId : afc6ad74839e4409a1c87fd11f778b63
         * time : 2018-02-02 16:04:45
         * qlc : 3.9
         * formP2pId : B773C104F30BE7875A85D08DAE0F39E1B81448776D3B38BD96BEB877D1484B078238BCC9508F
         * addressFrom : AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo
         * addressTo : AMYHqUaN5odrZKwXssoqmGtfKeS16uEy7M
         * isGratuity : rewardSuccess
         */

        private String toP2pId;
        private String recordId;
        private String time;
        private double qlc;
        private String formP2pId;
        private String addressFrom;
        private String addressTo;
        private String isGratuity;

        public String getToP2pId() {
            return toP2pId;
        }

        public void setToP2pId(String toP2pId) {
            this.toP2pId = toP2pId;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getQlc() {
            return qlc;
        }

        public void setQlc(double qlc) {
            this.qlc = qlc;
        }

        public String getFormP2pId() {
            return formP2pId;
        }

        public void setFormP2pId(String formP2pId) {
            this.formP2pId = formP2pId;
        }

        public String getAddressFrom() {
            return addressFrom;
        }

        public void setAddressFrom(String addressFrom) {
            this.addressFrom = addressFrom;
        }

        public String getAddressTo() {
            return addressTo;
        }

        public void setAddressTo(String addressTo) {
            this.addressTo = addressTo;
        }

        public String getIsGratuity() {
            return isGratuity;
        }

        public void setIsGratuity(String isGratuity) {
            this.isGratuity = isGratuity;
        }
    }
}

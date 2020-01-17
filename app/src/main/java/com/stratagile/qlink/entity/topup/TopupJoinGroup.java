package com.stratagile.qlink.entity.topup;

import com.stratagile.qlink.entity.BaseBack;

public class TopupJoinGroup extends BaseBack {

    /**
     * item : {"head":"/data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg","payFiatMoney":3.68,"id":"200a45c126144d7b9d1796d9537fd43a","deductionToken":"QGAS","payTokenAmount":2.45,"userId":"bafe415310bd41fdb055fb0fe6cd1080","deductionTokenAmount":4.089,"payTokenPrice":1,"status":"NEW","payToken":"QLC","createDate":"2020-01-15 09:45:11"}
     */

    private GroupItemList.ItemListBean item;

    public GroupItemList.ItemListBean getItem() {
        return item;
    }

    public void setItem(GroupItemList.ItemListBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * head : /data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg
         * payFiatMoney : 3.68
         * id : 200a45c126144d7b9d1796d9537fd43a
         * deductionToken : QGAS
         * payTokenAmount : 2.45
         * userId : bafe415310bd41fdb055fb0fe6cd1080
         * deductionTokenAmount : 4.089
         * payTokenPrice : 1.0
         * status : NEW
         * payToken : QLC
         * createDate : 2020-01-15 09:45:11
         */

        private String head;
        private double payFiatMoney;
        private String id;
        private String deductionToken;
        private double payTokenAmount;
        private String userId;
        private double deductionTokenAmount;
        private double payTokenPrice;
        private String status;
        private String payToken;
        private String createDate;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public double getPayFiatMoney() {
            return payFiatMoney;
        }

        public void setPayFiatMoney(double payFiatMoney) {
            this.payFiatMoney = payFiatMoney;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeductionToken() {
            return deductionToken;
        }

        public void setDeductionToken(String deductionToken) {
            this.deductionToken = deductionToken;
        }

        public double getPayTokenAmount() {
            return payTokenAmount;
        }

        public void setPayTokenAmount(double payTokenAmount) {
            this.payTokenAmount = payTokenAmount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getDeductionTokenAmount() {
            return deductionTokenAmount;
        }

        public void setDeductionTokenAmount(double deductionTokenAmount) {
            this.deductionTokenAmount = deductionTokenAmount;
        }

        public double getPayTokenPrice() {
            return payTokenPrice;
        }

        public void setPayTokenPrice(double payTokenPrice) {
            this.payTokenPrice = payTokenPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}

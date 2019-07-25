package com.stratagile.qlink.entity.otc;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class TradeOrderList extends BaseBack<TradeOrderList.OrderListBean> {


    private List<OrderListBean> orderList;

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {

        /**
         * unitPrice : 0.001
         * appealStatus : FAIL
         * buyerId : 7060628a65e4450690976bf56c127787
         * usdtAmount : 0.05
         * head : /data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg
         * number : 20190719111929654715
         * qgasAmount : 50.0
         * sellerId : bafe415310bd41fdb055fb0fe6cd1080
         * nickname : hzpa
         * entrustOrderId : 36c6bb2e05df4ad9ad6cd8f4d5fe9be4
         * id : ec841bb576564b6cb9158eed0ca8529f
         * status : USDT_PAID
         * createDate : 2019-07-19 11:19:30
         */

        private double unitPrice;
        private String appealStatus;
        private String buyerId;
        private double usdtAmount;
        private String head;
        private String number;
        private double qgasAmount;
        private String sellerId;
        private String nickname;
        private String entrustOrderId;
        private String id;
        private String status;
        private String createDate;

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getAppealStatus() {
            return appealStatus;
        }

        public void setAppealStatus(String appealStatus) {
            this.appealStatus = appealStatus;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public double getUsdtAmount() {
            return usdtAmount;
        }

        public void setUsdtAmount(double usdtAmount) {
            this.usdtAmount = usdtAmount;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public double getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(double qgasAmount) {
            this.qgasAmount = qgasAmount;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getEntrustOrderId() {
            return entrustOrderId;
        }

        public void setEntrustOrderId(String entrustOrderId) {
            this.entrustOrderId = entrustOrderId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}

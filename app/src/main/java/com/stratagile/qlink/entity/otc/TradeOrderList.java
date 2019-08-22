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
         * unitPrice : 1.0
         * appealStatus : NO
         * buyerId : 7060628a65e4450690976bf56c127787
         * usdtAmount : 10.0
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 20190821112115263366
         * qgasAmount : 10.0
         * sellerId : bafe415310bd41fdb055fb0fe6cd1080
         * nickname : hzp
         * entrustOrderId : 7dc7eb7f78554e6985c5b76020a67731
         * tradeToken : QGAS
         * id : 8593d9ffc8484c699318b79eec489cc1
         * status : TRADE_TOKEN_TO_PLATFORM
         * createDate : 2019-08-21 11:21:16
         * payToken : QLC
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
        private String tradeToken;
        private String id;
        private String status;
        private String createDate;
        private String payToken;

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

        public String getTradeToken() {
            return tradeToken;
        }

        public void setTradeToken(String tradeToken) {
            this.tradeToken = tradeToken;
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

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }
    }
}

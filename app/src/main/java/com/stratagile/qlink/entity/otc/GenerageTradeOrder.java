package com.stratagile.qlink.entity.otc;

import com.stratagile.qlink.entity.BaseBack;

public class GenerageTradeOrder extends BaseBack<GenerageTradeOrder.OrderBean> {

    /**
     * order : {"unitPrice":1,"appealStatus":"NO","txid":"","buyerId":"bafe415310bd41fdb055fb0fe6cd1080","payTokenAmount":1,"closeDate":"","head":"/data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg","number":"20190821100236270718","tradeTokenAmount":1,"tradeTokenFromAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","buyerConfirmDate":"","sellerId":"7060628a65e4450690976bf56c127787","orderTime":"2019-08-21 10:02:36","payTokenFromAddress":"","nickname":"hzp","payTokenToAddress":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","entrustOrderId":"50212355d722444fb77440a4ca7d8992","tradeTokenTransferAddress":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","tradeToken":"QGAS","id":"dfd9470d89bc4bc4b58c3d84da723524","sellerConfirmDate":"","tradeTokenToAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","status":"TRADE_TOKEN_TO_PLATFORM","payToken":"QLC"}
     */

    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean {

        /**
         * unitPrice : 1.0
         * qgasFromAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * appealStatus : NO
         * txid :
         * buyerId : bafe415310bd41fdb055fb0fe6cd1080
         * usdtFromAddress :
         * qgasToAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * usdtAmount : 10
         * closeDate :
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 20190821141140332734
         * qgasAmount : 10
         * buyerConfirmDate :
         * sellerId : 7060628a65e4450690976bf56c127787
         * orderTime : 2019-08-21 14:11:40
         * usdtToAddress : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * nickname : hzp
         * entrustOrderId : 50212355d722444fb77440a4ca7d8992
         * tradeToken : QGAS
         * id : 9a618a4c89304b57a267eeadbdc87882
         * qgasTransferAddress : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
         * sellerConfirmDate :
         * status : QGAS_TO_PLATFORM
         * payToken : QLC
         */

        private double unitPrice;
        private String qgasFromAddress;
        private String appealStatus;
        private String txid;
        private String buyerId;
        private String usdtFromAddress;
        private String qgasToAddress;
        private float usdtAmount;
        private String closeDate;
        private String head;
        private String number;
        private int qgasAmount;
        private String buyerConfirmDate;
        private String sellerId;
        private String orderTime;
        private String usdtToAddress;
        private String nickname;
        private String entrustOrderId;
        private String tradeToken;

        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
        }

        private String id;
        private String qgasTransferAddress;
        private String sellerConfirmDate;
        private String tradeTokenChain;
        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
        }

        private String status;
        private String payToken;
        private String payTokenChain;

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getQgasFromAddress() {
            return qgasFromAddress;
        }

        public void setQgasFromAddress(String qgasFromAddress) {
            this.qgasFromAddress = qgasFromAddress;
        }

        public String getAppealStatus() {
            return appealStatus;
        }

        public void setAppealStatus(String appealStatus) {
            this.appealStatus = appealStatus;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getUsdtFromAddress() {
            return usdtFromAddress;
        }

        public void setUsdtFromAddress(String usdtFromAddress) {
            this.usdtFromAddress = usdtFromAddress;
        }

        public String getQgasToAddress() {
            return qgasToAddress;
        }

        public void setQgasToAddress(String qgasToAddress) {
            this.qgasToAddress = qgasToAddress;
        }

        public float getUsdtAmount() {
            return usdtAmount;
        }

        public void setUsdtAmount(float usdtAmount) {
            this.usdtAmount = usdtAmount;
        }

        public String getCloseDate() {
            return closeDate;
        }

        public void setCloseDate(String closeDate) {
            this.closeDate = closeDate;
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

        public int getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(int qgasAmount) {
            this.qgasAmount = qgasAmount;
        }

        public String getBuyerConfirmDate() {
            return buyerConfirmDate;
        }

        public void setBuyerConfirmDate(String buyerConfirmDate) {
            this.buyerConfirmDate = buyerConfirmDate;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getUsdtToAddress() {
            return usdtToAddress;
        }

        public void setUsdtToAddress(String usdtToAddress) {
            this.usdtToAddress = usdtToAddress;
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

        public String getQgasTransferAddress() {
            return qgasTransferAddress;
        }

        public void setQgasTransferAddress(String qgasTransferAddress) {
            this.qgasTransferAddress = qgasTransferAddress;
        }

        public String getSellerConfirmDate() {
            return sellerConfirmDate;
        }

        public void setSellerConfirmDate(String sellerConfirmDate) {
            this.sellerConfirmDate = sellerConfirmDate;
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
    }
}

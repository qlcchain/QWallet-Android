package com.stratagile.qlink.entity.otc;

import com.stratagile.qlink.entity.BaseBack;

public class GenerageTradeOrder extends BaseBack<GenerageTradeOrder.OrderBean> {

    /**
     * order : {"unitPrice":0.1,"qgasFromAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","appealStatus":"NO","txid":"","buyerId":"7060628a65e4450690976bf56c127787","usdtFromAddress":"","qgasToAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","usdtAmount":0.1,"closeDate":"","head":"/data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg","number":"20190731143923846241","qgasAmount":1,"buyerConfirmDate":"","sellerId":"7060628a65e4450690976bf56c127787","orderTime":"2019-07-31 14:39:23","usdtToAddress":"0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C","nickname":"hzp","entrustOrderId":"f5a662d5876948d8bf53abe10bc84025","id":"931ff0f74ec6477abf347c266df74b9e","qgasTransferAddress":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","sellerConfirmDate":"","status":"QGAS_TO_PLATFORM"}
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
         * unitPrice : 0.1
         * qgasFromAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * appealStatus : NO
         * txid :
         * buyerId : 7060628a65e4450690976bf56c127787
         * usdtFromAddress :
         * qgasToAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * usdtAmount : 0.1
         * closeDate :
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 20190731143923846241
         * qgasAmount : 1
         * buyerConfirmDate :
         * sellerId : 7060628a65e4450690976bf56c127787
         * orderTime : 2019-07-31 14:39:23
         * usdtToAddress : 0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C
         * nickname : hzp
         * entrustOrderId : f5a662d5876948d8bf53abe10bc84025
         * id : 931ff0f74ec6477abf347c266df74b9e
         * qgasTransferAddress : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
         * sellerConfirmDate :
         * status : QGAS_TO_PLATFORM
         */

        private double unitPrice;
        private String qgasFromAddress;
        private String appealStatus;
        private String txid;
        private String buyerId;
        private String usdtFromAddress;
        private String qgasToAddress;
        private double usdtAmount;
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
        private String id;
        private String qgasTransferAddress;
        private String sellerConfirmDate;
        private String status;

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

        public double getUsdtAmount() {
            return usdtAmount;
        }

        public void setUsdtAmount(double usdtAmount) {
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
    }
}

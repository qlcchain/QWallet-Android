package com.stratagile.qlink.entity;

public class TradeOrder extends BaseBack<TradeOrder.OrderBean> {

    /**
     * order : {"appealStatus":"NO","buyerId":"864dd51d5f874152a1f89a9e73384176","qgasToAddress":"qlc_3uxypgx1aotqbx7fcy3pg8bfngu75ng8usp1nkwj7isugqxmind38u31x98m","tradeTokenChain":"QLC_CHAIN","usdtAmount":0.001,"head":"/data/dapp/head/e9f9e605d5964f4b8330caeee0ff7c13.jpg","number":"20191125155259629430","qgasAmount":1,"sellerId":"7060628a65e4450690976bf56c127787","orderTime":"2019-11-25 15:52:59","usdtToAddress":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","nickname":"6899680","payTokenChain":"NEO_CHAIN","tradeToken":"QGAS","id":"c407c890e6e94edca1ccd8f692832c20","unitPrice":0.001,"qgasFromAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","txid":"","usdtFromAddress":"","closeDate":"","buyerConfirmDate":"","entrustOrderId":"19569df1c1c74f318c5736249459dbeb","qgasTransferAddress":"qlc_34ye9r7qfzyrx1izbjgp453ickhgbtejwqnhpfsiow3wqiefxyezqthis5st","sellerConfirmDate":"","status":"NEW","payToken":"QLC"}
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
         * appealStatus : NO
         * buyerId : 864dd51d5f874152a1f89a9e73384176
         * qgasToAddress : qlc_3uxypgx1aotqbx7fcy3pg8bfngu75ng8usp1nkwj7isugqxmind38u31x98m
         * tradeTokenChain : QLC_CHAIN
         * usdtAmount : 0.001
         * head : /data/dapp/head/e9f9e605d5964f4b8330caeee0ff7c13.jpg
         * number : 20191125155259629430
         * qgasAmount : 1
         * sellerId : 7060628a65e4450690976bf56c127787
         * orderTime : 2019-11-25 15:52:59
         * usdtToAddress : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * nickname : 6899680
         * payTokenChain : NEO_CHAIN
         * tradeToken : QGAS
         * id : c407c890e6e94edca1ccd8f692832c20
         * unitPrice : 0.001
         * qgasFromAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * txid :
         * usdtFromAddress :
         * closeDate :
         * buyerConfirmDate :
         * entrustOrderId : 19569df1c1c74f318c5736249459dbeb
         * qgasTransferAddress : qlc_34ye9r7qfzyrx1izbjgp453ickhgbtejwqnhpfsiow3wqiefxyezqthis5st
         * sellerConfirmDate :
         * status : NEW
         * payToken : QLC
         */

        private String appealStatus;
        private String buyerId;
        private String qgasToAddress;
        private String tradeTokenChain;
        private double usdtAmount;
        private String head;
        private String number;
        private double qgasAmount;
        private String sellerId;
        private String orderTime;
        private String usdtToAddress;
        private String nickname;
        private String payTokenChain;
        private String tradeToken;
        private String id;
        private double unitPrice;
        private String qgasFromAddress;
        private String txid;
        private String usdtFromAddress;
        private String closeDate;
        private String buyerConfirmDate;
        private String entrustOrderId;
        private String qgasTransferAddress;
        private String sellerConfirmDate;
        private String status;
        private String payToken;

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

        public String getQgasToAddress() {
            return qgasToAddress;
        }

        public void setQgasToAddress(String qgasToAddress) {
            this.qgasToAddress = qgasToAddress;
        }

        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
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

        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
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

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getUsdtFromAddress() {
            return usdtFromAddress;
        }

        public void setUsdtFromAddress(String usdtFromAddress) {
            this.usdtFromAddress = usdtFromAddress;
        }

        public String getCloseDate() {
            return closeDate;
        }

        public void setCloseDate(String closeDate) {
            this.closeDate = closeDate;
        }

        public String getBuyerConfirmDate() {
            return buyerConfirmDate;
        }

        public void setBuyerConfirmDate(String buyerConfirmDate) {
            this.buyerConfirmDate = buyerConfirmDate;
        }

        public String getEntrustOrderId() {
            return entrustOrderId;
        }

        public void setEntrustOrderId(String entrustOrderId) {
            this.entrustOrderId = entrustOrderId;
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

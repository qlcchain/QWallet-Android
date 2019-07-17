package com.stratagile.qlink.entity.otc;

import com.stratagile.qlink.entity.BaseBack;

public class TradeOrderDetail extends BaseBack<TradeOrderDetail.OrderBean> {


    /**
     * order : {"unitPrice":0.001,"qgasFromAddress":"qlc_1ek43jimwtcg9efmgznbozt7zi4qyz73wz9zi6b65ydrzbsywxy897tznpxm","txid":"","buyerId":"7060628a65e4450690976bf56c127787","usdtFromAddress":"","qgasToAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","usdtAmount":0.01,"closeDate":"","head":"","qgasAmount":10,"sellerId":"61be9c09c0784827af303005f983c705","orderTime":"2019-07-17 16:22:31","usdtToAddress":"0x9B833b57eFF94c45C91C5D28302CF481a9c766D0","nickname":"ios_test","entrustOrderId":"4a74b1232302439b8029a227e5d5c9ea","id":"c320ec4bb4fb40d4b043eb744f57d171","qgasTransferAddress":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","status":"QGAS_TO_PLATFORM"}
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
         * unitPrice : 0.001
         * qgasFromAddress : qlc_1ek43jimwtcg9efmgznbozt7zi4qyz73wz9zi6b65ydrzbsywxy897tznpxm
         * txid :
         * buyerId : 7060628a65e4450690976bf56c127787
         * usdtFromAddress :
         * qgasToAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * usdtAmount : 0.01
         * closeDate :
         * head :
         * qgasAmount : 10.0
         * sellerId : 61be9c09c0784827af303005f983c705
         * orderTime : 2019-07-17 16:22:31
         * usdtToAddress : 0x9B833b57eFF94c45C91C5D28302CF481a9c766D0
         * nickname : ios_test
         * entrustOrderId : 4a74b1232302439b8029a227e5d5c9ea
         * id : c320ec4bb4fb40d4b043eb744f57d171
         * qgasTransferAddress : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
         * status : QGAS_TO_PLATFORM
         */

        private double unitPrice;
        private String qgasFromAddress;
        private String txid;
        private String buyerId;
        private String usdtFromAddress;
        private String qgasToAddress;
        private double usdtAmount;
        private String closeDate;
        private String head;
        private double qgasAmount;
        private String sellerId;
        private String orderTime;
        private String usdtToAddress;
        private String nickname;
        private String entrustOrderId;
        private String id;
        private String qgasTransferAddress;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

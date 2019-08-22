package com.stratagile.qlink.entity.otc;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

public class TradeOrderDetail extends BaseBack<TradeOrderDetail.OrderBean> {


    /**
     * order : {"reason":"是的","appealStatus":"YES","buyerId":"61be9c09c0784827af303005f983c705","photo2":"","qgasToAddress":"qlc_1ek43jimwtcg9efmgznbozt7zi4qyz73wz9zi6b65ydrzbsywxy897tznpxm","photo3":"","photo4":"","usdtAmount":0.001,"head":"","number":"20190722105915560044","qgasAmount":1,"sellerId":"7060628a65e4450690976bf56c127787","orderTime":"2019-07-22 10:59:16","usdtToAddress":"0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C","nickname":"ios_test","id":"392e7f2574b446afb2876bb810177d6c","unitPrice":0.001,"qgasFromAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","appealDate":"2019-07-22 11:58:45","txid":"0x102c94447cd732e56f816c3828315edf43edcc4b0475d6325170e80472e177ad","usdtFromAddress":"0x255eecd17e11c5d2ffd5818da31d04b5c1721d7c","closeDate":"","buyerConfirmDate":"2019-07-22 11:06:13","appealerId":"61be9c09c0784827af303005f983c705","photo1":"/data/dapp/head/03b8a58a175647569f726de6b6c0977f.jpg","entrustOrderId":"dff779e206a04e07bbcc4cc96faed827","qgasTransferAddress":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","sellerConfirmDate":"","auditFeedback":"","status":"USDT_PAID"}
     */

    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean implements Parcelable {
        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
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

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        /**
         * reason : 是的
         * appealStatus : YES
         * buyerId : 61be9c09c0784827af303005f983c705
         * photo2 :
         * qgasToAddress : qlc_1ek43jimwtcg9efmgznbozt7zi4qyz73wz9zi6b65ydrzbsywxy897tznpxm
         * photo3 :
         * photo4 :
         * usdtAmount : 0.001
         * head :
         * number : 20190722105915560044
         * qgasAmount : 1.0
         * sellerId : 7060628a65e4450690976bf56c127787
         * orderTime : 2019-07-22 10:59:16
         * usdtToAddress : 0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C
         * nickname : ios_test
         * id : 392e7f2574b446afb2876bb810177d6c
         * unitPrice : 0.001
         * qgasFromAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * appealDate : 2019-07-22 11:58:45
         * txid : 0x102c94447cd732e56f816c3828315edf43edcc4b0475d6325170e80472e177ad
         * usdtFromAddress : 0x255eecd17e11c5d2ffd5818da31d04b5c1721d7c
         * closeDate :
         * buyerConfirmDate : 2019-07-22 11:06:13
         * appealerId : 61be9c09c0784827af303005f983c705
         * photo1 : /data/dapp/head/03b8a58a175647569f726de6b6c0977f.jpg
         * entrustOrderId : dff779e206a04e07bbcc4cc96faed827
         * qgasTransferAddress : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
         * sellerConfirmDate :
         * auditFeedback :
         * status : USDT_PAID
         */

        private String reason;
        private String appealStatus;
        private String buyerId;
        private String photo2;
        private String qgasToAddress;
        private String photo3;
        private String photo4;
        private double usdtAmount;
        private String head;
        private String number;
        private double qgasAmount;
        private String sellerId;
        private String orderTime;
        private String usdtToAddress;
        private String nickname;
        private String id;
        private double unitPrice;
        private String qgasFromAddress;
        private String appealDate;
        private String txid;
        private String usdtFromAddress;
        private String closeDate;
        private String buyerConfirmDate;
        private String appealerId;
        private String photo1;
        private String entrustOrderId;
        private String qgasTransferAddress;
        private String sellerConfirmDate;
        private String auditFeedback;
        private String status;
        private String tradeTokenChain;
        private String payTokenChain;
        private String tradeToken;
        private String payToken;


        protected OrderBean(Parcel in) {
            reason = in.readString();
            appealStatus = in.readString();
            buyerId = in.readString();
            photo2 = in.readString();
            qgasToAddress = in.readString();
            photo3 = in.readString();
            photo4 = in.readString();
            usdtAmount = in.readDouble();
            head = in.readString();
            number = in.readString();
            qgasAmount = in.readDouble();
            sellerId = in.readString();
            orderTime = in.readString();
            usdtToAddress = in.readString();
            nickname = in.readString();
            id = in.readString();
            unitPrice = in.readDouble();
            qgasFromAddress = in.readString();
            appealDate = in.readString();
            txid = in.readString();
            usdtFromAddress = in.readString();
            closeDate = in.readString();
            buyerConfirmDate = in.readString();
            appealerId = in.readString();
            photo1 = in.readString();
            entrustOrderId = in.readString();
            qgasTransferAddress = in.readString();
            sellerConfirmDate = in.readString();
            auditFeedback = in.readString();
            status = in.readString();
            tradeTokenChain = in.readString();
            payTokenChain = in.readString();
            tradeToken = in.readString();
            payToken = in.readString();
        }

        public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
            @Override
            public OrderBean createFromParcel(Parcel in) {
                return new OrderBean(in);
            }

            @Override
            public OrderBean[] newArray(int size) {
                return new OrderBean[size];
            }
        };

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
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

        public String getPhoto2() {
            return photo2;
        }

        public void setPhoto2(String photo2) {
            this.photo2 = photo2;
        }

        public String getQgasToAddress() {
            return qgasToAddress;
        }

        public void setQgasToAddress(String qgasToAddress) {
            this.qgasToAddress = qgasToAddress;
        }

        public String getPhoto3() {
            return photo3;
        }

        public void setPhoto3(String photo3) {
            this.photo3 = photo3;
        }

        public String getPhoto4() {
            return photo4;
        }

        public void setPhoto4(String photo4) {
            this.photo4 = photo4;
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

        public String getAppealDate() {
            return appealDate;
        }

        public void setAppealDate(String appealDate) {
            this.appealDate = appealDate;
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

        public String getAppealerId() {
            return appealerId;
        }

        public void setAppealerId(String appealerId) {
            this.appealerId = appealerId;
        }

        public String getPhoto1() {
            return photo1;
        }

        public void setPhoto1(String photo1) {
            this.photo1 = photo1;
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

        public String getAuditFeedback() {
            return auditFeedback;
        }

        public void setAuditFeedback(String auditFeedback) {
            this.auditFeedback = auditFeedback;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(reason);
            parcel.writeString(appealStatus);
            parcel.writeString(buyerId);
            parcel.writeString(photo2);
            parcel.writeString(qgasToAddress);
            parcel.writeString(photo3);
            parcel.writeString(photo4);
            parcel.writeDouble(usdtAmount);
            parcel.writeString(head);
            parcel.writeString(number);
            parcel.writeDouble(qgasAmount);
            parcel.writeString(sellerId);
            parcel.writeString(orderTime);
            parcel.writeString(usdtToAddress);
            parcel.writeString(nickname);
            parcel.writeString(id);
            parcel.writeDouble(unitPrice);
            parcel.writeString(qgasFromAddress);
            parcel.writeString(appealDate);
            parcel.writeString(txid);
            parcel.writeString(usdtFromAddress);
            parcel.writeString(closeDate);
            parcel.writeString(buyerConfirmDate);
            parcel.writeString(appealerId);
            parcel.writeString(photo1);
            parcel.writeString(entrustOrderId);
            parcel.writeString(qgasTransferAddress);
            parcel.writeString(sellerConfirmDate);
            parcel.writeString(auditFeedback);
            parcel.writeString(status);
            parcel.writeString(tradeTokenChain);
            parcel.writeString(payTokenChain);
            parcel.writeString(tradeToken);
            parcel.writeString(payToken);
        }
    }
}

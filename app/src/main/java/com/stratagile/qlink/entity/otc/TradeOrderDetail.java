package com.stratagile.qlink.entity.otc;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

public class TradeOrderDetail extends BaseBack<TradeOrderDetail.OrderBean> {

    /**
     * order : {"unitPrice":0.001,"qgasFromAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","appealDate":"","txid":"0xf324e0617155565d5cdbfae408e598af6ba7ddb3d4f6e045c530b0368b74ab84","buyerId":"bafe415310bd41fdb055fb0fe6cd1080","usdtFromAddress":"0x980e7917c610e2c2d4e669c920980cb1b915bbc7","qgasToAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","usdtAmount":0.1,"closeDate":"","head":"/data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg","number":"20190718144850581968","qgasAmount":100,"buyerConfirmDate":"2019-07-18 15:08:39","sellerId":"7060628a65e4450690976bf56c127787","orderTime":"2019-07-18 14:48:50","usdtToAddress":"0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C","nickname":"hzp","entrustOrderId":"675ff58f8b764ae09e84de2ee1ffcb71","id":"590ff75289934a5ab3b4ebb3e543a1dc","qgasTransferAddress":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","sellerConfirmDate":"","status":"USDT_PAID"}
     */

    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean implements Parcelable {
        /**
         * unitPrice : 0.001
         * qgasFromAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * appealDate :
         * txid : 0xf324e0617155565d5cdbfae408e598af6ba7ddb3d4f6e045c530b0368b74ab84
         * buyerId : bafe415310bd41fdb055fb0fe6cd1080
         * usdtFromAddress : 0x980e7917c610e2c2d4e669c920980cb1b915bbc7
         * qgasToAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * usdtAmount : 0.1
         * closeDate :
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 20190718144850581968
         * qgasAmount : 100.0
         * buyerConfirmDate : 2019-07-18 15:08:39
         * sellerId : 7060628a65e4450690976bf56c127787
         * orderTime : 2019-07-18 14:48:50
         * usdtToAddress : 0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C
         * nickname : hzp
         * entrustOrderId : 675ff58f8b764ae09e84de2ee1ffcb71
         * id : 590ff75289934a5ab3b4ebb3e543a1dc
         * qgasTransferAddress : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
         * sellerConfirmDate :
         * status : USDT_PAID
         */

        private double unitPrice;
        private String qgasFromAddress;
        private String appealDate;
        private String txid;
        private String buyerId;
        private String usdtFromAddress;
        private String qgasToAddress;
        private double usdtAmount;
        private String closeDate;
        private String head;
        private String number;
        private double qgasAmount;
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

        protected OrderBean(Parcel in) {
            unitPrice = in.readDouble();
            qgasFromAddress = in.readString();
            appealDate = in.readString();
            txid = in.readString();
            buyerId = in.readString();
            usdtFromAddress = in.readString();
            qgasToAddress = in.readString();
            usdtAmount = in.readDouble();
            closeDate = in.readString();
            head = in.readString();
            number = in.readString();
            qgasAmount = in.readDouble();
            buyerConfirmDate = in.readString();
            sellerId = in.readString();
            orderTime = in.readString();
            usdtToAddress = in.readString();
            nickname = in.readString();
            entrustOrderId = in.readString();
            id = in.readString();
            qgasTransferAddress = in.readString();
            sellerConfirmDate = in.readString();
            status = in.readString();
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

        public double getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(double qgasAmount) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(unitPrice);
            parcel.writeString(qgasFromAddress);
            parcel.writeString(appealDate);
            parcel.writeString(txid);
            parcel.writeString(buyerId);
            parcel.writeString(usdtFromAddress);
            parcel.writeString(qgasToAddress);
            parcel.writeDouble(usdtAmount);
            parcel.writeString(closeDate);
            parcel.writeString(head);
            parcel.writeString(number);
            parcel.writeDouble(qgasAmount);
            parcel.writeString(buyerConfirmDate);
            parcel.writeString(sellerId);
            parcel.writeString(orderTime);
            parcel.writeString(usdtToAddress);
            parcel.writeString(nickname);
            parcel.writeString(entrustOrderId);
            parcel.writeString(id);
            parcel.writeString(qgasTransferAddress);
            parcel.writeString(sellerConfirmDate);
            parcel.writeString(status);
        }
    }
}

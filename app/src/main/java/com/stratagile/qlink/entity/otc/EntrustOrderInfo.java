package com.stratagile.qlink.entity.otc;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

public class EntrustOrderInfo extends BaseBack<EntrustOrderInfo.OrderBean> {

    /**
     * order : {"unitPrice":0.001,"minAmount":10,"qgasAddress":"","lockingAmount":50,"type":"SELL","completeAmount":0,"head":"/data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg","totalAmount":50,"usdtAddress":"0x980e7917C610E2c2D4e669c920980cB1b915bBc7","orderTime":"2019-07-16 13:38:21","nickname":"18670819116","id":"4050d1db715e4c79aa0d261ec874030d","maxAmount":50,"otcTimes":0,"status":"NORMAL"}
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
         * minAmount : 10.0
         * qgasAddress :
         * lockingAmount : 50.0
         * type : SELL
         * completeAmount : 0.0
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * totalAmount : 50.0
         * usdtAddress : 0x980e7917C610E2c2D4e669c920980cB1b915bBc7
         * orderTime : 2019-07-16 13:38:21
         * nickname : 18670819116
         * id : 4050d1db715e4c79aa0d261ec874030d
         * maxAmount : 50.0
         * otcTimes : 0
         * status : NORMAL
         */

        private double unitPrice;
        private double minAmount;
        private String qgasAddress;
        private double lockingAmount;
        private String type;
        private double completeAmount;
        private String head;
        private double totalAmount;
        private String usdtAddress;
        private String orderTime;
        private String nickname;
        private String id;
        private double maxAmount;
        private int otcTimes;
        private String status;

        protected OrderBean(Parcel in) {
            unitPrice = in.readDouble();
            minAmount = in.readDouble();
            qgasAddress = in.readString();
            lockingAmount = in.readDouble();
            type = in.readString();
            completeAmount = in.readDouble();
            head = in.readString();
            totalAmount = in.readDouble();
            usdtAddress = in.readString();
            orderTime = in.readString();
            nickname = in.readString();
            id = in.readString();
            maxAmount = in.readDouble();
            otcTimes = in.readInt();
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

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public String getQgasAddress() {
            return qgasAddress;
        }

        public void setQgasAddress(String qgasAddress) {
            this.qgasAddress = qgasAddress;
        }

        public double getLockingAmount() {
            return lockingAmount;
        }

        public void setLockingAmount(double lockingAmount) {
            this.lockingAmount = lockingAmount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getCompleteAmount() {
            return completeAmount;
        }

        public void setCompleteAmount(double completeAmount) {
            this.completeAmount = completeAmount;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getUsdtAddress() {
            return usdtAddress;
        }

        public void setUsdtAddress(String usdtAddress) {
            this.usdtAddress = usdtAddress;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
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

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public int getOtcTimes() {
            return otcTimes;
        }

        public void setOtcTimes(int otcTimes) {
            this.otcTimes = otcTimes;
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
            parcel.writeDouble(minAmount);
            parcel.writeString(qgasAddress);
            parcel.writeDouble(lockingAmount);
            parcel.writeString(type);
            parcel.writeDouble(completeAmount);
            parcel.writeString(head);
            parcel.writeDouble(totalAmount);
            parcel.writeString(usdtAddress);
            parcel.writeString(orderTime);
            parcel.writeString(nickname);
            parcel.writeString(id);
            parcel.writeDouble(maxAmount);
            parcel.writeInt(otcTimes);
            parcel.writeString(status);
        }
    }
}

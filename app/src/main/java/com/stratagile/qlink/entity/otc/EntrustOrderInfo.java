package com.stratagile.qlink.entity.otc;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

public class EntrustOrderInfo extends BaseBack<EntrustOrderInfo.OrderBean> {

    /**
     * order : {"unitPrice":0.001,"minAmount":1,"qgasAddress":"","lockingAmount":100,"type":"SELL","completeAmount":0,"head":"/data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg","number":"20190718162107137907","totalAmount":200,"usdtAddress":"0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C","orderTime":"2019-07-18 16:21:08","nickname":"hzp","id":"dff779e206a04e07bbcc4cc96faed827","maxAmount":200,"otcTimes":2,"status":"NORMAL"}
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
         * minAmount : 1.0
         * qgasAddress :
         * lockingAmount : 100.0
         * type : SELL
         * completeAmount : 0.0
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 20190718162107137907
         * totalAmount : 200.0
         * usdtAddress : 0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C
         * orderTime : 2019-07-18 16:21:08
         * nickname : hzp
         * id : dff779e206a04e07bbcc4cc96faed827
         * maxAmount : 200.0
         * otcTimes : 2
         * status : NORMAL
         */

        private double unitPrice;
        private double minAmount;
        private String qgasAddress;
        private double lockingAmount;
        private String type;
        private double completeAmount;
        private String head;

        protected OrderBean(Parcel in) {
            unitPrice = in.readDouble();
            minAmount = in.readDouble();
            qgasAddress = in.readString();
            lockingAmount = in.readDouble();
            type = in.readString();
            completeAmount = in.readDouble();
            head = in.readString();
            number = in.readString();
            totalAmount = in.readDouble();
            usdtAddress = in.readString();
            userId = in.readString();
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        private String number;
        private double totalAmount;
        private String usdtAddress;
        private String userId;
        private String orderTime;
        private String nickname;
        private String id;
        private double maxAmount;
        private int otcTimes;
        private String status;



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

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
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
            parcel.writeString(number);
            parcel.writeDouble(totalAmount);
            parcel.writeString(usdtAddress);
            parcel.writeString(userId);
            parcel.writeString(orderTime);
            parcel.writeString(nickname);
            parcel.writeString(id);
            parcel.writeDouble(maxAmount);
            parcel.writeInt(otcTimes);
            parcel.writeString(status);
        }
    }
}

package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class EntrustOrderList extends BaseBack<EntrustOrderList.OrderListBean> {


    private ArrayList<OrderListBean> orderList;

    public ArrayList<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean implements Parcelable {
        /**
         * unitPrice : 0.001
         * minAmount : 1.0
         * lockingAmount : 0.0
         * type : SELL
         * userId : 7060628a65e4450690976bf56c127787
         * completeAmount : 0.0
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 20190820155720763031
         * totalAmount : 100.0
         * orderTime : 2019-08-20 15:57:21
         * nickname : hzp
         * tradeToken : QGAS
         * id : 1b7b17cd27ad45fa990f0331ac8c5347
         * maxAmount : 100.0
         * otcTimes : 9
         * status : NORMAL
         * payToken : USDT
         */

        private double unitPrice;
        private double minAmount;
        private double lockingAmount;
        private String type;
        private String userId;
        private double completeAmount;
        private String head;
        private String number;
        private double totalAmount;
        private String orderTime;
        private String nickname;
        private String tradeToken;
        private String id;
        private double maxAmount;
        private int otcTimes;
        private String status;
        private String payToken;

        protected OrderListBean(Parcel in) {
            unitPrice = in.readDouble();
            minAmount = in.readDouble();
            lockingAmount = in.readDouble();
            type = in.readString();
            userId = in.readString();
            completeAmount = in.readDouble();
            head = in.readString();
            number = in.readString();
            totalAmount = in.readDouble();
            orderTime = in.readString();
            nickname = in.readString();
            tradeToken = in.readString();
            id = in.readString();
            maxAmount = in.readDouble();
            otcTimes = in.readInt();
            status = in.readString();
            payToken = in.readString();
        }

        public static final Creator<OrderListBean> CREATOR = new Creator<OrderListBean>() {
            @Override
            public OrderListBean createFromParcel(Parcel in) {
                return new OrderListBean(in);
            }

            @Override
            public OrderListBean[] newArray(int size) {
                return new OrderListBean[size];
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(unitPrice);
            parcel.writeDouble(minAmount);
            parcel.writeDouble(lockingAmount);
            parcel.writeString(type);
            parcel.writeString(userId);
            parcel.writeDouble(completeAmount);
            parcel.writeString(head);
            parcel.writeString(number);
            parcel.writeDouble(totalAmount);
            parcel.writeString(orderTime);
            parcel.writeString(nickname);
            parcel.writeString(tradeToken);
            parcel.writeString(id);
            parcel.writeDouble(maxAmount);
            parcel.writeInt(otcTimes);
            parcel.writeString(status);
            parcel.writeString(payToken);
        }
    }
}

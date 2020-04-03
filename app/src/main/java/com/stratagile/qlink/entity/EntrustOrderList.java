package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
         * unitPrice : 2.0
         * minAmount : 100.0
         * isBurnQgasOrder : 1
         * lockingAmount : 0.0
         * type : BUY
         * payTokenAmount : 40000.0
         * userId : 949caa0a0d8b4f2c81dd1750e8e867de
         * tradeTokenChain : QLC_CHAIN
         * completeAmount : 0.0
         * head : /data/dapp/head/d0b58c62e15445e4b26b93a2f18a138a.jpg
         * number : 20200227010000562142
         * totalAmount : 20000.0
         * orderTime : 2020-02-27 01:00:00
         * nickname : 黄大叔
         * payTokenChain : NEO_CHAIN
         * tradeToken : QGAS
         * id : 079ff6b193f54c769a9b5bd2c29ea7d6
         * maxAmount : 1000.0
         * otcTimes : 3
         * status : NORMAL
         * payToken : QLC
         */

        private double unitPrice;
        private double minAmount;
        private String isBurnQgasOrder;
        private double lockingAmount;
        private String type;
        private double payTokenAmount;
        private String userId;
        private String tradeTokenChain;
        private double completeAmount;
        private String head;
        private String number;
        private double totalAmount;
        private String orderTime;
        private String nickname;
        private String payTokenChain;
        private String tradeToken;
        private String id;
        private double maxAmount;
        private int otcTimes;
        private String status;
        private String payToken;

        protected OrderListBean(Parcel in) {
            unitPrice = in.readDouble();
            minAmount = in.readDouble();
            isBurnQgasOrder = in.readString();
            lockingAmount = in.readDouble();
            type = in.readString();
            payTokenAmount = in.readDouble();
            userId = in.readString();
            tradeTokenChain = in.readString();
            completeAmount = in.readDouble();
            head = in.readString();
            number = in.readString();
            totalAmount = in.readDouble();
            orderTime = in.readString();
            nickname = in.readString();
            payTokenChain = in.readString();
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

        public String getIsBurnQgasOrder() {
            return isBurnQgasOrder;
        }

        public void setIsBurnQgasOrder(String isBurnQgasOrder) {
            this.isBurnQgasOrder = isBurnQgasOrder;
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

        public double getPayTokenAmount() {
            return payTokenAmount;
        }

        public void setPayTokenAmount(double payTokenAmount) {
            this.payTokenAmount = payTokenAmount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
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
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(unitPrice);
            dest.writeDouble(minAmount);
            dest.writeString(isBurnQgasOrder);
            dest.writeDouble(lockingAmount);
            dest.writeString(type);
            dest.writeDouble(payTokenAmount);
            dest.writeString(userId);
            dest.writeString(tradeTokenChain);
            dest.writeDouble(completeAmount);
            dest.writeString(head);
            dest.writeString(number);
            dest.writeDouble(totalAmount);
            dest.writeString(orderTime);
            dest.writeString(nickname);
            dest.writeString(payTokenChain);
            dest.writeString(tradeToken);
            dest.writeString(id);
            dest.writeDouble(maxAmount);
            dest.writeInt(otcTimes);
            dest.writeString(status);
            dest.writeString(payToken);
        }
    }
}

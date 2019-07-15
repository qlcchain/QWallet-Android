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
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * totalAmount : 1000.0
         * minAmount : 100.0
         * nickname : 18670819116
         * id : f71e12acd7ef4765ae1399213719f982
         * type : BUY
         * maxAmount : 1000.0
         * otcTimes : 0
         * status : NORMAL
         */

        private double unitPrice;
        private String head;
        private double totalAmount;
        private double minAmount;
        private String nickname;
        private String id;
        private String type;
        private double maxAmount;
        private int otcTimes;
        private String status;

        protected OrderListBean(Parcel in) {
            unitPrice = in.readDouble();
            head = in.readString();
            totalAmount = in.readDouble();
            minAmount = in.readDouble();
            nickname = in.readString();
            id = in.readString();
            type = in.readString();
            maxAmount = in.readDouble();
            otcTimes = in.readInt();
            status = in.readString();
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

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
            parcel.writeString(head);
            parcel.writeDouble(totalAmount);
            parcel.writeDouble(minAmount);
            parcel.writeString(nickname);
            parcel.writeString(id);
            parcel.writeString(type);
            parcel.writeDouble(maxAmount);
            parcel.writeInt(otcTimes);
            parcel.writeString(status);
        }
    }
}

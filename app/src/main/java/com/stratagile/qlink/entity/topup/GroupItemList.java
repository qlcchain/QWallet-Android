package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;

public class GroupItemList extends BaseBack {

    private ArrayList<ItemListBean> itemList;

    public ArrayList<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean implements Parcelable {

        /**
         * payFiatMoney : 3.68
         * deductionToken : QGAS
         * payTokenAmount : 3.146
         * userId : bafe415310bd41fdb055fb0fe6cd1080
         * deductionTokenAmount : 10.35
         * head : /data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg
         * deductionTokenInTxid :
         * payTokenInTxid :
         * payTokenChain : NEO_CHAIN
         * id : dead59447d254529aa9af847db85171b
         * payTokenPrice : 1.0
         * status : NEW
         * deductionTokenChain : QLC_CHAIN
         * payToken : QLC
         * createDate : 2020-01-15 16:34:48
         */

        private double payFiatMoney;
        private String deductionToken;

        public ItemListBean() {
        }

        private double payTokenAmount;
        private String userId;
        private double deductionTokenAmount;
        private String head;
        private String deductionTokenInTxid;
        private String payTokenInTxid;
        private String payTokenChain;
        private String id;
        private double payTokenPrice;
        private String status;
        private String deductionTokenChain;
        private String payToken;
        private String createDate;

        protected ItemListBean(Parcel in) {
            payFiatMoney = in.readDouble();
            deductionToken = in.readString();
            payTokenAmount = in.readDouble();
            userId = in.readString();
            deductionTokenAmount = in.readDouble();
            head = in.readString();
            deductionTokenInTxid = in.readString();
            payTokenInTxid = in.readString();
            payTokenChain = in.readString();
            id = in.readString();
            payTokenPrice = in.readDouble();
            status = in.readString();
            deductionTokenChain = in.readString();
            payToken = in.readString();
            createDate = in.readString();
        }

        public static final Creator<ItemListBean> CREATOR = new Creator<ItemListBean>() {
            @Override
            public ItemListBean createFromParcel(Parcel in) {
                return new ItemListBean(in);
            }

            @Override
            public ItemListBean[] newArray(int size) {
                return new ItemListBean[size];
            }
        };

        public double getPayFiatMoney() {
            return payFiatMoney;
        }

        public void setPayFiatMoney(double payFiatMoney) {
            this.payFiatMoney = payFiatMoney;
        }

        public String getDeductionToken() {
            return deductionToken;
        }

        public void setDeductionToken(String deductionToken) {
            this.deductionToken = deductionToken;
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

        public double getDeductionTokenAmount() {
            return deductionTokenAmount;
        }

        public void setDeductionTokenAmount(double deductionTokenAmount) {
            this.deductionTokenAmount = deductionTokenAmount;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getDeductionTokenInTxid() {
            return deductionTokenInTxid;
        }

        public void setDeductionTokenInTxid(String deductionTokenInTxid) {
            this.deductionTokenInTxid = deductionTokenInTxid;
        }

        public String getPayTokenInTxid() {
            return payTokenInTxid;
        }

        public void setPayTokenInTxid(String payTokenInTxid) {
            this.payTokenInTxid = payTokenInTxid;
        }

        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getPayTokenPrice() {
            return payTokenPrice;
        }

        public void setPayTokenPrice(double payTokenPrice) {
            this.payTokenPrice = payTokenPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeductionTokenChain() {
            return deductionTokenChain;
        }

        public void setDeductionTokenChain(String deductionTokenChain) {
            this.deductionTokenChain = deductionTokenChain;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(payFiatMoney);
            dest.writeString(deductionToken);
            dest.writeDouble(payTokenAmount);
            dest.writeString(userId);
            dest.writeDouble(deductionTokenAmount);
            dest.writeString(head);
            dest.writeString(deductionTokenInTxid);
            dest.writeString(payTokenInTxid);
            dest.writeString(payTokenChain);
            dest.writeString(id);
            dest.writeDouble(payTokenPrice);
            dest.writeString(status);
            dest.writeString(deductionTokenChain);
            dest.writeString(payToken);
            dest.writeString(createDate);
        }
    }
}

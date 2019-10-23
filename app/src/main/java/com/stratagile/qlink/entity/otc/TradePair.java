package com.stratagile.qlink.entity.otc;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class TradePair extends BaseBack {

    private ArrayList<PairsListBean> pairsList;

    public ArrayList<PairsListBean> getPairsList() {
        return pairsList;
    }

    public void setPairsList(ArrayList<PairsListBean> pairsList) {
        this.pairsList = pairsList;
    }

    public static class PairsListBean implements Cloneable, Parcelable {
        /**
         * tradeTokenHash : ea842234e4dc5b17c33b35f99b5b86111a3af0bd8e4a8822602b866711de6d81
         * payTokenChain : ETH
         * tradeToken : QGAS
         * id : f3795b94b8ed4554bf9997a31d7bb687
         * tradeTokenChain : QLCCHAIN
         * payTokenHash : 0xdac17f958d2ee523a2206206994597c13d831ec7
         * payToken : USDT
         */

        private String tradeTokenHash;
        private String payTokenChain;
        private String tradeToken;
        private String id;
        private String tradeTokenChain;
        private String payTokenHash;
        private String payToken;

        protected PairsListBean(Parcel in) {
            tradeTokenHash = in.readString();
            payTokenChain = in.readString();
            tradeToken = in.readString();
            id = in.readString();
            tradeTokenChain = in.readString();
            payTokenHash = in.readString();
            payToken = in.readString();
            select = in.readByte() != 0;
        }

        public static final Creator<PairsListBean> CREATOR = new Creator<PairsListBean>() {
            @Override
            public PairsListBean createFromParcel(Parcel in) {
                return new PairsListBean(in);
            }

            @Override
            public PairsListBean[] newArray(int size) {
                return new PairsListBean[size];
            }
        };

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        private boolean select;

        public String getTradeTokenHash() {
            return tradeTokenHash;
        }

        public void setTradeTokenHash(String tradeTokenHash) {
            this.tradeTokenHash = tradeTokenHash;
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

        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
        }

        public String getPayTokenHash() {
            return payTokenHash;
        }

        public void setPayTokenHash(String payTokenHash) {
            this.payTokenHash = payTokenHash;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        @Override
        public PairsListBean clone() throws CloneNotSupportedException {
            return (PairsListBean)super.clone();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(tradeTokenHash);
            parcel.writeString(payTokenChain);
            parcel.writeString(tradeToken);
            parcel.writeString(id);
            parcel.writeString(tradeTokenChain);
            parcel.writeString(payTokenHash);
            parcel.writeString(payToken);
            parcel.writeByte((byte) (select ? 1 : 0));
        }
    }
}

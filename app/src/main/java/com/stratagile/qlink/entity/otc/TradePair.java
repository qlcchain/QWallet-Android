package com.stratagile.qlink.entity.otc;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;

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
         * minTradeTokenAmount : 0.0055
         * minPayTokenAmount : 1.0
         * payTokenDecimal : 8
         * tradeTokenDecimal : 18
         * tradeTokenChain : ETH_CHAIN
         * payTokenHash : 0x5d3a536E4D6DbD6114cc1Ead35777bAB948E3643
         * payTokenLogo : /userfiles/1/images/otc/2020/07/548e85490f3d4050970f37772404025d.png
         * tradeTokenHash : 0xc00e94cb662c3520282e6f5717214004a7f26888
         * tradeTokenLogo : /userfiles/1/images/otc/2020/07/9bb90b39f59245b096b85892f5bdda33.png
         * payTokenChain : ETH_CHAIN
         * tradeToken : COMP
         * id : 59e328a47be64b5a943907e2ddbd7582
         * payToken : cDAI
         */

        private double minTradeTokenAmount;
        private double minPayTokenAmount;
        private int payTokenDecimal;
        private int tradeTokenDecimal;
        private String tradeTokenChain;
        private String payTokenHash;
        private String payTokenLogo;
        private String tradeTokenHash;
        private String tradeTokenLogo;
        private String payTokenChain;
        private String tradeToken;
        private String id;
        private String payToken;
        private boolean select;

        protected PairsListBean(Parcel in) {
            minTradeTokenAmount = in.readDouble();
            minPayTokenAmount = in.readDouble();
            payTokenDecimal = in.readInt();
            tradeTokenDecimal = in.readInt();
            tradeTokenChain = in.readString();
            payTokenHash = in.readString();
            payTokenLogo = in.readString();
            tradeTokenHash = in.readString();
            tradeTokenLogo = in.readString();
            payTokenChain = in.readString();
            tradeToken = in.readString();
            id = in.readString();
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



        public double getMinTradeTokenAmount() {
            return minTradeTokenAmount;
        }

        public void setMinTradeTokenAmount(double minTradeTokenAmount) {
            this.minTradeTokenAmount = minTradeTokenAmount;
        }

        public double getMinPayTokenAmount() {
            return minPayTokenAmount;
        }

        public void setMinPayTokenAmount(double minPayTokenAmount) {
            this.minPayTokenAmount = minPayTokenAmount;
        }

        public int getPayTokenDecimal() {
            return payTokenDecimal;
        }

        public void setPayTokenDecimal(int payTokenDecimal) {
            this.payTokenDecimal = payTokenDecimal;
        }

        public int getTradeTokenDecimal() {
            return tradeTokenDecimal;
        }

        public void setTradeTokenDecimal(int tradeTokenDecimal) {
            this.tradeTokenDecimal = tradeTokenDecimal;
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

        public String getPayTokenLogo() {
            return payTokenLogo;
        }

        public void setPayTokenLogo(String payTokenLogo) {
            this.payTokenLogo = payTokenLogo;
        }

        public String getTradeTokenHash() {
            return tradeTokenHash;
        }

        public void setTradeTokenHash(String tradeTokenHash) {
            this.tradeTokenHash = tradeTokenHash;
        }

        public String getTradeTokenLogo() {
            return tradeTokenLogo;
        }

        public void setTradeTokenLogo(String tradeTokenLogo) {
            this.tradeTokenLogo = tradeTokenLogo;
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
        public PairsListBean clone() throws CloneNotSupportedException {
            return (PairsListBean)super.clone();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(minTradeTokenAmount);
            dest.writeDouble(minPayTokenAmount);
            dest.writeInt(payTokenDecimal);
            dest.writeInt(tradeTokenDecimal);
            dest.writeString(tradeTokenChain);
            dest.writeString(payTokenHash);
            dest.writeString(payTokenLogo);
            dest.writeString(tradeTokenHash);
            dest.writeString(tradeTokenLogo);
            dest.writeString(payTokenChain);
            dest.writeString(tradeToken);
            dest.writeString(id);
            dest.writeString(payToken);
            dest.writeByte((byte) (select ? 1 : 0));
        }
    }
}

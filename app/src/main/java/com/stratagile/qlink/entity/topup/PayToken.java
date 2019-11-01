package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class PayToken extends BaseBack<PayToken.PayTokenListBean> {


    private ArrayList<PayTokenListBean> payTokenList;

    public ArrayList<PayTokenListBean> getPayTokenList() {
        return payTokenList;
    }

    public void setPayTokenList(ArrayList<PayTokenListBean> payTokenList) {
        this.payTokenList = payTokenList;
    }

    public static class PayTokenListBean implements Parcelable {
        protected PayTokenListBean(Parcel in) {
            symbol = in.readString();
            chain = in.readString();
            price = in.readDouble();
            id = in.readString();
            logo_png = in.readString();
            decimal = in.readInt();
            hash = in.readString();
            logo_webp = in.readString();
            selected = in.readByte() != 0;
        }

        public static final Creator<PayTokenListBean> CREATOR = new Creator<PayTokenListBean>() {
            @Override
            public PayTokenListBean createFromParcel(Parcel in) {
                return new PayTokenListBean(in);
            }

            @Override
            public PayTokenListBean[] newArray(int size) {
                return new PayTokenListBean[size];
            }
        };

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogo_png() {
            return logo_png;
        }

        public void setLogo_png(String logo_png) {
            this.logo_png = logo_png;
        }

        public int getDecimal() {
            return decimal;
        }

        public void setDecimal(int decimal) {
            this.decimal = decimal;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getLogo_webp() {
            return logo_webp;
        }

        public void setLogo_webp(String logo_webp) {
            this.logo_webp = logo_webp;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        /**
         * symbol : QGAS
         * chain : QLC_CHAIN
         * price : 1.0
         * id : 0bba9abd1b9d4eea9c1b5032a0d5257f
         * logo_png :
         * decimal : 8
         * hash : ea842234e4dc5b17c33b35f99b5b86111a3af0bd8e4a8822602b866711de6d81
         * logo_webp :
         */

        private String symbol;
        private String chain;
        private double price;
        private String id;
        private String logo_png;
        private int decimal;
        private String hash;
        private String logo_webp;
        private boolean selected;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(symbol);
            dest.writeString(chain);
            dest.writeDouble(price);
            dest.writeString(id);
            dest.writeString(logo_png);
            dest.writeInt(decimal);
            dest.writeString(hash);
            dest.writeString(logo_webp);
            dest.writeByte((byte) (selected ? 1 : 0));
        }
    }
}

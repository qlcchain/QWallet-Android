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
        /**
         * symbol : OKB
         * chain : ETH_CHAIN
         * price : 1.0
         * id : 01d821143cee4ced80e1rt6b30fdc123
         * hash : 0x75231f58b43240c9718dd58b4967c5114342a86c
         */

        private String symbol;
        private String chain;
        private double price;
        private int decimal;
        private String id;


        public int getDecimal() {
            return decimal;
        }

        public void setDecimal(int decimal) {
            this.decimal = decimal;
        }

        protected PayTokenListBean(Parcel in) {
            symbol = in.readString();
            chain = in.readString();
            price = in.readDouble();
            decimal = in.readInt();
            id = in.readString();
            selected = in.readByte() != 0;
            hash = in.readString();
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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean selected;
        private String hash;

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

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(symbol);
            dest.writeString(chain);
            dest.writeDouble(price);
            dest.writeInt(decimal);
            dest.writeString(id);
            dest.writeByte((byte) (selected ? 1 : 0));
            dest.writeString(hash);
        }
    }
}

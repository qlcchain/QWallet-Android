package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by huzhipeng on 2018/1/26.
 */

public class Record extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * recordId : 43a83a2d2ca7456cb475489d5de46dd2
         * type : 2
         * addressFrom : AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo
         * addressTo : AQC7Bod2LxaRxmLewRrwCA1Nt6AQMWSm28
         * neoNum : 1
         * exchangeRate : 387.70
         * exchangeOfQlc : 387.70
         * exchangeDate : 2018-01-26 22:54:48
         * time : 2018-01-26 22:54:48
         */

        private String recordId;
        private int type;
        private String addressFrom;
        private String addressTo;
        private String neoNum;
        private String exchangeRate;
        private String exchangeOfQlc;
        private String exchangeDate;
        private String time;
        private String assetName;
        private String isGratuity;
        private String wifiName;

        protected DataBean(Parcel in) {
            recordId = in.readString();
            type = in.readInt();
            addressFrom = in.readString();
            addressTo = in.readString();
            neoNum = in.readString();
            exchangeRate = in.readString();
            exchangeOfQlc = in.readString();
            exchangeDate = in.readString();
            time = in.readString();
            assetName = in.readString();
            isGratuity = in.readString();
            wifiName = in.readString();
            connectType = in.readInt();
            useFlow = in.readString();
            useLength = in.readString();
            vpnName = in.readString();
            fromP2pId = in.readString();
            toP2pId = in.readString();
            qlc = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public int getConnectType() {
            return connectType;
        }

        public void setConnectType(int connectType) {
            this.connectType = connectType;
        }

        /**
         * 记录连接类型，0是使用者，1是提供者
         */
        private int connectType;

        public String getWifiName() {
            return wifiName;
        }

        public void setWifiName(String wifiName) {
            this.wifiName = wifiName;
        }

        public String getVpnName() {
            return vpnName;
        }

        public void setVpnName(String vpnName) {
            this.vpnName = vpnName;
        }

        private String useFlow;
        private String useLength;

        public String getFromP2pId() {
            return fromP2pId;
        }

        public void setFromP2pId(String fromP2pId) {
            this.fromP2pId = fromP2pId;
        }

        public String getToP2pId() {
            return toP2pId;
        }

        public void setToP2pId(String toP2pId) {
            this.toP2pId = toP2pId;
        }

        private String vpnName;
        private String fromP2pId;
        private String toP2pId;

        public String getIsGratuity() {
            return isGratuity;
        }

        public void setIsGratuity(String isGratuity) {
            this.isGratuity = isGratuity;
        }

        public String getUseFlow() {
            return useFlow;
        }

        public void setUseFlow(String useFlow) {
            this.useFlow = useFlow;
        }

        public String getUseLength() {
            return useLength;
        }

        public void setUseLength(String useLength) {
            this.useLength = useLength;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public String getQlc() {
            return qlc;
        }

        public void setQlc(String qlc) {
            this.qlc = qlc;
        }

        private String qlc;

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAddressFrom() {
            return addressFrom;
        }

        public void setAddressFrom(String addressFrom) {
            this.addressFrom = addressFrom;
        }

        public String getAddressTo() {
            return addressTo;
        }

        public void setAddressTo(String addressTo) {
            this.addressTo = addressTo;
        }

        public String getNeoNum() {
            return neoNum;
        }

        public void setNeoNum(String neoNum) {
            this.neoNum = neoNum;
        }

        public String getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public String getExchangeOfQlc() {
            return exchangeOfQlc;
        }

        public void setExchangeOfQlc(String exchangeOfQlc) {
            this.exchangeOfQlc = exchangeOfQlc;
        }

        public String getExchangeDate() {
            return exchangeDate;
        }

        public void setExchangeDate(String exchangeDate) {
            this.exchangeDate = exchangeDate;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(recordId);
            dest.writeInt(type);
            dest.writeString(addressFrom);
            dest.writeString(addressTo);
            dest.writeString(neoNum);
            dest.writeString(exchangeRate);
            dest.writeString(exchangeOfQlc);
            dest.writeString(exchangeDate);
            dest.writeString(time);
            dest.writeString(assetName);
            dest.writeString(isGratuity);
            dest.writeString(wifiName);
            dest.writeInt(connectType);
            dest.writeString(useFlow);
            dest.writeString(useLength);
            dest.writeString(vpnName);
            dest.writeString(fromP2pId);
            dest.writeString(toP2pId);
            dest.writeString(qlc);
        }
    }
}

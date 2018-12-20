package com.stratagile.qlink.entity.eos;

import android.os.Parcel;
import android.os.Parcelable;

public class EosQrBean implements Parcelable {
    private String accountName;
    private String ownerPublicKey;
    private String activePublicKey;

    protected EosQrBean(Parcel in) {
        accountName = in.readString();
        ownerPublicKey = in.readString();
        activePublicKey = in.readString();
    }

    public static final Creator<EosQrBean> CREATOR = new Creator<EosQrBean>() {
        @Override
        public EosQrBean createFromParcel(Parcel in) {
            return new EosQrBean(in);
        }

        @Override
        public EosQrBean[] newArray(int size) {
            return new EosQrBean[size];
        }
    };

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public EosQrBean(String accountName, String ownerPublicKey, String activePublicKey) {
        this.accountName = accountName;
        this.ownerPublicKey = ownerPublicKey;
        this.activePublicKey = activePublicKey;
    }

    public String getOwnerPublicKey() {
        return ownerPublicKey;

    }

    public void setOwnerPublicKey(String ownerPublicKey) {
        this.ownerPublicKey = ownerPublicKey;
    }

    public String getActivePublicKey() {
        return activePublicKey;
    }

    public void setActivePublicKey(String activePublicKey) {
        this.activePublicKey = activePublicKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountName);
        dest.writeString(ownerPublicKey);
        dest.writeString(activePublicKey);
    }
}

package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EthWallet implements Parcelable{
    private String address;
    @Id(autoincrement = true)
    private Long id;

    protected EthWallet(Parcel in) {
        address = in.readString();
    }

    @Generated(hash = 1020178272)
    public EthWallet(String address, Long id) {
        this.address = address;
        this.id = id;
    }

    @Generated(hash = 579314833)
    public EthWallet() {
    }

    public static final Creator<EthWallet> CREATOR = new Creator<EthWallet>() {
        @Override
        public EthWallet createFromParcel(Parcel in) {
            return new EthWallet(in);
        }

        @Override
        public EthWallet[] newArray(int size) {
            return new EthWallet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(address);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

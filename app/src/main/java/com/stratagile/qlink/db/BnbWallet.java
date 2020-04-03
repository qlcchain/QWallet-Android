package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BnbWallet implements Parcelable {
    @Id(autoincrement = true)
    private Long id;

    private String address;
    private String name;
    private String mnemonic;
    private boolean isCurrent;
    private boolean isBackup;

    @Override
    public String toString() {
        return "BnbWallet{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", isCurrent=" + isCurrent +
                ", isBackup=" + isBackup +
                ", isLook=" + isLook +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }

    private boolean isLook;

    /**
     * 钱包私钥
     */
    private String privateKey;
    /**
     *钱包公钥
     * 可以根据这个字段进行导入
     */
    private String publicKey;
    @Generated(hash = 806720539)
    public BnbWallet(Long id, String address, String name, String mnemonic,
            boolean isCurrent, boolean isBackup, boolean isLook, String privateKey,
            String publicKey) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.mnemonic = mnemonic;
        this.isCurrent = isCurrent;
        this.isBackup = isBackup;
        this.isLook = isLook;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    @Generated(hash = 1468236654)
    public BnbWallet() {
    }

    protected BnbWallet(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        address = in.readString();
        name = in.readString();
        mnemonic = in.readString();
        isCurrent = in.readByte() != 0;
        isBackup = in.readByte() != 0;
        isLook = in.readByte() != 0;
        privateKey = in.readString();
        publicKey = in.readString();
    }

    public static final Creator<BnbWallet> CREATOR = new Creator<BnbWallet>() {
        @Override
        public BnbWallet createFromParcel(Parcel in) {
            return new BnbWallet(in);
        }

        @Override
        public BnbWallet[] newArray(int size) {
            return new BnbWallet[size];
        }
    };

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMnemonic() {
        return this.mnemonic;
    }
    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
    public boolean getIsCurrent() {
        return this.isCurrent;
    }
    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    public boolean getIsBackup() {
        return this.isBackup;
    }
    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }
    public boolean getIsLook() {
        return this.isLook;
    }
    public void setIsLook(boolean isLook) {
        this.isLook = isLook;
    }
    public String getPrivateKey() {
        return this.privateKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    public String getPublicKey() {
        return this.publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(mnemonic);
        dest.writeByte((byte) (isCurrent ? 1 : 0));
        dest.writeByte((byte) (isBackup ? 1 : 0));
        dest.writeByte((byte) (isLook ? 1 : 0));
        dest.writeString(privateKey);
        dest.writeString(publicKey);
    }
}

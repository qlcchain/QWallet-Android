package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * 钱包bean对象
 * Created by dwq on 2018/3/20/020.
 * e-mail:lomapa@163.com
 */
@Entity
public class EthWallet implements Parcelable{
    @Id(autoincrement = true)
    private Long id;

    private String address;
    private String name;
    private String password;
    private String keystorePath;
    private String mnemonic;
    private boolean isCurrent;
    private boolean isBackup;
    private boolean isLook;


    @Keep()
    public EthWallet() {
    }

    @Generated(hash = 1397269304)
    public EthWallet(Long id, String address, String name, String password,
            String keystorePath, String mnemonic, boolean isCurrent,
            boolean isBackup, boolean isLook) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.password = password;
        this.keystorePath = keystorePath;
        this.mnemonic = mnemonic;
        this.isCurrent = isCurrent;
        this.isBackup = isBackup;
        this.isLook = isLook;
    }

    protected EthWallet(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        address = in.readString();
        name = in.readString();
        password = in.readString();
        keystorePath = in.readString();
        mnemonic = in.readString();
        isCurrent = in.readByte() != 0;
        isBackup = in.readByte() != 0;
        isLook = in.readByte() != 0;
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

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
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

    @Override
    public String toString() {
        return "ETHWallet{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", keystorePath='" + keystorePath + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", isCurrent=" + isCurrent +
                ", isBackup=" + isBackup +
                '}';
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
        dest.writeString(password);
        dest.writeString(keystorePath);
        dest.writeString(mnemonic);
        dest.writeByte((byte) (isCurrent ? 1 : 0));
        dest.writeByte((byte) (isBackup ? 1 : 0));
        dest.writeByte((byte) (isLook ? 1 : 0));
    }

    public boolean getIsLook() {
        return this.isLook;
    }

    public void setIsLook(boolean isLook) {
        this.isLook = isLook;
    }
}

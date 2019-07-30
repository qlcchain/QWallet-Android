package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class QLCAccount implements Parcelable {
    @Id(autoincrement = true)

    private Long id;

    private String seed;
    private String password;
    private String pubKey;
    private String privKey;
    private String address;
    private boolean isCurrent;
    private String accountName;
    //是否为主账号的种子
    private boolean isAccountSeed;
    private int walletIndex;
    private String mnemonic;
    private boolean isBackUp;

    protected QLCAccount(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        seed = in.readString();
        password = in.readString();
        pubKey = in.readString();
        privKey = in.readString();
        address = in.readString();
        isCurrent = in.readByte() != 0;
        accountName = in.readString();
        isAccountSeed = in.readByte() != 0;
        walletIndex = in.readInt();
        mnemonic = in.readString();
        isBackUp = in.readByte() != 0;
    }

    public static final Creator<QLCAccount> CREATOR = new Creator<QLCAccount>() {
        @Override
        public QLCAccount createFromParcel(Parcel in) {
            return new QLCAccount(in);
        }

        @Override
        public QLCAccount[] newArray(int size) {
            return new QLCAccount[size];
        }
    };

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }


    @Override
    public String toString() {
        return "QLCAccount{" +
                "id=" + id +
                ", seed='" + seed + '\'' +
                ", password='" + password + '\'' +
                ", pubKey='" + pubKey + '\'' +
                ", privKey='" + privKey + '\'' +
                ", address='" + address + '\'' +
                ", isCurrent=" + isCurrent +
                ", accountName='" + accountName + '\'' +
                '}';
    }

    @Generated(hash = 726867681)
    public QLCAccount(Long id, String seed, String password, String pubKey,
            String privKey, String address, boolean isCurrent, String accountName,
            boolean isAccountSeed, int walletIndex, String mnemonic,
            boolean isBackUp) {
        this.id = id;
        this.seed = seed;
        this.password = password;
        this.pubKey = pubKey;
        this.privKey = privKey;
        this.address = address;
        this.isCurrent = isCurrent;
        this.accountName = accountName;
        this.isAccountSeed = isAccountSeed;
        this.walletIndex = walletIndex;
        this.mnemonic = mnemonic;
        this.isBackUp = isBackUp;
    }

    @Generated(hash = 1173276367)
    public QLCAccount() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSeed() {
        return this.seed;
    }
    public void setSeed(String seed) {
        this.seed = seed;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPubKey() {
        return this.pubKey;
    }
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
    public String getPrivKey() {
        return this.privKey;
    }
    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getIsCurrent() {
        return this.isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(seed);
        parcel.writeString(password);
        parcel.writeString(pubKey);
        parcel.writeString(privKey);
        parcel.writeString(address);
        parcel.writeByte((byte) (isCurrent ? 1 : 0));
        parcel.writeString(accountName);
        parcel.writeByte((byte) (isAccountSeed ? 1 : 0));
        parcel.writeInt(walletIndex);
        parcel.writeString(mnemonic);
        parcel.writeByte((byte) (isBackUp ? 1 : 0));
    }

    public boolean getIsAccountSeed() {
        return this.isAccountSeed;
    }

    public void setIsAccountSeed(boolean isAccountSeed) {
        this.isAccountSeed = isAccountSeed;
    }

    public int getWalletIndex() {
        return this.walletIndex;
    }

    public void setWalletIndex(int walletIndex) {
        this.walletIndex = walletIndex;
    }

    public String getMnemonic() {
        return this.mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public boolean getIsBackUp() {
        return this.isBackUp;
    }

    public void setIsBackUp(boolean isBackUp) {
        this.isBackUp = isBackUp;
    }
}

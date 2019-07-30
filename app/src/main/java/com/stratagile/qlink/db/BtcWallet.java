package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class BtcWallet implements Parcelable{
    public String getHexPrivateKey() {
        return hexPrivateKey;
    }

    public void setHexPrivateKey(String hexPrivateKey) {
        this.hexPrivateKey = hexPrivateKey;
    }

    public String getWifPrivateKey() {
        return wifPrivateKey;
    }

    public void setWifPrivateKey(String wifPrivateKey) {
        this.wifPrivateKey = wifPrivateKey;
    }

    @Id(autoincrement = true)

    private Long id;
    private Long i;
    /**
     * 钱包私钥
     */
    private String hexPrivateKey;
    /**
     *钱包公钥
     * 可以根据这个字段进行导入
     */
    private String publicKey;

    /**
     *钱包的wifi。可以根据这个字段进行钱包导入
     */
    private String wifPrivateKey;
    /**
     *钱包地址，根据这个字段进行钱包之间的转账
     */
    private String address;

    private String name;

    /**
     * 是否已经备份
     */
    private boolean isBackup;

    private boolean isCurrent;

    @Override
    public String toString() {
        return "BtcWallet{" +
                "id=" + id +
                ", i=" + i +
                ", hexPrivateKey='" + hexPrivateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", wifPrivateKey='" + wifPrivateKey + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", isBackup=" + isBackup +
                ", isCurrent=" + isCurrent +
                ", bigIntegerPrivateKey='" + bigIntegerPrivateKey + '\'' +
                '}';
    }

    @Generated(hash = 1042605644)
    public BtcWallet() {
    }

    protected BtcWallet(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            i = null;
        } else {
            i = in.readLong();
        }
        hexPrivateKey = in.readString();
        publicKey = in.readString();
        wifPrivateKey = in.readString();
        address = in.readString();
        name = in.readString();
        isBackup = in.readByte() != 0;
        isCurrent = in.readByte() != 0;
        bigIntegerPrivateKey = in.readString();
    }

    @Generated(hash = 1498064263)
    public BtcWallet(Long id, Long i, String hexPrivateKey, String publicKey,
            String wifPrivateKey, String address, String name, boolean isBackup,
            boolean isCurrent, String bigIntegerPrivateKey) {
        this.id = id;
        this.i = i;
        this.hexPrivateKey = hexPrivateKey;
        this.publicKey = publicKey;
        this.wifPrivateKey = wifPrivateKey;
        this.address = address;
        this.name = name;
        this.isBackup = isBackup;
        this.isCurrent = isCurrent;
        this.bigIntegerPrivateKey = bigIntegerPrivateKey;
    }

    public static final Creator<BtcWallet> CREATOR = new Creator<BtcWallet>() {
        @Override
        public BtcWallet createFromParcel(Parcel in) {
            return new BtcWallet(in);
        }

        @Override
        public BtcWallet[] newArray(int size) {
            return new BtcWallet[size];
        }
    };

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getBigIntegerPrivateKey() {
        return bigIntegerPrivateKey;
    }

    public void setBigIntegerPrivateKey(String bigIntegerPrivateKey) {
        this.bigIntegerPrivateKey = bigIntegerPrivateKey;
    }

    private String bigIntegerPrivateKey;

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getI() {
        return this.i;
    }

    public void setI(Long i) {
        this.i = i;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public boolean getIsBackup() {
        return this.isBackup;
    }



    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }



    public boolean getIsCurrent() {
        return this.isCurrent;
    }



    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getName() {
        return this.name;
    }



    public void setName(String name) {
        this.name = name;
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
        if (i == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(i);
        }
        dest.writeString(hexPrivateKey);
        dest.writeString(publicKey);
        dest.writeString(wifPrivateKey);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeByte((byte) (isBackup ? 1 : 0));
        dest.writeByte((byte) (isCurrent ? 1 : 0));
        dest.writeString(bigIntegerPrivateKey);
    }
}

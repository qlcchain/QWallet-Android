package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huzhipeng on 2018/1/19.
 * 钱包的实体类
 */

@Entity
public class Wallet implements Parcelable{
    @Id(autoincrement = true)
    private Long id;
    private Long i;
    /**
     * 钱包私钥
     */
    private String privateKey;
    /**
     *钱包公钥
     * 可以根据这个字段进行导入
     */
    private String publicKey;
    /**
     *？？？
     */
    private String scriptHash;
    /**
     *钱包的wifi。可以根据这个字段进行钱包导入
     */
    private String wif;
    /**
     *钱包地址，根据这个字段进行钱包之间的转账
     */
    private String address;

    /**
     * 是否是主网钱包
     */
    private boolean isMain;

    private String name;

    /**
     * 是否已经备份
     */
    private boolean isBackup;

    private boolean isCurrent;


    protected Wallet(Parcel in) {
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
        privateKey = in.readString();
        publicKey = in.readString();
        scriptHash = in.readString();
        wif = in.readString();
        address = in.readString();
        isMain = in.readByte() != 0;
        name = in.readString();
        isBackup = in.readByte() != 0;
        isCurrent = in.readByte() != 0;
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", i=" + i +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", scriptHash='" + scriptHash + '\'' +
                ", wif='" + wif + '\'' +
                ", address='" + address + '\'' +
                ", isMain=" + isMain +
                '}';
    }



    @Generated(hash = 528944251)
    public Wallet(Long id, Long i, String privateKey, String publicKey,
            String scriptHash, String wif, String address, boolean isMain,
            String name, boolean isBackup, boolean isCurrent) {
        this.id = id;
        this.i = i;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.scriptHash = scriptHash;
        this.wif = wif;
        this.address = address;
        this.isMain = isMain;
        this.name = name;
        this.isBackup = isBackup;
        this.isCurrent = isCurrent;
    }



    @Generated(hash = 1197745249)
    public Wallet() {
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

    public String getScriptHash() {
        return this.scriptHash;
    }

    public void setScriptHash(String scriptHash) {
        this.scriptHash = scriptHash;
    }

    public String getWif() {
        return this.wif;
    }

    public void setWif(String wif) {
        this.wif = wif;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public boolean getIsMain() {
        return this.isMain;
    }



    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
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
        dest.writeString(privateKey);
        dest.writeString(publicKey);
        dest.writeString(scriptHash);
        dest.writeString(wif);
        dest.writeString(address);
        dest.writeByte((byte) (isMain ? 1 : 0));
        dest.writeString(name);
        dest.writeByte((byte) (isBackup ? 1 : 0));
        dest.writeByte((byte) (isCurrent ? 1 : 0));
    }



    public String getName() {
        return this.name;
    }



    public void setName(String name) {
        this.name = name;
    }
}

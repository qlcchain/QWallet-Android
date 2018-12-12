package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EosAccount implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String accountName;
    private String walletName;
    private String activePrivateKey;
    private String activePublicKey;
    private String ownerPrivateKey;
    private String ownerPublicKey;
    private boolean isCurrent;
    private String accountPassword;

    /**
     * 是否正在被创建，
     */
    private boolean isCreating;


    @Generated(hash = 1425059378)
    public EosAccount(Long id, String accountName, String walletName,
            String activePrivateKey, String activePublicKey, String ownerPrivateKey,
            String ownerPublicKey, boolean isCurrent, String accountPassword,
            boolean isCreating) {
        this.id = id;
        this.accountName = accountName;
        this.walletName = walletName;
        this.activePrivateKey = activePrivateKey;
        this.activePublicKey = activePublicKey;
        this.ownerPrivateKey = ownerPrivateKey;
        this.ownerPublicKey = ownerPublicKey;
        this.isCurrent = isCurrent;
        this.accountPassword = accountPassword;
        this.isCreating = isCreating;
    }

    @Generated(hash = 1777469907)
    public EosAccount() {
    }


    protected EosAccount(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        accountName = in.readString();
        walletName = in.readString();
        activePrivateKey = in.readString();
        activePublicKey = in.readString();
        ownerPrivateKey = in.readString();
        ownerPublicKey = in.readString();
        isCurrent = in.readByte() != 0;
        accountPassword = in.readString();
        isCreating = in.readByte() != 0;
    }

    public static final Creator<EosAccount> CREATOR = new Creator<EosAccount>() {
        @Override
        public EosAccount createFromParcel(Parcel in) {
            return new EosAccount(in);
        }

        @Override
        public EosAccount[] newArray(int size) {
            return new EosAccount[size];
        }
    };

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getActivePrivateKey() {
        return this.activePrivateKey;
    }

    public void setActivePrivateKey(String activePrivateKey) {
        this.activePrivateKey = activePrivateKey;
    }

    public String getActivePublicKey() {
        return this.activePublicKey;
    }

    public void setActivePublicKey(String activePublicKey) {
        this.activePublicKey = activePublicKey;
    }

    public String getOwnerPrivateKey() {
        return this.ownerPrivateKey;
    }

    public void setOwnerPrivateKey(String ownerPrivateKey) {
        this.ownerPrivateKey = ownerPrivateKey;
    }

    public String getOwnerPublicKey() {
        return this.ownerPublicKey;
    }

    public void setOwnerPublicKey(String ownerPublicKey) {
        this.ownerPublicKey = ownerPublicKey;
    }

    public boolean getIsCurrent() {
        return this.isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getAccountPassword() {
        return this.accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
    public String getWalletName() {
        return this.walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public boolean getIsCreating() {
        return this.isCreating;
    }

    public void setIsCreating(boolean isCreating) {
        this.isCreating = isCreating;
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
        dest.writeString(accountName);
        dest.writeString(walletName);
        dest.writeString(activePrivateKey);
        dest.writeString(activePublicKey);
        dest.writeString(ownerPrivateKey);
        dest.writeString(ownerPublicKey);
        dest.writeByte((byte) (isCurrent ? 1 : 0));
        dest.writeString(accountPassword);
        dest.writeByte((byte) (isCreating ? 1 : 0));
    }
}

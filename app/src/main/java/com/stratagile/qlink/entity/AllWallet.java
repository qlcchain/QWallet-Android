package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;

import java.util.Objects;

public class AllWallet {
    private WalletType walletType;
    private EthWallet ethWallet;
    private Wallet wallet;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    private String walletName;
    private String walletAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllWallet allWallet = (AllWallet) o;
        return Objects.equals(walletAddress, allWallet.walletAddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(walletAddress);
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public EthWallet getEthWallet() {
        return ethWallet;
    }

    public void setEthWallet(EthWallet ethWallet) {
        this.ethWallet = ethWallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     *
     */
    public enum WalletType implements Parcelable {
        NeoWallet, EthWallet, EosWallet;

        public static final Creator<WalletType> CREATOR = new Creator<WalletType>() {
            @Override
            public WalletType createFromParcel(Parcel in) {
                return WalletType.values()[in.readInt()];
            }

            @Override
            public WalletType[] newArray(int size) {
                return new WalletType[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
        }
    }
}

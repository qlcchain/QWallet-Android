package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class TokenInfo implements Parcelable {
    private String tokenName;
    private double tokenValue;
    private String tokenImgName;
    private double tokenUsd;
    private double tokenCnn;
    private String tokenSymol;
    private double tokenPrice;
    private AllWallet.WalletType walletType;
    private String tokenAddress;
    private String walletAddress;
    private double ethPrice;
    private int tokenDecimals;

    protected TokenInfo(Parcel in) {
        tokenName = in.readString();
        tokenValue = in.readDouble();
        tokenImgName = in.readString();
        tokenUsd = in.readDouble();
        tokenCnn = in.readDouble();
        tokenSymol = in.readString();
        tokenPrice = in.readDouble();
        walletType = in.readParcelable(AllWallet.WalletType.class.getClassLoader());
        tokenAddress = in.readString();
        walletAddress = in.readString();
        ethPrice = in.readDouble();
        tokenDecimals = in.readInt();
        eosTokenValue = in.readString();
        isMainNetToken = in.readByte() != 0;
    }

    public static final Creator<TokenInfo> CREATOR = new Creator<TokenInfo>() {
        @Override
        public TokenInfo createFromParcel(Parcel in) {
            return new TokenInfo(in);
        }

        @Override
        public TokenInfo[] newArray(int size) {
            return new TokenInfo[size];
        }
    };

    public String getEosTokenValue() {
        return eosTokenValue;
    }

    public void setEosTokenValue(String eosTokenValue) {
        this.eosTokenValue = eosTokenValue;
    }

    private String eosTokenValue;


    public int getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(int tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }


    public double getEthPrice() {
        return ethPrice;
    }

    public void setEthPrice(double ethPrice) {
        this.ethPrice = ethPrice;
    }

    public TokenInfo() {}



    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public AllWallet.WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(AllWallet.WalletType walletType) {
        this.walletType = walletType;
    }

    public double getTokenPrice() {
        return tokenPrice;
    }

    public void setTokenPrice(double tokenPrice) {
        this.tokenPrice = tokenPrice;
    }

    public boolean isMainNetToken() {
        return isMainNetToken;
    }

    public void setMainNetToken(boolean mainNetToken) {
        isMainNetToken = mainNetToken;
    }

    private boolean isMainNetToken;

    public String getTokenSymol() {
        return tokenSymol;
    }

    public void setTokenSymol(String tokenSymol) {
        this.tokenSymol = tokenSymol;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public double getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(double tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenImgName() {
        return tokenImgName;
    }

    public void setTokenImgName(String tokenImgName) {
        this.tokenImgName = tokenImgName;
    }

    public double getTokenUsd() {
        return tokenUsd;
    }

    public void setTokenUsd(double tokenUsd) {
        this.tokenUsd = tokenUsd;
    }

    public double getTokenCnn() {
        return tokenCnn;
    }

    public void setTokenCnn(double tokenCnn) {
        this.tokenCnn = tokenCnn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenName);
        dest.writeDouble(tokenValue);
        dest.writeString(tokenImgName);
        dest.writeDouble(tokenUsd);
        dest.writeDouble(tokenCnn);
        dest.writeString(tokenSymol);
        dest.writeDouble(tokenPrice);
        dest.writeParcelable(walletType, flags);
        dest.writeString(tokenAddress);
        dest.writeString(walletAddress);
        dest.writeDouble(ethPrice);
        dest.writeInt(tokenDecimals);
        dest.writeString(eosTokenValue);
        dest.writeByte((byte) (isMainNetToken ? 1 : 0));
    }
}

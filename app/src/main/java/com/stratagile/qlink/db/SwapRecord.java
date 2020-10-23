package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SwapRecord implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    /// 原文
    private String rOrign;
    /// 原文hash
    private String rHash;
    /// 交易hash
    private String txHash;
    /// 最后认领交易hash
    private String swaptxHash;
    //类型，1为nep5 --> erc20, 2为erc20 --> nep5
    private int type;
    //状态

    private int state;
    /// swap数量
    private int amount;
    /// swap时间
    private long lockTime;
    /// swap 钱包私钥
    private String fromAddress;
    /// swap to 钱包地址
    private String toAddress;
    //wrapper 钱包地址
    private String wrapperNeoAddress;

    private String wrpperEthAddress;
    private String ethContractAddress;
    private String neoContractAddress;

    private int lockedNep5Height;
    private int unlockedNep5Height;
    private int lockedErc20Height;
    private int unlockedErc20Height;
    private boolean neoTimeout;
    private boolean ethTimeout;
    private boolean fail;
    private String remark;
    private boolean isMainNet;

    private int index;

    protected SwapRecord(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        rOrign = in.readString();
        rHash = in.readString();
        txHash = in.readString();
        swaptxHash = in.readString();
        type = in.readInt();
        state = in.readInt();
        amount = in.readInt();
        lockTime = in.readLong();
        fromAddress = in.readString();
        toAddress = in.readString();
        wrapperNeoAddress = in.readString();
        wrpperEthAddress = in.readString();
        ethContractAddress = in.readString();
        neoContractAddress = in.readString();
        lockedNep5Height = in.readInt();
        unlockedNep5Height = in.readInt();
        lockedErc20Height = in.readInt();
        unlockedErc20Height = in.readInt();
        neoTimeout = in.readByte() != 0;
        ethTimeout = in.readByte() != 0;
        fail = in.readByte() != 0;
        remark = in.readString();
        isMainNet = in.readByte() != 0;
        index = in.readInt();
    }

    public static final Creator<SwapRecord> CREATOR = new Creator<SwapRecord>() {
        @Override
        public SwapRecord createFromParcel(Parcel in) {
            return new SwapRecord(in);
        }

        @Override
        public SwapRecord[] newArray(int size) {
            return new SwapRecord[size];
        }
    };

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
        dest.writeString(rOrign);
        dest.writeString(rHash);
        dest.writeString(txHash);
        dest.writeString(swaptxHash);
        dest.writeInt(type);
        dest.writeInt(state);
        dest.writeInt(amount);
        dest.writeLong(lockTime);
        dest.writeString(fromAddress);
        dest.writeString(toAddress);
        dest.writeString(wrapperNeoAddress);
        dest.writeString(wrpperEthAddress);
        dest.writeString(ethContractAddress);
        dest.writeString(neoContractAddress);
        dest.writeInt(lockedNep5Height);
        dest.writeInt(unlockedNep5Height);
        dest.writeInt(lockedErc20Height);
        dest.writeInt(unlockedErc20Height);
        dest.writeByte((byte) (neoTimeout ? 1 : 0));
        dest.writeByte((byte) (ethTimeout ? 1 : 0));
        dest.writeByte((byte) (fail ? 1 : 0));
        dest.writeString(remark);
        dest.writeByte((byte) (isMainNet ? 1 : 0));
        dest.writeInt(index);
    }


    public enum SwapType {
        //
        tyNone,
        //
        typeNep5ToErc20,
        //
        typeErc20ToNep5
    }

    /**
     * // deposit
     * DepositInit LockerState = iota
     * DepositNeoLockedDone
     * DepositEthLockedPending
     * DepositEthLockedDone
     * DepositEthUnLockedDone
     * DepositNeoUnLockedPending
     * DepositNeoUnLockedDone
     * DepositEthFetchPending
     * DepositEthFetchDone
     * DepositNeoFetchDone
     * <p>
     * // withdraw
     * WithDrawEthLockedDone
     * WithDrawNeoLockedPending
     * WithDrawNeoLockedDone
     * WithDrawNeoUnLockedDone
     * WithDrawEthUnlockPending
     * WithDrawEthUnlockDone
     * WithDrawNeoFetchPending
     * WithDrawNeoFetchDone
     * WithDrawEthFetchDone
     * <p>
     * Failed
     * Invalid
     */
    public enum SwapState {
        //d eposit
        DepositInit,
        DepositNeoLockedDone,
        DepositEthLockedPending,
        DepositEthLockedDone,
        DepositEthUnLockedDone,
        DepositNeoUnLockedPending,
        //6
        DepositNeoUnLockedDone,
        //7
        DepositEthFetchPending,
        //8
        DepositEthFetchDone,
        //9
        DepositNeoFetchPending,
        //10
        DepositNeoFetchDone,


        // withdraw
        //11 新加的状态
        WithDrawInit,
        //12
        WithDrawEthLockedDone,
        //13
        WithDrawNeoLockedPending,
        //14
        WithDrawNeoLockedDone,
        //15
        WithDrawNeoUnLockedPending,
        //16 正常最终状态
        WithDrawNeoUnLockedDone,
        //17
        WithDrawEthUnlockPending,
        //18 正常，eth qlc hub拿走了.
        WithDrawEthUnlockDone,
        //19
        WithDrawNeoFetchPending,
        //20 异常，hub拿走了nep5qlc，可以作为异常的最终状态
        WithDrawNeoFetchDone,
        //21 异常，用户拿走了eth qlc，可以作为异常的最终状态
        WithDrawEthFetchDone,
        // failed
        Failed,
        Invalid
    }


    @Generated(hash = 1619830694)
    public SwapRecord(Long id, String rOrign, String rHash, String txHash, String swaptxHash,
            int type, int state, int amount, long lockTime, String fromAddress,
            String toAddress, String wrapperNeoAddress, String wrpperEthAddress,
            String ethContractAddress, String neoContractAddress, int lockedNep5Height,
            int unlockedNep5Height, int lockedErc20Height, int unlockedErc20Height,
            boolean neoTimeout, boolean ethTimeout, boolean fail, String remark,
            boolean isMainNet, int index) {
        this.id = id;
        this.rOrign = rOrign;
        this.rHash = rHash;
        this.txHash = txHash;
        this.swaptxHash = swaptxHash;
        this.type = type;
        this.state = state;
        this.amount = amount;
        this.lockTime = lockTime;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.wrapperNeoAddress = wrapperNeoAddress;
        this.wrpperEthAddress = wrpperEthAddress;
        this.ethContractAddress = ethContractAddress;
        this.neoContractAddress = neoContractAddress;
        this.lockedNep5Height = lockedNep5Height;
        this.unlockedNep5Height = unlockedNep5Height;
        this.lockedErc20Height = lockedErc20Height;
        this.unlockedErc20Height = unlockedErc20Height;
        this.neoTimeout = neoTimeout;
        this.ethTimeout = ethTimeout;
        this.fail = fail;
        this.remark = remark;
        this.isMainNet = isMainNet;
        this.index = index;
    }

    @Generated(hash = 2114401654)
    public SwapRecord() {
    }

    @Override
    public String toString() {
        return "SwapRecord{" +
                "id=" + id +
                ", rOrign='" + rOrign + '\'' +
                ", rHash='" + rHash + '\'' +
                ", txHash='" + txHash + '\'' +
                ", swaptxHash='" + swaptxHash + '\'' +
                ", type=" + type +
                ", state=" + state +
                ", amount=" + amount +
                ", lockTime=" + lockTime +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", wrapperNeoAddress='" + wrapperNeoAddress + '\'' +
                ", wrpperEthAddress='" + wrpperEthAddress + '\'' +
                ", ethContractAddress='" + ethContractAddress + '\'' +
                ", neoContractAddress='" + neoContractAddress + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getROrign() {
        return this.rOrign;
    }

    public void setROrign(String rOrign) {
        this.rOrign = rOrign;
    }

    public String getRHash() {
        return this.rHash;
    }

    public void setRHash(String rHash) {
        this.rHash = rHash;
    }

    public String getTxHash() {
        return this.txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getSwaptxHash() {
        return this.swaptxHash;
    }

    public void setSwaptxHash(String swaptxHash) {
        this.swaptxHash = swaptxHash;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getLockTime() {
        return this.lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getWrapperNeoAddress() {
        return this.wrapperNeoAddress;
    }

    public void setWrapperNeoAddress(String wrapperNeoAddress) {
        this.wrapperNeoAddress = wrapperNeoAddress;
    }

    public String getWrpperEthAddress() {
        return this.wrpperEthAddress;
    }

    public void setWrpperEthAddress(String wrpperEthAddress) {
        this.wrpperEthAddress = wrpperEthAddress;
    }

    public String getEthContractAddress() {
        return this.ethContractAddress;
    }

    public void setEthContractAddress(String ethContractAddress) {
        this.ethContractAddress = ethContractAddress;
    }

    public String getNeoContractAddress() {
        return this.neoContractAddress;
    }

    public void setNeoContractAddress(String neoContractAddress) {
        this.neoContractAddress = neoContractAddress;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLockedNep5Height() {
        return this.lockedNep5Height;
    }

    public void setLockedNep5Height(int lockedNep5Height) {
        this.lockedNep5Height = lockedNep5Height;
    }

    public int getUnlockedNep5Height() {
        return this.unlockedNep5Height;
    }

    public void setUnlockedNep5Height(int unlockedNep5Height) {
        this.unlockedNep5Height = unlockedNep5Height;
    }

    public int getLockedErc20Height() {
        return this.lockedErc20Height;
    }

    public void setLockedErc20Height(int lockedErc20Height) {
        this.lockedErc20Height = lockedErc20Height;
    }

    public int getUnlockedErc20Height() {
        return this.unlockedErc20Height;
    }

    public void setUnlockedErc20Height(int unlockedErc20Height) {
        this.unlockedErc20Height = unlockedErc20Height;
    }

    public boolean getNeoTimeout() {
        return this.neoTimeout;
    }

    public void setNeoTimeout(boolean neoTimeout) {
        this.neoTimeout = neoTimeout;
    }

    public boolean getEthTimeout() {
        return this.ethTimeout;
    }

    public void setEthTimeout(boolean ethTimeout) {
        this.ethTimeout = ethTimeout;
    }

    public boolean getFail() {
        return this.fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getIsMainNet() {
        return this.isMainNet;
    }

    public void setIsMainNet(boolean isMainNet) {
        this.isMainNet = isMainNet;
    }
}

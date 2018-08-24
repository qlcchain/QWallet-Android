package com.stratagile.qlink.db;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huzhipeng on 2018/1/26.
 * 交易记录的实体类
 */

@Entity
public class TransactionRecord implements  Comparable<TransactionRecord>{
    @Id(autoincrement = true)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionRecord that = (TransactionRecord) o;

        return txid.equals(that.txid);
    }


    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", transactiomType=" + transactiomType +
                ", isReported=" + isReported +
                ", connectType=" + connectType +
                ", txid='" + txid + '\'' +
                ", neoCount=" + neoCount +
                ", qlcCount=" + qlcCount +
                ", timestamp=" + timestamp +
                ", exChangeId='" + exChangeId + '\'' +
                ", friendNum='" + friendNum + '\'' +
                ", assetName='" + assetName + '\'' +
                ", toP2pId='" + toP2pId + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return txid.hashCode();
    }


    //0 wifi连接，1.兑换， 2 转账, 3vpn连接， 4 wifi注册扣费 、 5 vpn注册扣费, 、6， 、7， 、8, bnb兑换为qlc
    private int transactiomType;

    public enum transactionType{
        trasactionConnectWifi, transactionGetQlc,transactionSendQlc, transactionConnectVpn, transactionRegisteWifi, transactionRegisteVPN, transaction2, transaction3, transactionBnb2Qlc

    }
    /**
     * 记录是否上报给wifi或者vpn资产的提供端
     * 现在只有两种记录需要上报，wifi使用和vpn使用,并且是在connectType == 0的时候才要上报
     */
    private boolean isReported;
    /**
     * 记录连接类型，0代表是使用端的连接记录，1代表是提供端的记录
     */
    private int connectType;
    /**
     *交易的流水号
     */
    private String txid;
    /**
     *在交易类型为1的时候，表示兑换的neo的数量
     */
    private double neoCount;
    /**
     *   交易的qlcCount
     */
    private double qlcCount;
    /**
     *交易生成的时间戳
     */
    private long timestamp;
    /**
     *同txid
     */
    private String exChangeId;
    /**
     *对方的好友编号
     */
    private String friendNum;
    /**
     *资产名字
     * 当交易类型为0 或者3的时候
     */
    private String assetName;

    private boolean isMainNet;
    /**
     * 需要接收人的p2pid.
     * 因为加入了每次启动app的删除好友的逻辑，所以记录friendNum是不行的了，必须用p2pId来进行识别
     */
    private String toP2pId;

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getToP2pId() {
        return toP2pId;
    }

    public void setToP2pId(String toP2pId) {
        this.toP2pId = toP2pId;
    }


    @Generated(hash = 1215017002)
    public TransactionRecord() {
    }


    @Generated(hash = 261598317)
    public TransactionRecord(Long id, int transactiomType, boolean isReported, int connectType, String txid, double neoCount, double qlcCount, long timestamp, String exChangeId,
            String friendNum, String assetName, boolean isMainNet, String toP2pId) {
        this.id = id;
        this.transactiomType = transactiomType;
        this.isReported = isReported;
        this.connectType = connectType;
        this.txid = txid;
        this.neoCount = neoCount;
        this.qlcCount = qlcCount;
        this.timestamp = timestamp;
        this.exChangeId = exChangeId;
        this.friendNum = friendNum;
        this.assetName = assetName;
        this.isMainNet = isMainNet;
        this.toP2pId = toP2pId;
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getTransactiomType() {
        return this.transactiomType;
    }
    public void setTransactiomType(int transactiomType) {
        this.transactiomType = transactiomType;
    }
    public String getTxid() {
        return this.txid;
    }
    public void setTxid(String txid) {
        this.txid = txid;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getExChangeId() {
        return this.exChangeId;
    }
    public void setExChangeId(String exChangeId) {
        this.exChangeId = exChangeId;
    }
    public double getNeoCount() {
        return this.neoCount;
    }
    public void setNeoCount(double neoCount) {
        this.neoCount = neoCount;
    }
    public String getAssetName() {
        return this.assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public boolean getIsReported() {
        return this.isReported;
    }
    public void setIsReported(boolean isReported) {
        this.isReported = isReported;
    }
    public int getConnectType() {
        return this.connectType;
    }
    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }
    public String getFriendNum() {
        return this.friendNum;
    }
    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }
    public double getQlcCount() {
        return qlcCount;
    }
    public void setQlcCount(double qlcCount) {
        this.qlcCount = qlcCount;
    }

    @Override
    public int compareTo(@NonNull TransactionRecord o) {
        if(getTimestamp() - o.getTimestamp() > 0)
            return -1;
        else if(getTimestamp() - o.getTimestamp() == 0)
            return  0;
        else
            return 1;
    }


    public boolean getIsMainNet() {
        return this.isMainNet;
    }


    public void setIsMainNet(boolean isMainNet) {
        this.isMainNet = isMainNet;
    }

}

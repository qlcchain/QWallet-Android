package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/3/7.
 * 连接资产之后做记录的请求
 */

public class RecordSaveReq {
    //0 wifi连接，1.兑换， 2 转账, 3vpn连接， 4 wifi注册扣费 、 5 vpn注册扣费
    private int transactiomType;
    /**
     * 记录连接类型，0代表是使用端的连接记录，1代表是提供端的记录
     */
    private int connectType;
    private String txid;
    private long timestamp;

    public int getTransactiomType() {
        return transactiomType;
    }

    @Override
    public String toString() {
        return "RecordSaveReq{" +
                "transactiomType=" + transactiomType +
                ", connectType=" + connectType +
                ", txid='" + txid + '\'' +
                ", timestamp=" + timestamp +
                ", exChangeId='" + exChangeId + '\'' +
                ", assetName='" + assetName + '\'' +
                ", qlcCount='" + qlcCount + '\'' +
                '}';
    }

    public void setTransactiomType(int transactiomType) {
        this.transactiomType = transactiomType;
    }

    public int getConnectType() {
        return connectType;
    }

    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExChangeId() {
        return exChangeId;
    }

    public void setExChangeId(String exChangeId) {
        this.exChangeId = exChangeId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public double getQlcCount() {
        return qlcCount;
    }

    public void setQlcCount(double qlcCount) {
        this.qlcCount = qlcCount;
    }

    private String exChangeId;
    //资产的名字
    private String assetName;

    //qlc数量
    private double qlcCount;
}

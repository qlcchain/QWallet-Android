package com.stratagile.qlink.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huzhipeng on 2018/1/20.
 * @deprecated
 */

@Entity
public class RecordSave {
    @Id(autoincrement = true)
    private Long id;
    private String recordId;
    private String addressFrom;
    private String addressTo;
    private String fromP2pId;
    private String toP2pId;
    private float qlc;
    private String ssid;
    //记录类型， 0， WiFi  1，vpn
    private int recordType;
    private String qlcTotal;

    //0 为消费， 1 为提供服务
    private int serviceType;


    /**
     * 开始时间
     */
    private long startTimeMillins;

    /**
     * 使用的流量
     */
    private String userFlow;

    @Generated(hash = 1373787415)
    public RecordSave(Long id, String recordId, String addressFrom,
            String addressTo, String fromP2pId, String toP2pId, float qlc,
            String ssid, int recordType, String qlcTotal, int serviceType,
            long startTimeMillins, String userFlow) {
        this.id = id;
        this.recordId = recordId;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.fromP2pId = fromP2pId;
        this.toP2pId = toP2pId;
        this.qlc = qlc;
        this.ssid = ssid;
        this.recordType = recordType;
        this.qlcTotal = qlcTotal;
        this.serviceType = serviceType;
        this.startTimeMillins = startTimeMillins;
        this.userFlow = userFlow;
    }

    @Generated(hash = 69003929)
    public RecordSave() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getAddressFrom() {
        return this.addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return this.addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getFromP2pId() {
        return this.fromP2pId;
    }

    public void setFromP2pId(String fromP2pId) {
        this.fromP2pId = fromP2pId;
    }

    public String getToP2pId() {
        return this.toP2pId;
    }

    public void setToP2pId(String toP2pId) {
        this.toP2pId = toP2pId;
    }

    public float getQlc() {
        return this.qlc;
    }

    public void setQlc(float qlc) {
        this.qlc = qlc;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRecordType() {
        return this.recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getQlcTotal() {
        return this.qlcTotal;
    }

    public void setQlcTotal(String qlcTotal) {
        this.qlcTotal = qlcTotal;
    }

    public int getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public long getStartTimeMillins() {
        return this.startTimeMillins;
    }

    public void setStartTimeMillins(long startTimeMillins) {
        this.startTimeMillins = startTimeMillins;
    }

    public String getUserFlow() {
        return this.userFlow;
    }

    public void setUserFlow(String userFlow) {
        this.userFlow = userFlow;
    }
    
}

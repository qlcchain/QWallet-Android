package com.stratagile.qlink.entity;

import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;

/**
 * Created by huzhipeng on 2018/1/10.
 */

public class QlinkScanResult {
    public QlinkScanResult(ScanResult data) {
        this.data = data;
    }

    public QlinkScanResult(ScanResult data, int status) {
        this.data = data;
        this.status = status;
    }

    public QlinkScanResult(int status) {
        this.status = status;

    }

    public ScanResult getData() {
        return data;
    }

    public void setData(ScanResult data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private ScanResult data;
    //WiFi是否注册 0 未注册， 1已注册
    private int status;
}

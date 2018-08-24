package com.stratagile.qlink.entity.vpn;

/**
 * Created by zl on 2018/08/14.
 * vpn server 配置数据
 */

public class VpnServerFileRsp {

    String vpnfileName;
    int status;
    String fileData;

    public String getVpnfileName() {
        return vpnfileName;
    }

    public void setVpnfileName(String vpnfileName) {
        this.vpnfileName = vpnfileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "VpnServerFileRsp{" +
                "vpnfileName='" + vpnfileName + '\'' +
                ", status=" + status +
                ", fileData='" + fileData + '\'' +
                '}';
    }
}

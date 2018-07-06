package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class SendVpnFileRequest {
    private String filePath;
    private String vpnName;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }
}

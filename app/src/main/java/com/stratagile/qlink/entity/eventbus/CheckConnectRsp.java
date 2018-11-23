package com.stratagile.qlink.entity.eventbus;

/**
 * Created by huzhipeng on 2018/4/16.
 */

public class CheckConnectRsp {

    /**
     * appVersion : 62
     * version : 1
     */

    private int appVersion;
    private int version;

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

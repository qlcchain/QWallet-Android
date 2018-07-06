package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/2/27.
 */

public class RegisterWiFi extends BaseBack {

    /**
     * ssId : YYM-1
     * recordId : 42bcaf0d05a875dbcdd1d8b404ea63f448d8274adf49b2194e59763fdd9afc93
     */

    private String ssId;
    private String recordId;

    public String getSsId() {
        return ssId;
    }

    public void setSsId(String ssId) {
        this.ssId = ssId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}

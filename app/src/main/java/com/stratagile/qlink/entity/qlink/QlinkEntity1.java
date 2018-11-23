package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class QlinkEntity1 {
    private String type;
    private String data;

    public QlinkEntity1(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

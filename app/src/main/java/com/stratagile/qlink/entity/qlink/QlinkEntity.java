package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class QlinkEntity<T> {
    private String type;
    private T data;

    public QlinkEntity(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

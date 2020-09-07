package com.today.step.net;

/**
 * Created by huzhipeng on 2018/1/9.
 */

public class BaseBack<T> {

    /**
     * code : 0
     * msg : success
     */

    private String code;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

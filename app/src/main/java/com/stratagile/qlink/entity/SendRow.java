package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/5/2.
 */

public class SendRow {

    /**
     * jsonrpc : 2.0
     * id : 3
     * result : true
     */

    private String jsonrpc;
    private int id;
    private boolean result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

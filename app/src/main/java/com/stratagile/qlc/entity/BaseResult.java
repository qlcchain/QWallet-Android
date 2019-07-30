package com.stratagile.qlc.entity;

public class BaseResult {

    /**
     * jsonrpc : 2.0
     * id : 2
     * result : 1234567890123456789012345678901234567890123456789012345678901234
     */

    private String jsonrpc;
    private int id;
    private Object result;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

package com.stratagile.qlc.QLCDataModels;

public class BaseResult {

    /**
     * jsonrpc : 2.0
     * id : 2
     * result : 1234567890123456789012345678901234567890123456789012345678901234
     */

    private String jsonrpc;
    private int id;
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

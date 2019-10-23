package com.stratagile.qlink.entity;

public class SendNep5TokenBack {

    /**
     * jsonrpc : 2.0
     * id : 1234
     * result : true
     * txid : 37c6794c6a3d251717785be7f2587e24db156e603faf8341c7305038d423b61c
     */

    private String jsonrpc;
    private int id;
    private boolean result;
    private String txid;

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

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}

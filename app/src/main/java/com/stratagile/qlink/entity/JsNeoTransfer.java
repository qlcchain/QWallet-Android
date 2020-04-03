package com.stratagile.qlink.entity;

public class JsNeoTransfer {

    /**
     * jsonrpc : 2.0
     * id : 1234
     * result : true
     * txid : aa7ba589293e498e8106b17d6ad62e7695b7b63f5d30be9a5488d010ae62c9c5
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

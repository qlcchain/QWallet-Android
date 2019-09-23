package com.stratagile.qlink.entity.stake;

import qlc.bean.StateBlock;

public class LockResult {

    /**
     * jsonrpc : 2.0
     * id : 1234
     * result : true
     * txid : eac10877c222d3e6aad52abd3c4ed9201afda52a5971901a40fd8f714b893e59
     */

    private String jsonrpc;
    private int id;
    private boolean result;
    private String txid;
    private StakeType stakeType;
    private StateBlock stateBlock;

    public StateBlock getStateBlock() {
        return stateBlock;
    }

    public void setStateBlock(StateBlock stateBlock) {
        this.stateBlock = stateBlock;
    }

    public StakeType getStakeType() {
        return stakeType;
    }

    public void setStakeType(StakeType stakeType) {
        this.stakeType = stakeType;
    }

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

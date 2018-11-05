package com.stratagile.qlink.entity;

public class Test {

    /**
     * msg : Request success
     * code : 0
     * data : {operations:[{timestamp:1529963627,transactionHash:0xe9bbf27ef03d207392ddfbfc14e28893591ad76d7feea21b6c7ef06ef8e9b3bc,tokenInfo:{address:0x48f775efbe4f5ece6e0df2f7b5932df56823b990,name:R token,decimals:0,symbol:R,totalSupply:1000000000,owner:0x,txsCount:90988,transfersCount:92204,lastUpdated:1540804255,issuancesCount:0,holdersCount:52967,ethTransfersCount:1,price:{rate:0.2472102166,diff:0.27,diff7d:47.74,ts:1540805202,marketCapUsd:82679457.0,availableSupply:334450000.0,volume24h:2435993.20803,diff30d:137.05252,currency:USD}},type:transfer,value:4,from:0x75e2ecbe0d8b83f4bb30bf8ab0f7e3cfa9271429,to:0x7d5114d0eb75beaa24344a8f8adbdf1936525cc5}]}
     */

    private String msg;
    private String code;
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

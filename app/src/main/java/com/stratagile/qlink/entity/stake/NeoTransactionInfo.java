package com.stratagile.qlink.entity.stake;

import java.util.List;

public class NeoTransactionInfo {

    /**
     * vouts : []
     * vin : []
     * version : 1
     * type : InvocationTransaction
     * txid : 9a8cd41097e5a5826fdc39fffaf2141aa84d4b7aae5c422c79dcae722edf107e
     * time : 1569209318
     * sys_fee : 0.0
     * size : 302
     * scripts : [{"verification":"21030a21d4f076f8098a7ad738fc60bb3edd8fa069e3ea3421cdc4beca739a1b4e5fac","invocation":"408ae079f8b8193a4d473078c6f665875cb26ae1b93680f753865343fbcdb97e06f59563e057bf97328a1c8549e13184377e57f27ed61c97996fec1bf51e6a453b"}]
     * pubkey : null
     * nonce : null
     * net_fee : 0.0
     * description : null
     * contract : null
     * claims : []
     * block_height : 4330404
     * block_hash : 06b2e989423eae0b915e23227d24bcce797c60915edd3ff5c2f5139b9b7d1ae4
     * attributes : [{"usage":"Script","data":"ad44f38c00e2d21acbea1623649129fa9fb551e8"},{"usage":"Remark","data":"313536393230393330383433396565373335373665"}]
     * asset : null
     */

    private int version;
    private String type;
    private String txid;
    private int time;
    private double sys_fee;
    private int size;
    private Object pubkey;
    private Object nonce;
    private double net_fee;
    private Object description;
    private Object contract;
    private int block_height;
    private String block_hash;
    private Object asset;
    private List<?> vouts;
    private List<?> vin;
    private List<ScriptsBean> scripts;
    private List<?> claims;
    private List<AttributesBean> attributes;
    private List<String> errors;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getSys_fee() {
        return sys_fee;
    }

    public void setSys_fee(double sys_fee) {
        this.sys_fee = sys_fee;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Object getPubkey() {
        return pubkey;
    }

    public void setPubkey(Object pubkey) {
        this.pubkey = pubkey;
    }

    public Object getNonce() {
        return nonce;
    }

    public void setNonce(Object nonce) {
        this.nonce = nonce;
    }

    public double getNet_fee() {
        return net_fee;
    }

    public void setNet_fee(double net_fee) {
        this.net_fee = net_fee;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getContract() {
        return contract;
    }

    public void setContract(Object contract) {
        this.contract = contract;
    }

    public int getBlock_height() {
        return block_height;
    }

    public void setBlock_height(int block_height) {
        this.block_height = block_height;
    }

    public String getBlock_hash() {
        return block_hash;
    }

    public void setBlock_hash(String block_hash) {
        this.block_hash = block_hash;
    }

    public Object getAsset() {
        return asset;
    }

    public void setAsset(Object asset) {
        this.asset = asset;
    }

    public List<?> getVouts() {
        return vouts;
    }

    public void setVouts(List<?> vouts) {
        this.vouts = vouts;
    }

    public List<?> getVin() {
        return vin;
    }

    public void setVin(List<?> vin) {
        this.vin = vin;
    }

    public List<ScriptsBean> getScripts() {
        return scripts;
    }

    public void setScripts(List<ScriptsBean> scripts) {
        this.scripts = scripts;
    }

    public List<?> getClaims() {
        return claims;
    }

    public void setClaims(List<?> claims) {
        this.claims = claims;
    }

    public List<AttributesBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributesBean> attributes) {
        this.attributes = attributes;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public static class ScriptsBean {
        /**
         * verification : 21030a21d4f076f8098a7ad738fc60bb3edd8fa069e3ea3421cdc4beca739a1b4e5fac
         * invocation : 408ae079f8b8193a4d473078c6f665875cb26ae1b93680f753865343fbcdb97e06f59563e057bf97328a1c8549e13184377e57f27ed61c97996fec1bf51e6a453b
         */

        private String verification;
        private String invocation;

        public String getVerification() {
            return verification;
        }

        public void setVerification(String verification) {
            this.verification = verification;
        }

        public String getInvocation() {
            return invocation;
        }

        public void setInvocation(String invocation) {
            this.invocation = invocation;
        }
    }

    public static class AttributesBean {
        /**
         * usage : Script
         * data : ad44f38c00e2d21acbea1623649129fa9fb551e8
         */

        private String usage;
        private String data;

        public String getUsage() {
            return usage;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}

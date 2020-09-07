package com.stratagile.qlink.entity;

import java.util.List;

public class TestNetNeoBalance {

    /**
     * jsonrpc : 2.0
     * id : 3
     * result : [{"nep5balance":"8127.18366812"}]
     */

    private String jsonrpc;
    private int id;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * nep5balance : 8127.18366812
         */

        private String nep5balance;

        public String getNep5balance() {
            return nep5balance;
        }

        public void setNep5balance(String nep5balance) {
            this.nep5balance = nep5balance;
        }
    }
}

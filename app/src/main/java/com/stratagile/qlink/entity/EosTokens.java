package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EosTokens extends BaseBack<EosTokens.DataBeanX> {

    /**
     * data : {"errno":0,"data":{"symbol_list":[{"symbol":"EOS","code":"eosio.token","balance":"0.5222"},{"symbol":"ZKS","code":"zkstokensr4u","balance":"31"},{"symbol":"HVT","code":"hirevibeshvt","balance":"0.4785"}]},"errmsg":"Success"}
     */

//    private DataBeanX data;
//
//    public DataBeanX getData() {
//        return data;
//    }
//
//    public void setData(DataBeanX data) {
//        this.data = data;
//    }

    public static class DataBeanX {
        /**
         * errno : 0
         * data : {"symbol_list":[{"symbol":"EOS","code":"eosio.token","balance":"0.5222"},{"symbol":"ZKS","code":"zkstokensr4u","balance":"31"},{"symbol":"HVT","code":"hirevibeshvt","balance":"0.4785"}]}
         * errmsg : Success
         */

        private int errno;
        private DataBean data;
        private String errmsg;

        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public static class DataBean {
            private List<SymbolListBean> symbol_list;

            public List<SymbolListBean> getSymbol_list() {
                return symbol_list;
            }

            public void setSymbol_list(List<SymbolListBean> symbol_list) {
                this.symbol_list = symbol_list;
            }

            public static class SymbolListBean {
                /**
                 * symbol : EOS
                 * code : eosio.token
                 * balance : 0.5222
                 */

                private String symbol;
                @SerializedName("code")
                private String codeX;
                private String balance;

                public String getSymbol() {
                    return symbol;
                }

                public void setSymbol(String symbol) {
                    this.symbol = symbol;
                }

                public String getCodeX() {
                    return codeX;
                }

                public void setCodeX(String codeX) {
                    this.codeX = codeX;
                }

                public String getBalance() {
                    return balance;
                }

                public void setBalance(String balance) {
                    this.balance = balance;
                }
            }
        }
    }
}

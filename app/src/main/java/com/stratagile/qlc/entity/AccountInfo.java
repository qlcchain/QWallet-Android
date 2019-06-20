package com.stratagile.qlc.entity;

import java.util.List;

public class AccountInfo {

    /**
     * account : qlc_1t1uynkmrs597z4ns6ymppwt65baksgdjy1dnw483ubzm97oayyo38ertg44
     * coinBalance : 40000000000000
     * representative : qlc_1t1uynkmrs597z4ns6ymppwt65baksgdjy1dnw483ubzm97oayyo38ertg44
     * tokens : [{"type":"45dd217cd9ff89f7b64ceda4886cc68dde9dfa47a8a422d165e2ce6f9a834fad","header":"758f79b656340c329cb5b11302865c5ff0b0c99fd8a268d6b8760170e33e8cd1","representative":"qlc_1t1uynkmrs597z4ns6ymppwt65baksgdjy1dnw483ubzm97oayyo38ertg44","open":"758f79b656340c329cb5b11302865c5ff0b0c99fd8a268d6b8760170e33e8cd1","balance":"40000000000000","account":"qlc_1t1uynkmrs597z4ns6ymppwt65baksgdjy1dnw483ubzm97oayyo38ertg44","modified":1552455585,"blockCount":1,"tokenName":"QLC","pending":"0"}]
     */

    private String account;

    @Override
    public String toString() {
        return "AccountInfo{" +
                "account='" + account + '\'' +
                ", coinBalance='" + coinBalance + '\'' +
                ", representative='" + representative + '\'' +
                ", tokens=" + tokens +
                '}';
    }

    private String coinBalance;
    private String representative;
    private List<TokensBean> tokens;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(String coinBalance) {
        this.coinBalance = coinBalance;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public List<TokensBean> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokensBean> tokens) {
        this.tokens = tokens;
    }

    public static class TokensBean {
        @Override
        public String toString() {
            return "TokensBean{" +
                    "type='" + type + '\'' +
                    ", header='" + header + '\'' +
                    ", representative='" + representative + '\'' +
                    ", open='" + open + '\'' +
                    ", balance='" + balance + '\'' +
                    ", account='" + account + '\'' +
                    ", modified=" + modified +
                    ", blockCount=" + blockCount +
                    ", tokenName='" + tokenName + '\'' +
                    ", pending='" + pending + '\'' +
                    '}';
        }

        /**
         * type : 45dd217cd9ff89f7b64ceda4886cc68dde9dfa47a8a422d165e2ce6f9a834fad
         * header : 758f79b656340c329cb5b11302865c5ff0b0c99fd8a268d6b8760170e33e8cd1
         * representative : qlc_1t1uynkmrs597z4ns6ymppwt65baksgdjy1dnw483ubzm97oayyo38ertg44
         * open : 758f79b656340c329cb5b11302865c5ff0b0c99fd8a268d6b8760170e33e8cd1
         * balance : 40000000000000
         * account : qlc_1t1uynkmrs597z4ns6ymppwt65baksgdjy1dnw483ubzm97oayyo38ertg44
         * modified : 1552455585
         * blockCount : 1
         * tokenName : QLC
         * pending : 0
         */

        private String type;
        private String header;
        private String representative;
        private String open;
        private String balance;
        private String account;
        private int modified;
        private int blockCount;
        private String tokenName;
        private String pending;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getRepresentative() {
            return representative;
        }

        public void setRepresentative(String representative) {
            this.representative = representative;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getModified() {
            return modified;
        }

        public void setModified(int modified) {
            this.modified = modified;
        }

        public int getBlockCount() {
            return blockCount;
        }

        public void setBlockCount(int blockCount) {
            this.blockCount = blockCount;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }
    }
}

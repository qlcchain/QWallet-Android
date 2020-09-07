package com.stratagile.qlink.entity;

import java.util.List;

public class DefiPrice extends BaseBack{

    private List<DefiTokenListBean> deFiTokenList;

    public List<DefiTokenListBean> getPriceList() {
        return deFiTokenList;
    }

    public void setPriceList(List<DefiTokenListBean> priceList) {
        this.deFiTokenList = priceList;
    }

    public static class DefiTokenListBean {

        /**
         * symbol : WBTC
         * marketCap : 53791965.48558144
         * chain : Ethereum
         * percentChange24h : -1.68129
         * percentChange7d : 0.952075
         * totalSupply : 5818.30964815
         * price : 9245.29094162
         * defiName : Wrapped Bitcoin
         * id : 10a4b5eac65a4ac78402595c3746576d
         * hash : 0x2260fac5e5542a773aa44fbcfedf7c193bc2c599
         * circulatingSupply : 5818.30964815
         */

        private String symbol;
        private String marketCap;
        private String chain;
        private String percentChange24h;
        private String percentChange7d;
        private String totalSupply;
        private String price;
        private String defiName;
        private String id;
        private String hash;
        private String circulatingSupply;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getMarketCap() {
            return marketCap;
        }

        public void setMarketCap(String marketCap) {
            this.marketCap = marketCap;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public String getPercentChange24h() {
            return percentChange24h;
        }

        public void setPercentChange24h(String percentChange24h) {
            this.percentChange24h = percentChange24h;
        }

        public String getPercentChange7d() {
            return percentChange7d;
        }

        public void setPercentChange7d(String percentChange7d) {
            this.percentChange7d = percentChange7d;
        }

        public String getTotalSupply() {
            return totalSupply;
        }

        public void setTotalSupply(String totalSupply) {
            this.totalSupply = totalSupply;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDefiName() {
            return defiName;
        }

        public void setDefiName(String defiName) {
            this.defiName = defiName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getCirculatingSupply() {
            return circulatingSupply;
        }

        public void setCirculatingSupply(String circulatingSupply) {
            this.circulatingSupply = circulatingSupply;
        }
    }
}

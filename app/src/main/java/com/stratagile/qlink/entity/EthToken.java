package com.stratagile.qlink.entity;

public class EthToken {

    /**
     * tokenInfo : {"address":"0xb8c77482e45f1f44de1745f52c74426c631bdd52","name":"BNB","decimals":"18","symbol":"BNB","totalSupply":"194972068000000000000000000","owner":"0x00c5e04176d95a286fcce0e68c683ca0bfec8454","lastUpdated":1527584247,"totalIn":1.4289164214539E26,"totalOut":1.4289164214539E26,"issuancesCount":0,"holdersCount":298000,"price":{"rate":"11.8434","diff":-2.83,"diff7d":-17.95,"ts":"1527583762","marketCapUsd":"1350636615.0","availableSupply":"114041290.0","volume24h":"29083700.0","diff30d":-22.33479569554,"currency":"USD"}}
     * balance : 8.17E18
     * totalIn : 0
     * totalOut : 0
     */

    private TokenInfoBean tokenInfo;
    private double balance;
    private int totalIn;
    private int totalOut;

    public TokenInfoBean getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfoBean tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(int totalIn) {
        this.totalIn = totalIn;
    }

    public int getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(int totalOut) {
        this.totalOut = totalOut;
    }

    public static class TokenInfoBean {
        /**
         * address : 0xb8c77482e45f1f44de1745f52c74426c631bdd52
         * name : BNB
         * decimals : 18
         * symbol : BNB
         * totalSupply : 194972068000000000000000000
         * owner : 0x00c5e04176d95a286fcce0e68c683ca0bfec8454
         * lastUpdated : 1527584247
         * totalIn : 1.4289164214539E26
         * totalOut : 1.4289164214539E26
         * issuancesCount : 0
         * holdersCount : 298000
         * price : {"rate":"11.8434","diff":-2.83,"diff7d":-17.95,"ts":"1527583762","marketCapUsd":"1350636615.0","availableSupply":"114041290.0","volume24h":"29083700.0","diff30d":-22.33479569554,"currency":"USD"}
         */

        private String address;
        private String name;
        private String decimals;
        private String symbol;
        private String totalSupply;
        private String owner;
        private int lastUpdated;
        private double totalIn;
        private double totalOut;
        private int issuancesCount;
        private int holdersCount;
        private PriceBean price;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDecimals() {
            return decimals;
        }

        public void setDecimals(String decimals) {
            this.decimals = decimals;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getTotalSupply() {
            return totalSupply;
        }

        public void setTotalSupply(String totalSupply) {
            this.totalSupply = totalSupply;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public int getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(int lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public double getTotalIn() {
            return totalIn;
        }

        public void setTotalIn(double totalIn) {
            this.totalIn = totalIn;
        }

        public double getTotalOut() {
            return totalOut;
        }

        public void setTotalOut(double totalOut) {
            this.totalOut = totalOut;
        }

        public int getIssuancesCount() {
            return issuancesCount;
        }

        public void setIssuancesCount(int issuancesCount) {
            this.issuancesCount = issuancesCount;
        }

        public int getHoldersCount() {
            return holdersCount;
        }

        public void setHoldersCount(int holdersCount) {
            this.holdersCount = holdersCount;
        }

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public static class PriceBean {
            /**
             * rate : 11.8434
             * diff : -2.83
             * diff7d : -17.95
             * ts : 1527583762
             * marketCapUsd : 1350636615.0
             * availableSupply : 114041290.0
             * volume24h : 29083700.0
             * diff30d : -22.33479569554
             * currency : USD
             */

            private String rate;
            private double diff;
            private double diff7d;
            private String ts;
            private String marketCapUsd;
            private String availableSupply;
            private String volume24h;
            private double diff30d;
            private String currency;

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public double getDiff() {
                return diff;
            }

            public void setDiff(double diff) {
                this.diff = diff;
            }

            public double getDiff7d() {
                return diff7d;
            }

            public void setDiff7d(double diff7d) {
                this.diff7d = diff7d;
            }

            public String getTs() {
                return ts;
            }

            public void setTs(String ts) {
                this.ts = ts;
            }

            public String getMarketCapUsd() {
                return marketCapUsd;
            }

            public void setMarketCapUsd(String marketCapUsd) {
                this.marketCapUsd = marketCapUsd;
            }

            public String getAvailableSupply() {
                return availableSupply;
            }

            public void setAvailableSupply(String availableSupply) {
                this.availableSupply = availableSupply;
            }

            public String getVolume24h() {
                return volume24h;
            }

            public void setVolume24h(String volume24h) {
                this.volume24h = volume24h;
            }

            public double getDiff30d() {
                return diff30d;
            }

            public void setDiff30d(double diff30d) {
                this.diff30d = diff30d;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }
        }
    }
}

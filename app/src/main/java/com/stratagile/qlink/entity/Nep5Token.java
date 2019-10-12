package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// FIXME generate failure  field _$324
public class Nep5Token {

    private ArrayList<TokensBean> tokens;

    public ArrayList<TokensBean> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<TokensBean> tokens) {
        this.tokens = tokens;
    }

    public static class TokensBean {
        /**
         * symbol : NEO
         * networks : {"1":{"decimals":0,"hash":"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","totalSupply":10000000}}
         * companyName : ACAT Token
         * type : NEP5
         * image : https://rawgit.com/CityOfZion/neo-tokens/master/assets/png/asa.png
         */

        private String symbol;
        private NetworksBean networks;
        private String companyName;
        private String type;
        private String image;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public NetworksBean getNetworks() {
            return networks;
        }

        public void setNetworks(NetworksBean networks) {
            this.networks = networks;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public static class NetworksBean {
            /**
             * 1 : {"decimals":0,"hash":"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","totalSupply":10000000}
             */

            @SerializedName("1")
            private _$1Bean _$1;

            public _$1Bean get_$1() {
                return _$1;
            }

            public void set_$1(_$1Bean _$1) {
                this._$1 = _$1;
            }

            public static class _$1Bean {
                /**
                 * decimals : 0
                 * hash : c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b
                 * totalSupply : 10000000
                 */

                private int decimals;
                private String hash;
                private float totalSupply;

                public int getDecimals() {
                    return decimals;
                }

                public void setDecimals(int decimals) {
                    this.decimals = decimals;
                }

                public String getHash() {
                    return hash;
                }

                public void setHash(String hash) {
                    this.hash = hash;
                }

                public float getTotalSupply() {
                    return totalSupply;
                }

                public void setTotalSupply(float totalSupply) {
                    this.totalSupply = totalSupply;
                }
            }
        }
    }
}

package com.stratagile.qlink.blockchain.bean;

import java.util.List;

public class CreateAccountBean {

    /**
     * creator : bitcoin
     * name : eason
     * owner : {"threshold":1,"keys":[{"key":"EOS4ufZoTw95yHJS6Cyz3h4w5a2W4cyYpMYRnd7gbFZuCfPxUFS6r","weight":1}],"accounts":[],"waits":[]}
     * active : {"threshold":1,"keys":[{"key":"EOS4ufZoTw95yHJS6Cyz3h4w5a2W4cyYpMYRnd7gbFZuCfPxUFS6r","weight":1}],"accounts":[],"waits":[]}
     */

    private String creator;
    private String name;
    private OwnerBean owner;
    private ActiveBean active;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OwnerBean getOwner() {
        return owner;
    }

    public void setOwner(OwnerBean owner) {
        this.owner = owner;
    }

    public ActiveBean getActive() {
        return active;
    }

    public void setActive(ActiveBean active) {
        this.active = active;
    }

    public static class OwnerBean {
        /**
         * threshold : 1
         * keys : [{"key":"EOS4ufZoTw95yHJS6Cyz3h4w5a2W4cyYpMYRnd7gbFZuCfPxUFS6r","weight":1}]
         * accounts : []
         * waits : []
         */

        private int threshold;
        private List<KeysBean> keys;
        private List<?> accounts;
        private List<?> waits;

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }

        public List<KeysBean> getKeys() {
            return keys;
        }

        public void setKeys(List<KeysBean> keys) {
            this.keys = keys;
        }

        public List<?> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<?> accounts) {
            this.accounts = accounts;
        }

        public List<?> getWaits() {
            return waits;
        }

        public void setWaits(List<?> waits) {
            this.waits = waits;
        }

    }

    public static class ActiveBean {
        /**
         * threshold : 1
         * keys : [{"key":"EOS4ufZoTw95yHJS6Cyz3h4w5a2W4cyYpMYRnd7gbFZuCfPxUFS6r","weight":1}]
         * accounts : []
         * waits : []
         */

        private int threshold;
        private List<KeysBean> keys;
        private List<?> accounts;
        private List<?> waits;

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }

        public List<KeysBean> getKeys() {
            return keys;
        }

        public void setKeys(List<KeysBean> keys) {
            this.keys = keys;
        }

        public List<?> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<?> accounts) {
            this.accounts = accounts;
        }

        public List<?> getWaits() {
            return waits;
        }

        public void setWaits(List<?> waits) {
            this.waits = waits;
        }
    }

    public static class KeysBean {
        /**
         * key : EOS4ufZoTw95yHJS6Cyz3h4w5a2W4cyYpMYRnd7gbFZuCfPxUFS6r
         * weight : 1
         */

        private String key;
        private int weight;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}

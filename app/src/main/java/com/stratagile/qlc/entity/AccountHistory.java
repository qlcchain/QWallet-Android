package com.stratagile.qlc.entity;

import java.util.List;

public class AccountHistory {

    /**
     * result : [{"amount":"3665000000","address":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","oracle":"0","previous":"349729b97fbdefbc5aa66353666131c92bc38eea206d01a2eccaf3cd9135b6a1","signature":"4664dc19e6127bc9aac9ea261c7d8889be3c65105e06b8af25b92fb59874b9c0023c2f668162c8d32d9997eca832d8c13391a35eea805f295f3282ee410ec602","work":"71dafdf3df4659ad","link":"daf90051663b02916eb31dc1906de1747ae849e3d18d4b92793ddc0d3c5b37b8","tokenName":"QLC","storage":"0","type":"Send","message":"141c62870a7e72e79f619529b740767082f39bab651b16b7ed8cae0822554e1d","token":"a7e8fa30c063e96a489a47bc43909505bd86735da4a109dca28be936118a8582","network":"0","povHeight":0,"balance":"9459882638679","extra":"0000000000000000000000000000000000000000000000000000000000000000","vote":"0","representative":"qlc_3hw8s1zubhxsykfsq5x7kh6eyibas9j3ga86ixd7pnqwes1cmt9mqqrngap4","hash":"98bfcb89a4155f3dbc383c2a75d42a2068019becb1178d06d1705d8cee4c0963","timestamp":1559025456},{"amount":"4643640000","address":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb","oracle":"0","previous":"90b8a9b51851ab1cd6705d397812cba4edb4d72c61302bd0b0b40fbc65ab9a7a","signature":"052ff5f2da0c8c51795e88ff184dad8fef3b5724274d6e5bfa1b72533abc18f314a93835c14e797b67b836dc1d881a31105c37d2f04e4f459350f1a75ac81f07","work":"a1994cfab778408f","link":"daf90051663b02916eb31dc1906de1747ae849e3d18d4b92793ddc0d3c5b37b8","tokenName":"QLC","storage":"0","type":"Send","message":"7fac1ae6890acfa10ea7210406dfd80e1ef857c7f3cda818b1ac7127322b58be","token":"a7e8fa30c063e96a489a47bc43909505bd86735da4a109dca28be936118a8582","network":"0","povHeight":0,"balance":"9463547638679","extra":"0000000000000000000000000000000000000000000000000000000000000000","vote":"0","representative":"qlc_3hw8s1zubhxsykfsq5x7kh6eyibas9j3ga86ixd7pnqwes1cmt9mqqrngap4","hash":"349729b97fbdefbc5aa66353666131c92bc38eea206d01a2eccaf3cd9135b6a1","timestamp":1559025359}]
     * id : 02c6f607626c4a909772c8e79451e55f
     * jsonrpc : 2.0
     */

    private String id;
    private String jsonrpc;
    private List<ResultBean> result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * amount : 3665000000
         * address : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
         * oracle : 0
         * previous : 349729b97fbdefbc5aa66353666131c92bc38eea206d01a2eccaf3cd9135b6a1
         * signature : 4664dc19e6127bc9aac9ea261c7d8889be3c65105e06b8af25b92fb59874b9c0023c2f668162c8d32d9997eca832d8c13391a35eea805f295f3282ee410ec602
         * work : 71dafdf3df4659ad
         * link : daf90051663b02916eb31dc1906de1747ae849e3d18d4b92793ddc0d3c5b37b8
         * tokenName : QLC
         * storage : 0
         * type : Send
         * message : 141c62870a7e72e79f619529b740767082f39bab651b16b7ed8cae0822554e1d
         * token : a7e8fa30c063e96a489a47bc43909505bd86735da4a109dca28be936118a8582
         * network : 0
         * povHeight : 0
         * balance : 9459882638679
         * extra : 0000000000000000000000000000000000000000000000000000000000000000
         * vote : 0
         * representative : qlc_3hw8s1zubhxsykfsq5x7kh6eyibas9j3ga86ixd7pnqwes1cmt9mqqrngap4
         * hash : 98bfcb89a4155f3dbc383c2a75d42a2068019becb1178d06d1705d8cee4c0963
         * timestamp : 1559025456
         */

        private String amount;
        private String address;
        private String oracle;
        private String previous;
        private String signature;
        private String work;
        private String link;
        private String tokenName;
        private String storage;
        private String type;
        private String message;
        private String token;
        private String network;
        private int povHeight;
        private String balance;
        private String extra;
        private String vote;
        private String representative;
        private String hash;
        private int timestamp;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOracle() {
            return oracle;
        }

        public void setOracle(String oracle) {
            this.oracle = oracle;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public int getPovHeight() {
            return povHeight;
        }

        public void setPovHeight(int povHeight) {
            this.povHeight = povHeight;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getVote() {
            return vote;
        }

        public void setVote(String vote) {
            this.vote = vote;
        }

        public String getRepresentative() {
            return representative;
        }

        public void setRepresentative(String representative) {
            this.representative = representative;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}

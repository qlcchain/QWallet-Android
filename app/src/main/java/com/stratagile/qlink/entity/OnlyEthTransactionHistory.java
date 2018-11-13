package com.stratagile.qlink.entity;

import java.util.List;

public class OnlyEthTransactionHistory extends BaseBack {


    /**
     * data : [{"timestamp":1541762107,"from":"0x980e7917c610e2c2d4e669c920980cb1b915bbc7","to":"0x14758245c6e5f450e9fbed333f0d9c36ace3bc76","hash":"0x1365886a48900059afa4406d2c27cea38dd937adffaa91e35b6d868748b1f156","value":1.0e-6,"input":"0x","success":true},{"timestamp":1541671275,"from":"0x980e7917c610e2c2d4e669c920980cb1b915bbc7","to":"0xdf14da8d2f72bf5cf1f6c3e429e5c759b5739e6c","hash":"0x2054bdcfd1c03019abe17bf4e3c1a3159acace64652fe3c82dc32ad65b91c438","value":0.001,"input":"0x","success":true},{"timestamp":1541668734,"from":"0xfdae196edc10a085d95cf157c658d526fb94e4ae","to":"0x980e7917c610e2c2d4e669c920980cb1b915bbc7","hash":"0x5b2eed5d5fce4827f3fad417a4a76fdadbedf0fe46454fe0b2566214ae7e1030","value":0.05,"input":"0x","success":true},{"timestamp":1526458812,"from":"0xfdae196edc10a085d95cf157c658d526fb94e4ae","to":"0x980e7917c610e2c2d4e669c920980cb1b915bbc7","hash":"0xc053fb80be37166f1c0a6d17011750b3a0441f1275715b78590bb69459fbdb98","value":0.01,"input":"0x","success":true}]
     */

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class EthTransactionBean{
        private List<OperationsBean> operations;

        public List<OperationsBean> getOperations() {
            return operations;
        }

        public void setOperations(List<OperationsBean> operations) {
            this.operations = operations;
        }
        public static class OperationsBean {
            /**
             * timestamp : 1541762107
             * from : 0x980e7917c610e2c2d4e669c920980cb1b915bbc7
             * to : 0x14758245c6e5f450e9fbed333f0d9c36ace3bc76
             * hash : 0x1365886a48900059afa4406d2c27cea38dd937adffaa91e35b6d868748b1f156
             * value : 1.0E-6
             * input : 0x
             * success : true
             */

            private int timestamp;
            private String from;
            private String to;
            private String hash;
            private double value;
            private String input;
            private boolean success;

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }

            public String getInput() {
                return input;
            }

            public void setInput(String input) {
                this.input = input;
            }

            public boolean isSuccess() {
                return success;
            }

            public void setSuccess(boolean success) {
                this.success = success;
            }
        }
    }

}

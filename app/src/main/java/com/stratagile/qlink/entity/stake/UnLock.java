package com.stratagile.qlink.entity.stake;

import com.stratagile.qlink.entity.BaseBack;

public class UnLock extends BaseBack<UnLock.DataBean> {

    /**
     * data : {"result":{"unlockTxId":"08b2534acb7697f842769254602b5a1fba97e3c933b4d072dab86e7fba6ca3bc","unsignedRawTx":"d1013f202d6316321c4ba7ed16d4f59a80c54b9999bebb9f6240c9820e2217f1a8e1dbfc51c106756e6c6f636b676a58fbec87018112afe0003fbaef5d090eaa7830000000000000000001205ae2d6f67509a6a336edd060ec622e65043918870000"},"success":true}
     */


    public static class DataBean {
        /**
         * result : {"unlockTxId":"08b2534acb7697f842769254602b5a1fba97e3c933b4d072dab86e7fba6ca3bc","unsignedRawTx":"d1013f202d6316321c4ba7ed16d4f59a80c54b9999bebb9f6240c9820e2217f1a8e1dbfc51c106756e6c6f636b676a58fbec87018112afe0003fbaef5d090eaa7830000000000000000001205ae2d6f67509a6a336edd060ec622e65043918870000"}
         * success : true
         */

        private ResultBean result;
        private boolean success;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public static class ResultBean {
            /**
             * unlockTxId : 08b2534acb7697f842769254602b5a1fba97e3c933b4d072dab86e7fba6ca3bc
             * unsignedRawTx : d1013f202d6316321c4ba7ed16d4f59a80c54b9999bebb9f6240c9820e2217f1a8e1dbfc51c106756e6c6f636b676a58fbec87018112afe0003fbaef5d090eaa7830000000000000000001205ae2d6f67509a6a336edd060ec622e65043918870000
             */

            private String unlockTxId;
            private String unsignedRawTx;
            private String signature;
            private String publicKey;

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getPublicKey() {
                return publicKey;
            }

            public void setPublicKey(String publicKey) {
                this.publicKey = publicKey;
            }

            public String getUnlockTxId() {
                return unlockTxId;
            }

            public void setUnlockTxId(String unlockTxId) {
                this.unlockTxId = unlockTxId;
            }

            public String getUnsignedRawTx() {
                return unsignedRawTx;
            }

            public void setUnsignedRawTx(String unsignedRawTx) {
                this.unsignedRawTx = unsignedRawTx;
            }
        }
    }
}

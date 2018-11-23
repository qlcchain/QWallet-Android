package com.stratagile.qlink.entity.qlink;

public class SendVpnFileNew {

    /**
     * type : sendVpnFileNewRsp
     * data : {"msgid":18,"msglen":5761,"md5":"328bfe622565d20a7dceb74257f23302","more":0,"offset":0,"msg":"\nclient\nnobind\ndev tun\nremote-cert-tls server\n\nremote 121.60.36.234 18888 udp\n\n<key>\n-----BEGIN PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDIXQ8oYrtB2l4n\ndHeWmpqPZAGhSyIaqieqFOL3D5ol17bKV5gCZ2Qgbe3ly8NMy8Lkbs0vVX94ftcf\nHFhzLH3iLpM6X0nQKfG/TYtPLkrhTjJpq+wuERrDKB/Jn6wgwyFIU9Lcpyztg3YB\n/RmfMylYhpuYJESW+IjUBb1R2yZxjvx4qLGImb2nUSzYJ/IhDrxIxsHHFmuxzzZz\nSjXoztxQpQgsRFwdqq3QtYrhOIzjp1rninerAykMbdMWVSJ7d6NckdvhSqETm5Bv\nez5rynA1e1CkJ1KFXfDZsE8XiG02WTjBKTOSZubdyZu527XO6BZ563saR7b4PhSN\nsLUdZ39nAgMBAAECggEBAIal0yvmvcTRhPiq0jsJhtjZ8iZ8oVyeAK3R/3zcpVDN\nGG/+UY88ABOzDG6jconHXR+6PnWS1WkahGLJ3772pVo8xoAxzR7xMR7Ic9gwWe3z\naPmOqdeDcyK3cjVC8p/JwjIi8s+KIS00bTeE6ZUNAroVK7cgmF+Egh9KBCJgTgN9\nTIkeQ6M3pgmLTGxODuk/2z5GWUNYyNK70sJOyaby0J3/648hVUDYcQ+7iAve9311\nKxmkShsPkhqcZ2L78C3M8rBpVgSGYjXAtVIKfR5B++K02XO3LGy1JcanH5boWZs5\nq49er/UdgRaDz2t8sVCengGSO2njXJWPHv8dsXh/SMECgYEA82AvR6mhIMbbHrtx\nMpHsGmob/4l4vAGmOaffaM7Nfc6wvoeXOJRz8xaOtOTiY14kXBWB4PoCg+8PT8mz\nriUZI1gssVs8l2AKaqcQ8rKkHACNc8GsVoXrScQVhC"}
     */

    private String type;
    private DataBean data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * msgid : 18
         * msglen : 5761
         * md5 : 328bfe622565d20a7dceb74257f23302
         * more : 0
         * offset : 0
         * msg :
         client
         nobind
         dev tun
         remote-cert-tls server

         remote 121.60.36.234 18888 udp

         <key>
         -----BEGIN PRIVATE KEY-----
         MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDIXQ8oYrtB2l4n
         dHeWmpqPZAGhSyIaqieqFOL3D5ol17bKV5gCZ2Qgbe3ly8NMy8Lkbs0vVX94ftcf
         HFhzLH3iLpM6X0nQKfG/TYtPLkrhTjJpq+wuERrDKB/Jn6wgwyFIU9Lcpyztg3YB
         /RmfMylYhpuYJESW+IjUBb1R2yZxjvx4qLGImb2nUSzYJ/IhDrxIxsHHFmuxzzZz
         SjXoztxQpQgsRFwdqq3QtYrhOIzjp1rninerAykMbdMWVSJ7d6NckdvhSqETm5Bv
         ez5rynA1e1CkJ1KFXfDZsE8XiG02WTjBKTOSZubdyZu527XO6BZ563saR7b4PhSN
         sLUdZ39nAgMBAAECggEBAIal0yvmvcTRhPiq0jsJhtjZ8iZ8oVyeAK3R/3zcpVDN
         GG/+UY88ABOzDG6jconHXR+6PnWS1WkahGLJ3772pVo8xoAxzR7xMR7Ic9gwWe3z
         aPmOqdeDcyK3cjVC8p/JwjIi8s+KIS00bTeE6ZUNAroVK7cgmF+Egh9KBCJgTgN9
         TIkeQ6M3pgmLTGxODuk/2z5GWUNYyNK70sJOyaby0J3/648hVUDYcQ+7iAve9311
         KxmkShsPkhqcZ2L78C3M8rBpVgSGYjXAtVIKfR5B++K02XO3LGy1JcanH5boWZs5
         q49er/UdgRaDz2t8sVCengGSO2njXJWPHv8dsXh/SMECgYEA82AvR6mhIMbbHrtx
         MpHsGmob/4l4vAGmOaffaM7Nfc6wvoeXOJRz8xaOtOTiY14kXBWB4PoCg+8PT8mz
         riUZI1gssVs8l2AKaqcQ8rKkHACNc8GsVoXrScQVhC
         */

        private int msgid;
        private int msglen;
        private String md5;
        private int more;
        private int offset;
        private String msg;
        private int register;

        public int getRegister() {
            return register;
        }

        public void setRegister(int register) {
            this.register = register;
        }

        public int getMsgid() {
            return msgid;
        }

        public void setMsgid(int msgid) {
            this.msgid = msgid;
        }

        public int getMsglen() {
            return msglen;
        }

        public void setMsglen(int msglen) {
            this.msglen = msglen;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public int getMore() {
            return more;
        }

        public void setMore(int more) {
            this.more = more;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

package com.stratagile.qlink.entity.qlink;

public class SendVpnFileList {


    /**
     * type : sendVpnFileListRsp
     * data : {"msgid":4,"msglen":12,"md5":"5a5923fba5ac783f38f8077712454db8","more":0,"offset":0,"msg":"WinQVpn.ovpn"}
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
         * msgid : 4
         * msglen : 12
         * md5 : 5a5923fba5ac783f38f8077712454db8
         * more : 0
         * offset : 0
         * msg : WinQVpn.ovpn
         */

        private int msgid;
        private int msglen;
        private String md5;
        private int more;
        private int offset;
        private String msg;

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

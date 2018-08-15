package com.stratagile.qlink.entity.eventbus;

import com.stratagile.qlink.entity.vpn.VpnServerFileRsp;

/**
 * Created by zl on 2018/8/10.
 */

public class ServerVpnSendComplete {


    /**
     * type : sendVpnFileRsp
     * data : {"vpnfileName":"/root/QLCChain/HZ_NO.ovpn","status":0,"fileData":"IwrdHTNrG/w4YcS6Iqq\nzqX6R80QNtzoqFZrkHQ2FmrttQ6JRDaJ8dAEptqSxPHEdIdhJ7CqoF405XJRC7vU\n+GbYkSMHEZUPIsXAEK2jrg65WzsMxMFQDw6tSKFMleg6Bj2B8cJFcBycvRtxr6e0\nfqDZwGYSUeqNwednQhIcsQXH5R3AGZB8tuhdE78ZhL0cM0XRfo0izFqUPkZ9Qjs5\nL3BNN9q4L42y5gDcuWWQLf0/+R1DfzBZr7JXalCPp8pB9KH7fg==\n-----END CERTIFICATE-----\n</ca>\n<cert>\nCertificate:\n    Data:\n        Version: 3 (0x2)\n        Serial Number:\n            a0:de:fb:83:d5:7b:63:41:b0:ea:61:6d:99:93:02:a5\n    Signature Algorithm: sha256WithRSAEncryption\n        Issuer: CN=HZ_CC\n        Validity\n            Not Before: Jul 26 07:11:31 2018 GMT\n            Not After : Jul 23 07:11:31 2028 GMT\n        Subject: CN=client\n        Subject Public Key Info:\n            Public Key Algorithm: rsaEncryption\n                Public-Key: (2048 bit)\n                Modulus:\n                    00:b5:03:20:4b:60:17:69:e6:66:7a:2b:8f:78:4c:\n                    56:a1:ea:ec:cd:9a:48:1f:85:0e:76:75:56:8e:67:\n                    5f:96:eb:39:7f:78:e0:1b:e1:be:c5:a7:c6:07:fd:\n   "}
     */

    private String type;
    private VpnServerFileRsp data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VpnServerFileRsp getData() {
        return data;
    }

    public void setData(VpnServerFileRsp data) {
        this.data = data;
    }
}

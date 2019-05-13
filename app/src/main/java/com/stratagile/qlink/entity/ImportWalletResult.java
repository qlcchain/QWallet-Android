package com.stratagile.qlink.entity;

import com.stratagile.qlink.db.Wallet;

import java.util.List;
/**
 * Created by zl on 2018/04/16.
 */

public class ImportWalletResult extends BaseBack<List<ImportWalletResult.DataBean>> {

//    private List<DataBean> data;
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * wp : a8d45f014557c4d3a3423dba6017a157b9768061206aadaeb50d0333a717c584
         * wallet : {"privateKey":"a8d45f014557c4d3a3423dba6017a157b9768061206aadaeb50d0333a717c584","wif":"L2stn2iGSXSoLi1a6ARQ2UQUbSB1uFPDWRbY5GgaZTZHvWBGSaCq","publicKey":"0397e61ba9f7daaad812c33a989c10d2a17ce90136a9d7369eee2f5847526365b2","scriptHash":"04358451c14f8160466654aed84c0cfe35d83910","address":"AHFfkgLdWd9acxd4NL7BAJ8YvTpw5bQFe2"}
         */

        private String wp;
        private Wallet wallet;

        public String getWp() {
            return wp;
        }

        public void setWp(String wp) {
            this.wp = wp;
        }

        public Wallet getWallet() {
            return wallet;
        }

        public void setWallet(Wallet wallet) {
            this.wallet = wallet;
        }


    }
}

package com.stratagile.qlink.entity;

import com.stratagile.qlink.db.Wallet;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class CreateWallet extends BaseBack {

    /**
     * data : {"privateKey":"a823e1724c88b73723e284939bbb982f3f6c04ea31b31848ed6973625d34cc0e","publicKey":"02085ba339c8294da33df07d620442fd8b1633ce6e1027d87b89e268a03da530a1","scriptHash":"6c3d998e3ae16927d2f1dea09b7daf989fdbdd7a","address":"ASyXrrn6nV9MjXQRQ5RoGfFN9xArNFH9Vg"}
     */

    private Wallet data;

    public Wallet getData() {
        return data;
    }

    public void setData(Wallet data) {
        this.data = data;
    }


}

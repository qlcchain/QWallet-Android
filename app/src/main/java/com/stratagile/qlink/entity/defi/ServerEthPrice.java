package com.stratagile.qlink.entity.defi;

import com.stratagile.qlink.entity.BaseBack;

public class ServerEthPrice extends BaseBack {

    /**
     * msg : Request success
     * code : 0
     * currentTimeMillis : 1598433992262
     * gasPrice : {"status":"1","message":"OK","result":{"LastBlock":"10735136","SafeGasPrice":"52","ProposeGasPrice":"77","FastGasPrice":"84"}}
     */

    private long currentTimeMillis;
    private String gasPrice;

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }
}

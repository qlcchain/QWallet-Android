package com.stratagile.qlink.api.transaction;

/**
 * Created by huzhipeng on 2018/5/4.
 */

public interface SendBackWithTxId {
    void onSuccess(String txid);

    void onFailure();
}

package com.stratagile.qlink.web3;


import com.stratagile.qlink.web3.entity.Web3Transaction;

public interface OnSignTransactionListener {
    void onSignTransaction(Web3Transaction transaction, String url);
}

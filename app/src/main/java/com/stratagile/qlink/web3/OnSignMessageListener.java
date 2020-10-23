package com.stratagile.qlink.web3;


import com.stratagile.qlink.entity.walletconnect.EthereumMessage;

public interface OnSignMessageListener {
    void onSignMessage(EthereumMessage message);
}

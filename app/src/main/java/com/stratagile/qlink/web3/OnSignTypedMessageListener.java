package com.stratagile.qlink.web3;


import com.stratagile.qlink.entity.walletconnect.EthereumTypedMessage;

public interface OnSignTypedMessageListener {
    void onSignTypedMessage(EthereumTypedMessage message);
}

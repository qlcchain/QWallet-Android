package com.stratagile.qlink.web3;


import com.stratagile.qlink.entity.walletconnect.EthereumMessage;

public interface OnSignPersonalMessageListener {
    void onSignPersonalMessage(EthereumMessage message);
}

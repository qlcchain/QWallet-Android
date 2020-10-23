package com.stratagile.qlink.web3.entity;


import com.stratagile.qlink.entity.DAppFunction;
import com.stratagile.qlink.entity.walletconnect.Signable;

/**
 * Created by James on 6/04/2019.
 * Stormbird in Singapore
 */
public interface FunctionCallback
{
    void signMessage(Signable sign, DAppFunction dAppFunction);
    void functionSuccess();
    void functionFailed();
}

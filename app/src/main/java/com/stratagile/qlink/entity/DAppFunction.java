package com.stratagile.qlink.entity;


import com.stratagile.qlink.entity.walletconnect.Signable;

public interface DAppFunction
{
    void DAppError(Throwable error, Signable message);
    void DAppReturn(byte[] data, Signable message);
}

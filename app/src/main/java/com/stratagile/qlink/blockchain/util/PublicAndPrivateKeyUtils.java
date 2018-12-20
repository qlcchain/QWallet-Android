package com.stratagile.qlink.blockchain.util;


import com.stratagile.qlink.blockchain.cypto.ec.EosPrivateKey;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pocketEos on 2018/2/3.
 */

public class PublicAndPrivateKeyUtils {

    public static EosPrivateKey[] getPrivateKey(int count) {
        EosPrivateKey[] retKeys = new EosPrivateKey[count];
        for (int i = 0; i < count; i++) {
            retKeys[i] = new EosPrivateKey();
        }

        return retKeys;
    }

}

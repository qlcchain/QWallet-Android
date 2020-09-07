package com.stratagile.qlink.utils

import com.stratagile.qlink.hexStringToByteArray
import com.stratagile.qlink.toHex
import io.neow3j.crypto.ECKeyPair
import io.neow3j.crypto.Sign
import io.neow3j.utils.Numeric

//import io.neow3j.crypto.ECKeyPair
//import io.neow3j.crypto.Sign
//import io.neow3j.utils.Keys
//import io.neow3j.utils.Numeric

object NeoUtils {
    @JvmStatic
    fun sign(data: String, privateKey : String): String {
//        return String(neoutils.Neoutils.sign(data.toByteArray(), privateKey))
        return Sign.signMessage(data.hexStringToByteArray(), ECKeyPair.create(Numeric.toBigInt(privateKey))).concatenated.toHex().toLowerCase()
    }

    @JvmStatic
    fun isValidAddress(address: String?): Boolean {
        return neoutils.Neoutils.validateNEOAddress(address)
//        return Keys.isValidAddress(address)
    }
}
package com.stratagile.qlink.Crypto

import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Created by drei on 11/29/17.
 */

class Decryptor {
    private val TRANSFORM = "AES/GCM/NoPadding"
    private val ANDROID_KEY_STORE = "AndroidKeyStore"

    private var keyStore: KeyStore? = null
    init {
        initKeyStore()
    }

    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore?.load(null)
    }

    fun keyStoreEntryExists(alias: String): Boolean {
        return keyStore?.getEntry(alias, null) != null
    }

    fun decrypt(alias: String, encryptedData: ByteArray, encryptionIv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORM)
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        return String(cipher.doFinal(encryptedData), Charsets.UTF_8)
    }

    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore?.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}
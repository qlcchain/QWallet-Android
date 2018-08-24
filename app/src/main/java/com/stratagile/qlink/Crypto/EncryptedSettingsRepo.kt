package com.stratagile.qlink.Crypto

import android.content.Context
import android.preference.PreferenceManager
import android.util.Base64

/**
 * Created by drei on 11/29/17.
 */

class EncryptedInfo {
    var data: String? = null
    var iv: ByteArray? = null
}

object EncryptedSettingsRepository {
    fun getProperty(key: String, context: Context): EncryptedInfo {
        val info = EncryptedInfo()

        info.data = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null)

        val iv = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("${key}_iv", null)

        if (iv != null) {
            info.iv = Base64.decode(iv, Base64.DEFAULT)
        }
        return info
    }

    fun setProperty(key: String, encryptedValue: String, iv: ByteArray, context: Context) {
        val ivString = Base64.encodeToString(iv, Base64.DEFAULT)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(context).edit()
        settingPref.putString(key, encryptedValue)
        settingPref.apply()

        val settingIvPref = PreferenceManager.getDefaultSharedPreferences(context).edit()
        settingIvPref.putString("${key}_iv", ivString)
        settingIvPref.apply()
    }
}
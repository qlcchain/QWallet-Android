package com.stratagile.qlink.shadowsocks.utils

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.stratagile.qlink.application.AppConfig
//import com.github.shadowsocks.App.Companion.app
//import com.github.shadowsocks.bg.BaseService
//import com.github.shadowsocks.database.Profile
//import com.github.shadowsocks.database.ProfileManager
//import com.github.shadowsocks.preference.DataStore
import com.stratagile.qlink.shadowsocks.bg.BaseService
import com.stratagile.qlink.shadowsocks.database.Profile
import com.stratagile.qlink.shadowsocks.database.ProfileManager
import com.stratagile.qlink.shadowsocks.preference.DataStore
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@TargetApi(24)
object DirectBoot : BroadcastReceiver() {
    private val file = File(AppConfig.instance.deviceStorage.noBackupFilesDir, "directBootProfile")
    private var registered = false

    fun getDeviceProfile(): Profile? = try {
        ObjectInputStream(file.inputStream()).use { it.readObject() as Profile }
    } catch (_: IOException) { null }

    fun clean() {
        file.delete()
        File(AppConfig.instance.deviceStorage.noBackupFilesDir, BaseService.CONFIG_FILE).delete()
    }

    /**
     * app.currentProfile will call this.
     */
    fun update(profile: Profile? = ProfileManager.getProfile(DataStore.profileId)) =
            if (profile == null) clean() else ObjectOutputStream(file.outputStream()).use { it.writeObject(profile) }

    fun flushTrafficStats() {
        val profile = getDeviceProfile()
        if (profile?.dirty == true) ProfileManager.updateProfile(profile)
        update()
    }

    fun listenForUnlock() {
        if (registered) return
        AppConfig.instance.registerReceiver(this, IntentFilter(Intent.ACTION_BOOT_COMPLETED))
        registered = true
    }
    override fun onReceive(context: Context, intent: Intent) {
        flushTrafficStats()
        AppConfig.instance.unregisterReceiver(this)
        registered = false
    }
}

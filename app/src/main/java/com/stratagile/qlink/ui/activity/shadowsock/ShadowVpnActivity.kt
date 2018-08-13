package com.stratagile.qlink.ui.activity.shadowsock

import android.app.Activity
import android.app.PendingIntent
import android.app.UiModeManager
import android.app.backup.BackupManager
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.view.View
import android.widget.FrameLayout

import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.IShadowsocksServiceCallback
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.shadowsocks.ProfilesFragment
import com.stratagile.qlink.shadowsocks.ShadowsocksConnection
import com.stratagile.qlink.shadowsocks.bg.BaseService
import com.stratagile.qlink.shadowsocks.bg.VpnService
import com.stratagile.qlink.shadowsocks.widget.ServiceButton
//import com.stratagile.qlink.ui.activity.shadowsock.component.DaggerShadowVpnComponent
import com.stratagile.qlink.ui.activity.shadowsock.contract.ShadowVpnContract
import com.stratagile.qlink.ui.activity.shadowsock.module.ShadowVpnModule
import com.stratagile.qlink.ui.activity.shadowsock.presenter.ShadowVpnPresenter

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R.string.app
import com.stratagile.qlink.shadowsocks.ToolbarFragment
import com.stratagile.qlink.shadowsocks.bg.Executable
import com.stratagile.qlink.shadowsocks.database.Profile
import com.stratagile.qlink.shadowsocks.database.ProfileManager
import com.stratagile.qlink.shadowsocks.preference.DataStore
import com.stratagile.qlink.shadowsocks.preference.OnPreferenceDataStoreChangeListener
import com.stratagile.qlink.shadowsocks.preference.PreferenceDataStore
import com.stratagile.qlink.shadowsocks.utils.Key
import com.stratagile.qlink.shadowsocks.utils.getSystemService
import timber.log.Timber
import java.util.*

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.shadowsock
 * @Description: $description
 * @date 2018/08/07 11:54:13
 */

class ShadowVpnActivity : BaseActivity(), ShadowsocksConnection.Interface, OnPreferenceDataStoreChangeListener {
    companion object {
        private const val TAG = "ShadowsocksMainActivity"
        private const val REQUEST_CONNECT = 1

        fun pendingIntent(context: Context) = PendingIntent.getActivity(context, 0,
                Intent(context, ShadowVpnActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0)

        var stateListener: ((Int) -> Unit)? = null
    }
//    @Inject
//    internal lateinit var mPresenter: ShadowVpnPresenter

    var state = BaseService.IDLE
    internal var fragmentHolder: FrameLayout? = null
    internal var fab: ServiceButton? = null
    internal var snackbar: CoordinatorLayout? = null

    override val serviceCallback: IShadowsocksServiceCallback.Stub by lazy {
        object : IShadowsocksServiceCallback.Stub() {
            override fun stateChanged(state: Int, profileName: String?, msg: String?) {
                AppConfig.instance.handler.post { changeState(state, msg, true) }
            }

            override fun trafficUpdated(profileId: Long, txRate: Long, rxRate: Long, txTotal: Long, rxTotal: Long) {
                AppConfig.instance.handler.post {
                    //                    stats.updateTraffic(txRate, rxRate, txTotal, rxTotal)
                    val child = supportFragmentManager.findFragmentById(R.id.fragment_holder) as ToolbarFragment?
                    if (state != BaseService.STOPPING)
                        child?.onTrafficUpdated(profileId, txRate, rxRate, txTotal, rxTotal)
                }
            }

            override fun trafficPersisted(profileId: Long) {
                AppConfig.instance.handler.post { ProfilesFragment.instance?.onTrafficPersisted(profileId) }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_shadow_vpn)
        ButterKnife.bind(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun initData() {
        setTitle("Shadowsocks")
        fab = findViewById(R.id.fab)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, ProfilesFragment()).commitAllowingStateLoss()
        fab!!.setOnClickListener {
            when {
                state == BaseService.CONNECTED -> AppConfig.instance.stopService()
                BaseService.usingVpnMode -> {
                    val intent = android.net.VpnService.prepare(this)
                    if (intent != null) startActivityForResult(intent, REQUEST_CONNECT)
                    else onActivityResult(REQUEST_CONNECT, Activity.RESULT_OK, null)
                }
                else -> AppConfig.instance.startService()
            }
        }
        changeState(BaseService.IDLE)
        AppConfig.instance.handler.post { connection.connect() }
        DataStore.publicStore.registerChangeListener(this)

        val intent = this.intent
        if (intent != null) handleShareIntent(intent)
    }

    override val listenForDeath: Boolean get() = true
    override fun onServiceConnected(service: IShadowsocksService) = changeState(service.state)
    override fun onServiceDisconnected() = changeState(BaseService.IDLE)
    override fun binderDied() {
        super.binderDied()
        AppConfig.instance.handler.post {
            connection.disconnect()
            Executable.killAll()
            connection.connect()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CONNECT) super.onActivityResult(requestCode, resultCode, data)
        else if (resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, BaseService.serviceClass.java)
            startService(intent)
//            AppConfig.instance.startService()
        } else {
            toast(R.string.vpn_permission_denied)
//            Crashlytics.log(Log.ERROR, TAG, "Failed to start VpnService from onActivityResult: $data")
        }
    }

    private fun changeState(state: Int, msg: String? = null, animate: Boolean = false) {
        fab!!.changeState(state, animate)
//        stats.changeState(state)
        if (msg != null) toast(getString(R.string.vpn_error, msg))
        Timber.i(msg)
        this.state = state
        ProfilesFragment.instance?.profilesAdapter?.notifyDataSetChanged()  // refresh button enabled state
        stateListener?.invoke(state)
    }

    override fun setupActivityComponent() {
//        DaggerShadowVpnComponent
//                .builder()
//                .appComponent((application as AppConfig).applicationComponent)
//                .shadowVpnModule(ShadowVpnModule(this))
//                .build()
//                .inject(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleShareIntent(intent)
    }

    private fun handleShareIntent(intent: Intent) {
        val sharedStr = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data.toString()
            NfcAdapter.ACTION_NDEF_DISCOVERED -> {
                val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
                if (rawMsgs != null && rawMsgs.isNotEmpty()) String((rawMsgs[0] as NdefMessage).records[0].payload)
                else null
            }
            else -> null
        }
        if (sharedStr.isNullOrEmpty()) return
        val profiles = Profile.findAllUrls(sharedStr, AppConfig.instance.currentProfile).toList()
        if (profiles.isEmpty()) {
            toast(R.string.profile_invalid_input)
            return
        }
        AlertDialog.Builder(this)
                .setTitle(R.string.add_profile_dialog)
                .setPositiveButton(R.string.yes) { _, _ -> profiles.forEach { ProfileManager.createProfile(it) } }
                .setNegativeButton(R.string.no, null)
                .setMessage(profiles.joinToString("\n"))
                .create()
                .show()
    }

    override fun onPreferenceDataStoreChanged(store: PreferenceDataStore, key: String?) {
        when (key) {
            Key.serviceMode -> AppConfig.instance.handler.post {
                connection.disconnect()
                connection.connect()
            }
            Key.nightMode -> {
                val mode = DataStore.nightMode
                AppCompatDelegate.setDefaultNightMode(when (mode) {
                    AppCompatDelegate.getDefaultNightMode() -> return
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> getSystemService<UiModeManager>()!!.nightMode
                    else -> mode
                })
                recreate()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connection.listeningForBandwidth = true
    }

    override fun onStop() {
        connection.listeningForBandwidth = false
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        DataStore.publicStore.unregisterChangeListener(this)
        connection.disconnect()
        BackupManager(this).dataChanged()
        AppConfig.instance.handler.removeCallbacksAndMessages(null)
    }
}
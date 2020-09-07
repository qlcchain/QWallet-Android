package com.stratagile.qlink.ui.activity.my

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.pawegio.kandroid.runDelayedOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.constant.MainConstant
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.VCodeVerifyCode
import com.stratagile.qlink.ui.activity.my.component.DaggerEpidemicClaimQlcComponent
import com.stratagile.qlink.ui.activity.my.contract.EpidemicClaimQlcContract
import com.stratagile.qlink.ui.activity.my.module.EpidemicClaimQlcModule
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicClaimQlcPresenter
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.FireBaseUtils
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepService
import com.today.step.net.ClaimQgas
import kotlinx.android.synthetic.main.activity_epidemic_claim.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/04/16 15:57:15
 */

class EpidemicClaimQlcActivity : BaseActivity(), EpidemicClaimQlcContract.View {

    @Inject
    internal lateinit var mPresenter: EpidemicClaimQlcPresenter
    private var iSportStepInterface: ISportStepInterface? = null
    lateinit var serviceConnection : ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_epidemic_claim_qlc)
    }
    override fun initData() {
        title.text = getString(R.string.claim)

        EventBus.getDefault().register(this)

        //开启计步Service，同时绑定Activity进行aidl通信
        val intent = Intent(this, TodayStepService::class.java)
        startService(intent)
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                runDelayedOnUiThread(300) {
                    iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        tvClaimQgasCount.text = intent.getStringExtra("qlc") + " QLC"
        llSelectReceiveWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, 1)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvClaim.setOnClickListener {
            if (receiveNeoWallet == null) {
                return@setOnClickListener
            }
            if ("".equals(sig)) {
                return@setOnClickListener
            }
            claimQgas()
        }
//        ivVcode.setOnClickListener {
//            getVcode()
//        }
//        getVcode()

        webview.setBackgroundColor(Color.WHITE)
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true
        // 禁止缓存加载，以确保可获取最新的验证图片。
        // 禁止缓存加载，以确保可获取最新的验证图片。
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        // 设置不使用默认浏览器，而直接使用WebView组件加载页面。
        // 设置不使用默认浏览器，而直接使用WebView组件加载页面。
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                KLog.i(url)
                view.loadUrl(url)
                return true
            }
        }
        // 设置WebView组件支持加载JavaScript。
        // 设置WebView组件支持加载JavaScript。
        webview.settings.javaScriptEnabled = true
        webview.isHorizontalScrollBarEnabled = false
        webview.isVerticalScrollBarEnabled = false
        // 建立JavaScript调用Java接口的桥梁。
        // 建立JavaScript调用Java接口的桥梁。
        webview.addJavascriptInterface(WebAppInterface(), "successCallback")
        webview.visibility = View.VISIBLE
        webview.loadUrl("file:///android_asset/slideActivity.html")
    }

    var token = ""
    var sid = ""
    var sig = ""
    inner class WebAppInterface {
        @JavascriptInterface
        fun postMessage(message: String?) {

        }

        @JavascriptInterface
        fun sendToken(token1: String, sid1: String, sig1: String) {
            token = token1
            sid = sid1
            sig = sig1
            KLog.i(token)
            KLog.i(sid)
            KLog.i(sig)
            runOnUiThread {
                webview.setVisibility(View.GONE)
            }
        }
    }

    fun claimQgas() {
        FireBaseUtils.logEvent(this, FireBaseUtils.Campaign_Covid19_QLC_Claim)
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveNeoWallet!!.address
        map["code"] = etVcode.text.toString().trim()
        iSportStepInterface!!.claimQlc(ConstantValue.currentUser.account, AccountUtil.getUserToken(), receiveNeoWallet!!.address, sid, sig, token, MainConstant.afsFFFF0N0N000000009290AppKey, MainConstant.ncActivity)
    }

    fun getVcode() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["type"] = "CLAIM_COVID_QLC"
        mPresenter.getVCode(map)
    }

    override fun setInviteCode(vCodeVerifyCode: VCodeVerifyCode) {
        Glide.with(this)
                .load(vCodeVerifyCode.codeUrl)
                .into(ivVcode)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handlerError(throwable: Throwable) {
        closeProgressDialog()
        toast(throwable.message!!)
    }


    var receiveNeoWallet : Wallet? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            receiveNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
            tvReceiveWalletName.text = receiveNeoWallet!!.name
            tvReceiveWalletAddess.text = receiveNeoWallet!!.address
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClaimBack(claimQgas: ClaimQgas) {
        closeProgressDialog()
        toast(getString(R.string.claim_success))
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun setupActivityComponent() {
       DaggerEpidemicClaimQlcComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .epidemicClaimQlcModule(EpidemicClaimQlcModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: EpidemicClaimQlcContract.EpidemicClaimQlcContractPresenter) {
            mPresenter = presenter as EpidemicClaimQlcPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
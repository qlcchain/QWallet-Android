package com.stratagile.qlink.ui.activity.my

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.webkit.WebSettings
import butterknife.ButterKnife
import com.pawegio.kandroid.runDelayedOnUiThread
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.ui.activity.my.component.DaggerEpidemicWebViewComponent
import com.stratagile.qlink.ui.activity.my.contract.EpidemicWebViewContract
import com.stratagile.qlink.ui.activity.my.module.EpidemicWebViewModule
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicWebViewPresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.XWebViewClient
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepService
import kotlinx.android.synthetic.main.activity_web_view.*
import org.greenrobot.eventbus.EventBus

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/04/16 17:48:35
 */

class EpidemicWebViewActivity : BaseActivity(), EpidemicWebViewContract.View {

    @Inject
    internal lateinit var mPresenter: EpidemicWebViewPresenter
    private var iSportStepInterface: ISportStepInterface? = null
    lateinit var serviceConnection : ServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_web_view)
    }
    override fun initData() {
        setTitle(intent.getStringExtra("title"))
        webView.getSettings().setBuiltInZoomControls(true)
        webView.getSettings().setDefaultFontSize(16)
        webView.getSettings().setDisplayZoomControls(false)
        webView.getSettings().setSupportZoom(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT)
        webView.getSettings().setDefaultTextEncodingName("UTF-8")
        webView.getSettings().setJavaScriptEnabled(true)
        webView.getSettings().setDomStorageEnabled(true)
        webView.getSettings().setAllowContentAccess(true)
        webView.getSettings().setAppCacheEnabled(false)
        webView.getSettings().setUseWideViewPort(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        webView.loadUrl(intent.getStringExtra("url"))
//        webView.loadUrl("https://www.baidu.com");
        KLog.i(intent.getStringExtra("url"))
        webView.setWebViewClient(XWebViewClient())

        //开启计步Service，同时绑定Activity进行aidl通信
        val intent = Intent(this, TodayStepService::class.java)
        startService(intent)
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                runDelayedOnUiThread(300) {
                    iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                    focusEpidemic()
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }

    /**
     * 关注疫情，关注之后才能领取
     */
    fun focusEpidemic() {
        if (ConstantValue.currentUser != null) {
            iSportStepInterface!!.focusEpidemic(ConstantValue.currentUser.account, AccountUtil.getUserToken())
        }
    }

    override fun setupActivityComponent() {
       DaggerEpidemicWebViewComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .epidemicWebViewModule(EpidemicWebViewModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: EpidemicWebViewContract.EpidemicWebViewContractPresenter) {
            mPresenter = presenter as EpidemicWebViewPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
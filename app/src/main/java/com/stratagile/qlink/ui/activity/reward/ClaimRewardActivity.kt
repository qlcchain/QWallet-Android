package com.stratagile.qlink.ui.activity.reward

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.constant.MainConstant
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.VCodeVerifyCode
import com.stratagile.qlink.entity.reward.ClaimQgas
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.reward.component.DaggerClaimRewardComponent
import com.stratagile.qlink.ui.activity.reward.contract.ClaimRewardContract
import com.stratagile.qlink.ui.activity.reward.module.ClaimRewardModule
import com.stratagile.qlink.ui.activity.reward.presenter.ClaimRewardPresenter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_claim_reward.*
import java.util.*
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.reward
 * @Description: $description
 * @date 2019/10/10 15:28:24
 */

class ClaimRewardActivity : BaseActivity(), ClaimRewardContract.View {
    override fun claimQgasBack(claimQgas: ClaimQgas) {
        if ("0".equals(claimQgas.code)) {
            toast(getString(R.string.success))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    var isInvite = false

    @Inject
    internal lateinit var mPresenter: ClaimRewardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_claim_reward)
    }
    override fun initData() {
        if (ConstantValue.currentUser == null) {
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
        }
        isInvite = "invite".equals(intent.getStringExtra("claimType"))
        if (isInvite) {
            title.text = getString(R.string.get_now)
            tvClaim.text = getString(R.string.get_now)
        } else {
            title.text = getString(R.string.claim)
            tvClaim.text = getString(R.string.claim)
        }
        tvClaimQgasCount.text = intent.getStringExtra("total") + " QGAS"
        llSelectReceiveWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, 1)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvClaim.setOnClickListener {
            if (receiveQlcWallet == null) {
                return@setOnClickListener
            }
            if (isInvite) {
                claiminviteQgas()
            } else {
                claimQgas()
            }
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

    fun getVcode() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        if (isInvite) {
            map["type"] = "CLAIM_INVITE"
        } else {
            map["type"] = "CLAIM_BIND"
        }
        mPresenter.getVCode(map)
    }

    override fun setInviteCode(vCodeVerifyCode: VCodeVerifyCode) {

    }

    fun claimQgas() {
        if ("".equals(token)) {
            toast(getString(R.string.please_complete_the_man_machine_verfy))
            return
        }
        showProgressDialog()
        val map: MutableMap<String, String> = HashMap<String, String>()
        map["sessionId"] = sid
        map["sig"] = sig
        map["afsToken"] = token
        map["appKey"] = MainConstant.afsAppKey
        map["scene"] = MainConstant.ncActivity
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveQlcWallet!!.address
        mPresenter.claimQgas(map)
    }

    fun claiminviteQgas() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveQlcWallet!!.address
        map["sessionId"] = sid
        map["sig"] = sig
        map["afsToken"] = token
        map["appKey"] = MainConstant.afsAppKey
        map["scene"] = MainConstant.ncActivity
        mPresenter.claiminviteQgas(map)
    }

    var receiveQlcWallet : QLCAccount? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            receiveQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
            tvReceiveWalletName.text = receiveQlcWallet!!.accountName
            tvReceiveWalletAddess.text = receiveQlcWallet!!.address
        }
    }

    override fun setupActivityComponent() {
       DaggerClaimRewardComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .claimRewardModule(ClaimRewardModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: ClaimRewardContract.ClaimRewardContractPresenter) {
            mPresenter = presenter as ClaimRewardPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
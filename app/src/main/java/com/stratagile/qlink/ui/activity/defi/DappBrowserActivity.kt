package com.stratagile.qlink.ui.activity.defi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.C
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.DAppFunction
import com.stratagile.qlink.entity.defi.ConfirmationType
import com.stratagile.qlink.entity.walletconnect.EthereumMessage
import com.stratagile.qlink.entity.walletconnect.EthereumTypedMessage
import com.stratagile.qlink.ui.activity.defi.component.DaggerDappBrowserComponent
import com.stratagile.qlink.ui.activity.defi.contract.DappBrowserContract
import com.stratagile.qlink.ui.activity.defi.module.DappBrowserModule
import com.stratagile.qlink.ui.activity.defi.presenter.DappBrowserPresenter
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.utils.WXH5PayHandler
import com.stratagile.qlink.web3.OnSignMessageListener
import com.stratagile.qlink.web3.OnSignPersonalMessageListener
import com.stratagile.qlink.web3.OnSignTransactionListener
import com.stratagile.qlink.web3.OnSignTypedMessageListener
import com.stratagile.qlink.web3.entity.Address
import com.stratagile.qlink.web3.entity.Web3Transaction
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.fragment_dapp.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.web3j.utils.Convert
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/10/15 17:56:57
 */

class DappBrowserActivity : BaseActivity(), DappBrowserContract.View, OnSignMessageListener, OnSignPersonalMessageListener, OnSignTransactionListener, OnSignTypedMessageListener {

    @Inject
    internal lateinit var mPresenter: DappBrowserPresenter
    lateinit var ethWallet: EthWallet
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_dapp_browser)
    }

    override fun initData() {
        getEthWallet()
    }

    fun getEthWallet() {
        var ethWallets = AppConfig.instance.daoSession.ethWalletDao.loadAll()

        var walletList = ethWallets.filter { it.isCurrent() }
        if (walletList.size > 0) {
            ethWallet = walletList[0]
            setupWebView()
        } else {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal)
            intent1.putExtra("select", true)
            intent1.putExtra("onlyeth", true)
            startActivityForResult(intent1, AllWallet.WalletType.EthWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
//            toast(getString(R.string.please_switch_to_eth_wallet))
//            finish()
        }
    }

    fun setupWebView() {
        webView.setActivity(this)
        webView.chainId = ConstantValue.currentChainId
        webView.setRpcUrl(ConstantValue.ethNodeUrl)
        webView.setWalletAddress(Address(ethWallet.address))
        if (refresh != null) {
            refresh.visibility = View.VISIBLE;
            refresh.setOnClickListener {
                //重新加载
                webView.reload()
            }
        }
        if (close != null) {
            close.visibility = View.VISIBLE;
            close.setOnClickListener {
                finish()
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                title.text = view.title
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                KLog.i(url)
                KLog.i(view.title)
                title.text = view.title
                val prefixCheck: Array<String> = url.split(":").toTypedArray()
                if (prefixCheck.size > 1) {
                    val intent: Intent
                    when (prefixCheck[0]) {
                        C.DAPP_PREFIX_TELEPHONE -> {
                            intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse(url)
                            startActivity(Intent.createChooser(intent, "Call " + prefixCheck[1]))
                            return true
                        }
                        C.DAPP_PREFIX_MAILTO -> {
                            intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse(url)
                            startActivity(Intent.createChooser(intent, "Email: " + prefixCheck[1]))
                            return true
                        }
                        C.DAPP_PREFIX_ALPHAWALLET -> if (prefixCheck[1] == C.DAPP_SUFFIX_RECEIVE) {
//                            viewModel.showMyAddress(context)
                            return true
                        }
                        C.DAPP_PREFIX_WALLETCONNECT -> {
                            //start walletconnect
//                            viewModel.handleWalletConnect(context, url)
                            startActivity(Intent(this@DappBrowserActivity, WalletConnectActivity::class.java).putExtra("session", url))
                            return true
                        }
                        else -> {
                        }
                    }
                }
                return false
            }
        }
        webView.setOnSignMessageListener(this)
        webView.setOnSignPersonalMessageListener(this)
        webView.setOnSignTransactionListener(this)
        webView.setOnSignTypedMessageListener(this)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//        webView.loadUrl("https://app.uniswap.org/#/swap")
//        webView.loadUrl("https://sakeswap.finance/#/swap")
//        webView.loadUrl("https://exchange.ekas.x0.si/#/swap")
//        webView.loadUrl("https://example.walletconnect.org/")
        webView.loadUrl(intent.getStringExtra("url"))
    }


    override fun setupActivityComponent() {
        DaggerDappBrowserComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .dappBrowserModule(DappBrowserModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DappBrowserContract.DappBrowserContractPresenter) {
        mPresenter = presenter as DappBrowserPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    private val dAppFunction: DAppFunction? = null

    override fun onSignMessage(message: EthereumMessage?) {
        KLog.i(message?.message)
    }

    override fun onSignPersonalMessage(message: EthereumMessage?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == C.REQUEST_TRANSACTION_CALLBACK) {
            val web3Tx: Web3Transaction = data!!.getParcelableExtra(C.EXTRA_WEB3TRANSACTION)
            if (resultCode == Activity.RESULT_OK) {
                KLog.i(data?.getStringExtra(C.EXTRA_TRANSACTION_DATA))
                val hashData = data.getStringExtra(C.EXTRA_TRANSACTION_DATA)
                webView.onSignTransactionSuccessful(web3Tx, hashData)
            } else {
                KLog.i("取消...")
                webView.onSignCancel(web3Tx)
            }

        }
        if (requestCode == AllWallet.WalletType.EthWallet.ordinal) {
            if (resultCode == Activity.RESULT_OK) {
                ethWallet = data!!.getParcelableExtra("wallet")
                setupWebView()
            } else {
                finish()
            }
        }
    }

    override fun onSignTransaction(transaction: Web3Transaction?, url: String?) {
        KLog.i(url)
        KLog.i(transaction?.payload)
        KLog.i(transaction?.contract)
        KLog.i(transaction?.recipient)
        KLog.i(transaction?.gasPrice)
        KLog.i(transaction?.gasLimit)
        KLog.i(transaction?.nonce)
        KLog.i(transaction?.value.toString())
        var intent = Intent(this, ConfirmActivity::class.java).putExtra(C.EXTRA_WEB3TRANSACTION, transaction)
//        intent.putExtra(C.EXTRA_AMOUNT, Convert.fromWei(transaction!!.value.toString(10), Convert.Unit.WEI).toString())
        intent.putExtra(C.EXTRA_AMOUNT, transaction?.value.toString())
        intent.putExtra(C.EXTRA_ADDRESS, ethWallet.address)
        intent.putExtra(C.TOKEN_TYPE, ConfirmationType.WEB3TRANSACTION.ordinal)
        intent.putExtra(C.EXTRA_NETWORK_NAME, "Ethereum")
        intent.putExtra(C.EXTRA_ACTION_NAME, url)
        intent.putExtra(C.EXTRA_NETWORKID, ConstantValue.currentChainId)
        intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        startActivityForResult(intent, C.REQUEST_TRANSACTION_CALLBACK)
    }

    override fun onSignTypedMessage(message: EthereumTypedMessage?) {

    }

    override fun onBackPressed() {
        // back history
        var index = -1 // -1表示回退history上一页
        var url: String
        val history = webView.copyBackForwardList()
        while (webView.canGoBackOrForward(index)) {
            url = history.getItemAtIndex(history.currentIndex + index).url
            if (URLUtil.isNetworkUrl(url) && !WXH5PayHandler.isWXH5Pay(url)) {
                webView.goBackOrForward(index)
                return
            }
            index--
        }
        super.onBackPressed()
    }

}
package com.stratagile.qlink.ui.activity.defi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.socks.library.KLog
import com.stratagile.qlink.C
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.entity.DAppFunction
import com.stratagile.qlink.entity.defi.ConfirmationType
import com.stratagile.qlink.entity.walletconnect.EthereumMessage
import com.stratagile.qlink.entity.walletconnect.EthereumTypedMessage
import com.stratagile.qlink.ui.activity.defi.component.DaggerDappComponent
import com.stratagile.qlink.ui.activity.defi.contract.DappContract
import com.stratagile.qlink.ui.activity.defi.module.DappModule
import com.stratagile.qlink.ui.activity.defi.presenter.DappPresenter
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.web3.OnSignMessageListener
import com.stratagile.qlink.web3.OnSignPersonalMessageListener
import com.stratagile.qlink.web3.OnSignTransactionListener
import com.stratagile.qlink.web3.OnSignTypedMessageListener
import com.stratagile.qlink.web3.entity.Address
import com.stratagile.qlink.web3.entity.Web3Transaction
import kotlinx.android.synthetic.main.fragment_dapp.*
import org.web3j.utils.Convert
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/09/17 10:44:40
 */

class DappFragment : BaseFragment(), DappContract.View, OnSignMessageListener, OnSignPersonalMessageListener, OnSignTransactionListener, OnSignTypedMessageListener {

    @Inject
    lateinit internal var mPresenter: DappPresenter
    lateinit var ethWallet : EthWallet
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dapp, null)
        val mBundle = arguments
        return view
    }

    override fun initDataFromNet() {

    }

    fun getEthWallet() {
        var ethWallets = AppConfig.instance.daoSession.ethWalletDao.loadAll()

        var walletList = ethWallets.filter { it.isCurrent() }
        if (walletList.size > 0) {
            ethWallet = walletList[0]
            setupWebView()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val llp = LinearLayout.LayoutParams(UIUtils.getDisplayWidth(activity), UIUtils.getStatusBarHeight(activity))
        status_bar.setLayoutParams(llp)
        getEthWallet()
    }

    fun setupWebView() {
        webView.setActivity(activity)
        webView.chainId = 1
        webView.setRpcUrl(ConstantValue.ethNodeUrl)
        webView.setWalletAddress(Address(ethWallet.address))
        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {

            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                KLog.i(url)
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
                            startActivity(Intent(activity, WalletConnectActivity::class.java).putExtra("session", url))
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
//        webView.loadUrl("https://app.uniswap.org/#/swap")
        webView.loadUrl("https://sakeswap.finance/#/swap")
//        webView.loadUrl("https://exchange.ekas.x0.si/#/swap")
//        webView.loadUrl("https://example.walletconnect.org/")
    }


    override fun setupFragmentComponent() {
        DaggerDappComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .dappModule(DappModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DappContract.DappContractPresenter) {
        mPresenter = presenter as DappPresenter
    }

    override fun initDataFromLocal() {

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
        var intent = Intent(activity, ConfirmActivity::class.java).putExtra(C.EXTRA_WEB3TRANSACTION, transaction)
        intent.putExtra(C.EXTRA_AMOUNT, Convert.fromWei(transaction!!.value.toString(10), Convert.Unit.WEI).toString())
        intent.putExtra(C.EXTRA_ADDRESS, ethWallet.address)
        intent.putExtra(C.TOKEN_TYPE, ConfirmationType.WEB3TRANSACTION.ordinal)
        intent.putExtra(C.EXTRA_NETWORK_NAME, "Ethumrm")
        intent.putExtra(C.EXTRA_ACTION_NAME, url)
        intent.putExtra(C.EXTRA_NETWORKID, 1)
        intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        startActivityForResult(intent, C.REQUEST_TRANSACTION_CALLBACK)
    }

    override fun onSignTypedMessage(message: EthereumTypedMessage?) {

    }
}
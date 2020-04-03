package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.Account
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.SendNep5TokenBack
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.entity.stake.MultSign
import com.stratagile.qlink.ui.activity.otc.component.DaggerOtcNeoChainPayComponent
import com.stratagile.qlink.ui.activity.otc.contract.OtcNeoChainPayContract
import com.stratagile.qlink.ui.activity.otc.module.OtcNeoChainPayModule
import com.stratagile.qlink.ui.activity.otc.presenter.OtcNeoChainPayPresenter
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_otc_neo_chain_pay.*
import kotlinx.android.synthetic.main.activity_otc_neo_chain_pay.tvReceiveAddress
import kotlinx.android.synthetic.main.activity_otc_neo_chain_pay.tvSend
import kotlinx.android.synthetic.main.activity_usdt_pay.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import wendu.dsbridge.DWebView
import wendu.dsbridge.OnReturnValue
import java.util.HashMap

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/08/21 15:07:05
 */

class OtcNeoChainPayActivity : BaseActivity(), OtcNeoChainPayContract.View {

    override fun sendUsdtSuccess(txid: String) {
        closeProgressDialog()
        setResult(Activity.RESULT_OK)
        startActivity(Intent(this, OtcOrderRecordActivity::class.java).putExtra("position", 1))
        finish()
    }

    override fun setNeoDetail(neoWalletInfo: NeoWalletInfo) {
        this.neoWalletInfo = neoWalletInfo
        neoWalletInfo.data.balance.forEach {
            if (it.asset_symbol.equals(intent.getStringExtra("payToken"))) {
                tvPayTokenBalance.text = getString(R.string.balance) + ": ${it.amount} ${intent.getStringExtra("payToken")}"
                payTokenAmount = it.amount
                payTokenInfo = it
            }
            if (it.asset_symbol.equals("GAS")) {
                gasTokenInfo = it
            }
        }
    }

    @Inject
    internal lateinit var mPresenter: OtcNeoChainPayPresenter
    var neoWalletInfo: NeoWalletInfo? = null
    var payTokenInfo : NeoWalletInfo.DataBean.BalanceBean? = null
    var gasTokenInfo : NeoWalletInfo.DataBean.BalanceBean? = null
    var payTokenAmount = 0.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_otc_neo_chain_pay)
    }
    override fun initData() {
        webview = DWebView(this)
        webview!!.loadUrl("file:///android_asset/contract.html")

        title.text = getString(R.string.send) + " " + intent.getStringExtra("payToken")
        tvPayToken.text = intent.getStringExtra("payToken")
        tvReceiveAddress.text = intent.getStringExtra("receiveAddress")
        tvAmountPayTokenn.text = intent.getStringExtra("usdt")
        tvPayTokenBalance.text = getString(R.string.balance) + ": -/-"
        etNeoTokenSendMemo.setText(getString(R.string.buy) + intent.getStringExtra("tradeToken") + "( " + intent.getStringExtra("orderNumber") + " )")
        llSelectNeoWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.NeoWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvSend.setOnClickListener {
            if (neoWallet == null) {
                return@setOnClickListener
            }
//            if (gasTokenInfo == null || gasTokenInfo!!.amount < 0.00000001) {
//                toast(getString(R.string.no_enough) + " GAS")
//                return@setOnClickListener
//            }
            if (neoWalletInfo == null || intent.getStringExtra("usdt").toDouble() > payTokenAmount) {
                toast(getString(R.string.no_enough) + " " + intent.getStringExtra("payToken"))
                return@setOnClickListener
            }
            showConfirmSendUsdtDialog()
        }
    }

    fun showConfirmSendUsdtDialog() {
        //
        val view = View.inflate(this, R.layout.dialog_send_usdt_layout, null)
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val ivSendChain = view.findViewById<ImageView>(R.id.ivSendChain)
        ivSendChain.setImageResource(R.mipmap.icons_neo_wallet)
        val tvEthWalletName1 = view.findViewById<TextView>(R.id.tvEthWalletName1)
        val tvEthWalletAddess1 = view.findViewById<TextView>(R.id.tvEthWalletAddess1)
        val tvEthAmount1 = view.findViewById<TextView>(R.id.tvEthAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainEthAddress)
        val tvMemo = view.findViewById<TextView>(R.id.tvMemo)
        tvEthWalletName1.text = neoWallet!!.name
        tvEthWalletAddess1.text = neoWallet!!.address
        tvMemo.text = etNeoTokenSendMemo.text.toString()
        tvEthAmount1.text = tvAmountPayTokenn.text.toString().trim() + " " + intent.getStringExtra("payToken")
        tvMainQgasAddress.text = tvReceiveAddress.text.toString()
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(this)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        ivClose.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
            sweetAlertDialog.cancel()
            showProgressDialog()
            testTransfer()
            //sendNep5Token(address : String, amount : String, toAddress : String, dataBean : NeoWalletInfo.DataBean.BalanceBean, remark : String, tradeOrderId: String)
//            thread {
//                mPresenter.sendNep5Token(neoWallet!!.address, intent.getStringExtra("usdt"), intent.getStringExtra("receiveAddress"), payTokenInfo!!, etNeoTokenSendMemo.toString(), intent.getStringExtra("tradeOrderId"))
//            }
        }
    }

    private var webview: DWebView? = null
    private fun testTransfer() {
        //fromAddress, toAddress, assetHash, amount, wif, responseCallback
        val arrays = arrayOfNulls<Any>(7)
        arrays[0] = neoWallet!!.address
        arrays[1] = intent.getStringExtra("receiveAddress")
        arrays[2] = payTokenInfo!!.asset_hash
        arrays[3] = intent.getStringExtra("usdt")
        arrays[4] = 8
        arrays[5] = Account.getWallet()!!.wif
        arrays[6] = "xxx"
        webview!!.callHandler("staking.send", arrays, OnReturnValue<JSONObject> { retValue ->
            KLog.i("call succeed,return value is " + retValue!!)
            var nep5SendBack = Gson().fromJson(retValue.toString(), SendNep5TokenBack::class.java)
            mPresenter.markAsPaid(nep5SendBack.txid, intent.getStringExtra("tradeOrderId"))
        })
    }

    var neoWallet : Wallet? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AllWallet.WalletType.NeoWallet.ordinal -> {
                    neoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                    tvNeoWalletName.text = neoWallet!!.name
                    tvNeoWalletAddess.text = neoWallet!!.address

                    val infoMap = HashMap<String, String>()
                    infoMap["address"] = neoWallet!!.address
                    mPresenter.getNeoWalletDetail(infoMap, neoWallet!!.address)

                    val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
                    if (qlcAccounts.size != 0) {
                        qlcAccounts.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                return@forEach
                            }
                        }
                    }
                    val neoWallets = AppConfig.getInstance().daoSession.walletDao.loadAll()
                    if (neoWallets.size != 0) {
                        neoWallets.forEach {
                            if (it.isCurrent) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.walletDao.update(it)
                                return@forEach
                            }
                        }
                        neoWallets.forEach {
                            if (it.address.equals(neoWallet!!.address)) {
                                it.setIsCurrent(true)
                                AppConfig.getInstance().daoSession.walletDao.update(it)
                                EventBus.getDefault().post(ChangeCurrency())
                                return@forEach
                            }
                        }
                    }

                    val wallets2 = AppConfig.getInstance().daoSession.eosAccountDao.loadAll()
                    if (wallets2 != null && wallets2.size != 0) {
                        wallets2.forEach {
                            if (it.isCurrent) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.eosAccountDao.update(it)
                                return@forEach
                            }
                        }
                    }

                    val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
                    if (ethWallets != null && ethWallets.size != 0) {
                        ethWallets.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.ethWalletDao.update(it)
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setupActivityComponent() {
       DaggerOtcNeoChainPayComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .otcNeoChainPayModule(OtcNeoChainPayModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OtcNeoChainPayContract.OtcNeoChainPayContractPresenter) {
            mPresenter = presenter as OtcNeoChainPayPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
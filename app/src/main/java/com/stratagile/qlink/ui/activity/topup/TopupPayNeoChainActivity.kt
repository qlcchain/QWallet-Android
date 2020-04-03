package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import com.google.gson.Gson
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.Account
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.SendNep5TokenBack
import com.stratagile.qlink.entity.SwitchToOtc
import com.stratagile.qlink.entity.topup.*
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.recommend.MyTopupGroupActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupPayNeoChainComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupPayNeoChainContract
import com.stratagile.qlink.ui.activity.topup.module.TopupPayNeoChainModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupPayNeoChainPresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.SpUtil
import kotlinx.android.synthetic.main.activity_topup_pay_neo_chain.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import wendu.dsbridge.DWebView
import wendu.dsbridge.OnReturnValue
import java.io.File
import java.math.BigDecimal
import java.util.*

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/12/26 16:34:35
 */

class TopupPayNeoChainActivity : BaseActivity(), TopupPayNeoChainContract.View {

    @Inject
    internal lateinit var mPresenter: TopupPayNeoChainPresenter

    lateinit var topupOrderBean : TopupOrder.OrderBean
    lateinit var groupItemList: GroupItemList.ItemListBean

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_pay_neo_chain)
    }
    override fun initData() {
        webview = DWebView(this)
        webview!!.loadUrl("file:///android_asset/contract.html")

        mainColor = R.color.white
        title.text = getString(R.string.payment_wallet)

        if (intent.hasExtra("isGroup")) {
            groupItemList = intent.getParcelableExtra("groupBean")
            topupOrderBean = TopupOrder.OrderBean()

            topupOrderBean.payTokenSymbol = groupItemList.payToken
            topupOrderBean.symbol = groupItemList.deductionToken
            topupOrderBean.id = groupItemList.id
            topupOrderBean.userId = groupItemList.userId
            topupOrderBean.chain = groupItemList.deductionTokenChain
            topupOrderBean.payTokenChain = groupItemList.payTokenChain
            topupOrderBean.qgasAmount = groupItemList.deductionTokenAmount
            topupOrderBean.payTokenAmount = groupItemList.payTokenAmount.toString()
            topupOrderBean.discountPrice = groupItemList.payFiatMoney

        } else {
            topupOrderBean = intent.getParcelableExtra("order")
        }

        payToken1.text = topupOrderBean.payTokenSymbol
        tvPayTokenBalance.text = topupOrderBean.payTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString()
        tvReceiveAddress.text = ConstantValue.mainAddressData.neo.address
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.NeoWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var message = "topup_" + topupOrderBean.id + "_" + topupOrderBean.discountPrice.toString()
        etEthTokenSendMemo.setText(message)
        var neoAccounts = AppConfig.instance.daoSession.walletDao.loadAll()
        if (neoAccounts.size > 0) {
            neoAccounts.forEach {
                if (it.isCurrent) {
                    neoWallet = it
                    tvQlcWalletName.text = neoWallet!!.name
                    tvQlcWalletAddess.text = neoWallet!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${topupOrderBean.payTokenSymbol}"
                    thread {
                        val infoMap = HashMap<String, String>()
                        infoMap["address"] = neoWallet!!.address
                        mPresenter.getNeoWalletDetail(infoMap, neoWallet!!.address)
                    }
                }
            }
        }
        tvSend.setOnClickListener {
            if (neoWallet == null) {
                return@setOnClickListener
            }
            if (tvPayTokenBalance.text.toString().toBigDecimal() > payTokenAmount.toBigDecimal()) {
                alert(getString(R.string.balance_insufficient_to_purchase_qgas_on_otc_pages, topupOrderBean.symbol)) {
                    negativeButton(getString(R.string.cancel)) {
                        dismiss()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    positiveButton(getString(R.string.buy_topup)) {
                        setResult(Activity.RESULT_OK)
                        EventBus.getDefault().post(SwitchToOtc())
                        finish()
                    }
                }.show()
                return@setOnClickListener
            }
            sendNeoToken()
        }
    }

    fun saveItemPayTokenTxid(txid: String, orderId: String) {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()

        map["groupItemId"] = orderId
        map["payTokenTxid"] = txid
        mPresenter.saveItemPayTokenTxid(map)
    }

    fun savePayTokenTxid(txid : String) {
        var map = hashMapOf<String, String>()
        var topUpP2pId = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
        if ("".equals(topUpP2pId)) {
            var saveP2pId = FileUtil.readData("/Qwallet/p2pId.txt")
            if ("".equals(saveP2pId)) {
                val uuid = UUID.randomUUID()
                var p2pId = ""
                p2pId += uuid.toString().replace("-", "")
                topUpP2pId = p2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, p2pId)

                val file = File(Environment.getExternalStorageDirectory().toString() + "/Qwallet/p2pId.txt")
                if (file.exists()) {
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                } else {
                    file.createNewFile()
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                }

            } else {
                topUpP2pId = saveP2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, topUpP2pId)
            }
        }
        KLog.i("p2pId为：" + topUpP2pId)
        if (ConstantValue.currentUser != null) {
            map["account"] = ConstantValue.currentUser.account
            map["p2pId"] = topUpP2pId
        } else {
            map["p2pId"] = topUpP2pId
        }

        map["orderId"] = topupOrderBean.id
        map["payTokenTxid"] = txid
        mPresenter.savePayTokenTxid(map)
    }



    var payTokenInfo : NeoWalletInfo.DataBean.BalanceBean? = null
    var neoWalletInfo: NeoWalletInfo? = null
    var payTokenAmount = 0.toDouble()

    override fun setNeoDetail(neoWalletInfo: NeoWalletInfo) {
        this.neoWalletInfo = neoWalletInfo
        neoWalletInfo.data.balance.forEach {
            if (it.asset_symbol.replace("0x", "").equals(topupOrderBean.payTokenSymbol.replace("0x", ""))) {
                tvQGASBalance.text = getString(R.string.balance) + ": ${it.amount} ${topupOrderBean.payTokenSymbol}"
                payTokenAmount = it.amount
                payTokenInfo = it
            }
        }
    }

    override fun savePayTokenTxidBack(topupOrder: TopupOrder) {
        closeProgressDialog()
        startActivity(Intent(this, TopupOrderListActivity::class.java))
        finish()
    }

    override fun saveItemPayTokenTxidBack(topupJoinGroup: TopupJoinGroup) {
        closeProgressDialog()
        startActivity(Intent(this, MyTopupGroupActivity::class.java))
        finish()
    }


    override fun saveItemPayTokenError() {

    }

    override fun savePayTokenTxidError() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AllWallet.WalletType.NeoWallet.ordinal -> {
                if (resultCode == Activity.RESULT_OK) {
                    neoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                    tvQlcWalletName.text = neoWallet!!.name
                    tvQlcWalletAddess.text = neoWallet!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${topupOrderBean.payTokenSymbol}"
                    payTokenAmount = 0.toDouble()
                    thread {
                        val infoMap = HashMap<String, String>()
                        infoMap["address"] = neoWallet!!.address
                        mPresenter.getNeoWalletDetail(infoMap, neoWallet!!.address)
                    }
                }
            }
            10 -> {
                startActivity(Intent(this, TopupOrderListActivity::class.java))
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private var webview: DWebView? = null
    private fun sendNeoToken() {
        showProgressDialog()
        //fromAddress, toAddress, assetHash, amount, wif, responseCallback
        val arrays = arrayOfNulls<Any>(7)
        arrays[0] = neoWallet!!.address
        arrays[1] = ConstantValue.mainAddressData.neo.address
        arrays[2] = payTokenInfo!!.asset_hash
        arrays[3] = topupOrderBean.payTokenAmount.toString()
        arrays[4] = 8
        arrays[5] = neoWallet!!.wif
        arrays[6] = "xxx"
        webview!!.callHandler("staking.send", arrays, OnReturnValue<JSONObject> { retValue ->
            KLog.i("call succeed,return value is " + retValue!!)
            var nep5SendBack = Gson().fromJson(retValue.toString(), SendNep5TokenBack::class.java)
            if (nep5SendBack.txid != null) {
                if (intent.hasExtra("isGroup")) {
                    saveItemPayTokenTxid(nep5SendBack.txid, topupOrderBean.id)
                } else {
                    savePayTokenTxid(nep5SendBack.txid)
                }
            } else {
                toast(getString(R.string.send_qgas_error, topupOrderBean.payTokenSymbol))
                closeProgressDialog()
            }
        })
    }

    var neoWallet : Wallet? = null

    override fun setupActivityComponent() {
       DaggerTopupPayNeoChainComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .topupPayNeoChainModule(TopupPayNeoChainModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: TopupPayNeoChainContract.TopupPayNeoChainContractPresenter) {
            mPresenter = presenter as TopupPayNeoChainPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
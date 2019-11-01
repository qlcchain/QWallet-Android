package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderBuyComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderBuyContract
import com.stratagile.qlink.ui.activity.otc.module.OrderBuyModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderBuyPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.google.gson.Gson
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.Account
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EosAccount
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.SendNep5TokenBack
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.fragment_order_buy.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import neoutils.Neoutils
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import qlc.mng.AccountMng
import qlc.mng.WalletMng
import wendu.dsbridge.DWebView
import wendu.dsbridge.OnReturnValue
import java.math.BigDecimal
import java.util.ArrayList
import java.util.HashMap
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/08 17:24:58
 */

class OrderBuyFragment : BaseFragment(), OrderBuyContract.View {

    override fun setEthTokens(ethWalletInfo: EthWalletInfo) {
        closeProgressDialog()
        if (ethWalletInfo.data.eth != null && !"false".equals(ethWalletInfo.data.eth.balance.toString()) && !"-1".equals(ethWalletInfo.data.eth.balance.toString())) {
            ethCount = ethWalletInfo.data.eth.balance.toString().toDouble()
            if (gasEth.toDouble() > ethCount) {
                toast(getString(R.string.no_enough_eth))
            }
        } else {
            toast(getString(R.string.no_enough_eth))
        }
        ethWalletInfo.data.tokens.forEach {
            if (selectedPair!!.payToken.equals(it.tokenInfo.symbol)) {
                payTokenCount = it.balance / Math.pow(10.0, it.tokenInfo.decimals.toDouble())
                ethPayTokenBean = it
                return@forEach
            }
        }
    }

    var ethPayTokenBean : EthWalletInfo.DataBean.TokensBean? = null

    var ethCount = 0.toDouble()

    /**
     * 支付钱包有的余额
     */
    var payTokenCount = 0.toDouble()

    /**
     * 需要支付的数量
     */
    var payTokenAmount = 0.toDouble()

    private var gasEth: String = "0.0004"


    override fun generatebuyQgasOrderFailed(content: String) {
        runOnUiThread {
            closeProgressDialog()
            toast(content)
        }
    }

    var payTokenInfo: NeoWalletInfo.DataBean.BalanceBean? = null
    var gasTokenInfo: NeoWalletInfo.DataBean.BalanceBean? = null
    var neoWalletInfo: NeoWalletInfo? = null

    var tradeTokenAmount = 0.toDouble()
    override fun setNeoDetail(neoWalletInfo: NeoWalletInfo) {
        closeProgressDialog()
        this.neoWalletInfo = neoWalletInfo
        neoWalletInfo.data.balance.forEach {
            if (it.asset_symbol.equals(selectedPair!!.payToken)) {
//                tvPayTokenBalance.text = getString(R.string.balance) + ": ${it.amount} ${selectedPair!!.tradeToken}"
                payTokenCount = it.amount
                payTokenInfo = it
            }
            if (it.asset_symbol.equals("GAS")) {
                gasTokenInfo = it
            }
        }
    }

    override fun generateBuyQgasOrderSuccess() {
        toast(getString(R.string.success))
        closeProgressDialog()
        activity?.finish()
    }

    @Inject
    lateinit internal var mPresenter: OrderBuyPresenter

    var receiveQgasWallet : QLCAccount? = null

    val sendTokenType = 0
    val receiveTokenType = 1
    val selectPair = 11

    var receiveEthWallet : EthWallet? = null
    var receiveEosWallet : EosAccount? = null
    var receiveQlcWallet : QLCAccount? = null
    var receiveNeoWallet : Wallet? = null

    var sendQlcWallet: QLCAccount? = null
    var sendEthWallet: EthWallet? = null
    var sendEosWallet: EosAccount? = null
    var sendNeoWallet: Wallet? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_buy, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCreateWallet.setOnClickListener {
            startActivity(Intent(activity, SelectWalletTypeActivity::class.java))
        }
        tvNext.setOnClickListener {
            if ("".equals(etMinAmount.text.toString()) || "".equals(etMaxAmount.text.toString()) || "".equals(etAmount.text.toString()) || "".equals(etUnitPrice.text.toString())) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            if (etMinAmount.text.toString().trim().toBigDecimal() > etMaxAmount.text.toString().trim().toBigDecimal()) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            if (etMaxAmount.text.toString().trim().toBigDecimal() > etAmount.text.toString().trim().toBigDecimal()) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            payTokenAmount = etAmount.text.toString().trim().toBigDecimal().multiply(etUnitPrice.text.toString().trim().toBigDecimal()).stripTrailingZeros().toPlainString().toDouble()
            when(OtcUtils.parseChain(selectedPair!!.tradeTokenChain)) {
                AllWallet.WalletType.QlcWallet -> {
                    if (!AccountMng.isValidAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EthWallet -> {
                    if (!ETHWalletUtils.isETHValidAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (!Neoutils.validateNEOAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EosWallet -> {
                    if (!EosUtil.isEosName(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
            }
            when(OtcUtils.parseChain(selectedPair!!.payTokenChain)) {
                AllWallet.WalletType.QlcWallet -> {
                    if (!AccountMng.isValidAddress(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EthWallet -> {
                    if (!ETHWalletUtils.isETHValidAddress(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (!Neoutils.validateNEOAddress(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EosWallet -> {
                    if (!EosUtil.isEosName(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
            }
            if (payTokenCount < payTokenAmount) {
                toast(getString(R.string.no_enough) + " " + selectedPair!!.payToken)
                return@setOnClickListener
            }
            if (etUnitPrice.text.toString().toBigDecimal() == BigDecimal.ZERO) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            if (selectedPair!!.tradeToken.equals("QGAS") && etAmount.text.toString().trim().toDouble() > 1000 && !"KYC_SUCCESS".equals(ConstantValue.currentUser.getVstatus())) {
                KotlinConvertJavaUtils.needVerify(activity!!)
                return@setOnClickListener
            }

            showConfirmSellDialog()

        }
        etUnitPrice.filters = arrayOf<InputFilter>(MoneyValueFilter().setDigits(3))
        etUnitPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!"".equals(etUnitPrice.text.toString())) {
                    if (etUnitPrice.text.toString().length == 5 && etUnitPrice.text.toString().toBigDecimal() < 0.001.toBigDecimal()) {
                        etUnitPrice.setText("0.001")
                        etUnitPrice.setSelection(5)
                    }
                }
            }

        })
        llSelectReceiveWallet.setOnClickListener {
            if (selectedPair == null) {
                toast(getString(R.string.please_select_a_trade_pair))
                return@setOnClickListener
            }
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", OtcUtils.parseChain(selectedPair!!.tradeTokenChain).ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, receiveTokenType)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        llSelectSendWallet.setOnClickListener {
            if (selectedPair == null) {
                toast(getString(R.string.please_select_a_trade_pair))
                return@setOnClickListener
            }
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", OtcUtils.parseChain(selectedPair!!.payTokenChain).ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, sendTokenType)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvReceiveWalletAddess.setOnClickListener {
            showEnterQlcWalletDialog()
        }
        llSellToken.setOnClickListener {
            startActivityForResult(Intent(activity, SelectCurrencyActivity::class.java), selectPair)
        }
        llBuyToken.setOnClickListener {
            startActivityForResult(Intent(activity, SelectCurrencyActivity::class.java), selectPair)
        }
    }
    var selectedPair : TradePair.PairsListBean? = null

    fun showConfirmSellDialog() {
        //
        val view = View.inflate(activity!!, R.layout.dialog_send_qgas_layout, null)
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvQLCWalletName1 = view.findViewById<TextView>(R.id.tvQLCWalletName1)
        val tvQLCWalletAddess1 = view.findViewById<TextView>(R.id.tvQLCWalletAddess1)
        val tvQgasAmount1 = view.findViewById<TextView>(R.id.tvQgasAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainQgasAddress)
        val ivSendChain = view.findViewById<ImageView>(R.id.ivSendChain)
        when (OtcUtils.parseChain(selectedPair!!.payTokenChain)) {
            AllWallet.WalletType.EthWallet -> {
                ivSendChain.setImageResource(R.mipmap.icons_eth_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.eth.address
                tvQLCWalletName1.text = sendEthWallet!!.name
                tvQLCWalletAddess1.text = sendEthWallet!!.address

            }
            AllWallet.WalletType.EosWallet -> {
                ivSendChain.setImageResource(R.mipmap.icons_eos_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.qlcchian.address
                tvQLCWalletName1.text = sendEosWallet!!.accountName
                tvQLCWalletAddess1.text = sendEosWallet!!.accountName
            }
            AllWallet.WalletType.NeoWallet -> {
                ivSendChain.setImageResource(R.mipmap.icons_neo_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.neo.address
                tvQLCWalletName1.text = sendNeoWallet!!.name
                tvQLCWalletAddess1.text = sendNeoWallet!!.address
            }
            AllWallet.WalletType.QlcWallet -> {
                ivSendChain.setImageResource(R.mipmap.icons_qlc_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.qlcchian.address
                tvQLCWalletName1.text = sendQlcWallet!!.accountName
                tvQLCWalletAddess1.text = sendQlcWallet!!.address
            }
        }
        tvQgasAmount1.text = payTokenAmount.toString() + " " + selectedPair!!.payToken
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(activity!!)
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
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
            map["pairsId"] = selectedPair!!.id
            map.put("type", ConstantValue.orderTypeBuy)
            map.put("unitPrice", etUnitPrice.text.toString().trim())
            map.put("totalAmount", etAmount.text.toString().trim())
            map.put("minAmount", etMinAmount.text.toString().trim())
            map.put("maxAmount", etMaxAmount.text.toString().trim())
            map.put("qgasAddress", tvReceiveWalletAddess.text.toString().trim())
            map.put("usdtAddress", "")
            thread {
                when (OtcUtils.parseChain(selectedPair!!.payTokenChain)) {
                    AllWallet.WalletType.EthWallet -> {
                        mPresenter.sendEthToken(sendEthWallet!!.address, ConstantValue.mainAddressData.eth.address, payTokenAmount.toString(), 6, ethPayTokenBean!!, map)
                    }
                    AllWallet.WalletType.EosWallet -> {
                        mPresenter.sendQgas(payTokenAmount.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
                    }
                    AllWallet.WalletType.NeoWallet -> {
                        if (payTokenInfo == null) {
                            runOnUiThread {
                                toast(getString(R.string.pleasewait))
                            }
                            return@thread
                        }
                        runOnUiThread {
                            testTransfer(map, sendNeoWallet!!.address)
                        }
//                        mPresenter.sendNep5Token(sendNeoWallet!!.address, etAmount.text.toString(), ConstantValue.mainAddressData.neo.address, tradeTokenInfo!!, getString(R.string.sell) + " " + selectedPair!!.tradeToken, map)
                    }
                    AllWallet.WalletType.QlcWallet -> {
                        mPresenter.sendQgas(payTokenAmount.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
                    }
                }
            }
        }
    }

    private var webview: DWebView? = null
    private fun testTransfer(map : HashMap<String, String>, address : String) {
        webview = DWebView(activity!!)
        webview!!.loadUrl("file:///android_asset/contract.html")
        //fromAddress, toAddress, assetHash, amount, wif, responseCallback
        val arrays = arrayOfNulls<Any>(7)
        arrays[0] = sendNeoWallet!!.address
        arrays[1] = ConstantValue.mainAddressData.neo.address
        arrays[2] = payTokenInfo!!.asset_hash
        arrays[3] = payTokenAmount.toString()
        arrays[4] = 8
        arrays[5] = Account.getWallet()!!.wif
        arrays[6] = "xxx"
        var retStr = ""
        try {
            webview!!.callHandler("staking.send", arrays, OnReturnValue<JSONObject> { retValue ->
                KLog.i("call succeed,return value is " + retValue!!)
                retStr = retValue!!.toString()
                var nep5SendBack = Gson().fromJson(retValue.toString(), SendNep5TokenBack::class.java)
                if (nep5SendBack != null) {
                    mPresenter.generateEntrustBuyOrder(nep5SendBack.txid, address, map)
                } else {

                }
            })
        } catch (e : Exception) {
            closeProgressDialog()
            AppConfig.instance.saveLog("otc_Create_entrust_buy_order", "neo_transfer", retStr)
            toast(getString(R.string.error))
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                receiveTokenType -> {
                    when(OtcUtils.parseChain(selectedPair!!.tradeTokenChain)) {
                        AllWallet.WalletType.EthWallet -> {
                            receiveEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                            tvReceiveWalletName.text = receiveEthWallet!!.name
                            tvReceiveWalletAddess.text = receiveEthWallet!!.address
                        }
                        AllWallet.WalletType.EosWallet -> {
                            receiveEosWallet = data!!.getParcelableExtra<EosAccount>("wallet")
                            tvReceiveWalletName.text = receiveEosWallet!!.walletName
                            tvReceiveWalletAddess.text = receiveEosWallet!!.accountName
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            receiveNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                            tvReceiveWalletName.text = receiveNeoWallet!!.name
                            tvReceiveWalletAddess.text = receiveNeoWallet!!.address
                        }
                        AllWallet.WalletType.QlcWallet -> {
                            receiveQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                            tvReceiveWalletName.text = receiveQlcWallet!!.accountName
                            tvReceiveWalletAddess.text = receiveQlcWallet!!.address
                        }
                    }
                }
                sendTokenType -> {
                    showProgressDialog()
                    payTokenCount = 0.toDouble()
                    val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
                    if (ethWallets.size != 0) {
                        ethWallets.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.ethWalletDao.update(it)
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
                    val qlcAccountList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
                    qlcAccountList.forEach {
                        if (it.isCurrent()) {
                            it.setIsCurrent(false)
                            AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                            return@forEach
                        }
                    }

                    when (OtcUtils.parseChain(selectedPair!!.payTokenChain)) {
                        AllWallet.WalletType.EthWallet -> {
                            sendEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                            tvSendWalletName.text = sendEthWallet!!.name
                            tvSendWalletAddess.text = sendEthWallet!!.address

                            if (ethWallets != null && ethWallets.size != 0) {
                                ethWallets.forEach {
                                    if (it.address.equals(sendEthWallet!!.address)) {
                                        it.setIsCurrent(true)
                                        AppConfig.getInstance().daoSession.ethWalletDao.update(it)
                                        EventBus.getDefault().post(ChangeCurrency())
                                        launch {
                                            delay(2500)
                                            val infoMap = HashMap<String, String>()
                                            infoMap["address"] = sendEthWallet!!.address
                                            mPresenter.getEthWalletDetail(infoMap)
                                        }

                                        return@forEach
                                    }
                                }
                            }
                        }
                        AllWallet.WalletType.EosWallet -> {
                            sendEosWallet = data!!.getParcelableExtra<EosAccount>("wallet")
                            tvSendWalletName.text = sendEosWallet!!.walletName
                            tvSendWalletAddess.text = sendEosWallet!!.accountName

                            if (wallets2 != null && wallets2.size != 0) {
                                wallets2.forEach {
                                    if (it.accountName.equals(sendEosWallet!!.accountName)) {
                                        it.setIsCurrent(true)
                                        AppConfig.getInstance().daoSession.eosAccountDao.update(it)
                                        EventBus.getDefault().post(ChangeCurrency())
                                        return@forEach
                                    }
                                }
                            }
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            sendNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                            tvSendWalletName.text = sendNeoWallet!!.name
                            tvSendWalletAddess.text = sendNeoWallet!!.address

                            if (neoWallets != null && neoWallets.size != 0) {
                                neoWallets.forEach {
                                    if (it.address.equals(sendNeoWallet!!.address)) {
                                        it.setIsCurrent(true)
                                        AppConfig.getInstance().daoSession.walletDao.update(it)
                                        EventBus.getDefault().post(ChangeCurrency())
                                        launch {
                                            delay(2500)
                                            val infoMap = HashMap<String, String>()
                                            infoMap["address"] = sendNeoWallet!!.address
                                            mPresenter.getNeoWalletDetail(infoMap, sendNeoWallet!!.address)
                                        }
                                        return@forEach
                                    }
                                }
                            }
                        }
                        AllWallet.WalletType.QlcWallet -> {
                            closeProgressDialog()
                            sendQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                            tvSendWalletName.text = sendQlcWallet!!.accountName
                            tvSendWalletAddess.text = sendQlcWallet!!.address

                            if (qlcAccountList != null && qlcAccountList.size != 0) {
                                qlcAccountList.forEach {
                                    if (it.address.equals(sendQlcWallet!!.address)) {
                                        it.setIsCurrent(true)
                                        AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                        EventBus.getDefault().post(ChangeCurrency())
                                        getQlcAccountBalance(it)
                                        return@forEach
                                    }
                                }
                            }
                        }
                    }
                }
                selectPair -> {
                    selectedPair = data!!.getParcelableExtra("pair")
                    buyingToken.text = selectedPair!!.tradeToken
                    buyingTokenPrice.text = selectedPair!!.tradeToken
                    sellingToken.text = selectedPair!!.payToken
                    sellinngTokenQuantity.text = selectedPair!!.payToken
                    ivReceiveChain.setImageDrawable(null)

                    tvReceiveWalletAddess.text = getString(R.string.input_wallet_address)
                    tvReceiveWalletName.text = getString(R.string.select_a_wallet_to_receive_token)

                    when(OtcUtils.parseChain(selectedPair!!.tradeTokenChain)) {
                        AllWallet.WalletType.QlcWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_qlc_wallet)
                        }
                        AllWallet.WalletType.EthWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_eth_wallet)
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_neo_wallet)
                        }
                        AllWallet.WalletType.EosWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_eos_wallet)
                        }
                    }

                    when (OtcUtils.parseChain(selectedPair!!.payTokenChain)) {
                        AllWallet.WalletType.QlcWallet -> {
                            ivSendChain.setImageResource(R.mipmap.icons_qlc_wallet)
                        }
                        AllWallet.WalletType.EthWallet -> {
                            ivSendChain.setImageResource(R.mipmap.icons_eth_wallet)
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            ivSendChain.setImageResource(R.mipmap.icons_neo_wallet)
                        }
                        AllWallet.WalletType.EosWallet -> {
                            ivSendChain.setImageResource(R.mipmap.icons_eos_wallet)
                        }
                    }
                }
            }
        }
    }

    fun getQlcAccountBalance(qlcAccount: QLCAccount) {
        thread {
            QLCAPI().walletGetBalance(qlcAccount.getAddress(), "", object : QLCAPI.BalanceInter {
                override fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: Error?) {
                    if (error == null) {
                        KLog.i(baseResult)
                        baseResult!!.forEach {
                            if (it.symbol.equals(selectedPair!!.tradeToken)) {
                                payTokenCount = it.balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toDouble()
                                return@forEach
                            }
                        }
                    } else {
                        payTokenCount = 0.toDouble()
                    }
                }
            })
        }
    }

    fun showEnterQlcWalletDialog() {
        //
        val view = View.inflate(activity!!, R.layout.dialog_input_walletaddress_layout, null)
        val etContent = view.findViewById<View>(R.id.etContent) as EditText//输入内容
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)//取消按钮
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(activity)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        tvCancel.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
            if (AccountMng.isValidAddress(etContent.text.toString().trim())) {
                tvReceiveWalletName.text = etContent.text.toString().trim()
                tvReceiveWalletAddess.text = etContent.text.toString().trim()
            } else {
                toast(getString(R.string.illegal_receipt_address))
            }
            sweetAlertDialog.cancel()
        }
    }

    override fun setupFragmentComponent() {
        DaggerOrderBuyComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .orderBuyModule(OrderBuyModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: OrderBuyContract.OrderBuyContractPresenter) {
        mPresenter = presenter as OrderBuyPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
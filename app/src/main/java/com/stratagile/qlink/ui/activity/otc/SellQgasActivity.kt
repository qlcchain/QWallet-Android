package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.gson.Gson
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.Account
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EosAccount
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.*
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.ui.activity.otc.component.DaggerSellQgasComponent
import com.stratagile.qlink.ui.activity.otc.contract.SellQgasContract
import com.stratagile.qlink.ui.activity.otc.module.SellQgasModule
import com.stratagile.qlink.ui.activity.otc.presenter.SellQgasPresenter
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_sell_qgas.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.web3j.utils.Convert
import qlc.mng.AccountMng
import wendu.dsbridge.DWebView
import wendu.dsbridge.OnReturnValue
import java.math.BigDecimal
import java.util.HashMap

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/09 14:18:11
 */

class SellQgasActivity : BaseActivity(), SellQgasContract.View {


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
            if (entrustOrderInfo.order!!.tradeToken.equals(it.tokenInfo.symbol)) {
                payTokenCount = it.balance / Math.pow(it.tokenInfo.decimals.toDouble(), it.tokenInfo.decimals.toDouble())
                ethPayTokenBean = it
                return@forEach
            }
        }
        mPresenter.getEthPrice()
    }

    var ethPayTokenBean : EthWalletInfo.DataBean.TokensBean? = null

    var ethCount = 0.toDouble()

    var payTokenCount = 0.toDouble()

    var payTokenAmount = 0.toDouble()

    private var gasEth: String = "0.0004"

    var gasTokenInfo: NeoWalletInfo.DataBean.BalanceBean? = null
    var neoWalletInfo: NeoWalletInfo? = null
    var tradeTokenInfo: NeoWalletInfo.DataBean.BalanceBean? = null
    var tradeTokenAmount = 0.toDouble()
    override fun setNeoDetail(neoWalletInfo: NeoWalletInfo) {
        closeProgressDialog()
        this.neoWalletInfo = neoWalletInfo
        neoWalletInfo.data.balance.forEach {
            if (it.asset_symbol.equals(entrustOrderInfo.order.tradeToken)) {
                tradeTokenAmount = it.amount
                tradeTokenInfo = it
            }
            if (it.asset_symbol.equals("GAS")) {
                gasTokenInfo = it
            }
        }
    }

    override fun tradeOrderTxidSuccess() {
        toast(getString(R.string.success))
        closeProgressDialog()
        startActivity(Intent(this, OtcOrderRecordActivity::class.java).putExtra("position", 1))
        finish()
    }

    override fun generateSellQgasOrderFailed(content: String) {
        runOnUiThread {
            closeProgressDialog()
            toast(content)
        }
    }

    override fun setEthPrice(tokenPrice: TokenPrice) {
        ethPrice = tokenPrice.data[0].price
        gasPrice = seekBar.progress + ConstantValue.gasPrice
        tvGwei.text = "$gasPrice gwei"
        val gas = Convert.toWei(gasPrice.toString() + "", Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        val f = gas.multiply(BigDecimal(gasLimit))
        val gasMoney = f.multiply(BigDecimal(ethPrice.toString() + ""))
        val f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()
        tvCostEth.text = gasEth + " ether ≈ " + ConstantValue.currencyBean.currencyImg + f1
    }


    /**
     * eth当前的市价
     */
    private var ethPrice: Double = 0.toDouble()

    private val gasLimit = 60000

    private var gasPrice = ConstantValue.gasPrice

    override fun generateBuyQgasOrderSuccess(tradeOrder: TradeOrder) {
        var tradeOrderId = tradeOrder.order.id
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", AccountUtil.getUserToken())
        map["tradeOrderId"] = tradeOrderId
        var message = "otc_trade_sell_" + tradeOrderId
        thread {
            when (OtcUtils.parseChain(entrustOrderInfo.order!!.tradeTokenChain)) {
                AllWallet.WalletType.EthWallet -> {
                    mPresenter.sendEthToken(sendEthWallet!!.address, ConstantValue.mainAddressData.eth.address, etQgas.text.toString().trim(), gasPrice, ethPayTokenBean!!.tokenInfo.address, map, ethPayTokenBean!!.tokenInfo.decimals.toInt())
                }
                AllWallet.WalletType.EosWallet -> {
//                    mPresenter.sendQgas(etQgas.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map, message)
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (tradeTokenInfo == null) {
                        runOnUiThread {
                            toast(getString(R.string.pleasewait))
                        }
                    }
                    testTransfer(map, sendNeoWallet!!.address)
                }
                AllWallet.WalletType.QlcWallet -> {
                    mPresenter.sendQgas(etQgas.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map, message)
                }
            }
        }
    }

    override fun generateTradeSellQgasOrderSuccess() {
//        val qrEntity = QrEntity(entrustOrderInfo.order.qgasAddress, "QGAS" + " Receivable Address", "qgas", 4)
//        val intent = Intent(this, UsdtReceiveAddressActivity::class.java)
//        intent.putExtra("qrentity", qrEntity)
//        startActivity(intent)
//        finish()
    }

    override fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo) {
        closeProgressDialog()
        this.entrustOrderInfo = entrustOrderInfo
        maxQgas = entrustOrderInfo.order.totalAmount.toBigDecimal() - entrustOrderInfo.order.lockingAmount.toBigDecimal() - entrustOrderInfo.order.completeAmount.toBigDecimal()
//        maxQgas = entrustOrderInfo.order.totalAmount.toBigDecimal()
        tvAmount.text = maxQgas.stripTrailingZeros().toPlainString()
        if (entrustOrderInfo.order.maxAmount.toBigDecimal() < maxQgas) {
            maxQgas = entrustOrderInfo.order.maxAmount.toBigDecimal()
        }

        maxUsdt = maxQgas.multiply(orderList.unitPrice.toBigDecimal())
        etUsdt.hint = getString(R.string.max) + " " + maxUsdt.stripTrailingZeros().toPlainString()
        etQgas.hint = getString(R.string.max) + " " + maxQgas

        if (maxQgas < BigDecimal.valueOf(orderList.maxAmount)) {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + maxQgas.stripTrailingZeros().toPlainString()
        } else {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        }

        when(OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain)) {
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
        when(OtcUtils.parseChain(entrustOrderInfo.order.payTokenChain)) {
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
        llSelectSendWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain).ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, sendTokenType)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        llSelectReceiveWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", OtcUtils.parseChain(entrustOrderInfo.order.payTokenChain).ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, receiveTokenType)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
//            showSpinnerPopWindow()
        }
    }

    @Inject
    internal lateinit var mPresenter: SellQgasPresenter
    lateinit var orderList : EntrustOrderList.OrderListBean
    lateinit var entrustOrderInfo :EntrustOrderInfo

    var maxUsdt = BigDecimal.ZERO
    var maxQgas = BigDecimal.ZERO

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_sell_qgas)
    }
    override fun initData() {
        if (ConstantValue.mainAddressData == null) {
            mPresenter.getMainAddress()
        }
        orderList = intent.getParcelableExtra("order")
        title.text = getString(R.string.sell) + " " + orderList.tradeToken

        tvTradeToken.text = orderList.tradeToken
        tvPayToken.text = orderList.payToken
        tvUnitPriceTip.text = getString(R.string.unit_price) + "(" + orderList.payToken + ")"
        tvVolumeAmount.text = getString(R.string.volume_amount_qgas) + "(" + orderList.tradeToken + ")"
        tvVolumeSetting.text = getString(R.string.limits) + "(" + orderList.tradeToken + ")"

        tvUnitPrice.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        tvAmount.text= BigDecimal.valueOf(orderList.totalAmount).stripTrailingZeros().toPlainString()
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        maxQgas = orderList.totalAmount.toBigDecimal()
        if (orderList.maxAmount < orderList.totalAmount) {
            maxQgas = orderList.maxAmount.toBigDecimal()
        }
        maxUsdt = maxQgas.multiply(orderList.unitPrice.toBigDecimal())
        etUsdt.hint = getString(R.string.max) + " " + maxUsdt.toPlainString()
        etQgas.hint = getString(R.string.max) + " " + maxQgas

        tvReceiveWalletAddess.setOnClickListener {
            showEnterEthWalletDialog()
        }

        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("entrustOrderId", orderList.id)
        mPresenter.getEntrustOrderDetail(map)

        tvNext.setOnClickListener {
            if (entrustOrderInfo.order.userId.equals(ConstantValue.currentUser.userId)) {
                toast(getString(R.string.this_is_your_order))
                return@setOnClickListener
            }
            if ("".equals(etQgas.text.toString())) {
                return@setOnClickListener
            }
            if ("".equals(etUsdt.text.toString())) {
                return@setOnClickListener
            }
            if (etUsdt.text.toString().toBigDecimal() > maxUsdt) {
                return@setOnClickListener
            }
            if (etQgas.text.toString().toBigDecimal() > maxQgas) {
                return@setOnClickListener
            }
            if ("".equals(tvSendWalletAddess)) {
                return@setOnClickListener
            }

            if (etQgas.text.toString().toBigDecimal() > 1000.toBigDecimal() && !"KYC_SUCCESS".equals(ConstantValue.currentUser.getVstatus())) {
                KotlinConvertJavaUtils.needVerify(this)
                return@setOnClickListener
            }

            when(OtcUtils.parseChain(entrustOrderInfo.order.payTokenChain)) {
                AllWallet.WalletType.QlcWallet -> {
                    if (!AccountMng.isValidAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EthWallet -> {
                    if (!ETHWalletUtils.isETHValidAddress(tvReceiveWalletAddess.text.toString())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (!NeoUtils.isValidAddress(tvReceiveWalletAddess.text.toString().trim())) {
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
            when(OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain)) {
                AllWallet.WalletType.QlcWallet -> {
                    if (!AccountMng.isValidAddress(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_send_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EthWallet -> {
                    if (!ETHWalletUtils.isETHValidAddress(tvSendWalletAddess.text.toString())) {
                        toast(getString(R.string.illegal_send_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (!NeoUtils.isValidAddress(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_send_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EosWallet -> {
                    if (!EosUtil.isEosName(tvSendWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_send_address))
                        return@setOnClickListener
                    }
                }
            }

            if ("".equals(tvSendWalletAddess.text.toString())) {
                toast(getString(R.string.please_select_a_qlc_wallet_to_payment))
                return@setOnClickListener
            }

            if (maxQgas < entrustOrderInfo.order.minAmount.toBigDecimal()) {
                //交易量不足的情况，输入剩余的数量
                if (etQgas.text.toString().toBigDecimal() < maxQgas) {
                    return@setOnClickListener
                }
            } else {
                // 最小的数量要大于等于minAmount
                if (etQgas.text.toString().toBigDecimal() < entrustOrderInfo.order.minAmount.toBigDecimal()) {
                    toast(getString(R.string.the_smallest) + entrustOrderInfo.order.tradeToken + getString(R.string.is_otc) + entrustOrderInfo.order.minAmount.toBigDecimal())
                    return@setOnClickListener
                }
            }

            showConfirmSellDialog()
        }
        etUsdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etUsdt.hasFocus()) {
                    if ("".equals(etUsdt.text.toString())) {
                        etQgas.setText("")
                    } else if (".".equals(etUsdt.text.toString())) {
                        etUsdt.setText("")
                        etQgas.setText("")
                    } else if ("0.".equals(etUsdt.text.toString())) {

                    } else {
                        if (etUsdt.text.toString().toBigDecimal() > maxUsdt) {
                            etUsdt.setText(maxUsdt.stripTrailingZeros().toPlainString())
                            etQgas.setText(maxQgas.stripTrailingZeros().toString())
                        } else {
                            etQgas.setText((etUsdt.text.toString().toBigDecimal().divide(entrustOrderInfo.order.unitPrice.toBigDecimal(), 8, BigDecimal.ROUND_HALF_UP)).stripTrailingZeros().toPlainString())
                        }
                    }
                } else {

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        etQgas.filters = arrayOf<InputFilter>(InputNumLengthFilter(3, 13))
        etQgas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etQgas.hasFocus()) {
                    if ("".equals(etQgas.text.toString())) {
                        etUsdt.setText("")
                    } else {
                        if (etQgas.text.toString().toBigDecimal() > maxQgas) {
                            etUsdt.setText(maxUsdt.stripTrailingZeros().toPlainString())
                            etQgas.setText(maxQgas.stripTrailingZeros().toPlainString())
                        } else {
                            etUsdt.setText((BigDecimal.valueOf(entrustOrderInfo.order.unitPrice).multiply(etQgas.text.toString().toBigDecimal())).setScale(8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
                        }
                    }
                } else {

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        llOpen.setOnClickListener {
            toggleCost()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                gasPrice = progress + ConstantValue.gasPrice
                tvGwei.text = "$gasPrice gwei"
                val gas = Convert.toWei(gasPrice.toString() + "", Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
                val f = gas.multiply(BigDecimal(gasLimit))

                val gasMoney = f.multiply(BigDecimal(ethPrice.toString() + ""))
                val f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                KLog.i(f1)
                gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()
                tvCostEth.text = gasEth + " ether ≈ " + ConstantValue.currencyBean.currencyImg + f1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    var isOpen = false

    private fun toggleCost() {
        if (isOpen) {
            isOpen = false
            group.visibility = View.GONE
            SpringAnimationUtil.endRotatoSpringViewAnimation(ivShow) { animation, canceled, value, velocity -> }
        } else {
            isOpen = true
            group.visibility = View.VISIBLE
            SpringAnimationUtil.startRotatoSpringViewAnimation(ivShow) { animation, canceled, value, velocity -> }
        }
    }

    var receiveEthWallet : EthWallet? = null
    var receiveEosWallet : EosAccount? = null
    var receiveQlcWallet : QLCAccount? = null
    var receiveNeoWallet : Wallet? = null

    var sendQlcWallet : QLCAccount? = null
    var sendEthWallet : EthWallet? = null
    var sendEosWallet : EosAccount? = null
    var sendNeoWallet : Wallet? = null

    val sendTokenType = 0
    val receiveTokenType = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                sendTokenType -> {
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
                    val qlcAccounList = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
                    if (qlcAccounList != null && qlcAccounList.size != 0) {
                        qlcAccounList.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                return@forEach
                            }
                        }
                    }
                    when(OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain)) {
                        AllWallet.WalletType.EthWallet -> {
                            showProgressDialog()
                            constraintLayout.visibility = View.VISIBLE
                            sendEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                            ivSendChain.setImageResource(R.mipmap.icons_eth_wallet)
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
                        AllWallet.WalletType.QlcWallet -> {
                            sendQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                            ivSendChain.setImageResource(R.mipmap.icons_qlc_wallet)
                            tvSendWalletName.text = sendQlcWallet!!.accountName
                            tvSendWalletAddess.text = sendQlcWallet!!.address

                            if (qlcAccounList != null && qlcAccounList.size != 0) {
                                qlcAccounList.forEach {
                                    if (it.address.equals(sendQlcWallet!!.address)) {
                                        it.setIsCurrent(true)
                                        AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                        EventBus.getDefault().post(ChangeCurrency())
                                        return@forEach
                                    }
                                }
                            }
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            showProgressDialog()
                            sendNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                            ivSendChain.setImageResource(R.mipmap.icons_neo_wallet)
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
                    }
                }
                receiveTokenType -> {
                    when(OtcUtils.parseChain(entrustOrderInfo.order.payTokenChain)) {
                        AllWallet.WalletType.EthWallet -> {
                            receiveEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                            ivReceiveChain.setImageResource(R.mipmap.icons_eth_wallet)
                            tvReceiveWalletName.text = receiveEthWallet!!.name
                            tvReceiveWalletAddess.text = receiveEthWallet!!.address
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            receiveNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                            ivReceiveChain.setImageResource(R.mipmap.icons_neo_wallet)
                            tvReceiveWalletName.text = receiveNeoWallet!!.name
                            tvReceiveWalletAddess.text = receiveNeoWallet!!.address
                        }
                        AllWallet.WalletType.QlcWallet -> {
                            receiveQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                            ivReceiveChain.setImageResource(R.mipmap.icons_qlc_wallet)
                            tvReceiveWalletName.text = receiveQlcWallet!!.accountName
                            tvReceiveWalletAddess.text = receiveQlcWallet!!.address
                        }
                    }
                }
            }
        }
    }

    fun showConfirmSellDialog() {
        //
        val view = View.inflate(this, R.layout.dialog_send_qgas_layout, null)
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val ivSendChain1 = view.findViewById<ImageView>(R.id.ivSendChain)
        val tvQLCWalletName1 = view.findViewById<TextView>(R.id.tvQLCWalletName1)
        val tvQLCWalletAddess1 = view.findViewById<TextView>(R.id.tvQLCWalletAddess1)
        val tvQgasAmount1 = view.findViewById<TextView>(R.id.tvQgasAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainQgasAddress)
        val tvMemo = view.findViewById<TextView>(R.id.tvMemo)
        tvMemo.text = getString(R.string.sell) + " " + entrustOrderInfo.order.tradeToken
        tvQgasAmount1.text = etQgas.text.toString().trim() + " " + entrustOrderInfo.order.tradeToken

        when(OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain)) {
            AllWallet.WalletType.EthWallet -> {
                ivSendChain1.setImageResource(R.mipmap.icons_eth_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.eth.address
                tvQLCWalletName1.text = sendEthWallet!!.name
                tvQLCWalletAddess1.text = sendEthWallet!!.address

            }
            AllWallet.WalletType.EosWallet -> {
                ivSendChain1.setImageResource(R.mipmap.icons_eos_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.qlcchian.address
                tvQLCWalletName1.text = sendEosWallet!!.accountName
                tvQLCWalletAddess1.text = sendEosWallet!!.accountName
            }
            AllWallet.WalletType.NeoWallet -> {
                ivSendChain1.setImageResource(R.mipmap.icons_neo_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.neo.address
                tvQLCWalletName1.text = sendNeoWallet!!.name
                tvQLCWalletAddess1.text = sendNeoWallet!!.address
            }
            AllWallet.WalletType.QlcWallet -> {
                ivSendChain1.setImageResource(R.mipmap.icons_qlc_wallet)
                tvMainQgasAddress.text = ConstantValue.mainAddressData.qlcchian.address
                tvQLCWalletName1.text = sendQlcWallet!!.accountName
                tvQLCWalletAddess1.text = sendQlcWallet!!.address
            }
        }

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
            FireBaseUtils.logEvent(this, FireBaseUtils.OTC_SELL_Submit)
            sweetAlertDialog.cancel()
            showProgressDialog()
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", AccountUtil.getUserToken())
            map.put("entrustOrderId", orderList.id)
            map.put("usdtAmount", etUsdt.text.toString())
            map["usdtToAddress"] = tvReceiveWalletAddess.text.toString()
            map.put("qgasAmount", etQgas.text.toString())
            when (OtcUtils.parseChain(entrustOrderInfo.order!!.tradeTokenChain)) {
                AllWallet.WalletType.EthWallet -> {
                    map.put("fromAddress", sendEthWallet!!.address)
//                    mPresenter.sendEthToken(sendEthWallet!!.address, ConstantValue.mainAddressData.eth.address, etQgas.text.toString().trim(), 6, ethPayTokenBean!!.tokenInfo.address, map)
                }
                AllWallet.WalletType.EosWallet -> {
//                    mPresenter.sendQgas(etQgas.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (tradeTokenInfo == null) {
                        runOnUiThread {
                            toast(getString(R.string.pleasewait))
                        }
                    }
                    map.put("fromAddress", sendNeoWallet!!.address)
//                    testTransfer(map, sendNeoWallet!!.address)
                }
                AllWallet.WalletType.QlcWallet -> {
                    map.put("fromAddress", sendQlcWallet!!.address)
//                    mPresenter.sendQgas(etQgas.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
                }
            }
            mPresenter.generateTradeSellOrder(map)
        }
    }


    fun uploadTxid() {

    }



    private var webview: DWebView? = null
    private fun testTransfer(map : HashMap<String, String>, address : String) {
        webview = DWebView(this)
        webview!!.loadUrl("file:///android_asset/contract.html")
        //fromAddress, toAddress, assetHash, amount, wif, responseCallback
        val arrays = arrayOfNulls<Any>(8)
        arrays[0] = sendNeoWallet!!.address
        arrays[1] = ConstantValue.mainAddressData.neo.address
        arrays[2] = tradeTokenInfo!!.asset_hash
        arrays[3] = etQgas.text.toString().trim()
        arrays[4] = 8
//        arrays[5] = Account.getWallet()!!.ecKeyPair.exportAsWIF()
        arrays[5] = Account.getWallet()!!.wif
        arrays[6] = "xxx"
        arrays[7] = AppConfig.instance.isMainNet
        try {
            webview!!.callHandler("staking.send", arrays, OnReturnValue<JSONObject> { retValue ->
                KLog.i("call succeed,return value is " + retValue!!)
                var nep5SendBack = Gson().fromJson(retValue.toString(), SendNep5TokenBack::class.java)
                if (nep5SendBack != null) {
                    mPresenter.confirmTradeOrderTxid(nep5SendBack.txid, map)
                } else {
                    toast(getString(R.string.send_qgas_error, tradeTokenInfo!!.asset_symbol))
                }
            })
        } catch (e : Exception) {
            toast(getString(R.string.send_qgas_error, tradeTokenInfo!!.asset_symbol))
            e.printStackTrace()
        }
    }



    fun showEnterEthWalletDialog() {
        //
        val view = View.inflate(this, R.layout.dialog_input_walletaddress_layout, null)
        val etContent = view.findViewById<View>(R.id.etContent) as EditText//输入内容
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)//取消按钮
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(this)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        tvCancel.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
            if (ETHWalletUtils.isETHValidAddress(etContent.text.toString().trim())) {
                tvReceiveWalletAddess.text = etContent.text.toString().trim()
                tvReceiveWalletName.text = etContent.text.toString().trim()
            } else {
                toast(getString(R.string.illegal_receipt_address))
            }
            sweetAlertDialog.cancel()
        }
    }

    override fun setupActivityComponent() {
       DaggerSellQgasComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .sellQgasModule(SellQgasModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SellQgasContract.SellQgasContractPresenter) {
            mPresenter = presenter as SellQgasPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
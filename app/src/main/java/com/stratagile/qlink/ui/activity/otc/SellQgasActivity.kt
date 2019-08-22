package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EosAccount
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.entity.QrEntity
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
import neoutils.Neoutils
import org.greenrobot.eventbus.EventBus
import qlc.mng.AccountMng
import java.math.BigDecimal

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/09 14:18:11
 */

class SellQgasActivity : BaseActivity(), SellQgasContract.View {
    override fun generateSellQgasOrderFailed(content: String) {
        runOnUiThread {
            closeProgressDialog()
            toast(content)
        }
    }

    override fun generateBuyQgasOrderSuccess() {
        toast("success")
        closeProgressDialog()
        finish()
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
        maxQgas = entrustOrderInfo.order.totalAmount.toInt() - entrustOrderInfo.order.lockingAmount.toInt() - entrustOrderInfo.order.completeAmount.toInt()
        maxUsdt = BigDecimal.valueOf(orderList.unitPrice * maxQgas)
        etUsdt.hint = getString(R.string.max) + " " + maxUsdt.toPlainString()
        etQgas.hint = getString(R.string.max) + " " + maxQgas
        tvAmount.text = maxQgas.toString()
        if (maxQgas.toBigDecimal() < BigDecimal.valueOf(orderList.maxAmount)) {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + maxQgas.toString()
        } else {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        }

        when(OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain)) {
            AllWallet.WalletType.QlcWallet -> {

            }
            AllWallet.WalletType.EthWallet -> {

            }
            AllWallet.WalletType.NeoWallet -> {

            }
            AllWallet.WalletType.EosWallet -> {

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

    var maxUsdt = BigDecimal.ONE
    var maxQgas = 0

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
        tvVolumeSetting.text = getString(R.string.volume_settings) + "(" + orderList.tradeToken + ")"

        tvUnitPrice.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        tvAmount.text= BigDecimal.valueOf(orderList.totalAmount).stripTrailingZeros().toPlainString()
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        maxQgas = orderList.totalAmount.toInt()
        maxUsdt = BigDecimal.valueOf(orderList.unitPrice * maxQgas)
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
            if (etQgas.text.toString().toInt() > maxQgas) {
                return@setOnClickListener
            }
            if ("".equals(tvSendWalletAddess)) {
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
                    if (!Neoutils.validateNEOAddress(tvSendWalletAddess.text.toString().trim())) {
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

            if (maxQgas < entrustOrderInfo.order.minAmount) {
                //交易量不足的情况，输入剩余的数量
                if (etQgas.text.toString().toInt() < maxQgas) {
                    return@setOnClickListener
                }
            } else {
                // 最小的数量要大于等于minAmount
                if (etQgas.text.toString().toInt() < entrustOrderInfo.order.minAmount) {
                    toast(getString(R.string.the_smallest) + entrustOrderInfo.order.tradeToken + getString(R.string.is_otc) + entrustOrderInfo.order.minAmount.toInt())
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
                            etUsdt.setText(maxUsdt.toPlainString())
                            etQgas.setText(maxQgas.toString())
                        } else {
                            etQgas.setText((etUsdt.text.toString().toBigDecimal().divide(entrustOrderInfo.order.unitPrice.toBigDecimal(), 0, BigDecimal.ROUND_HALF_UP)).stripTrailingZeros().toPlainString())
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
        etQgas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etQgas.hasFocus()) {
                    if ("".equals(etQgas.text.toString())) {
                        etUsdt.setText("")
                    } else {
                        if (etQgas.text.toString().toInt() > maxQgas) {
                            etUsdt.setText(maxUsdt.toPlainString())
                            etQgas.setText(maxQgas.toString())
                        } else {
                            etUsdt.setText((BigDecimal.valueOf(entrustOrderInfo.order.unitPrice).multiply(etQgas.text.toString().toBigDecimal())).stripTrailingZeros().toPlainString())
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
                    when(OtcUtils.parseChain(entrustOrderInfo.order.tradeTokenChain)) {
                        AllWallet.WalletType.EthWallet -> {
                            sendEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                            ivSendChain.setImageResource(R.mipmap.icons_eth_wallet)
                            tvSendWalletName.text = sendEthWallet!!.name
                            tvSendWalletAddess.text = sendEthWallet!!.address
                        }
                        AllWallet.WalletType.QlcWallet -> {
                            sendQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                            ivSendChain.setImageResource(R.mipmap.icons_qlc_wallet)
                            tvSendWalletName.text = sendQlcWallet!!.accountName
                            tvSendWalletAddess.text = sendQlcWallet!!.address
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
        val tvQLCWalletName1 = view.findViewById<TextView>(R.id.tvQLCWalletName1)
        val tvQLCWalletAddess1 = view.findViewById<TextView>(R.id.tvQLCWalletAddess1)
        val tvQgasAmount1 = view.findViewById<TextView>(R.id.tvQgasAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainQgasAddress)
        tvQLCWalletName1.text = sendQlcWallet!!.accountName
        tvQLCWalletAddess1.text = sendQlcWallet!!.address
        tvQgasAmount1.text = etQgas.text.toString().trim() + " " + entrustOrderInfo.order.tradeToken
        tvMainQgasAddress.text = ConstantValue.mainAddressData.qlcchian.address
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
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", AccountUtil.getUserToken())
            map.put("entrustOrderId", orderList.id)
            map.put("usdtAmount", etUsdt.text.toString())
            map["usdtToAddress"] = tvReceiveWalletAddess.text.toString()
            map.put("qgasAmount", etQgas.text.toString())
            mPresenter.sendQgas(etQgas.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
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
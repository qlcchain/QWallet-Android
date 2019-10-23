package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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
import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.entity.otc.GenerageTradeOrder
import com.stratagile.qlink.ui.activity.otc.component.DaggerBuyQgasComponent
import com.stratagile.qlink.ui.activity.otc.contract.BuyQgasContract
import com.stratagile.qlink.ui.activity.otc.module.BuyQgasModule
import com.stratagile.qlink.ui.activity.otc.presenter.BuyQgasPresenter
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.EosUtil
import com.stratagile.qlink.utils.OtcUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_buy_qgas.*
import kotlinx.android.synthetic.main.activity_buy_qgas.llSelectQlcWallet
import kotlinx.android.synthetic.main.activity_buy_qgas.tvAmount
import kotlinx.android.synthetic.main.activity_buy_qgas.tvNext
import kotlinx.android.synthetic.main.activity_buy_qgas.tvUnitPrice
import neoutils.Neoutils
import qlc.mng.AccountMng
import java.math.BigDecimal

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/09 14:18:25
 */

class BuyQgasActivity : BaseActivity(), BuyQgasContract.View {
    override fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo) {
        closeProgressDialog()
        this.entrustOrderInfo = entrustOrderInfo
        maxQgas = entrustOrderInfo.order.totalAmount.toBigDecimal() - entrustOrderInfo.order.lockingAmount.toBigDecimal() - entrustOrderInfo.order.completeAmount.toBigDecimal()
        maxUsdt = maxQgas.multiply(orderList.unitPrice.toBigDecimal())
        etUsdt.hint = getString(R.string.max) + " " + maxUsdt.stripTrailingZeros().toPlainString()
        etQgas.hint = getString(R.string.max) + " " + maxQgas.stripTrailingZeros().toPlainString()
        tvAmount.text = maxQgas.stripTrailingZeros().toPlainString()
        if (maxQgas < BigDecimal.valueOf(orderList.maxAmount)) {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + maxQgas.stripTrailingZeros().toPlainString()
        } else {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        }
    }

    override fun generateTradeBuyQgasOrderSuccess(generageTradeOrder: GenerageTradeOrder) {
        val qrEntity = QrEntity(generageTradeOrder.order.usdtToAddress, "USDT" + " Receivable Address", "usdt", 2)
        val intent = Intent(this, UsdtReceiveAddressActivity::class.java)
        intent.putExtra("tradeToken", generageTradeOrder.order.tradeToken)
        intent.putExtra("usdt", generageTradeOrder.order.usdtAmount.toString())
        intent.putExtra("payToken", generageTradeOrder.order.payToken)
        intent.putExtra("payTokenChain", generageTradeOrder.order.payTokenChain)
        intent.putExtra("receiveAddress", generageTradeOrder.order.usdtToAddress)
        intent.putExtra("tradeOrderId", generageTradeOrder.order.id)
        intent.putExtra("orderNumber", generageTradeOrder.order.number.toString())
        intent.putExtra("qrentity", qrEntity)
        startActivity(intent)
        finish()
    }

    @Inject
    internal lateinit var mPresenter: BuyQgasPresenter

    lateinit var orderList: EntrustOrderList.OrderListBean
    lateinit var entrustOrderInfo: EntrustOrderInfo

    var maxUsdt = BigDecimal.ZERO
    var maxQgas = BigDecimal.ZERO

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_buy_qgas)
    }

    override fun initData() {
        tvCreateWallet.setOnClickListener {
            startActivity(Intent(this, SelectWalletTypeActivity::class.java))
        }
        orderList = intent.getParcelableExtra("order")
        title.text = getString(R.string.buy) + " " + orderList.tradeToken
        tvTradeToken.text = orderList.tradeToken
        tvPayToken.text = orderList.payToken
        tvUnitPriceTip.text = getString(R.string.unit_price) + "(" + orderList.payToken + ")"
        tvVolumeAmount.text = getString(R.string.volume_amount_qgas) + "(" + orderList.tradeToken + ")"
        tvVolumeSetting.text = getString(R.string.limits) + "(" + orderList.tradeToken + ")"
        tvUnitPrice.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        maxQgas = orderList.totalAmount.toBigDecimal()
        maxUsdt = maxQgas.multiply(orderList.unitPrice.toBigDecimal())
        etUsdt.hint = getString(R.string.max) + " " + maxUsdt.stripTrailingZeros().toPlainString()
        etQgas.hint = getString(R.string.max) + " " + maxQgas.stripTrailingZeros().toPlainString()
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        var map = hashMapOf<String, String>()
        map.put("entrustOrderId", orderList.id)
        mPresenter.getEntrustOrderDetail(map)

        when(OtcUtils.parseChain(orderList.tradeTokenChain)) {
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
            if (etQgas.text.toString().toBigDecimal() > maxQgas) {
                return@setOnClickListener
            }
            if ("".equals(tvReceiveWalletAddess)) {
                return@setOnClickListener
            }
            when(OtcUtils.parseChain(orderList.tradeTokenChain)) {
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

            if (maxQgas < entrustOrderInfo.order.minAmount.toBigDecimal()) {
                //交易量不足的情况，输入剩余的数量
                if (etQgas.text.toString().toBigDecimal() < maxQgas) {
                    return@setOnClickListener
                }
            } else {
                // 最小的数量要大于等于minAmount
                if (etQgas.text.toString().toInt() < entrustOrderInfo.order.minAmount) {
                    toast(getString(R.string.the_smallest) + entrustOrderInfo.order.tradeToken + getString(R.string.is_otc) + entrustOrderInfo.order.minAmount.toInt())
                    return@setOnClickListener
                }
            }
            showProgressDialog()
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", AccountUtil.getUserToken())
            map.put("entrustOrderId", orderList.id)
            map.put("usdtAmount", etUsdt.text.toString())
            map.put("qgasAmount", etQgas.text.toString())
            map.put("qgasToAddress", tvReceiveWalletAddess.text.toString())
            mPresenter.generateTradeBuyQgasOrder(map)
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
                            etQgas.setText(maxQgas.stripTrailingZeros().toPlainString())
                        } else {
                            etQgas.setText((etUsdt.text.toString().toBigDecimal().divide(entrustOrderInfo.order.unitPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)).stripTrailingZeros().toPlainString())
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
                        if (etQgas.text.toString().toBigDecimal() > maxQgas) {
                            etUsdt.setText(maxUsdt.stripTrailingZeros().toPlainString())
                            etQgas.setText(maxQgas.stripTrailingZeros().toString())
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
        tvReceiveWalletAddess.setOnClickListener {
            showEnterQlcWalletDialog()
        }


        llSelectQlcWallet.setOnClickListener {
            when(OtcUtils.parseChain(orderList.tradeTokenChain)) {
                AllWallet.WalletType.QlcWallet -> {
                    var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
                    intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
                    intent1.putExtra("select", true)
                    startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
                }
                AllWallet.WalletType.EthWallet -> {
                    var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
                    intent1.putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal)
                    intent1.putExtra("select", true)
                    startActivityForResult(intent1, AllWallet.WalletType.EthWallet.ordinal)
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
                }
                AllWallet.WalletType.NeoWallet -> {
                    var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
                    intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
                    intent1.putExtra("select", true)
                    startActivityForResult(intent1, AllWallet.WalletType.NeoWallet.ordinal)
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
                }
                AllWallet.WalletType.EosWallet -> {
                    var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
                    intent1.putExtra("walletType", AllWallet.WalletType.EosWallet.ordinal)
                    intent1.putExtra("select", true)
                    startActivityForResult(intent1, AllWallet.WalletType.EosWallet.ordinal)
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
                }
            }
        }
    }

    var receiveQlcWallet : QLCAccount? = null
    var receiveEthWallet: EthWallet? = null
    var receiveEosWallet: EosAccount? = null
    var receiveNeoWallet: Wallet? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AllWallet.WalletType.QlcWallet.ordinal -> {
                    receiveQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                    tvReceiveWalletName.text = receiveQlcWallet!!.accountName
                    tvReceiveWalletAddess.text = receiveQlcWallet!!.address
                }
                AllWallet.WalletType.EthWallet.ordinal -> {
                    receiveEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                    tvReceiveWalletName.text = receiveEthWallet!!.name
                    tvReceiveWalletAddess.text = receiveEthWallet!!.address
                }
                AllWallet.WalletType.NeoWallet.ordinal -> {
                    receiveNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                    tvReceiveWalletName.text = receiveNeoWallet!!.name
                    tvReceiveWalletAddess.text = receiveNeoWallet!!.address
                }
                AllWallet.WalletType.EosWallet.ordinal -> {
                    receiveEosWallet = data!!.getParcelableExtra<EosAccount>("wallet")
                    tvReceiveWalletName.text = receiveEosWallet!!.accountName
                    tvReceiveWalletAddess.text = receiveEosWallet!!.accountName
                }
            }
        }
    }

    fun showEnterQlcWalletDialog() {
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
            if (AccountMng.isValidAddress(etContent.text.toString().trim())) {
                tvReceiveWalletName.text = etContent.text.toString().trim()
                tvReceiveWalletAddess.text = etContent.text.toString().trim()
            } else {
                toast(getString(R.string.illegal_receipt_address))
            }
            sweetAlertDialog.cancel()
        }
    }

    override fun setupActivityComponent() {
        DaggerBuyQgasComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .buyQgasModule(BuyQgasModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: BuyQgasContract.BuyQgasContractPresenter) {
        mPresenter = presenter as BuyQgasPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
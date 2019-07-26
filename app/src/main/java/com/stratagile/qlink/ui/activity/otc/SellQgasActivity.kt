package com.stratagile.qlink.ui.activity.otc

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
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
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.PopWindowUtil
import com.stratagile.qlink.utils.SpringAnimationUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import kotlinx.android.synthetic.main.activity_buy_qgas.*
import kotlinx.android.synthetic.main.activity_buy_qgas.walletMore
import kotlinx.android.synthetic.main.activity_sell_qgas.*
import kotlinx.android.synthetic.main.activity_sell_qgas.etQgas
import kotlinx.android.synthetic.main.activity_sell_qgas.etReceiveAddress
import kotlinx.android.synthetic.main.activity_sell_qgas.etUsdt
import kotlinx.android.synthetic.main.activity_sell_qgas.tvAmount
import kotlinx.android.synthetic.main.activity_sell_qgas.tvNext
import kotlinx.android.synthetic.main.activity_sell_qgas.tvQgasVolume
import kotlinx.android.synthetic.main.activity_sell_qgas.tvUnitPrice
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
        val qrEntity = QrEntity(entrustOrderInfo.order.qgasAddress, "QGAS" + " Receivable Address", "qgas", 4)
        val intent = Intent(this, UsdtReceiveAddressActivity::class.java)
        intent.putExtra("qrentity", qrEntity)
        startActivity(intent)
        finish()
    }

    override fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo) {
        closeProgressDialog()
        this.entrustOrderInfo = entrustOrderInfo
        maxQgas = entrustOrderInfo.order.totalAmount.toInt() - entrustOrderInfo.order.lockingAmount.toInt() - entrustOrderInfo.order.completeAmount.toInt()
        maxUsdt = BigDecimal.valueOf(orderList.unitPrice * maxQgas)
        etUsdt.hint = "Max " + maxUsdt.toPlainString()
        etQgas.hint = "Max " + maxQgas
        tvAmount.text = maxQgas.toString()
        if (maxQgas.toBigDecimal() < BigDecimal.valueOf(orderList.maxAmount)) {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + maxQgas.toString()
        } else {
            tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + " - " + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        }
    }

    @Inject
    internal lateinit var mPresenter: SellQgasPresenter
    lateinit var orderList : EntrustOrderList.OrderListBean
    lateinit var entrustOrderInfo :EntrustOrderInfo
    var qlcAccounList = mutableListOf<QLCAccount>()

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
        title.text = "SELL QGAS"
        orderList = intent.getParcelableExtra("order")
        tvUnitPrice.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        tvAmount.text= BigDecimal.valueOf(orderList.totalAmount).stripTrailingZeros().toPlainString()
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
        maxQgas = orderList.totalAmount.toInt()
        maxUsdt = BigDecimal.valueOf(orderList.unitPrice * maxQgas)
        etUsdt.hint = "Max " + maxUsdt.toPlainString()
        etQgas.hint = "Max " + maxQgas

        qlcAccounList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounList.size > 0) {
            qlcAccounList.forEach {
                if (it.isCurrent()) {
                    etSendAddress.text = it.address
                    return@forEach
                }
            }
        }
        sendWalletMore.setOnClickListener {
            showSendQGASWalletPopWindow()
        }

        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("entrustOrderId", orderList.id)
        mPresenter.getEntrustOrderDetail(map)

        tvNext.setOnClickListener {
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
            if ("".equals(etReceiveAddress)) {
                return@setOnClickListener
            }
            if (!ETHWalletUtils.isETHValidAddress(etReceiveAddress.text.toString())) {
                return@setOnClickListener
            }

            if ("".equals(etSendAddress.text.toString())) {
                toast("Please select a qlc wallet to payment")
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
                    toast("The smallest QGAS is " + entrustOrderInfo.order.minAmount.toInt())
                    return@setOnClickListener
                }
            }

            showProgressDialog()
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", AccountUtil.getUserToken())
            map.put("entrustOrderId", orderList.id)
            map.put("usdtAmount", etUsdt.text.toString())
            map["usdtToAddress"] = etReceiveAddress.text.toString()
            map.put("qgasAmount", etQgas.text.toString())
            mPresenter.sendQgas(etQgas.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map)

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
                            etQgas.setText((etUsdt.text.toString().toFloat() / entrustOrderInfo.order.unitPrice).toLong().toString())
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

        walletMore.setOnClickListener {
            showSpinnerPopWindow()
        }
    }

    private fun showSpinnerPopWindow() {
        var ethWalletList = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        if (ethWalletList.size > 0) {
            PopWindowUtil.showSharePopWindow(this, walletMore, ethWalletList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
                override fun onSelect(content: String) {
                    if ("" != content) {
                        etReceiveAddress.setText(content)
                    }
                    SpringAnimationUtil.endRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
                }
            })
            SpringAnimationUtil.startRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
        }
    }
    private fun showSendQGASWalletPopWindow() {
        if (qlcAccounList.size > 0) {
            PopWindowUtil.showSharePopWindow(this, sendWalletMore, qlcAccounList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
                override fun onSelect(content: String) {
                    if ("" != content) {
                        etSendAddress.setText(content)
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

                        if (qlcAccounList != null && qlcAccounList.size != 0) {
                            qlcAccounList.forEach {
                                if (it.isCurrent()) {
                                    it.setIsCurrent(false)
                                    AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                    return@forEach
                                }
                            }
                            qlcAccounList.forEach {
                                if (it.address.equals(content)) {
                                    it.setIsCurrent(true)
                                    AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                    EventBus.getDefault().post(ChangeCurrency())
                                    return@forEach
                                }
                            }
                        }
                    }
                    SpringAnimationUtil.endRotatoSpringViewAnimation(sendWalletMore) { animation, canceled, value, velocity -> }
                }
            })
            SpringAnimationUtil.startRotatoSpringViewAnimation(sendWalletMore) { animation, canceled, value, velocity -> }
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
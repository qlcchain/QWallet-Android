package com.stratagile.qlink.ui.activity.otc

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.entity.QrEntity
import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.ui.activity.otc.component.DaggerBuyQgasComponent
import com.stratagile.qlink.ui.activity.otc.contract.BuyQgasContract
import com.stratagile.qlink.ui.activity.otc.module.BuyQgasModule
import com.stratagile.qlink.ui.activity.otc.presenter.BuyQgasPresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import kotlinx.android.synthetic.main.activity_buy_qgas.*
import qlc.mng.AccountMng
import qlc.mng.WalletMng
import qlc.utils.Helper
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
        this.entrustOrderInfo = entrustOrderInfo
        maxQgas = entrustOrderInfo.order.totalAmount.toInt() - entrustOrderInfo.order.lockingAmount.toInt() - entrustOrderInfo.order.completeAmount.toInt()
        maxUsdt = BigDecimal.valueOf(orderList.unitPrice * maxQgas)
        etUsdt.hint = "Max " + maxUsdt.toPlainString()
        etQgas.hint = "Max " + maxQgas
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
    }

    override fun generateTradeBuyQgasOrderSuccess() {
        val qrEntity = QrEntity(entrustOrderInfo.order.usdtAddress, "USDT" + " Receivable Address", "usdt", 2)
        val intent = Intent(this, UsdtReceiveAddressActivity::class.java)
        intent.putExtra("qrentity", qrEntity)
        startActivity(intent)
        finish()
    }

    @Inject
    internal lateinit var mPresenter: BuyQgasPresenter

    lateinit var orderList: EntrustOrderList.OrderListBean
    lateinit var entrustOrderInfo: EntrustOrderInfo

    var maxUsdt = BigDecimal.ONE
    var maxQgas = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_buy_qgas)
    }

    override fun initData() {
        title.text = "BUY QGAS"
        orderList = intent.getParcelableExtra("order")
        tvUnitPrice.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        tvAmount.text = BigDecimal.valueOf(orderList.totalAmount).stripTrailingZeros().toPlainString()
        maxQgas = orderList.totalAmount.toInt()
        maxUsdt = BigDecimal.valueOf(orderList.unitPrice * maxQgas)
        etUsdt.hint = "Max " + maxUsdt.toPlainString()
        etQgas.hint = "Max " + maxQgas
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
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
            if (!AccountMng.isValidAddress(etReceiveAddress.text.toString())) {
                return@setOnClickListener
            }
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", AccountUtil.getUserToken())
            map.put("entrustOrderId", orderList.id)
            map.put("usdtAmount", etUsdt.text.toString())
            map.put("qgasAmount", etQgas.text.toString())
            map.put("qgasToAddress", etReceiveAddress.text.toString())
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
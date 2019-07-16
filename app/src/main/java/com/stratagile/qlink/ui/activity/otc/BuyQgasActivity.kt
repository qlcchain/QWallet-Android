package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.ui.activity.otc.component.DaggerBuyQgasComponent
import com.stratagile.qlink.ui.activity.otc.contract.BuyQgasContract
import com.stratagile.qlink.ui.activity.otc.module.BuyQgasModule
import com.stratagile.qlink.ui.activity.otc.presenter.BuyQgasPresenter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_buy_qgas.*
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
    }

    override fun generateTradeBuyQgasOrderSuccess() {
        finish()
    }

    @Inject
    internal lateinit var mPresenter: BuyQgasPresenter

    lateinit var orderList : EntrustOrderList.OrderListBean
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
            var map = hashMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", AccountUtil.getUserToken())
            map.put("entrustOrderId",orderList.id)
            map.put("usdtAmount",etUsdt.text.toString())
            map.put("qgasAmount",etQgas.text.toString())
            map.put("qgasToAddress",etReceiveAddress.text.toString())
            mPresenter.generateTradeBuyQgasOrder(map)
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
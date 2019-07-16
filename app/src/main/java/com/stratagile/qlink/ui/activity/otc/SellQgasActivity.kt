package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.otc.component.DaggerSellQgasComponent
import com.stratagile.qlink.ui.activity.otc.contract.SellQgasContract
import com.stratagile.qlink.ui.activity.otc.module.SellQgasModule
import com.stratagile.qlink.ui.activity.otc.presenter.SellQgasPresenter
import kotlinx.android.synthetic.main.activity_sell_qgas.*
import java.math.BigDecimal

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/09 14:18:11
 */

class SellQgasActivity : BaseActivity(), SellQgasContract.View {

    @Inject
    internal lateinit var mPresenter: SellQgasPresenter
    lateinit var orderList : EntrustOrderList.OrderListBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_sell_qgas)
    }
    override fun initData() {
        title.text = "SELL QGAS"
        orderList = intent.getParcelableExtra("order")
        tvAmount.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        tvAmount.text= BigDecimal.valueOf(orderList.totalAmount).stripTrailingZeros().toPlainString()
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
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
package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.otc.component.DaggerBuyQgasComponent
import com.stratagile.qlink.ui.activity.otc.contract.BuyQgasContract
import com.stratagile.qlink.ui.activity.otc.module.BuyQgasModule
import com.stratagile.qlink.ui.activity.otc.presenter.BuyQgasPresenter
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

    @Inject
    internal lateinit var mPresenter: BuyQgasPresenter

    lateinit var orderList : EntrustOrderList.OrderListBean

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
        tvAmount.text = BigDecimal.valueOf(orderList.unitPrice).stripTrailingZeros().toPlainString()
        tvAmount.text= BigDecimal.valueOf(orderList.totalAmount).stripTrailingZeros().toPlainString()
        tvQgasVolume.text = BigDecimal.valueOf(orderList.minAmount).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(orderList.maxAmount).stripTrailingZeros().toPlainString()
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
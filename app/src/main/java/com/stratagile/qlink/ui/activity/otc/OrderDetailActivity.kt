package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderDetailComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderDetailContract
import com.stratagile.qlink.ui.activity.otc.module.OrderDetailModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderDetailPresenter

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/10 10:03:30
 */

class OrderDetailActivity : BaseActivity(), OrderDetailContract.View {

    @Inject
    internal lateinit var mPresenter: OrderDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_order_detail)
    }
    override fun initData() {
        title.text = "Detail"
    }

    override fun setupActivityComponent() {
       DaggerOrderDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .orderDetailModule(OrderDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OrderDetailContract.OrderDetailContractPresenter) {
            mPresenter = presenter as OrderDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
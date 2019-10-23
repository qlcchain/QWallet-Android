package com.stratagile.qlink.ui.activity.topup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.ui.activity.topup.component.DaggerVoucherDetailComponent
import com.stratagile.qlink.ui.activity.topup.contract.VoucherDetailContract
import com.stratagile.qlink.ui.activity.topup.module.VoucherDetailModule
import com.stratagile.qlink.ui.activity.topup.presenter.VoucherDetailPresenter
import kotlinx.android.synthetic.main.activity_voucher_detail.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/10/17 17:29:37
 */

class VoucherDetailActivity : BaseActivity(), VoucherDetailContract.View {

    @Inject
    internal lateinit var mPresenter: VoucherDetailPresenter
    lateinit var orderBean : TopupOrderList.OrderListBean
    override fun onCreate(savedInstanceState: Bundle?) {
        drawableBg = R.drawable.main_bg_shape
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_voucher_detail)
    }
    override fun initData() {
        title.text = "凭证详情"
        orderBean = intent.getParcelableExtra("orderBean")
        payer.text = orderBean.phoneNumber
        payCount.text = orderBean.discountPrice.toBigDecimal().stripTrailingZeros().toPlainString() + "元+" + orderBean.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString() + "QGAS"
        time.text = orderBean.orderTime
        txid.text = orderBean.txid
        txid.setOnClickListener {
            val intent1 = Intent()
            intent1.action = "android.intent.action.VIEW"
            intent1.data = Uri.parse("https://explorer.qlcchain.org/transaction/" + orderBean.txid)
            startActivity(intent1)
        }
    }

    override fun setupActivityComponent() {
       DaggerVoucherDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .voucherDetailModule(VoucherDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: VoucherDetailContract.VoucherDetailContractPresenter) {
            mPresenter = presenter as VoucherDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
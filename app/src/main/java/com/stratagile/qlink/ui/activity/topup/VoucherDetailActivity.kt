package com.stratagile.qlink.ui.activity.topup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.ui.activity.topup.component.DaggerVoucherDetailComponent
import com.stratagile.qlink.ui.activity.topup.contract.VoucherDetailContract
import com.stratagile.qlink.ui.activity.topup.module.VoucherDetailModule
import com.stratagile.qlink.ui.activity.topup.presenter.VoucherDetailPresenter
import com.stratagile.qlink.utils.OtcUtils
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.TimeUtil
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
    lateinit var orderBean : TopupOrder.OrderBean
    override fun onCreate(savedInstanceState: Bundle?) {
        drawableBg = R.drawable.main_bg_shape
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_voucher_detail)
    }
    override fun initData() {
        title.text = getString(R.string.invoice_detail)
        orderBean = intent.getParcelableExtra("orderBean")
        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
            //英文
            payCount.text = "¥" + orderBean.discountPrice.toBigDecimal().stripTrailingZeros().toPlainString() + "+" + orderBean.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString() + orderBean.symbol
        } else {
            payCount.text = orderBean.discountPrice.toBigDecimal().stripTrailingZeros().toPlainString() + "元+" + orderBean.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString() + orderBean.symbol
        }

        orderNumber.text = orderBean.number.substring(8, orderBean.number.length)
        time.text = TimeUtil.timeConvert(orderBean.orderTime)
        orderDetail.text = getString(R.string.top_up_100phone_bill_18825709441, (orderBean.localFiatMoney.toString() + orderBean.localFiat), orderBean.phoneNumber)
        txid.text = orderBean.txid
        txid.setOnClickListener {
            OtcUtils.gotoBlockBrowser(this, orderBean.chain, orderBean.txid)
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
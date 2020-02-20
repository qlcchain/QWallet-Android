package com.stratagile.qlink.ui.activity.place

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.SmsReport
import com.stratagile.qlink.ui.activity.place.component.DaggerSmsVourchComponent
import com.stratagile.qlink.ui.activity.place.contract.SmsVourchContract
import com.stratagile.qlink.ui.activity.place.module.SmsVourchModule
import com.stratagile.qlink.ui.activity.place.presenter.SmsVourchPresenter
import com.stratagile.qlink.utils.OtcUtils
import kotlinx.android.synthetic.main.activity_sms_vourch.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: $description
 * @date 2020/02/20 22:53:22
 */

class SmsVourchActivity : BaseActivity(), SmsVourchContract.View {

    @Inject
    internal lateinit var mPresenter: SmsVourchPresenter

    lateinit var reportBean: SmsReport.ReportBean
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.color_44cbfe
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_sms_vourch)
    }
    override fun initData() {
        title.text = getString(R.string.invoice_detail)
        reportBean = intent.getParcelableExtra("report")
        payCount.text = reportBean.qgasAmount + "QGAS"
        time.text = reportBean.createDate
        orderNumber.text = reportBean.number
        invoiceContent.text = reportBean.sms
        txid.text = reportBean.qgasHash
        txid.setOnClickListener {
            OtcUtils.gotoBlockBrowser(this, "QLC_CHAIN", reportBean.qgasHash)
        }
    }

    override fun setupActivityComponent() {
       DaggerSmsVourchComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .smsVourchModule(SmsVourchModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SmsVourchContract.SmsVourchContractPresenter) {
            mPresenter = presenter as SmsVourchPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupSelectDeductionTokenComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupSelectDeductionTokenContract
import com.stratagile.qlink.ui.activity.topup.module.TopupSelectDeductionTokenModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupSelectDeductionTokenPresenter
import com.stratagile.qlink.ui.adapter.topup.SelectPayTokenAdapter
import kotlinx.android.synthetic.main.activity_topup_select_deduction_token.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2020/02/13 20:41:09
 */

class TopupSelectDeductionTokenActivity : BaseActivity(), TopupSelectDeductionTokenContract.View {

    @Inject
    internal lateinit var mPresenter: TopupSelectDeductionTokenPresenter
    lateinit var selectPayTokenAdapter: SelectPayTokenAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_select_deduction_token)
    }
    override fun initData() {
        mPresenter.getPayToken()
        title.text = getString(R.string.select_deduction_token)
        selectPayTokenAdapter = SelectPayTokenAdapter(arrayListOf())
        recyclerView.adapter = selectPayTokenAdapter
        selectPayTokenAdapter.setOnItemClickListener { adapter, view, position ->
            var resultIntent = Intent()
            resultIntent.putExtra("selectPayToken", selectPayTokenAdapter.data[position])
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun setupActivityComponent() {
       DaggerTopupSelectDeductionTokenComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .topupSelectDeductionTokenModule(TopupSelectDeductionTokenModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: TopupSelectDeductionTokenContract.TopupSelectDeductionTokenContractPresenter) {
            mPresenter = presenter as TopupSelectDeductionTokenPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    override fun setPayToken(payToken: PayToken) {
        selectPayTokenAdapter.setNewData(payToken.payTokenList)
    }

}
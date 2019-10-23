package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.otc.component.DaggerSelectCurrencyComponent
import com.stratagile.qlink.ui.activity.otc.contract.SelectCurrencyContract
import com.stratagile.qlink.ui.activity.otc.module.SelectCurrencyModule
import com.stratagile.qlink.ui.activity.otc.presenter.SelectCurrencyPresenter
import com.stratagile.qlink.ui.adapter.otc.SelectPairAdapter
import kotlinx.android.synthetic.main.activity_currency_unit.*
import java.util.ArrayList

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/08/19 15:22:57
 */

class SelectCurrencyActivity : BaseActivity(), SelectCurrencyContract.View {
    lateinit var selectPairAdapter: SelectPairAdapter
    override fun setRemoteTradePairs(pairsListBeans: ArrayList<TradePair.PairsListBean>) {
        selectPairAdapter = SelectPairAdapter(pairsListBeans)
        recyclerView.adapter = selectPairAdapter
        selectPairAdapter.setOnItemClickListener { adapter, view, position ->
            var resultIntent = Intent()
            resultIntent.putExtra("pair", selectPairAdapter.data[position])
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    @Inject
    internal lateinit var mPresenter: SelectCurrencyPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_currency_unit)
    }
    override fun initData() {
        mPresenter.getPairs()
        title.text = getString(R.string.select_currency)
    }

    override fun setupActivityComponent() {
       DaggerSelectCurrencyComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .selectCurrencyModule(SelectCurrencyModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SelectCurrencyContract.SelectCurrencyContractPresenter) {
            mPresenter = presenter as SelectCurrencyPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
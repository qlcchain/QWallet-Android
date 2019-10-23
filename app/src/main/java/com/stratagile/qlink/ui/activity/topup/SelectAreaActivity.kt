package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.LanguageCountry
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.ui.activity.topup.component.DaggerSelectAreaComponent
import com.stratagile.qlink.ui.activity.topup.contract.SelectAreaContract
import com.stratagile.qlink.ui.activity.topup.module.SelectAreaModule
import com.stratagile.qlink.ui.activity.topup.presenter.SelectAreaPresenter
import com.stratagile.qlink.ui.adapter.topup.MobileAreaAdapter
import com.stratagile.qlink.utils.FileUtil
import kotlinx.android.synthetic.main.activity_select_area.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/24 16:07:07
 */

class SelectAreaActivity : BaseActivity(), SelectAreaContract.View {

    @Inject
    internal lateinit var mPresenter: SelectAreaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_select_area)
    }
    override fun initData() {
        title.text = getString(R.string.choose_a_counntry_and_a_region)
        val area = Gson().fromJson(FileUtil.getJson(this, "area.json"), Area::class.java)
        var mobileAreaAdapter = MobileAreaAdapter(area.area)
        recyclerView.adapter = mobileAreaAdapter
        mobileAreaAdapter.setOnItemClickListener { adapter, view, position ->
            var resultIntent = Intent()
            resultIntent.putExtra("area", mobileAreaAdapter.data[position].number)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun setupActivityComponent() {
       DaggerSelectAreaComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .selectAreaModule(SelectAreaModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SelectAreaContract.SelectAreaContractPresenter) {
            mPresenter = presenter as SelectAreaPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
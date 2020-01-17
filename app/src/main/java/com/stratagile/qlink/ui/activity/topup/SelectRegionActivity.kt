package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.topup.IspList
import com.stratagile.qlink.ui.activity.topup.component.DaggerSelectRegionComponent
import com.stratagile.qlink.ui.activity.topup.contract.SelectRegionContract
import com.stratagile.qlink.ui.activity.topup.module.SelectRegionModule
import com.stratagile.qlink.ui.activity.topup.presenter.SelectRegionPresenter
import com.stratagile.qlink.ui.adapter.topup.IspListAdapter
import kotlinx.android.synthetic.main.activity_select_operator.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/12/25 14:50:07
 * 选择区域，充值的第三个选项
 */

class SelectRegionActivity : BaseActivity(), SelectRegionContract.View {

    @Inject
    internal lateinit var mPresenter: SelectRegionPresenter

    lateinit var ispListAdapter: IspListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_select_region)
        title.text = getString(R.string.region)
    }
    override fun initData() {
        ispListAdapter = IspListAdapter(arrayListOf())
        recyclerView.adapter = ispListAdapter
        ispListAdapter.setOnItemClickListener { adapter, view, position ->
            var resultIntent = Intent()
            resultIntent.putExtra("region", ispListAdapter.data[position])
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        provinceList()
    }

    fun provinceList() {
        var map = mutableMapOf<String, String>()
        map.put("globalRoaming", intent.getStringExtra("area"))
        map.put("isp", intent.getStringExtra("isp"))
        mPresenter.provinceList(map)
    }

    override fun setupActivityComponent() {
       DaggerSelectRegionComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .selectRegionModule(SelectRegionModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SelectRegionContract.SelectRegionContractPresenter) {
            mPresenter = presenter as SelectRegionPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    override fun setIsp(ispList: IspList) {
        ispListAdapter.setNewData(ispList.ispList)
    }


}
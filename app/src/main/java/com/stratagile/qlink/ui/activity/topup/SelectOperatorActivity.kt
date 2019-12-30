package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.topup.IspList
import com.stratagile.qlink.ui.activity.topup.component.DaggerSelectOperatorComponent
import com.stratagile.qlink.ui.activity.topup.contract.SelectOperatorContract
import com.stratagile.qlink.ui.activity.topup.module.SelectOperatorModule
import com.stratagile.qlink.ui.activity.topup.presenter.SelectOperatorPresenter
import com.stratagile.qlink.ui.adapter.topup.IspListAdapter
import kotlinx.android.synthetic.main.activity_select_operator.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/12/25 10:21:32
 */

class SelectOperatorActivity : BaseActivity(), SelectOperatorContract.View {

    @Inject
    internal lateinit var mPresenter: SelectOperatorPresenter

    lateinit var ispListAdapter: IspListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor  = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_select_operator)
        title.text = getString(R.string.operator)
    }
    override fun initData() {
        getOperator()
        ispListAdapter = IspListAdapter(arrayListOf())
        recyclerView.adapter = ispListAdapter
        ispListAdapter.setOnItemClickListener { adapter, view, position ->
            var resultIntent = Intent()
            resultIntent.putExtra("isp", ispListAdapter.data[position])
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    fun getOperator() {
        var map = mutableMapOf<String, String>()
        map.put("globalRoaming", intent.getStringExtra("area"))
        mPresenter.getIspList(map)
    }

    override fun setupActivityComponent() {
       DaggerSelectOperatorComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .selectOperatorModule(SelectOperatorModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: SelectOperatorContract.SelectOperatorContractPresenter) {
            mPresenter = presenter as SelectOperatorPresenter
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
package com.stratagile.qlink.ui.activity.recommend

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.recommend.component.DaggerGroupExplainComponent
import com.stratagile.qlink.ui.activity.recommend.contract.GroupExplainContract
import com.stratagile.qlink.ui.activity.recommend.module.GroupExplainModule
import com.stratagile.qlink.ui.activity.recommend.presenter.GroupExplainPresenter

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: $description
 * @date 2020/01/17 13:37:58
 */

class GroupExplainActivity : BaseActivity(), GroupExplainContract.View {

    @Inject
    internal lateinit var mPresenter: GroupExplainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_group_explain)
    }
    override fun initData() {
        title.text = getString(R.string.details)
    }

    override fun setupActivityComponent() {
       DaggerGroupExplainComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .groupExplainModule(GroupExplainModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: GroupExplainContract.GroupExplainContractPresenter) {
            mPresenter = presenter as GroupExplainPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
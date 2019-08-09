package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.stake.component.DaggerStakeExplainComponent
import com.stratagile.qlink.ui.activity.stake.contract.StakeExplainContract
import com.stratagile.qlink.ui.activity.stake.module.StakeExplainModule
import com.stratagile.qlink.ui.activity.stake.presenter.StakeExplainPresenter

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/09 15:25:43
 */

class StakeExplainActivity : BaseActivity(), StakeExplainContract.View {

    @Inject
    internal lateinit var mPresenter: StakeExplainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_stake_explain)
    }
    override fun initData() {
        title.text = getString(R.string.about_staking_types)
    }

    override fun setupActivityComponent() {
       DaggerStakeExplainComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .stakeExplainModule(StakeExplainModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: StakeExplainContract.StakeExplainContractPresenter) {
            mPresenter = presenter as StakeExplainPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
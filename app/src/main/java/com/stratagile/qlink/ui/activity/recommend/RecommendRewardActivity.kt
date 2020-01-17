package com.stratagile.qlink.ui.activity.recommend

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.recommend.component.DaggerRecommendRewardComponent
import com.stratagile.qlink.ui.activity.recommend.contract.RecommendRewardContract
import com.stratagile.qlink.ui.activity.recommend.module.RecommendRewardModule
import com.stratagile.qlink.ui.activity.recommend.presenter.RecommendRewardPresenter

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: $description
 * @date 2020/01/09 13:57:40
 */

class RecommendRewardActivity : BaseActivity(), RecommendRewardContract.View {

    @Inject
    internal lateinit var mPresenter: RecommendRewardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_recommend_reward)
    }
    override fun initData() {

    }

    override fun setupActivityComponent() {
       DaggerRecommendRewardComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .recommendRewardModule(RecommendRewardModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: RecommendRewardContract.RecommendRewardContractPresenter) {
            mPresenter = presenter as RecommendRewardPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
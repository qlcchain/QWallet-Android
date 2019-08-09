package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.stake.component.DaggerStakeDetailComponent
import com.stratagile.qlink.ui.activity.stake.contract.StakeDetailContract
import com.stratagile.qlink.ui.activity.stake.module.StakeDetailModule
import com.stratagile.qlink.ui.activity.stake.presenter.StakeDetailPresenter
import com.stratagile.qlink.utils.UIUtils

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/09 15:26:02
 */

class StakeDetailActivity : BaseActivity(), StakeDetailContract.View {

    @Inject
    internal lateinit var mPresenter: StakeDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_stake_detail)
    }
    override fun initData() {
        var drawable= getResources().getDrawable(R.mipmap.confidant_a)
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight())
        title.setCompoundDrawables(drawable,null,null,null)
        title.compoundDrawablePadding = UIUtils.dip2px(6f, this)
        title.text = "Voting/Mining Node"

    }

    override fun setupActivityComponent() {
       DaggerStakeDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .stakeDetailModule(StakeDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: StakeDetailContract.StakeDetailContractPresenter) {
            mPresenter = presenter as StakeDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
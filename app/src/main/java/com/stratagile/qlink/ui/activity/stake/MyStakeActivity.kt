package com.stratagile.qlink.ui.activity.stake

import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.stake.component.DaggerMyStakeComponent
import com.stratagile.qlink.ui.activity.stake.contract.MyStakeContract
import com.stratagile.qlink.ui.activity.stake.module.MyStakeModule
import com.stratagile.qlink.ui.activity.stake.presenter.MyStakePresenter
import kotlinx.android.synthetic.main.activity_my_stake.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 15:32:14
 */

class MyStakeActivity : BaseActivity(), MyStakeContract.View {

    @Inject
    internal lateinit var mPresenter: MyStakePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        drawableBg = R.drawable.main_bg_shape
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_my_stake)
    }
    override fun initData() {
        title.text = getString(R.string.my_stakeings)
        newStaking.setOnClickListener {
            startActivity(Intent(this, NewStakeActivity::class.java))
        }
        tvStakeVol.setOnClickListener {
            startActivity(Intent(this, StakeDetailActivity::class.java))
        }
    }

    override fun setupActivityComponent() {
       DaggerMyStakeComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .myStakeModule(MyStakeModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: MyStakeContract.MyStakeContractPresenter) {
            mPresenter = presenter as MyStakePresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
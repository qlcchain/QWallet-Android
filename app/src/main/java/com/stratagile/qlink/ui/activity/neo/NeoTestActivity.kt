package com.stratagile.qlink.ui.activity.neo

import android.os.Bundle

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.neo.component.DaggerNeoTestComponent
import com.stratagile.qlink.ui.activity.neo.contract.NeoTestContract
import com.stratagile.qlink.ui.activity.neo.module.NeoTestModule
import com.stratagile.qlink.ui.activity.neo.presenter.NeoTestPresenter

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.neo
 * @Description: $description
 * @date 2019/06/10 10:30:03
 */

class NeoTestActivity : BaseActivity(), NeoTestContract.View {

    @Inject
    internal lateinit var mPresenter: NeoTestPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
//        setContentView(R.layout.activity_neoTest)
    }
    override fun initData() {

    }

    override fun setupActivityComponent() {
       DaggerNeoTestComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .neoTestModule(NeoTestModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: NeoTestContract.NeoTestContractPresenter) {
            mPresenter = presenter as NeoTestPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
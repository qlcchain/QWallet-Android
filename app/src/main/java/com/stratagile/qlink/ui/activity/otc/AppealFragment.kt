package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerAppealComponent
import com.stratagile.qlink.ui.activity.otc.contract.AppealContract
import com.stratagile.qlink.ui.activity.otc.module.AppealModule
import com.stratagile.qlink.ui.activity.otc.presenter.AppealPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/16 17:53:24
 */

class AppealFragment : BaseFragment(), AppealContract.View {

    @Inject
    lateinit internal var mPresenter: AppealPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_appeal, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }


    override fun setupFragmentComponent() {
        DaggerAppealComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .appealModule(AppealModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: AppealContract.AppealContractPresenter) {
        mPresenter = presenter as AppealPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
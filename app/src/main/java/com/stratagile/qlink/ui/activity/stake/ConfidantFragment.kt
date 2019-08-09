package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.stake.component.DaggerConfidantComponent
import com.stratagile.qlink.ui.activity.stake.contract.ConfidantContract
import com.stratagile.qlink.ui.activity.stake.module.ConfidantModule
import com.stratagile.qlink.ui.activity.stake.presenter.ConfidantPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 16:38:01
 */

class ConfidantFragment : BaseFragment(), ConfidantContract.View {

    @Inject
    lateinit internal var mPresenter: ConfidantPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_confidant, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }



    override fun setupFragmentComponent() {
        DaggerConfidantComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .confidantModule(ConfidantModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: ConfidantContract.ConfidantContractPresenter) {
        mPresenter = presenter as ConfidantPresenter
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
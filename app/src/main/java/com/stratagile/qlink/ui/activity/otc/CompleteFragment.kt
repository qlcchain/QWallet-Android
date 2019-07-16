package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerCompleteComponent
import com.stratagile.qlink.ui.activity.otc.contract.CompleteContract
import com.stratagile.qlink.ui.activity.otc.module.CompleteModule
import com.stratagile.qlink.ui.activity.otc.presenter.CompletePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/16 17:53:03
 */

class CompleteFragment : BaseFragment(), CompleteContract.View {

    @Inject
    lateinit internal var mPresenter: CompletePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_complete, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }


    override fun setupFragmentComponent() {
        DaggerCompleteComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .completeModule(CompleteModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: CompleteContract.CompleteContractPresenter) {
        mPresenter = presenter as CompletePresenter
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
package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerCloasedOrderComponent
import com.stratagile.qlink.ui.activity.otc.contract.CloasedOrderContract
import com.stratagile.qlink.ui.activity.otc.module.CloasedOrderModule
import com.stratagile.qlink.ui.activity.otc.presenter.CloasedOrderPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/17 14:39:46
 */

class CloasedOrderFragment : BaseFragment(), CloasedOrderContract.View {

    @Inject
    lateinit internal var mPresenter: CloasedOrderPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_process, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }



    override fun setupFragmentComponent() {
        DaggerCloasedOrderComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .cloasedOrderModule(CloasedOrderModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: CloasedOrderContract.CloasedOrderContractPresenter) {
        mPresenter = presenter as CloasedOrderPresenter
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
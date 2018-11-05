package com.stratagile.qlink.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.test.component.DaggerTestComponent
import com.stratagile.qlink.ui.activity.test.contract.TestContract
import com.stratagile.qlink.ui.activity.test.module.TestModule
import com.stratagile.qlink.ui.activity.test.presenter.TestPresenter

import javax.inject.Inject

import butterknife.ButterKnife

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.test
 * @Description: $description
 * @date 2018/09/10 16:51:54
 */

class TestFragment : BaseFragment(), TestContract.View {

    @Inject
    lateinit internal var mPresenter: TestPresenter

    //   @Nullable
    //   @Override
    //   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //       View view = inflater.inflate(R.layout.fragment_test, null);
    //       ButterKnife.bind(this, view);
    //       Bundle mBundle = getArguments();
    //       return view;
    //   }


    override fun setupFragmentComponent() {
        DaggerTestComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .testModule(TestModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TestContract.TestContractPresenter) {
        mPresenter = presenter as TestPresenter
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
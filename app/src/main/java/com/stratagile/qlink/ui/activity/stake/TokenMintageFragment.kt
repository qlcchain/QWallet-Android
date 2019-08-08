package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.stake.component.DaggerTokenMintageComponent
import com.stratagile.qlink.ui.activity.stake.contract.TokenMintageContract
import com.stratagile.qlink.ui.activity.stake.module.TokenMintageModule
import com.stratagile.qlink.ui.activity.stake.presenter.TokenMintagePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 16:38:20
 */

class TokenMintageFragment : BaseFragment(), TokenMintageContract.View {

    @Inject
    lateinit internal var mPresenter: TokenMintagePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_token_mintage, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }



    override fun setupFragmentComponent() {
        DaggerTokenMintageComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .tokenMintageModule(TokenMintageModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TokenMintageContract.TokenMintageContractPresenter) {
        mPresenter = presenter as TokenMintagePresenter
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
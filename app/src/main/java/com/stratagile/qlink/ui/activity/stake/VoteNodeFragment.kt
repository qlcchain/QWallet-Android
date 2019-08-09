package com.stratagile.qlink.ui.activity.stake

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.stake.component.DaggerVoteNodeComponent
import com.stratagile.qlink.ui.activity.stake.contract.VoteNodeContract
import com.stratagile.qlink.ui.activity.stake.module.VoteNodeModule
import com.stratagile.qlink.ui.activity.stake.presenter.VoteNodePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 16:37:41
 */

class VoteNodeFragment : BaseFragment(), VoteNodeContract.View {

    @Inject
    lateinit internal var mPresenter: VoteNodePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vote_node, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }



    override fun setupFragmentComponent() {
        DaggerVoteNodeComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .voteNodeModule(VoteNodeModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: VoteNodeContract.VoteNodeContractPresenter) {
        mPresenter = presenter as VoteNodePresenter
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
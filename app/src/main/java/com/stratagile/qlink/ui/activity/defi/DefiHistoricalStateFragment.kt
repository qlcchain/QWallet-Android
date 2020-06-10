package com.stratagile.qlink.ui.activity.defi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiHistoricalStateComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiHistoricalStateContract
import com.stratagile.qlink.ui.activity.defi.module.DefiHistoricalStateModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiHistoricalStatePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.defi.DefiDetail
import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.ui.adapter.defi.DefiTokenHistoryListAdapter
import kotlinx.android.synthetic.main.fragment_defi_historcal_state.*
import java.util.ArrayList

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/06/02 10:26:24
 */

class DefiHistoricalStateFragment : BaseFragment(), DefiHistoricalStateContract.View {

    @Inject
    lateinit internal var mPresenter: DefiHistoricalStatePresenter
    lateinit var viewModel: DefiViewModel
    lateinit var mDefiDetail : DefiDetail
    lateinit var defiTokenHistoryListAdapter: DefiTokenHistoryListAdapter
    var currentPage = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_defi_historcal_state, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(DefiViewModel::class.java)
        viewModel.defiDetailLiveData.observe(this, Observer {
            mDefiDetail = it!!
        })
        defiTokenHistoryListAdapter = DefiTokenHistoryListAdapter(arrayListOf())
        recyclerView.adapter = defiTokenHistoryListAdapter
        defiTokenHistoryListAdapter.setEnableLoadMore(true)
        defiTokenHistoryListAdapter.setOnLoadMoreListener({getDefiStateList()}, recyclerView)
        refreshLayout.setOnRefreshListener { initDataFromNet() }
    }

    override fun initDataFromNet() {
        currentPage = 0
        getDefiStateList()
    }

    fun getDefiStateList() {
        currentPage++
        var infoMap = hashMapOf<String, String>()
        infoMap["projectId"] =mDefiDetail.project.id
        infoMap["page"] = "1"
        infoMap["size"] = "30"
        mPresenter.getDefiStateList(infoMap, currentPage)
    }


    override fun setupFragmentComponent() {
        DaggerDefiHistoricalStateComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .defiHistoricalStateModule(DefiHistoricalStateModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DefiHistoricalStateContract.DefiHistoricalStateContractPresenter) {
        mPresenter = presenter as DefiHistoricalStatePresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    override fun setDefiStateListData(defiStateList: DefiStateList, currentPage: Int) {
        refreshLayout.isRefreshing = false
        if (currentPage == 1) {
            defiTokenHistoryListAdapter.setNewData(ArrayList())
        }
        defiTokenHistoryListAdapter.addData(defiStateList.historicalStatsList)
        if (currentPage != 1) {
            defiTokenHistoryListAdapter.loadMoreComplete()
        }
        if (defiStateList.historicalStatsList.size == 0) {
            defiTokenHistoryListAdapter.loadMoreEnd(true)
        }
    }
}
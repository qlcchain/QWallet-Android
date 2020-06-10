package com.stratagile.qlink.ui.activity.otc

import android.content.Intent
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
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.otc.TradeOrderList
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.otc.TradeOrderListAdapter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.fragment_posted.*
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/17 14:39:46
 */

class ClosedOrderFragment : BaseFragment(), CloasedOrderContract.View {

    @Inject
    lateinit internal var mPresenter: CloasedOrderPresenter

    lateinit var tradeOrderListAdapter: TradeOrderListAdapter
    var currentPage = 0

    override fun setTradeOrderList(tradeOrderList: TradeOrderList) {
        tradeOrderListAdapter.addData(tradeOrderList.orderList)
        if (currentPage != 1) {
            tradeOrderListAdapter.loadMoreComplete()
        }
        if (tradeOrderList.orderList.size == 0) {
            tradeOrderListAdapter.loadMoreEnd(true)
        }
    }

    override fun initDataFromNet() {
        currentPage = 0
        getTradeOrderList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_process, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tradeOrderListAdapter = TradeOrderListAdapter(arrayListOf())
        tradeOrderListAdapter.setEmptyView(R.layout.empty_layout, refreshLayout)
        recyclerView.adapter = tradeOrderListAdapter
        tradeOrderListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(activity, TradeOrderDetailActivity::class.java).putExtra("tradeOrderId", tradeOrderListAdapter.data[position].id))
        }
        tradeOrderListAdapter.setOnLoadMoreListener({
            getTradeOrderList()
        }, recyclerView)
        recyclerView.addItemDecoration(BottomMarginItemDecoration(resources.getDimension(R.dimen.x20).toInt()))
        refreshLayout.setOnRefreshListener {
            currentPage = 0
            refreshLayout.isRefreshing = false
            tradeOrderListAdapter.setNewData(arrayListOf())
            getTradeOrderList()
        }
    }

    fun getTradeOrderList() {
        currentPage++
        val map = HashMap<String, String>()
        if (ConstantValue.currentUser == null) {
            return
        }
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["page"] = currentPage.toString() + ""
        map["status"] = "closed"
        map["size"] = "10"
        map["entrustOrderId"] = ""
        mPresenter.getTradeOrderList(map)
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
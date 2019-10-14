package com.stratagile.qlink.ui.activity.otc

import android.content.Intent
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
import com.stratagile.qlink.blockchain.chain.PackedTransaction
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.otc.TradeOrderList
import com.stratagile.qlink.ui.activity.otc.component.DaggerProcessComponent
import com.stratagile.qlink.ui.activity.otc.contract.ProcessContract
import com.stratagile.qlink.ui.activity.otc.module.ProcessModule
import com.stratagile.qlink.ui.activity.otc.presenter.ProcessPresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.otc.TradeOrderListAdapter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.fragment_posted.*
import kotlinx.android.synthetic.main.fragment_process.*
import kotlinx.android.synthetic.main.fragment_process.recyclerView
import kotlinx.android.synthetic.main.fragment_process.refreshLayout
import java.util.HashMap

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
        val view = inflater.inflate(R.layout.fragment_process, null)
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

    override fun setTradeOrderList(tradeOrderList: TradeOrderList) {
        tradeOrderListAdapter.addData(tradeOrderList.orderList)
        if (currentPage != 1) {
            tradeOrderListAdapter.loadMoreComplete()
        }
        if (tradeOrderList.orderList.size == 0) {
            tradeOrderListAdapter.loadMoreEnd(true)
        }
    }

    lateinit var tradeOrderListAdapter: TradeOrderListAdapter
    var currentPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tradeOrderListAdapter = TradeOrderListAdapter(arrayListOf())
        recyclerView.adapter = tradeOrderListAdapter
        tradeOrderListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(activity, TradeOrderDetailActivity::class.java).putExtra("tradeOrderId", tradeOrderListAdapter.data[position].id))
        }
        tradeOrderListAdapter.setOnLoadMoreListener({
            getTradeOrderList()
        }, recyclerView)
        recyclerView.addItemDecoration(BottomMarginItemDecoration(resources.getDimension(R.dimen.x20).toInt()))
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            currentPage = 0
            tradeOrderListAdapter.setNewData(arrayListOf())
            getTradeOrderList()
        }
    }

    override fun initDataFromNet() {
        super.initDataFromNet()
        getTradeOrderList()
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
        map["status"] = "completed"
        map["size"] = "10"
        map["entrustOrderId"] = ""
        mPresenter.getTradeOrderList(map)
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
package com.stratagile.qlink.ui.activity.otc

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerTradeListComponent
import com.stratagile.qlink.ui.activity.otc.contract.TradeListContract
import com.stratagile.qlink.ui.activity.otc.module.TradeListModule
import com.stratagile.qlink.ui.activity.otc.presenter.TradeListPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.chad.library.adapter.base.BaseQuickAdapter
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.UserAccount
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.main.MainViewModel
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.otc.EntrustOrderListAdapter
import com.stratagile.qlink.utils.KotlinConvertJavaUtils
import kotlinx.android.synthetic.main.fragment_trade_list.*
import java.util.ArrayList

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/08/19 09:38:46
 */

class TradeListFragment : BaseFragment(), TradeListContract.View {

    companion object {
        fun getInstance(title: String): TradeListFragment {
            val fragment1 = TradeListFragment()
            val bundle = Bundle()
            bundle.putString("tradeToken", title)
            fragment1.setArguments(bundle)
            return fragment1
        }
    }

    @Inject
    lateinit internal var mPresenter: TradeListPresenter
    lateinit internal var viewModel: MainViewModel
    lateinit var entrustOrderListAdapter: EntrustOrderListAdapter

    var currentOrderType = ConstantValue.orderTypeSell
    var currentPage = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trade_list, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KLog.i("------>>>>重建fragment。。。" + arguments!!["tradeToken"])
        entrustOrderListAdapter = EntrustOrderListAdapter(ArrayList())
        entrustOrderListAdapter.setEnableLoadMore(true)
        recyclerView.addItemDecoration(BottomMarginItemDecoration(activity!!.resources.getDimension(R.dimen.x20).toInt()))
        recyclerView.setAdapter(entrustOrderListAdapter)
        currentOrderType = ConstantValue.orderTypeSell
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.currentEntrustOrderType.observe(this, Observer<String> { s ->
            if (isVisibleToUser) {
                currentPage = 0
                KLog.i("------>>>>" + arguments!!["tradeToken"])
                currentOrderType = s!!
                entrustOrderListAdapter.setNewData(ArrayList())
                getOrderList()
            }
        })
        entrustOrderListAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            if (ConstantValue.currentUser == null) {
                startActivity(Intent(activity, AccountActivity::class.java))
                return@OnItemClickListener
            }
            if ("KYC_SUCCESS" != ConstantValue.currentUser.vstatus) {
                KotlinConvertJavaUtils.needVerify(activity!!)
                return@OnItemClickListener
            }
            if (entrustOrderListAdapter.data[position].type.equals(ConstantValue.orderTypeSell)) {
                startActivityForResult(Intent(activity, BuyQgasActivity::class.java).putExtra("order", entrustOrderListAdapter.data[position]), 0)
            } else {
                startActivityForResult(Intent(activity, SellQgasActivity::class.java).putExtra("order", entrustOrderListAdapter.data[position]), 0)
            }
        })

        viewModel.timeStampLiveData.observe(this, Observer {
            if (isVisibleToUser) {
                currentPage = 0
                KLog.i("------>>>>" + arguments!!["tradeToken"])
                refreshLayout.isRefreshing = true
                entrustOrderListAdapter.setNewData(ArrayList())
                getOrderList()
            }
        })

        entrustOrderListAdapter.setOnLoadMoreListener({ getOrderList() }, recyclerView)

        viewModel.currentUserAccount.observe(this, Observer<UserAccount> {
            if (isVisibleToUser) {
                KLog.i("------>>>>" + arguments!!["tradeToken"])
                currentPage = 0
                getOrderList()
            }
        })

        refreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                KLog.i("------>>>>" + arguments!!["tradeToken"])
                refreshLayout.isRefreshing = true
                currentPage = 0
                entrustOrderListAdapter.setNewData(ArrayList())
                getOrderList()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentPage = 0
        KLog.i("------>>>>" + arguments!!["tradeToken"])
        entrustOrderListAdapter.setNewData(ArrayList())
        getOrderList()
    }

    fun getOrderList() {
        currentPage++
        refreshLayout.setRefreshing(false)
        var map = HashMap<String, String>()
        map.put("userId", "");
        map.put("type", currentOrderType)
        map.put("page", currentPage.toString())
        map.put("size", "5")
        var pairsId = arrayListOf<String>()
        var pairsIds = ""
        viewModel.pairsLiveData.value!!.forEach {
            if (it.tradeToken.equals(arguments!!["tradeToken"]) && it.isSelect) {
                pairsId.add(it.id)
                pairsIds += it.id + ","
            }
        }
        map["pairsId"] = pairsIds.substring(0, pairsIds.length - 1)
        map.put("status", "NORMAL")
        mPresenter.getOrderList(map, currentPage)
    }

    override fun initDataFromNet() {
        super.initDataFromNet()
        KLog.i("可见。。。" + arguments!!["tradeToken"])
        currentPage = 0
        getOrderList()
    }

    override fun setEntrustOrderList(list: ArrayList<EntrustOrderList.OrderListBean>, currentPage1: Int) {
        if (currentPage1 == 1) {
            entrustOrderListAdapter.setNewData(ArrayList())
        }
        entrustOrderListAdapter.addData(list)
        if (currentPage1 != 1) {
            entrustOrderListAdapter.loadMoreComplete()
        }
        if (list.size == 0) {
            entrustOrderListAdapter.loadMoreEnd(true)
        }
    }

    override fun getEutrustOrderError() {
        entrustOrderListAdapter.loadMoreEnd(true)
    }

    override fun setupFragmentComponent() {
        DaggerTradeListComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .tradeListModule(TradeListModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TradeListContract.TradeListContractPresenter) {
        mPresenter = presenter as TradeListPresenter
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
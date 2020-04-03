package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerPostedComponent
import com.stratagile.qlink.ui.activity.otc.contract.PostedContract
import com.stratagile.qlink.ui.activity.otc.module.PostedModule
import com.stratagile.qlink.ui.activity.otc.presenter.PostedPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.otc.EntrustPostedOrderListAdapter
import kotlinx.android.synthetic.main.fragment_posted.*
import java.util.ArrayList
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/16 17:52:28
 */

class PostedFragment : BaseFragment(), PostedContract.View {

    @Inject
    lateinit internal var mPresenter: PostedPresenter
    lateinit var entrustOrderListAdapter: EntrustPostedOrderListAdapter
    var currentPage = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_posted, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun initDataFromNet() {
        super.initDataFromNet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ConstantValue.currentUser == null) {
            return
        }
        entrustOrderListAdapter = EntrustPostedOrderListAdapter(ArrayList())
        entrustOrderListAdapter.setEnableLoadMore(true)
        entrustOrderListAdapter.setEmptyView(R.layout.empty_layout, refreshLayout)
        recyclerView.adapter = entrustOrderListAdapter
        recyclerView.addItemDecoration(BottomMarginItemDecoration(activity!!.resources.getDimension(R.dimen.x20).toInt()))
        entrustOrderListAdapter.setOnItemClickListener { adapter, view, position ->
            startActivityForResult(Intent(activity, OrderDetailActivity::class.java).putExtra("order", entrustOrderListAdapter.data[position]).putExtra("position", position), 0)
        }
        entrustOrderListAdapter.setOnLoadMoreListener({
            getOrderList()
        }, recyclerView)
        currentPage = 0
        getOrderList()
        refreshLayout.setRefreshing(false)
        refreshLayout.setOnRefreshListener {
            currentPage = 0
            entrustOrderListAdapter.setNewData(ArrayList())
            refreshLayout.isRefreshing = false
            getOrderList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            entrustOrderListAdapter.data[data!!.getIntExtra("position", 0)].status = "CANCEL"
            entrustOrderListAdapter.notifyItemChanged(data!!.getIntExtra("position", 0))
        }
    }

    fun getOrderList() {
        currentPage++
        val map = HashMap<String, String>()
        if (ConstantValue.currentUser == null) {
            return
        }
        map["userId"] = ConstantValue.currentUser.userId
        map["type"] = ""
        map["page"] = currentPage.toString() + ""
        map["size"] = "10"
        mPresenter.getOrderList(map)
    }

    override fun setEntrustOrderList(list: ArrayList<EntrustOrderList.OrderListBean>) {
        entrustOrderListAdapter.addData(list)
        if (currentPage != 1) {
            entrustOrderListAdapter.loadMoreComplete()
        }
        if (list.size == 0) {
            entrustOrderListAdapter.loadMoreEnd(true)
        }
    }


    override fun setupFragmentComponent() {
        DaggerPostedComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .postedModule(PostedModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: PostedContract.PostedContractPresenter) {
        mPresenter = presenter as PostedPresenter
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
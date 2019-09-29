package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupOrderListComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupOrderListContract
import com.stratagile.qlink.ui.activity.topup.module.TopupOrderListModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupOrderListPresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.topup.TopupOrderListAdapter
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.activity_topup_order_list.*
import java.util.ArrayList

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/26 15:00:15
 */

class TopupOrderListActivity : BaseActivity(), TopupOrderListContract.View {

    override fun setOrderList(topupOrderList: TopupOrderList, page: Int) {
        if (page == 1) {
            topupOrderListAdapter.setNewData(ArrayList())
        }
        topupOrderListAdapter.addData(topupOrderList.orderList)
        if (page != 1) {
            topupOrderListAdapter.loadMoreComplete()
        }
        if (topupOrderList.orderList.size == 0) {
            topupOrderListAdapter.loadMoreEnd(true)
        }
    }

    var currentPage = 1

    @Inject
    internal lateinit var mPresenter: TopupOrderListPresenter
    lateinit var topupOrderListAdapter: TopupOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_order_list)
    }

    override fun initData() {
        title.text = getString(R.string.order_list)
        topupOrderListAdapter = TopupOrderListAdapter(ArrayList())
        topupOrderListAdapter.setEnableLoadMore(true)
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, this)))
        recyclerView.adapter = topupOrderListAdapter
        currentPage = 1
        var map = hashMapOf<String, String>()
        if (ConstantValue.currentUser != null) {
            map["account"] = ConstantValue.currentUser.account
        }
        map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
        map["page"] = currentPage.toString()
        map["size"] = "20"
        mPresenter.getOderList(map, currentPage)

        topupOrderListAdapter.setOnLoadMoreListener({
            currentPage++
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["account"] = ConstantValue.currentUser.account
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)

        }, recyclerView)
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            refreshLayout.isRefreshing = false
            topupOrderListAdapter.setNewData(ArrayList())
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["account"] = ConstantValue.currentUser.account
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)
        }
        topupOrderListAdapter.setOnItemClickListener { adapter, view, position ->
            if ("QGAS_PAID".equals(topupOrderListAdapter.data[position].status)) {
                var url = "https://shop.huagaotx.cn/wap/charge_v3.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrderListAdapter.data[position].userId}_${topupOrderListAdapter.data[position].id}&package=${topupOrderListAdapter.data[position].originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}&mobile=${topupOrderListAdapter.data[position].phoneNumber}"
//                var url = "https://shop.huagaotx.cn/wap/charge_v3.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrderListAdapter.data[position].userId}_${topupOrderListAdapter.data[position].id}&package=0&mobile=${topupOrderListAdapter.data[position].phoneNumber}"
                KLog.i(url)
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(intent)

                paymentOk = false
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", getString(R.string.payment))
                startActivityForResult(intent, 1)
            }
        }
    }

    var paymentOk = false;

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            paymentOk = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (paymentOk) {
            currentPage = 1
            refreshLayout.isRefreshing = false
            topupOrderListAdapter.setNewData(ArrayList())
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["account"] = ConstantValue.currentUser.account
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)
        }
    }

    override fun setupActivityComponent() {
        DaggerTopupOrderListComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupOrderListModule(TopupOrderListModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupOrderListContract.TopupOrderListContractPresenter) {
        mPresenter = presenter as TopupOrderListPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
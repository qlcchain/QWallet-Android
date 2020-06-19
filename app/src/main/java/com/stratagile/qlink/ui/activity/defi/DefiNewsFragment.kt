package com.stratagile.qlink.ui.activity.defi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.entity.defi.DefiNewsList
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiNewsComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiNewsContract
import com.stratagile.qlink.ui.activity.defi.module.DefiNewsModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiNewsPresenter
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.adapter.FirstTopMarginItemDecoration
import com.stratagile.qlink.ui.adapter.defi.DefNewsiListAdapter
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.fragment_defi_list.*
import kotlinx.android.synthetic.main.fragment_defi_news.*
import kotlinx.android.synthetic.main.fragment_defi_news.recyclerView
import kotlinx.android.synthetic.main.fragment_defi_news.refreshLayout
import java.util.*
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/05/25 17:10:21
 */

class DefiNewsFragment : BaseFragment(), DefiNewsContract.View {

    @Inject
    lateinit internal var mPresenter: DefiNewsPresenter
    lateinit var defiNewsiListAdapter: DefNewsiListAdapter
    var currentPage = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_defi_news, null)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defiNewsiListAdapter = DefNewsiListAdapter(arrayListOf())
        recyclerView.adapter = defiNewsiListAdapter
        recyclerView.addItemDecoration(FirstTopMarginItemDecoration(UIUtils.dip2px(12f, activity)))
        defiNewsiListAdapter.setEmptyView(R.layout.empty_layout, recyclerView)
        refreshLayout.setOnRefreshListener {
            getNews()
        }
        UIUtils.configSwipeRefreshLayoutColors(refreshLayout)
        defiNewsiListAdapter.setEnableLoadMore(true)
        defiNewsiListAdapter.setOnLoadMoreListener({getNews()}, recyclerView)
        defiNewsiListAdapter.setOnItemClickListener { adapter, view, position ->
            var intent = Intent(activity, DefiNewsDetailActivity::class.java)
            intent.putExtra("newsId", defiNewsiListAdapter.data[position].id)
            startActivity(intent)
        }
    }

    override fun initDataFromNet() {
        currentPage = 0
        getNews()
    }


    fun getNews() {
        currentPage++
        refreshLayout.setRefreshing(false)
        var infoMap = hashMapOf<String, String>()
        infoMap["page"] = currentPage.toString()
        infoMap["size"] = "20"
        mPresenter.getDefiNews(infoMap, currentPage)
    }

    override fun setDefiNews(defiNewsList: DefiNewsList, currentPage: Int) {
        if (currentPage == 1) {
            defiNewsiListAdapter.setNewData(ArrayList())
        }
        defiNewsiListAdapter.addData(defiNewsList.newsList)
        if (currentPage != 1) {
            defiNewsiListAdapter.loadMoreComplete()
        }
        if (defiNewsList.newsList.size == 0) {
            defiNewsiListAdapter.loadMoreEnd(true)
        }
    }


    override fun setupFragmentComponent() {
        DaggerDefiNewsComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .defiNewsModule(DefiNewsModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: DefiNewsContract.DefiNewsContractPresenter) {
        mPresenter = presenter as DefiNewsPresenter
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
package com.stratagile.qlink.ui.activity.mining

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.mining.MiningRewardList
import com.stratagile.qlink.ui.activity.mining.component.DaggerMiningDailyDetailComponent
import com.stratagile.qlink.ui.activity.mining.contract.MiningDailyDetailContract
import com.stratagile.qlink.ui.activity.mining.module.MiningDailyDetailModule
import com.stratagile.qlink.ui.activity.mining.presenter.MiningDailyDetailPresenter
import com.stratagile.qlink.ui.adapter.mining.MiningRewardListAdapter
import com.stratagile.qlink.utils.UserUtils
import kotlinx.android.synthetic.main.activity_mining_daily_detail.*
import kotlinx.android.synthetic.main.fragment_order_buy.*

import javax.inject.Inject;
import kotlin.math.min

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: $description
 * @date 2019/11/14 18:13:10
 */

class MiningDailyDetailActivity : BaseActivity(), MiningDailyDetailContract.View {

    override fun setRewardList(miningRewardList: MiningRewardList, page: Int) {
        refreshLayout.isRefreshing = false
        if (page == 1) {
            miningRewardListAdapter.setNewData(miningRewardList.list)
            if (miningRewardList.list.size == 0) {
                showEmpty()
            }
        } else {
            miningRewardListAdapter.addData(miningRewardList.list)
        }
        if (miningRewardList.list.size != 0) {
            miningRewardListAdapter.loadMoreComplete()
        }
        if (miningRewardList.list.size == 0) {
            miningRewardListAdapter.loadMoreEnd(true)
        }
    }

    @Inject
    internal lateinit var mPresenter: MiningDailyDetailPresenter
    lateinit var miningRewardListAdapter: MiningRewardListAdapter

    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_mining_daily_detail)
    }
    override fun initData() {
        title.text = getString(R.string.minning_rewards_details_title)
        getMiningDetail()
        miningRewardListAdapter = MiningRewardListAdapter(arrayListOf())
        recyclerView.adapter = miningRewardListAdapter
        miningRewardListAdapter.setEnableLoadMore(true)
        miningRewardListAdapter.setOnLoadMoreListener({
            currentPage++
            getMiningDetail()
        }, recyclerView)
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            getMiningDetail()
        }
    }

    fun getMiningDetail() {
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
        map["page"] = currentPage.toString()
        map["size"] = "10"
        map["tradeMiningId"] = ""
//        map["status"] = "AWARDED"
//        map["status"] = "NO_AWARD"
        mPresenter.getMiningRewardList(map, currentPage)
    }

    fun showEmpty() {
        val view1 = layoutInflater.inflate(R.layout.mining_empty_layout, null, false)
        var tvNewOrder = view1.findViewById<TextView>(R.id.tvNewOrder)
        tvNewOrder.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
        miningRewardListAdapter.setEmptyView(view1)
    }

    override fun setupActivityComponent() {
       DaggerMiningDailyDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .miningDailyDetailModule(MiningDailyDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: MiningDailyDetailContract.MiningDailyDetailContractPresenter) {
            mPresenter = presenter as MiningDailyDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
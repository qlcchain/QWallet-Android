package com.stratagile.qlink.ui.activity.stake

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v4.view.LayoutInflaterFactory
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import com.alibaba.fastjson.JSONArray
import com.google.gson.Gson
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.stake.MyStakeList
import com.stratagile.qlink.ui.activity.stake.component.DaggerMyStakeComponent
import com.stratagile.qlink.ui.activity.stake.contract.MyStakeContract
import com.stratagile.qlink.ui.activity.stake.module.MyStakeModule
import com.stratagile.qlink.ui.activity.stake.presenter.MyStakePresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.stake.MyStakeAdapter
import com.stratagile.qlink.utils.LogUtil
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper
import kotlinx.android.synthetic.main.activity_my_stake.*
import kotlinx.android.synthetic.main.activity_my_stake.recyclerView
import kotlinx.android.synthetic.main.activity_my_stake.refreshLayout
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.fragment_trade_list.*
import qlc.network.QlcClient
import java.math.BigDecimal

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 15:32:14
 */

class MyStakeActivity : BaseActivity(), MyStakeContract.View {

    @Inject
    internal lateinit var mPresenter: MyStakePresenter
    lateinit var qlcWallet: QLCAccount
    lateinit var myStakeAdapter: MyStakeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        drawableBg = R.drawable.main_bg_shape
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), LayoutInflaterFactory { parent, name, context, attrs ->
            if (name == "com.android.internal.view.menu.IconMenuItemView" || name == "com.android.internal.view.menu.ActionMenuItemView" || name == "android.support.v7.view.menu.ActionMenuItemView") {
                try {
                    val view = layoutInflater.createView(name, null, attrs)
                    if (view is TextView) {
                        view.setTextColor(resources.getColor(R.color.white))
                        view.isAllCaps = false
                    }
                    return@LayoutInflaterFactory view
                } catch (e: InflateException) {
                    e.printStackTrace()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }

            }
            null
        })
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_my_stake)
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.txid, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enterTxid -> {
                startActivityForResult(Intent(this, NewStakeActivity::class.java).putExtra("txid", "txid"), 1)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
        title.text = getString(R.string.my_stakeings)
        var hasWallet = false
        var wallets = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        wallets.forEach {
            if (it.isCurrent()) {
                qlcWallet = it
                hasWallet = true
            }
        }
        if (!hasWallet) {
            qlcWallet = wallets[0]
        }
        newStaking.setOnClickListener {
            startActivityForResult(Intent(this, NewStakeActivity::class.java), 1)
        }
        myStakeAdapter = MyStakeAdapter(arrayListOf())
        myStakeAdapter.setEnableLoadMore(true)
        refreshLayout.setOnRefreshListener {

            thread {
                try {
                    val client = QlcClient("https://nep5.qlcchain.online")
                    val params = JSONArray()
                    params.add(qlcWallet.address)
                    params.add(10)
                    params.add(0)
                    KLog.i(params)
                    var result = client.call("ledger_pledgeInfoByBeneficial", params)
                    KLog.i(result.toString())
                    var myStakeList = Gson().fromJson(result.toString(), MyStakeList::class.java)
                    runOnUiThread {
                        refreshLayout.isRefreshing = false
                        myStakeAdapter.setNewData(myStakeList.result)
                        setStakedQlcAmount()
                    }
                } catch (e: java.lang.Exception) {

                }
            }
        }
        myStakeAdapter.setOnLoadMoreListener({ getMoreStake() }, recyclerView)
        recyclerView.adapter = myStakeAdapter
        recyclerView.addItemDecoration(BottomMarginItemDecoration(resources.getDimension(R.dimen.x20).toInt()))

        thread {
            try {
                refreshLayout.isRefreshing = true
                val client = QlcClient("https://nep5.qlcchain.online")
                val params = JSONArray()
                params.add(qlcWallet.address)
                params.add(10)
                params.add(0)
                KLog.i(params)
                var result = client.call("ledger_pledgeInfoByBeneficial", params)
                KLog.i(result.toString())
                var myStakeList = Gson().fromJson(result.toString(), MyStakeList::class.java)
                runOnUiThread {
                    refreshLayout.isRefreshing = false
                    myStakeAdapter.setNewData(myStakeList.result)
                    setStakedQlcAmount()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        myStakeAdapter.setOnItemClickListener { adapter, view, position ->
            startActivityForResult(Intent(this, StakeDetailActivity::class.java).putExtra("stake", myStakeAdapter.data[position]), 1)
        }
    }

    fun setStakedQlcAmount() {
        var stakedQlc = 0L
        var earnQgas = 0L
        myStakeAdapter.data.forEach {
            if ("PledgeDone".equals(it.state) || "WithdrawStart".equals(it.state) || "WithdrawProcess".equals(it.state)) {
                stakedQlc += it.amount.toLong()
            }
            earnQgas += it.qgas
        }
        tvStakeVol.text = stakedQlc.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        tvStakedQlc.text = stakedQlc.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        tvEarnQgas.text = earnQgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    fun getMoreStake() {
        try {
            thread {
                val client = QlcClient("https://nep5.qlcchain.online")
                val params = JSONArray()
                params.add(qlcWallet.address)
                params.add(10)
                params.add(myStakeAdapter.data.size)
                KLog.i(params)
                var result = client.call("ledger_pledgeInfoByBeneficial", params)
                KLog.i(result.toString())
                var myStakeList = Gson().fromJson(result.toString(), MyStakeList::class.java)
                runOnUiThread {
                    myStakeAdapter.addData(myStakeList.result)
                    if (myStakeList.result.size != 0) {
                        myStakeAdapter.loadMoreComplete()
                    }
                    if (myStakeList.result.size == 0) {
                        myStakeAdapter.loadMoreEnd(true)
                    }
                    setStakedQlcAmount()
                }
            }
        } catch (ex: java.lang.Exception) {

        }
    }

    override fun setupActivityComponent() {
        DaggerMyStakeComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .myStakeModule(MyStakeModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: MyStakeContract.MyStakeContractPresenter) {
        mPresenter = presenter as MyStakePresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
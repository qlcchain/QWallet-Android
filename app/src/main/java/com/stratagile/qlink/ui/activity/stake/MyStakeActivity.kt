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
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.reward.RewardTotal
import com.stratagile.qlink.entity.stake.MyStakeList
import com.stratagile.qlink.ui.activity.reward.MyClaimActivity
import com.stratagile.qlink.ui.activity.stake.component.DaggerMyStakeComponent
import com.stratagile.qlink.ui.activity.stake.contract.MyStakeContract
import com.stratagile.qlink.ui.activity.stake.module.MyStakeModule
import com.stratagile.qlink.ui.activity.stake.presenter.MyStakePresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.stake.MyStakeAdapter
import com.stratagile.qlink.utils.AccountUtil
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
    override fun setClaimedTotal(rewardTotal1: RewardTotal) {
        rewardTotal = rewardTotal1.rewardTotal.toBigDecimal()
        tvFreeEarnQgas.text = rewardTotal.stripTrailingZeros().toPlainString()
        setStakedQlcAmount()
    }

    override fun setRewardQlcAmount(dict: Dict) {
        rewardQlcAmount = dict.data.value.toBigDecimal()
        tvFreeStakedQlc.text = rewardQlcAmount.stripTrailingZeros().toPlainString()
        getClaimedTotal()
    }

    @Inject
    internal lateinit var mPresenter: MyStakePresenter
    lateinit var qlcWallet: QLCAccount
    lateinit var myStakeAdapter: MyStakeAdapter
    var rewardTotal = BigDecimal.ZERO
    var rewardQlcAmount = BigDecimal.ZERO


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
        getRewardQlcAmount()
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
        if (ConstantValue.currentUser != null && !"".equals(ConstantValue.currentUser.bindDate) ) {
            llQlcLendFree.visibility = View.VISIBLE
            llQlcLendFree.setOnClickListener {
                startActivity(Intent(this, MyClaimActivity::class.java))
            }
        } else {
            llQlcLendFree.visibility = View.GONE
        }
    }

    fun getRewardQlcAmount() {
        var map = hashMapOf<String, String>()
        map["dictType"] = "winq_reward_qlc_amount"
        mPresenter.rewardQlcAmount(map)
    }

    fun getClaimedTotal() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["type"] = "REGISTER"
        map["status"] = "SUCCESS"
        mPresenter.getClaimedTotal(map)
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
        tvStakeVol.text = (stakedQlc.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP) + rewardQlcAmount).stripTrailingZeros().toPlainString()
        tvStakedQlc.text = stakedQlc.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        tvEarnQgas.text = (earnQgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP)).stripTrailingZeros().toPlainString()

        tvTotalEarnVol.text = (earnQgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP) + rewardTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
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
package com.stratagile.qlink.ui.activity.reward

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.reward.RewardList
import com.stratagile.qlink.entity.reward.RewardTotal
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.reward.component.DaggerMyClaimComponent
import com.stratagile.qlink.ui.activity.reward.contract.MyClaimContract
import com.stratagile.qlink.ui.activity.reward.module.MyClaimModule
import com.stratagile.qlink.ui.activity.reward.presenter.MyClaimPresenter
import com.stratagile.qlink.ui.adapter.reward.RewardListAdapter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_my_claim.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.claim.MyClaimActivity
 * @Description: $description
 * @date 2019/10/09 11:57:31
 */

class MyClaimActivity : BaseActivity(), MyClaimContract.View {
    override fun setRewardList(rewardList: RewardList, page: Int) {
        refreshLayout.isRefreshing = false
        if (page == 1) {
            rewardListAdapter.setNewData(rewardList.rewardList)
        } else {
            rewardListAdapter.addData(rewardList.rewardList)
        }
    }

    override fun setCanClaimTotal(rewardTotal: RewardTotal) {
        tvCanClaimQlc.text = rewardTotal.rewardTotal.toString()
        getClaimedTotal()
    }

    override fun setClaimedTotal(rewardTotal: RewardTotal) {
        tvClaimedQlc.text = rewardTotal.rewardTotal.toString()
        currentPage = 1
        getRewardList()
    }

    override fun setRewardQlcAmount(dict: Dict) {
        tvStakeVol.text = dict.data.value
        getCanClaimTotal()
    }

    @Inject
    internal lateinit var mPresenter: MyClaimPresenter

    lateinit var rewardListAdapter: RewardListAdapter
    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        drawableBg = R.drawable.main_bg_shape
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_my_claim)
    }
    override fun initData() {
        if (ConstantValue.currentUser == null) {
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
        }
        title.text = getString(R.string.earning_detail)
        rewardListAdapter = RewardListAdapter(arrayListOf())
        recyclerView.adapter = rewardListAdapter
        getRewardQlcAmount()
        llClaimQlc.setOnClickListener {
            if ("0".equals(tvCanClaimQlc.text.toString()) || "0.0".equals(tvCanClaimQlc.text.toString())) {
                toast(getString(R.string.no_qgas_can_claim))
                return@setOnClickListener
            }
            startActivityForResult(Intent(this, ClaimRewardActivity::class.java).putExtra("total", tvCanClaimQlc.text.toString()).putExtra("claimType", "register"), 1)
        }
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            getCanClaimTotal()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getCanClaimTotal()
        }
    }

    fun getRewardQlcAmount() {
        var map = hashMapOf<String, String>()
        map["dictType"] = "winq_reward_qlc_amount"
        mPresenter.rewardQlcAmount(map)
    }

    fun getCanClaimTotal() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["type"] = "REGISTER"
        map["status"] = "NEW"
        mPresenter.getCanClaimTotal(map)
    }

    fun getClaimedTotal() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["type"] = "REGISTER"
        map["status"] = "SUCCESS"
        mPresenter.getClaimedTotal(map)
    }

    fun getRewardList() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["page"] = currentPage.toString()
        map["size"] = "10"
        map["type"] = "REGISTER"
        map["status"] = "NEW"
        mPresenter.getRewardList(map, currentPage)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.claim_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.claimQlc -> {
                var qlcIntent = Intent(this, WebViewActivity::class.java)
                qlcIntent.putExtra("url", "https://explorer.qlcchain.org")
                qlcIntent.putExtra("title", "QLCChain")
                startActivity(qlcIntent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupActivityComponent() {
       DaggerMyClaimComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .myClaimModule(MyClaimModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: MyClaimContract.MyClaimContractPresenter) {
            mPresenter = presenter as MyClaimPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
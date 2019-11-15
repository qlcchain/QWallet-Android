package com.stratagile.qlink.ui.activity.mining

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.eventbus.ChangeViewpager
import com.stratagile.qlink.entity.mining.MiningIndex
import com.stratagile.qlink.entity.mining.MiningRank
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.mining.component.DaggerMiningInviteComponent
import com.stratagile.qlink.ui.activity.mining.contract.MiningInviteContract
import com.stratagile.qlink.ui.activity.mining.module.MiningInviteModule
import com.stratagile.qlink.ui.activity.mining.presenter.MiningInvitePresenter
import com.stratagile.qlink.ui.activity.reward.ClaimRewardActivity
import com.stratagile.qlink.ui.adapter.mining.MiningRankAdapter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_mining_invite.*
import kotlinx.android.synthetic.main.activity_mining_invite.recyclerView
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: $description
 * @date 2019/11/14 09:43:06
 */

class MiningInviteActivity : BaseActivity(), MiningInviteContract.View {
    override fun setRewardRank(miningRank: MiningIndex) {
        val miningRankAdapter = MiningRankAdapter(miningRank.rewardRankings)
        recyclerView.adapter = miningRankAdapter
        canClaimCount = miningRank.noAwardTotal.toBigDecimal().stripTrailingZeros().toPlainString()
        tvCanClaimQGAS.text = miningRank.noAwardTotal.toBigDecimal().stripTrailingZeros().toPlainString()
        tvClaimedQgas.text = getString(R.string.i_have_claimed_qlc, miningRank.awardedTotal.toBigDecimal().stripTrailingZeros().toPlainString())
        tvInviteCount.text = getString(R.string._3000_qgas_traded, miningRank.totalFinish.toBigDecimal().stripTrailingZeros().toPlainString())
        tvActQlcAmount.text = miningRank.totalRewardAmount.toBigDecimal().stripTrailingZeros().toPlainString() + " QLC"
    }

    @Inject
    internal lateinit var mPresenter: MiningInvitePresenter

    var canClaimCount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_mining_invite)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        miningExplain.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", "https://medium.com/@QLC_team/qgas-buyback-campaign-kicking-off-in-q-wallet-otc-join-to-trade-qgas-for-qlc-rewards-39c7241e72d4")
            intent.putExtra("title", "QLC")
            startActivity(intent)
        }

    }

    override fun initData() {
        title.text = getString(R.string.sheet_minig)
        tvRewardDetail.setOnClickListener {
            startActivityForResult(Intent(this, MiningDailyDetailActivity::class.java), 2)
        }
        tvTradeNow.setOnClickListener {
            EventBus.getDefault().post(ChangeViewpager(1))
            finish()
        }
        getMiningRank()
        tvClaimNow.setOnClickListener {
            if (!"".equals(canClaimCount) || canClaimCount.toBigDecimal() > BigDecimal.ZERO) {
                val claimIntent = Intent(this, MiningRewardActivity::class.java)
                claimIntent.putExtra("total", canClaimCount)
                claimIntent.putExtra("claimType", "invite")
                startActivityForResult(claimIntent, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getMiningRank()
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            EventBus.getDefault().post(ChangeViewpager(1))
            finish()
        }
    }

    override fun setupActivityComponent() {
        DaggerMiningInviteComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .miningInviteModule(MiningInviteModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: MiningInviteContract.MiningInviteContractPresenter) {
        mPresenter = presenter as MiningInvitePresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    fun getMiningRank() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        mPresenter.getMiningRewardList(map)
    }

}
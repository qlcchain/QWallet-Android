package com.stratagile.qlink.ui.activity.mining

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.reward.ClaimQgas
import com.stratagile.qlink.ui.activity.mining.component.DaggerMiningRewardComponent
import com.stratagile.qlink.ui.activity.mining.contract.MiningRewardContract
import com.stratagile.qlink.ui.activity.mining.module.MiningRewardModule
import com.stratagile.qlink.ui.activity.mining.presenter.MiningRewardPresenter
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_claim_reward.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: $description
 * @date 2019/11/15 15:49:47
 */

class MiningRewardActivity : BaseActivity(), MiningRewardContract.View {
    override fun claimQlcBack(claimQgas: ClaimQgas) {
        if ("0".equals(claimQgas.code)) {
            toast(getString(R.string.success))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    @Inject
    internal lateinit var mPresenter: MiningRewardPresenter

    var receiveQlcWallet : Wallet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_mining_reward)
    }
    override fun initData() {
        title.text = getString(R.string.claim)
        tvClaim.text = getString(R.string.claim)
        tvClaimQgasCount.text = intent.getStringExtra("total") + " QLC"
        llSelectReceiveWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, 1)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvClaim.setOnClickListener {
            if (receiveQlcWallet == null) {
                return@setOnClickListener
            }
            claimQlc()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            receiveQlcWallet = data!!.getParcelableExtra<Wallet>("wallet")
            tvReceiveWalletName.text = receiveQlcWallet!!.name
            tvReceiveWalletAddess.text = receiveQlcWallet!!.address
        }
    }

    fun claimQlc() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveQlcWallet!!.address
        mPresenter.claimQlc(map)
    }

    override fun setupActivityComponent() {
       DaggerMiningRewardComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .miningRewardModule(MiningRewardModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: MiningRewardContract.MiningRewardContractPresenter) {
            mPresenter = presenter as MiningRewardPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.reward

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.VCodeVerifyCode
import com.stratagile.qlink.entity.reward.ClaimQgas
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.reward.component.DaggerClaimRewardComponent
import com.stratagile.qlink.ui.activity.reward.contract.ClaimRewardContract
import com.stratagile.qlink.ui.activity.reward.module.ClaimRewardModule
import com.stratagile.qlink.ui.activity.reward.presenter.ClaimRewardPresenter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_claim_reward.*
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.reward
 * @Description: $description
 * @date 2019/10/10 15:28:24
 */

class ClaimRewardActivity : BaseActivity(), ClaimRewardContract.View {
    override fun claimQgasBack(claimQgas: ClaimQgas) {
        if ("0".equals(claimQgas.code)) {
            toast(getString(R.string.success))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    var isInvite = false

    @Inject
    internal lateinit var mPresenter: ClaimRewardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_claim_reward)
    }
    override fun initData() {
        if (ConstantValue.currentUser == null) {
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
        }
        isInvite = "invite".equals(intent.getStringExtra("claimType"))
        if (isInvite) {
            title.text = getString(R.string.get_now)
            tvClaim.text = getString(R.string.get_now)
        } else {
            title.text = getString(R.string.claim)
            tvClaim.text = getString(R.string.claim)
        }
        tvClaimQgasCount.text = intent.getStringExtra("total") + " QGAS"
        llSelectReceiveWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, 1)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvClaim.setOnClickListener {
            if (receiveQlcWallet == null) {
                return@setOnClickListener
            }
            if ("".equals(etVcode.text.toString())) {
                return@setOnClickListener
            }
            if (isInvite) {
                claiminviteQgas()
            } else {
                claimQgas()
            }
        }
        ivVcode.setOnClickListener {
            getVcode()
        }
        getVcode()
    }

    fun getVcode() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        if (isInvite) {
            map["type"] = "CLAIM_INVITE"
        } else {
            map["type"] = "CLAIM_BIND"
        }
        mPresenter.getVCode(map)
    }

    override fun setInviteCode(vCodeVerifyCode: VCodeVerifyCode) {
        Glide.with(this)
                .load(vCodeVerifyCode.codeUrl)
                .into(ivVcode)
    }

    fun claimQgas() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveQlcWallet!!.address
        map["code"] = etVcode.text.toString().trim()
        mPresenter.claimQgas(map)
    }

    fun claiminviteQgas() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveQlcWallet!!.address
        map["code"] = etVcode.text.toString().trim()
        mPresenter.claiminviteQgas(map)
    }

    var receiveQlcWallet : QLCAccount? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            receiveQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
            tvReceiveWalletName.text = receiveQlcWallet!!.accountName
            tvReceiveWalletAddess.text = receiveQlcWallet!!.address
        }
    }

    override fun setupActivityComponent() {
       DaggerClaimRewardComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .claimRewardModule(ClaimRewardModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: ClaimRewardContract.ClaimRewardContractPresenter) {
            mPresenter = presenter as ClaimRewardPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
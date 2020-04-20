package com.stratagile.qlink.ui.activity.my

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import com.bumptech.glide.Glide
import com.pawegio.kandroid.runDelayedOnUiThread
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.VCodeVerifyCode
import com.stratagile.qlink.ui.activity.my.component.DaggerEpidemicClaimComponent
import com.stratagile.qlink.ui.activity.my.contract.EpidemicClaimContract
import com.stratagile.qlink.ui.activity.my.module.EpidemicClaimModule
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicClaimPresenter
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.FireBaseUtils
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepService
import com.today.step.net.ClaimQgas
import com.today.step.net.EpidemicList
import kotlinx.android.synthetic.main.activity_claim_reward.*
import kotlinx.android.synthetic.main.activity_epidemic_claim.*
import kotlinx.android.synthetic.main.activity_epidemic_claim.etVcode
import kotlinx.android.synthetic.main.activity_epidemic_claim.ivVcode
import kotlinx.android.synthetic.main.activity_epidemic_claim.llSelectReceiveWallet
import kotlinx.android.synthetic.main.activity_epidemic_claim.tvClaim
import kotlinx.android.synthetic.main.activity_epidemic_claim.tvClaimQgasCount
import kotlinx.android.synthetic.main.activity_epidemic_claim.tvReceiveWalletAddess
import kotlinx.android.synthetic.main.activity_epidemic_claim.tvReceiveWalletName
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/04/15 17:22:27
 */

class EpidemicClaimActivity : BaseActivity(), EpidemicClaimContract.View {

    @Inject
    internal lateinit var mPresenter: EpidemicClaimPresenter
    lateinit var epidemicBean : EpidemicList.RecordListBean

    private var iSportStepInterface: ISportStepInterface? = null
    lateinit var serviceConnection : ServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_epidemic_claim)
    }
    override fun initData() {
        title.text = getString(R.string.claim)

        EventBus.getDefault().register(this)
        epidemicBean = intent.getParcelableExtra("bean")

        //开启计步Service，同时绑定Activity进行aidl通信
        val intent = Intent(this, TodayStepService::class.java)
        startService(intent)
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                runDelayedOnUiThread(300) {
                    iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        tvClaimQgasCount.text = epidemicBean.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString() + " QGAS"
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
            claimQgas()
        }
        ivVcode.setOnClickListener {
            getVcode()
        }
        getVcode()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handlerError(throwable: Throwable) {
        closeProgressDialog()
        toast(throwable.message!!)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClaimBack(claimQgas: ClaimQgas) {
        closeProgressDialog()
        toast(getString(R.string.claim_success))
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    fun claimQgas() {
        FireBaseUtils.logEvent(this, FireBaseUtils.Campaign_Covid19_QGas_Claim)
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["toAddress"] = receiveQlcWallet!!.address
        map["code"] = etVcode.text.toString().trim()
        iSportStepInterface!!.claimQgas(ConstantValue.currentUser.account, AccountUtil.getUserToken(), etVcode.text.toString().trim(), epidemicBean.id, receiveQlcWallet!!.address)
    }

    fun getVcode() {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["type"] = "CLAIM_COVID"
        mPresenter.getVCode(map)
    }

    override fun setInviteCode(vCodeVerifyCode: VCodeVerifyCode) {
        Glide.with(this)
                .load(vCodeVerifyCode.codeUrl)
                .into(ivVcode)
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
       DaggerEpidemicClaimComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .epidemicClaimModule(EpidemicClaimModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: EpidemicClaimContract.EpidemicClaimContractPresenter) {
            mPresenter = presenter as EpidemicClaimPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
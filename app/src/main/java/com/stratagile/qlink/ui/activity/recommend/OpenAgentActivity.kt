package com.stratagile.qlink.ui.activity.recommend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.UserInfo
import com.stratagile.qlink.entity.eventbus.ChangeWallet
import com.stratagile.qlink.entity.qlc.AddressStakeAmount
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.recommend.component.DaggerOpenAgentComponent
import com.stratagile.qlink.ui.activity.recommend.contract.OpenAgentContract
import com.stratagile.qlink.ui.activity.recommend.module.OpenAgentModule
import com.stratagile.qlink.ui.activity.recommend.presenter.OpenAgentPresenter
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import com.stratagile.qlink.utils.QlcReceiveUtils.drivePrivateKey
import com.stratagile.qlink.utils.QlcReceiveUtils.drivePublicKey
import com.stratagile.qlink.utils.UserUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_agency_excellence.*
import kotlinx.android.synthetic.main.activity_open_agent.*
import kotlinx.android.synthetic.main.activity_open_agent.ivAvatar
import kotlinx.android.synthetic.main.activity_open_agent.tvKaitong
import kotlinx.android.synthetic.main.activity_open_agent.tvNickName
import org.greenrobot.eventbus.EventBus
import qlc.mng.WalletMng
import qlc.network.QlcClient
import qlc.utils.Helper
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: $description
 * @date 2020/01/09 13:59:03
 */

class OpenAgentActivity : BaseActivity(), OpenAgentContract.View {

    @Inject
    internal lateinit var mPresenter: OpenAgentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_open_agent)
        title.text = getString(R.string.open_agent_qualification)
        tvNickName.text = ConstantValue.currentUser.account
        if (ConstantValue.currentUser.avatar != null && "" != ConstantValue.currentUser.avatar) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.avatar)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar)
        } else {
            Glide.with(this)
                    .load(R.mipmap.icon_user_default)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar)
        }
    }
    override fun initData() {
        if (ConstantValue.currentUser.qlcAddress != null && !"".equals(ConstantValue.currentUser.qlcAddress)) {
            getStakedQlcAmount1()
//            tvKaitong.text = getString(R.string.already_opened)
        } else {
            tvKaitong.text = getString(R.string.not_opened)
        }
        llSelectNeoWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, 1)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvVerification.setOnClickListener {
            if (sendQlcWallet == null) {
                toast(getString(R.string.please_select_a_wallet))
                return@setOnClickListener
            }
            bindQlcChainAddress()
        }
    }

    fun getStakedQlcAmount1() {
        showProgressDialog()
        thread {
            val qlcClient = QlcClient(ConstantValue.qlcNode)
            val addressArr = JSONArray()
            addressArr.add(ConstantValue.currentUser.qlcAddress)
            val json: JSONObject = qlcClient.call("pledge_getBeneficialPledgeInfosByAddress", addressArr)
            KLog.i(json)
            val addressStakeAmount = Gson().fromJson(json.toJSONString(), AddressStakeAmount::class.java)
            runOnUiThread {
                closeProgressDialog()
                if (addressStakeAmount.result.totalAmounts >= 1500*100000000.toLong()) {
                    tvKaitong.text = getString(R.string.already_opened)
                } else {
                    tvKaitong.text = getString(R.string.not_opened)
                }
            }
        }
    }

    fun getStakedQlcAmount() {
        showProgressDialog()
        thread {
            val qlcClient = QlcClient(ConstantValue.qlcNode)
            val addressArr = JSONArray()
            addressArr.add(sendQlcWallet!!.address)
            val json: JSONObject = qlcClient.call("pledge_getBeneficialPledgeInfosByAddress", addressArr)
            KLog.i(json)
            val addressStakeAmount = Gson().fromJson(json.toJSONString(), AddressStakeAmount::class.java)
            runOnUiThread {
                closeProgressDialog()
                if (addressStakeAmount.result.totalAmounts >= 1500*100000000.toLong()) {
                    tvKaitong.text = getString(R.string.already_opened)
                    showGotAgentDialog()
                } else {
                    showNoEnoughQlcDialog()
                }
                getUserInfo()
            }
        }
    }

    fun showGotAgentDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.op_success))
        tvContent.text = getString(R.string.you_are_a_recharge_agent_partner_now)
        val sweetAlertDialog = SweetAlertDialog(this)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvOk = view.findViewById<TextView>(R.id.tvOpreate)
        tvOk.text = getString(R.string.got_it_stake)
        tvOk.setOnClickListener {
            sweetAlertDialog.cancel()
            finish()
        }
        ivClose.setOnClickListener { sweetAlertDialog.cancel() }
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
    }

    fun showNoEnoughQlcDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.icon_no_stake_enogh_qlc))
        tvContent.text = getString(R.string.you_havenot_staked_1500_qlc_in_this_wallet)
        val sweetAlertDialog = SweetAlertDialog(this)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvOk = view.findViewById<TextView>(R.id.tvOpreate)
        tvOk.text = getString(R.string.stake_now)
        tvOk.setOnClickListener {
            startActivity(Intent(this@OpenAgentActivity, MyStakeActivity::class.java))
            sweetAlertDialog.cancel()
        }
        ivClose.setOnClickListener { sweetAlertDialog.cancel() }
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
    }

    fun testSign() {
        var hash = "123".toByteArray()
        var signature = WalletMng.sign(hash, Helper.hexStringToBytes(drivePrivateKey(sendQlcWallet!!.address).substring(0, 64)))
        val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(drivePublicKey(sendQlcWallet!!.address)))
        KLog.i(signCheck)
        KLog.i(Helper.byteToHexString(signature))
    }

    fun getSign() : String{
        var hash = sendQlcWallet!!.address.toByteArray()
        var signature = WalletMng.sign(hash, Helper.hexStringToBytes(drivePrivateKey(sendQlcWallet!!.address).substring(0, 64)))
        val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(drivePublicKey(sendQlcWallet!!.address)))
        KLog.i(signCheck)
        return Helper.byteToHexString(signature)
    }

    fun bindQlcChainAddress() {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))

        map["signature"] = getSign()
        map["address"] = sendQlcWallet!!.address
        mPresenter.bindQlcChainAddress(map)
    }

    var sendQlcWallet : QLCAccount? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            sendQlcWallet = data!!.getParcelableExtra("wallet")
            tvNeoWalletName.text = sendQlcWallet!!.accountName
            tvNeoWalletAddess.text = sendQlcWallet!!.address
            var list = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
            list.forEach {
                it.setIsCurrent(false)
                AppConfig.instance.daoSession.qlcAccountDao.update(it)
            }
            sendQlcWallet!!.setCurrent(true)
            AppConfig.instance.daoSession.qlcAccountDao.update(sendQlcWallet)
            EventBus.getDefault().post(ChangeWallet())
        }
    }

    override fun setupActivityComponent() {
       DaggerOpenAgentComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .openAgentModule(OpenAgentModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OpenAgentContract.OpenAgentContractPresenter) {
            mPresenter = presenter as OpenAgentPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    fun getUserInfo() {
//        showProgressDialog()
        var map = hashMapOf<String, String>()
        map.put("account", ConstantValue.currentUser.account)
        map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
        mPresenter.getUserInfo(map)
    }

    override fun bindAddressSuccess() {
        getStakedQlcAmount()
    }

    override fun setUsrInfo(userInfo: UserInfo) {
        closeProgressDialog()
        ConstantValue.currentUser.qlcAddress = userInfo.qlcAddress
        AppConfig.instance.daoSession.userAccountDao.update(ConstantValue.currentUser)
        setResult(Activity.RESULT_OK)
//        finish()
    }

}
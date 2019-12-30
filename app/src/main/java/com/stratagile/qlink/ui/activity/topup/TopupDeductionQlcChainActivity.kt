package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.SwitchToOtc
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupDeductionQlcChainComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupDeductionQlcChainContract
import com.stratagile.qlink.ui.activity.topup.module.TopupDeductionQlcChainModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupDeductionQlcChainPresenter
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_topup_qlc_pay.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.lang.Error
import java.math.BigDecimal
import java.util.*

import javax.inject.Inject;
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/12/26 14:45:12
 */

class TopupDeductionQlcChainActivity : BaseActivity(), TopupDeductionQlcChainContract.View {

    @Inject
    internal lateinit var mPresenter: TopupDeductionQlcChainPresenter

    override fun initView() {
        setContentView(R.layout.activity_topup_deduction_qlc_chain)
    }

    override fun setupActivityComponent() {
        DaggerTopupDeductionQlcChainComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupDeductionQlcChainModule(TopupDeductionQlcChainModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupDeductionQlcChainContract.TopupDeductionQlcChainContractPresenter) {
        mPresenter = presenter as TopupDeductionQlcChainPresenter
    }


    override fun topupOrderStatus(topupOrder: TopupOrder) {
        if (isFinish) {
            return
        }
        if (!"QGAS_PAID".equals(topupOrder.order.status, true)) {
            thread {
                Thread.sleep(5000)
                var map = hashMapOf<String, String>()
                map["orderId"] = topupOrder.order.id
                mPresenter.topupOrderConfirm(map)
            }
        } else {
            ivLoad2.setImageResource(R.mipmap.background_success)
            tvPaying.text = getString(R.string.qgas_transferred, topupOrderBean.symbol)
            tvVoucher.text = getString(R.string.blockchain_inoice_created)
            ivLoad2.clearAnimation()
            sa1.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })
            saHalf.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
            showChangeAnimation(ivLoad2)
            tvSend.postDelayed({
                sweetAlertDialog.dismissWithAnimation()
            }, 1000)
            tvSend.postDelayed({
                if ("TOKEN".equals(topupOrderBean.payWay)) {
                    when(OtcUtils.parseChain(topupOrderBean.payTokenChain)) {
                        AllWallet.WalletType.NeoWallet -> {
                            var payIntent = Intent(this, TopupPayNeoChainActivity::class.java)
                            payIntent.putExtra("order", topupOrder.order)
                            startActivity(payIntent)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                } else {
                    var url = "https://shop.huagaotx.cn/vendor/third_pay/index.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=${topupOrder.order.originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}&mobile=${topupOrder.order.phoneNumber}"
                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.putExtra("url", url)
                    intent.putExtra("title", getString(R.string.payment))
                    startActivityForResult(intent, 10)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }, 1500)
        }
    }

    override fun onBackPressed() {
        if (sweetAlertDialog.isShowing) {
            sweetAlertDialog.dismissWithAnimation()
            isFinish = true
//            startActivity(Intent(this, TopupOrderListActivity::class.java))
//            setResult(Activity.RESULT_OK)
//            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun createTopupOrderError() {
        sweetAlertDialog.dismissWithAnimation()
    }

    override fun setMainAddress() {
        tvReceiveAddress.text = ConstantValue.mainAddressData.qlcchian.address
    }

    override fun createTopupOrderSuccess(topupOrder: TopupOrder) {

    }

    override fun saveDeductionTokenTxidBack(topupOrder: TopupOrder) {
        if (!"QGAS_PAID".equals(topupOrder.order.status, true)) {
            thread {
                Thread.sleep(5000)
                var map = hashMapOf<String, String>()
                map["orderId"] = topupOrder.order.id
                mPresenter.topupOrderConfirm(map)
            }
        } else {
            ivLoad2.setImageResource(R.mipmap.background_success)
            tvPaying.text = getString(R.string.qgas_transferred, topupOrderBean.symbol)
            tvVoucher.text = getString(R.string.blockchain_inoice_created)
            ivLoad2.clearAnimation()
            sa1.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })
            saHalf.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
            showChangeAnimation(ivLoad2)
            tvSend.postDelayed({
                sweetAlertDialog.dismissWithAnimation()
            }, 1000)
            tvSend.postDelayed({
                when(OtcUtils.parseChain(topupOrderBean.payTokenChain)) {
                    AllWallet.WalletType.NeoWallet -> {
                        var payIntent = Intent(this, TopupPayNeoChainActivity::class.java)
                        payIntent.putExtra("order", topupOrder.order)
                        startActivity(payIntent)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }, 1500)
        }

    }

    var qgasCount = BigDecimal.ZERO

    lateinit var saHalf : ScaleAnimation
    lateinit var sa1 : ScaleAnimation

    lateinit var animationView: View
    lateinit var ivLoad1: ImageView
    lateinit var ivLoad2: ImageView
    lateinit var ivChain: ImageView
    lateinit var tvPaying: TextView
    lateinit var tvVoucher: TextView
    lateinit var view2 : View
    var isFinish = false

    lateinit var sweetAlertDialog: SweetAlertDialog

    lateinit var topupOrderBean : TopupOrder.OrderBean

    var qlcAccount: QLCAccount? = null
    override fun initData() {
        title.text = getString(R.string.payment_wallet)
        topupOrderBean = intent.getParcelableExtra("order")

        payToken1.text = topupOrderBean.symbol

        animationView = layoutInflater.inflate(R.layout.alert_show_otc_pay_animation, null, false)
        ivLoad1 = animationView.findViewById<ImageView>(R.id.ivLoad1)
        ivLoad2 = animationView.findViewById<ImageView>(R.id.ivLoad2)
        ivChain = animationView.findViewById<ImageView>(R.id.ivChain)
        ivChain.setImageResource(R.mipmap.icons_qlc_wallet)
        tvPaying = animationView.findViewById<TextView>(R.id.tvPaying)
        tvVoucher = animationView.findViewById<TextView>(R.id.tvVoucher)
        view2 = animationView.findViewById(R.id.view2)
        sweetAlertDialog = SweetAlertDialog(this)
        sweetAlertDialog.setView(animationView)
        sweetAlertDialog.setOnBackListener {
            onBackPressed()
        }

        saHalf = ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        saHalf.setDuration(400)

        sa1 = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(1000)

        if (ConstantValue.mainAddressData != null) {
            tvReceiveAddress.text = ConstantValue.mainAddressData.qlcchian.address
        } else {
            mPresenter.getMainAddress()
        }
        tvAmountQgas.text = topupOrderBean.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString()
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var message = "topup_" + topupOrderBean.id + "_" + topupOrderBean.discountPrice
        etEthTokenSendMemo.setText(message)
        var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounts.size > 0) {
            qlcAccounts.forEach {
                if (it.isCurrent()) {
                    qlcAccount = it
                    tvQlcWalletName.text = qlcAccount!!.accountName
                    tvQlcWalletAddess.text = qlcAccount!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${topupOrderBean.symbol}"
                    thread {
                        getWalletBalance()
                    }
                }
            }
        }
        tvSend.setOnClickListener {
            if (qlcAccount == null) {
                return@setOnClickListener
            }
            if (tvAmountQgas.text.toString().toBigDecimal() > qgasCount) {
                alert(getString(R.string.balance_insufficient_to_purchase_qgas_on_otc_pages, topupOrderBean.symbol)) {
                    negativeButton(getString(R.string.cancel)) {
                        dismiss()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    positiveButton(getString(R.string.buy_topup)) {
                        setResult(Activity.RESULT_OK)
                        EventBus.getDefault().post(SwitchToOtc())
                        finish()
                    }
                }.show()
                return@setOnClickListener
            }
            showPayAnimation()
            thread {
                QlcReceiveUtils.sendQGas(qlcAccount!!, ConstantValue.mainAddressData.qlcchian.address, tvAmountQgas.text.toString(), etEthTokenSendMemo.text.toString(), true, object : SendBack {
                    override fun send(suceess: String) {
                        if ("".equals(suceess)) {
                            runOnUiThread {
                                toast(getString(R.string.send_qgas_error, topupOrderBean.symbol))
                            }
                        } else {
                            runOnUiThread {
                                tvPaying.text = getString(R.string.qgas_transferred, topupOrderBean.symbol)
                                ivLoad1.clearAnimation()
                                sa1.setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationRepeat(animation: Animation?) {

                                    }

                                    override fun onAnimationEnd(animation: Animation?) {

                                    }

                                    override fun onAnimationStart(animation: Animation?) {

                                    }

                                })
                                saHalf.setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationRepeat(animation: Animation?) {

                                    }

                                    override fun onAnimationEnd(animation: Animation?) {

                                    }

                                    override fun onAnimationStart(animation: Animation?) {
                                    }

                                })
                                ivLoad1.setImageResource(R.mipmap.background_success)
                                showChangeAnimation(ivLoad1)
                                showViewAnimation(view2)
                            }
                            KLog.i(suceess)
                            Thread.sleep(5000)
                            saveDeductionTokenTxid(suceess, topupOrderBean.id)
                        }
                    }

                })
            }
        }
    }

    fun saveDeductionTokenTxid(txid : String, orderId : String) {
        var map = hashMapOf<String, String>()
        var topUpP2pId = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
        if ("".equals(topUpP2pId)) {
            var saveP2pId = FileUtil.readData("/Qwallet/p2pId.txt")
            if ("".equals(saveP2pId)) {
                val uuid = UUID.randomUUID()
                var p2pId = ""
                p2pId += uuid.toString().replace("-", "")
                topUpP2pId = p2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, p2pId)

                val file = File(Environment.getExternalStorageDirectory().toString() + "/Qwallet/p2pId.txt")
                if (file.exists()) {
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                } else {
                    file.createNewFile()
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                }

            } else {
                topUpP2pId = saveP2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, topUpP2pId)
            }
        }
        KLog.i("p2pId为：" + topUpP2pId)
        if (ConstantValue.currentUser != null) {
            map["account"] = ConstantValue.currentUser.account
            map["p2pId"] = topUpP2pId
        } else {
            map["p2pId"] = topUpP2pId
        }

        map["orderId"] = orderId
        map["deductionTokenTxid"] = txid
        mPresenter.saveDeductionTokenTxid(map)
    }

    fun showPayAnimation() {
        tvPaying.text = getString(R.string.transferring_qgas, topupOrderBean.symbol)
        tvVoucher.text = getString(R.string.creating_blockchain_invoice)
        ivLoad1.visibility = View.VISIBLE
        view2.visibility = View.INVISIBLE
        ivLoad1.setImageResource(R.mipmap.background_load)
        ivLoad2.setImageResource(R.mipmap.background_no)
        var tvWalletName = animationView.findViewById<TextView>(R.id.tvWalletName)
        tvWalletName.text = qlcAccount!!.accountName
        scaleAnimationTo1(ivLoad1)
        var tvWalletAddess = animationView.findViewById<TextView>(R.id.tvWalletAddess)
        tvWalletAddess.text = qlcAccount!!.address

        sweetAlertDialog.show()
    }

    fun showViewAnimation(view1: View) {
        var viewSa = ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        viewSa.setDuration(1000)
        viewSa.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                view1.visibility = View.VISIBLE
                ivLoad2.setImageResource(R.mipmap.background_load)
                tvVoucher.text = getString(R.string.to_create_blockchain_invoice)
                showChangeAnimation(ivLoad2)
                ivLoad2.postDelayed({
                    scaleAnimationTo1(ivLoad2)
                }, 300)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        view1.startAnimation(viewSa)
    }

    fun scaleAnimationTo1(imageView: ImageView) {
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(sa1)
    }
    fun scaleAnimationToHalf(imageView: ImageView) {
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(saHalf)
    }

    fun showChangeAnimation(view1: View) {
        SpringAnimationUtil.startScaleSpringViewAnimation(view1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AllWallet.WalletType.QlcWallet.ordinal -> {
                if (resultCode == Activity.RESULT_OK) {
                    qlcAccount = data!!.getParcelableExtra<QLCAccount>("wallet")
                    tvQlcWalletName.text = qlcAccount!!.accountName
                    tvQlcWalletAddess.text = qlcAccount!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${topupOrderBean.symbol}"
                    qgasCount = BigDecimal.ZERO
                    thread {
                        getWalletBalance()
                    }
                }
            }
            10 -> {
                startActivity(Intent(this, TopupOrderListActivity::class.java))
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    fun getWalletBalance() {
        QLCAPI().walletGetBalance(qlcAccount!!.address, "", object : QLCAPI.BalanceInter {
            override fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: Error?) {
                if (error == null) {
                    KLog.i("发射2")
                    if (baseResult!!.filter { it.symbol.equals(topupOrderBean.symbol) }.size > 0) {
                        qgasCount = baseResult!!.filter { it.symbol.equals(topupOrderBean.symbol) }[0].balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN)
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": ${qgasCount.stripTrailingZeros().toPlainString()} ${topupOrderBean.symbol}"
                        }
                    } else {
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": -/- ${topupOrderBean.symbol}"
                        }
                        qgasCount = BigDecimal.ZERO
                        toast(getString(R.string.not_enough) + " ${topupOrderBean.symbol}")
                    }
                } else {
                    runOnUiThread {
                        tvQGASBalance.text = getString(R.string.balance) + ": -/- ${topupOrderBean.symbol}"
                    }
                    qgasCount = BigDecimal.ZERO
                    toast(getString(R.string.not_enough) + " ${topupOrderBean.symbol}")
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
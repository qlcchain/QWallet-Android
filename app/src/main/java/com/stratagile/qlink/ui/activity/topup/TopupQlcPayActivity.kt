package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.animation.FloatPropertyCompat
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.telecom.Call
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.entity.SwitchToOtc
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupQlcPayComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupQlcPayContract
import com.stratagile.qlink.ui.activity.topup.module.TopupQlcPayModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupQlcPayPresenter
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_topup_qlc_pay.*
import org.greenrobot.eventbus.EventBus
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.File
import java.lang.Error
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

import javax.inject.Inject;
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/26 10:08:40
 */

class TopupQlcPayActivity : BaseActivity(), TopupQlcPayContract.View {

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
            tvPaying.text = getString(R.string.qgas_transferred, payToken.symbol)
            tvVoucher.text = getString(R.string.blockchain_inoice_created)
            showChangeAnimation(ivLoad2)

            tvSend.postDelayed({
                sweetAlertDialog.dismissWithAnimation()
            }, 1000)
            tvSend.postDelayed({
                var url = "https://shop.huagaotx.cn/vendor/third_pay/index.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=${topupOrder.order.originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}&mobile=${intent.getStringExtra("phoneNumber")}"
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", getString(R.string.payment))
                startActivityForResult(intent, 10)
            }, 1500)
        }
    }

    override fun onBackPressed() {
        if (sweetAlertDialog.isShowing) {
            sweetAlertDialog.dismissWithAnimation()
            if (isCreatedOrder) {
                isFinish = true
                startActivity(Intent(this, TopupOrderListActivity::class.java))
                setResult(Activity.RESULT_OK)
                finish()
            }
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
        isCreatedOrder = true
        if (!"QGAS_PAID".equals(topupOrder.order.status, true)) {
            thread {
                Thread.sleep(5000)
                var map = hashMapOf<String, String>()
                map["orderId"] = topupOrder.order.id
                mPresenter.topupOrderConfirm(map)
            }
        } else {
            tvSend.postDelayed({
                sweetAlertDialog.dismissWithAnimation()
            }, 1000)
            tvSend.postDelayed({
                var url = "https://shop.huagaotx.cn/vendor/third_pay/index.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=${topupOrder.order.originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}&mobile=${intent.getStringExtra("phoneNumber")}"
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", getString(R.string.payment))
                startActivityForResult(intent, 10)
            }, 1500)
        }
    }

    @Inject
    internal lateinit var mPresenter: TopupQlcPayPresenter
    var qgasCount = BigDecimal.ZERO
    lateinit var product: TopupProduct.ProductListBean
    lateinit var payToken: PayToken.PayTokenListBean

    lateinit var animationView: View
    lateinit var ivLoad1: ImageView
    lateinit var ivLoad2: ImageView
    lateinit var ivChain: ImageView
    lateinit var tvPaying: TextView
    lateinit var tvVoucher: TextView
    var isCreatedOrder = false
    var isFinish = false

    lateinit var sweetAlertDialog: SweetAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_qlc_pay)
    }

    var qlcAccount: QLCAccount? = null
    override fun initData() {
        title.text = getString(R.string.payment_wallet)
        product = intent.getParcelableExtra("product")
        payToken = intent.getParcelableExtra("payToken")
        payToken1.text = payToken.symbol

        animationView = layoutInflater.inflate(R.layout.alert_show_otc_pay_animation, null, false)
        ivLoad1 = animationView.findViewById<ImageView>(R.id.ivLoad1)
        ivLoad2 = animationView.findViewById<ImageView>(R.id.ivLoad2)
        ivChain = animationView.findViewById<ImageView>(R.id.ivChain)
        ivChain.setImageResource(R.mipmap.icons_eth_wallet)
        tvPaying = animationView.findViewById<TextView>(R.id.tvPaying)
        tvVoucher = animationView.findViewById<TextView>(R.id.tvVoucher)
        sweetAlertDialog = SweetAlertDialog(this)
        sweetAlertDialog.setView(animationView)
        sweetAlertDialog.setOnBackListener {
            onBackPressed()
        }

        if (ConstantValue.mainAddressData != null) {
            tvReceiveAddress.text = ConstantValue.mainAddressData.qlcchian.address
        } else {
            mPresenter.getMainAddress()
        }
        tvAmountQgas.text = product.price.toBigDecimal().multiply((product.qgasDiscount.toBigDecimal())).divide(payToken.price.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var discountPrice = (product.price.toBigDecimal() * (1.toBigDecimal() - product.discount.toBigDecimal())).stripTrailingZeros().toPlainString()
        etEthTokenSendMemo.setText(getString(R.string.topup_200_deduct_10_from_10_qgas, product.price.toString(), discountPrice, tvAmountQgas.text.toString(), payToken.symbol))

        var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounts.size > 0) {
            qlcAccounts.forEach {
                if (it.isCurrent()) {
                    qlcAccount = it
                    tvQlcWalletName.text = qlcAccount!!.accountName
                    tvQlcWalletAddess.text = qlcAccount!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${payToken.symbol}"
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
                alert(getString(R.string.balance_insufficient_to_purchase_qgas_on_otc_pages, payToken.symbol)) {
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
                QlcReceiveUtils.sendQGas(qlcAccount!!, ConstantValue.mainAddressData.qlcchian.address, tvAmountQgas.text.toString(), "", true, object : SendBack {
                    override fun send(suceess: String) {
                        if ("".equals(suceess)) {
                            runOnUiThread {
                                toast(getString(R.string.send_qgas_error, payToken.symbol))
                                sweetAlertDialog.dismissWithAnimation()
                            }
                        } else {
                            runOnUiThread {
                                tvPaying.text = getString(R.string.qgas_transferred, payToken.symbol)
                                tvVoucher.text = getString(R.string.to_create_blockchain_invoice)

                                ivLoad1.setImageResource(R.mipmap.background_success)
                                showChangeAnimation(ivLoad1)
                                ivLoad2.setImageResource(R.mipmap.background_load)
                                showChangeAnimation(ivLoad2)
                            }
                            KLog.i(suceess)
                            Thread.sleep(5000)
                            generateTopupOrder(suceess)
                        }
                    }

                })
            }
        }
    }

    fun generateTopupOrder(txid: String) {
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
        map["productId"] = product.id
        map["areaCode"] = intent.getStringExtra("areaCode")
        map["phoneNumber"] = intent.getStringExtra("phoneNumber")
        map["amount"] = product.price.toString()
        map["txid"] = txid
        map["payTokenId"] = payToken.id
        mPresenter.createTopupOrder(map)
    }


    fun showPayAnimation() {
        tvPaying.text = getString(R.string.transferring_qgas, payToken.symbol)
        tvVoucher.text = getString(R.string.creating_blockchain_invoice)
        var tvWalletName = animationView.findViewById<TextView>(R.id.tvWalletName)
        tvWalletName.text = qlcAccount!!.accountName

        var tvWalletAddess = animationView.findViewById<TextView>(R.id.tvWalletAddess)
        tvWalletAddess.text = qlcAccount!!.address

        val ivChain = animationView.findViewById<ImageView>(R.id.ivChain)
        ivChain.setImageResource(R.mipmap.icons_qlc_wallet)

        sweetAlertDialog.show()
        showChangeAnimation(ivLoad1)
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
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${payToken.symbol}"
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
                    if (baseResult!!.filter { it.symbol.equals(payToken.symbol) }.size > 0) {
                        qgasCount = baseResult!!.filter { it.symbol.equals(payToken.symbol) }[0].balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN)
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": ${qgasCount.stripTrailingZeros().toPlainString()} ${payToken.symbol}"
                        }
                    } else {
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": -/- ${payToken.symbol}"
                        }
                        qgasCount = BigDecimal.ZERO
                        toast(getString(R.string.not_enough) + " ${payToken.symbol}")
                    }
                } else {
                    runOnUiThread {
                        tvQGASBalance.text = getString(R.string.balance) + ": -/- ${payToken.symbol}"
                    }
                    qgasCount = BigDecimal.ZERO
                    toast(getString(R.string.not_enough) + " ${payToken.symbol}")
                }
            }
        })
    }

    override fun setupActivityComponent() {
        DaggerTopupQlcPayComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupQlcPayModule(TopupQlcPayModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupQlcPayContract.TopupQlcPayContractPresenter) {
        mPresenter = presenter as TopupQlcPayPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
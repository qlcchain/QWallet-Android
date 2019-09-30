package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.stratagile.qlink.entity.SwitchToOtc
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupQlcPayComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupQlcPayContract
import com.stratagile.qlink.ui.activity.topup.module.TopupQlcPayModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupQlcPayPresenter
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.QlcReceiveUtils
import com.stratagile.qlink.utils.SendBack
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import kotlinx.android.synthetic.main.activity_qurry_mobile.*
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
 * @date 2019/09/26 10:08:40
 */

class TopupQlcPayActivity : BaseActivity(), TopupQlcPayContract.View {
    override fun createTopupOrderSuccess(topupOrder: TopupOrder) {
        closeProgressDialog()
        //mm_1000001_949caa0a0d8b4f2c81dd1750e8e867de_2148498a37484faf96c9717ba56bd809
        var url = "https://shop.huagaotx.cn/wap/charge_v3.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=${topupOrder.order.originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}&mobile=${intent.getStringExtra("phoneNumber")}"
//        var url = "https://shop.huagaotx.cn/wap/charge_v3.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=0&mobile=${intent.getStringExtra("phoneNumber")}"
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", getString(R.string.payment))
        startActivityForResult(intent, 10)
    }

    @Inject
    internal lateinit var mPresenter: TopupQlcPayPresenter
    var qgasCount = BigDecimal.ZERO
    lateinit var product : TopupProduct.ProductListBean
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
        tvReceiveAddress.text = ConstantValue.mainAddressData.qlcchian.address
        tvAmountQgas.text = product.price.toBigDecimal().multiply((product.qgasDiscount.toBigDecimal())).stripTrailingZeros().toPlainString()
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounts.size > 0) {
            qlcAccounts.forEach {
                if (it.isCurrent()) {
                    qlcAccount = it
                    tvQlcWalletName.text = qlcAccount!!.accountName
                    tvQlcWalletAddess.text = qlcAccount!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- QGAS"
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
                alert(getString(R.string.balance_insufficient_to_purchase_qgas_on_otc_pages)) {
                    negativeButton (getString(R.string.cancel)) {
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
            showProgressDialog()
//            thread {
//                generateTopupOrder("87a1b1f69770623d644c2d617851e66d88dacfe4c070d205a3669b92b4f55a0a")
//            }
            thread {
                QlcReceiveUtils.sendQGas(qlcAccount!!, ConstantValue.mainAddressData.qlcchian.address, tvAmountQgas.text.toString(), "", true, object : SendBack {
                    override fun send(suceess: String) {
                        if ("".equals(suceess)) {
                            runOnUiThread {
                                toast(getString(R.string.send_qgas_error))
                                closeProgressDialog()
                            }
                        } else {
                            KLog.i(suceess)
                            Thread.sleep(5000)
                            generateTopupOrder(suceess)
                        }
                    }

                })
            }
        }
    }

    fun generateTopupOrder(txid : String) {
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
        mPresenter.createTopupOrder(map)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AllWallet.WalletType.QlcWallet.ordinal -> {
                qlcAccount = data!!.getParcelableExtra<QLCAccount>("wallet")
                tvQlcWalletName.text = qlcAccount!!.accountName
                tvQlcWalletAddess.text = qlcAccount!!.address
                tvQGASBalance.text = getString(R.string.balance) + ": -/- QGAS"
                thread {
                    getWalletBalance()
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
                    if (baseResult!!.filter { it.symbol.equals("QGAS") }.size > 0) {
                        qgasCount = baseResult!!.filter { it.symbol.equals("QGAS") }[0].balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN)
                        runOnUiThread {
                            tvQGASBalance.text = getString(R.string.balance) + ": ${qgasCount.stripTrailingZeros()} QGAS"
                        }
//                        if (baseResult!!.filter { it.symbol.equals("QGAS") }[0].balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros() >= amount.toBigDecimal()) {
//                            QlcReceiveUtils.sendQGas(qlcAccount, receiveAddress, amount, "SELL QGAS", object : SendBack {
//                                override fun send(suceess: String) {
//                                    if ("".equals(suceess)) {
//                                        mView.generateSellQgasOrderFailed("send qgas error")
//                                        it.onComplete()
//                                    } else {
//                                        KLog.i(suceess)
//                                        it.onNext(suceess)
//                                        it.onComplete()
//                                    }
//                                }
//
//                            })
//                        } else {
//                            toast(getString(R.string.not_enough) + "QGAS")
//                        }
                    }
                } else {
                    runOnUiThread {
                        tvQGASBalance.text = getString(R.string.balance) + ": -/- QGAS"
                    }
                    qgasCount = BigDecimal.ZERO
                    toast(getString(R.string.not_enough) + " QGAS")
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
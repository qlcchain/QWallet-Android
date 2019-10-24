package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.SwitchToOtc
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupEthPayComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupEthPayContract
import com.stratagile.qlink.ui.activity.topup.module.TopupEthPayModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupEthPayPresenter
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_topup_eth_pay.*
import org.greenrobot.eventbus.EventBus
import org.web3j.utils.Convert
import java.io.File
import java.math.BigDecimal
import java.util.*

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/10/24 10:18:43
 */

class TopupEthPayActivity : BaseActivity(), TopupEthPayContract.View {
    override fun createTopupOrderSuccess(topupOrder: TopupOrder) {
        closeProgressDialog()
        var url = "https://shop.huagaotx.cn/vendor/third_pay/index.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=${topupOrder.order.originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}&mobile=${intent.getStringExtra("phoneNumber")}"
//        var url = "https://shop.huagaotx.cn/wap/charge_v3.html?sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrder.order.userId}_${topupOrder.order.id}&package=0&mobile=${intent.getStringExtra("phoneNumber")}"
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", getString(R.string.payment))
        startActivityForResult(intent, 10)
    }

    override fun sendPayTokenSuccess(txid: String) {
        closeProgressDialog()
        showProgressDialog()
        generateTopupOrder(txid)
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
        map["payTokenId"] = payToken.id
        mPresenter.createTopupOrder(map)
    }


    override fun getEthWalletBack(ethWalletInfo: EthWalletInfo) {
        if ("false".equals(ethWalletInfo.data.eth.balance.toString())) {
            toast(getString(R.string.not_enough) + "ETH")
        } else if ("-1".equals(ethWalletInfo.data.eth.balance.toString())){
            toast(getString(R.string.not_enough) + "ETH")
        } else {
            payTokenBalance = ethWalletInfo.data.eth.balance.toString().toBigDecimal()
            ethCount = ethWalletInfo.data.eth.balance.toString().toBigDecimal()
        }

        ethWalletInfo.data.tokens.forEach {
            ethWalletInfo.data.tokens.forEach {
                if (payToken.hash.equals(it.tokenInfo.address, true)) {
                    payTokenBalance = it.balance.toBigDecimal().divide(10.toBigDecimal().pow(it.tokenInfo.decimals.toInt()), it.tokenInfo.decimals.toInt(), BigDecimal.ROUND_HALF_UP)
                    tvQGASBalance.text = getString(R.string.balance) + " " + payTokenBalance.stripTrailingZeros().toPlainString()
                }
            }
        }
    }

    @Inject
    internal lateinit var mPresenter: TopupEthPayPresenter
    lateinit var product: TopupProduct.ProductListBean
    lateinit var payToken: PayToken.PayTokenListBean
    lateinit var ethAccount: EthWallet
    var ethCount = BigDecimal.ZERO

    private val gasLimit = 60000

    private var gasPrice = 6

    private var gasEth: String? = null

    var payTokenBalance = BigDecimal.ZERO
    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_eth_pay)
    }

    override fun initData() {
        title.text = getString(R.string.payment_wallet)
        product = intent.getParcelableExtra("product")
        payToken = intent.getParcelableExtra("payToken")
        payTokenSymbol.text = payToken.symbol
        val gas = Convert.toWei(gasPrice.toString() + "", Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        val f = gas.multiply(BigDecimal(gasLimit))
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()

        tvReceiveAddress.text = ConstantValue.mainAddressData.eth.address
        tvAmountQgas.text = product.price.toBigDecimal().multiply((product.qgasDiscount.toBigDecimal())).divide(payToken.price.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.EthWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var ethWallets = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        if (ethWallets.size > 0) {
            ethWallets.forEach {
                if (it.isCurrent()) {
                    ethAccount = it
                    tvQlcWalletName.text = ethAccount.name
                    tvQlcWalletAddess.text = ethAccount.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/-"
                    thread {
                        getWalletBalance()
                    }
                }
            }
        }
        tvSend.setOnClickListener {
            if (payTokenBalance < tvAmountQgas.text.toString().toBigDecimal()) {
                alert(getString(R.string.balance_insufficient_to_purchase_qgas_on_otc_pages, payToken.symbol)) {
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
            if (ethCount.toDouble() >= gasEth!!.toDouble()) {
                sendPayToken()
            } else {
                ToastUtil.displayShortToast(getString(R.string.not_enough) + " eth")
            }
        }
    }

    fun sendPayToken() {
        showProgressDialog()
        mPresenter.transaction(ethAccount.address, payToken.hash, payToken.decimal, tvReceiveAddress.getText().toString(), tvAmountQgas.text.toString(), gasLimit, gasPrice)
    }

    fun getWalletBalance() {
        val infoMap = HashMap<String, String>()
        infoMap["address"] = ethAccount.address
        mPresenter.getETHWalletDetail(ethAccount.address, infoMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AllWallet.WalletType.EthWallet.ordinal -> {
                if (resultCode == Activity.RESULT_OK) {
                    ethAccount = data!!.getParcelableExtra<EthWallet>("wallet")
                    tvQlcWalletName.text = ethAccount!!.name
                    tvQlcWalletAddess.text = ethAccount!!.address
                    tvQGASBalance.text = getString(R.string.balance) + ": -/- ${payToken.symbol}"
                    payTokenBalance = BigDecimal.ZERO
                    ethCount = BigDecimal.ZERO
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

    override fun setupActivityComponent() {
        DaggerTopupEthPayComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupEthPayModule(TopupEthPayModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupEthPayContract.TopupEthPayContractPresenter) {
        mPresenter = presenter as TopupEthPayPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
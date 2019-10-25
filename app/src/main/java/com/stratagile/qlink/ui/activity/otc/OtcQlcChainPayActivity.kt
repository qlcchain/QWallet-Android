package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pawegio.kandroid.toast
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.ui.activity.otc.component.DaggerOtcQlcChainPayComponent
import com.stratagile.qlink.ui.activity.otc.contract.OtcQlcChainPayContract
import com.stratagile.qlink.ui.activity.otc.module.OtcQlcChainPayModule
import com.stratagile.qlink.ui.activity.otc.presenter.OtcQlcChainPayPresenter
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_otc_qlc_chain_pay.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.util.HashMap

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/08/23 12:01:58
 */

class OtcQlcChainPayActivity : BaseActivity(), OtcQlcChainPayContract.View {
    override fun sendTokenFailed() {
        runOnUiThread {
            closeProgressDialog()
        }
    }

    override fun sendTokenSuccess(txid: String) {
        runOnUiThread {
            closeProgressDialog()
            setResult(Activity.RESULT_OK)
            startActivity(Intent(this, OtcOrderRecordActivity::class.java).putExtra("position", 1))
            finish()
        }
    }

    override fun setQlcChainToken(arrayList: ArrayList<QlcTokenbalance>) {
        runOnUiThread {
            qlcTokenbalances = arrayList
            arrayList.forEach {
                if (it.symbol.equals(intent.getStringExtra("payToken"))) {
                    sendQlcChainTokenbalance = it
                    tvPayTokenBalance.text = getString(R.string.balance) + ": ${ it.balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros()} ${intent.getStringExtra("payToken")}"
                    return@forEach
                }
            }
        }
    }

    @Inject
    internal lateinit var mPresenter: OtcQlcChainPayPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_otc_qlc_chain_pay)
    }
    override fun initData() {
        title.text = getString(R.string.send) + " " + intent.getStringExtra("payToken")
        tvPayToken.text = intent.getStringExtra("payToken")
        tvReceiveAddress.text = intent.getStringExtra("receiveAddress")
        tvAmountPayTokenn.text = intent.getStringExtra("usdt")
        tvPayTokenBalance.text = getString(R.string.balance) + ": -/-"

        etQlcTokenSendMemo.setText(getString(R.string.buy) + " " + intent.getStringExtra("tradeToken") + "( " + intent.getStringExtra("orderNumber") + " )")
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvSend.setOnClickListener {
            if (qlcAccount == null) {
                return@setOnClickListener
            }
            if (sendQlcChainTokenbalance!!.balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN) < tvAmountPayTokenn.text.toString().toBigDecimal()) {
                toast(getString(R.string.no_enough) + " " + intent.getStringExtra("payToken"))
                return@setOnClickListener
            }
            showConfirmSendUsdtDialog()
        }
    }

    fun showConfirmSendUsdtDialog() {
        //
        val view = View.inflate(this, R.layout.dialog_send_usdt_layout, null)
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val ivSendChain = view.findViewById<ImageView>(R.id.ivSendChain)
        ivSendChain.setImageResource(R.mipmap.icons_qlc_wallet)
        val tvEthWalletName1 = view.findViewById<TextView>(R.id.tvEthWalletName1)
        val tvEthWalletAddess1 = view.findViewById<TextView>(R.id.tvEthWalletAddess1)
        val tvEthAmount1 = view.findViewById<TextView>(R.id.tvEthAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainEthAddress)
        val tvMemo = view.findViewById<TextView>(R.id.tvMemo)
        tvEthWalletName1.text = qlcAccount!!.accountName
        tvEthWalletAddess1.text = qlcAccount!!.address
        tvMemo.text = etQlcTokenSendMemo.text.toString()
        tvEthAmount1.text = tvAmountPayTokenn.text.toString().trim() + " " + intent.getStringExtra("payToken")
        tvMainQgasAddress.text = tvReceiveAddress.text.toString()
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(this)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        ivClose.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
            sweetAlertDialog.cancel()
            showProgressDialog()
            thread {
                mPresenter.sendQlcChainToken(sendQlcChainTokenbalance!!, qlcAccount!!, intent.getStringExtra("receiveAddress"), intent.getStringExtra("usdt"), etQlcTokenSendMemo.text.toString(), intent.getStringExtra("tradeOrderId"))
            }
        }
    }

    var qlcAccount : QLCAccount? = null
    var qlcTokenbalances : ArrayList<QlcTokenbalance>? = null
    var sendQlcChainTokenbalance : QlcTokenbalance? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AllWallet.WalletType.QlcWallet.ordinal -> {
                    qlcAccount = data!!.getParcelableExtra<QLCAccount>("wallet")
                    tvQlcWalletName.text = qlcAccount!!.accountName
                    tvQlcWalletAddess.text = qlcAccount!!.address
                    thread {
                        mPresenter.getQlcWalletDetail(qlcAccount!!)
                    }
                    val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
                    if (qlcAccounts.size != 0) {
                        qlcAccounts.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                return@forEach
                            }
                        }
                    }
                    val neoWallets = AppConfig.getInstance().daoSession.walletDao.loadAll()
                    if (neoWallets.size != 0) {
                        neoWallets.forEach {
                            if (it.isCurrent) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.walletDao.update(it)
                                return@forEach
                            }
                        }
                        neoWallets.forEach {
                            if (it.address.equals(qlcAccount!!.address)) {
                                it.setIsCurrent(true)
                                AppConfig.getInstance().daoSession.walletDao.update(it)
                                EventBus.getDefault().post(ChangeCurrency())
                                return@forEach
                            }
                        }
                    }

                    val wallets2 = AppConfig.getInstance().daoSession.eosAccountDao.loadAll()
                    if (wallets2 != null && wallets2.size != 0) {
                        wallets2.forEach {
                            if (it.isCurrent) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.eosAccountDao.update(it)
                                return@forEach
                            }
                        }
                    }

                    val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
                    if (ethWallets != null && ethWallets.size != 0) {
                        ethWallets.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.ethWalletDao.update(it)
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setupActivityComponent() {
       DaggerOtcQlcChainPayComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .otcQlcChainPayModule(OtcQlcChainPayModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OtcQlcChainPayContract.OtcQlcChainPayContractPresenter) {
            mPresenter = presenter as OtcQlcChainPayPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
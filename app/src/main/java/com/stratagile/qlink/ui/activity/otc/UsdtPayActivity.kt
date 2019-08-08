package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
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
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.ui.activity.otc.component.DaggerUsdtPayComponent
import com.stratagile.qlink.ui.activity.otc.contract.UsdtPayContract
import com.stratagile.qlink.ui.activity.otc.module.UsdtPayModule
import com.stratagile.qlink.ui.activity.otc.presenter.UsdtPayPresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.SpringAnimationUtil
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_appeal.*
import kotlinx.android.synthetic.main.activity_sell_qgas.*
import kotlinx.android.synthetic.main.activity_usdt_pay.*
import kotlinx.android.synthetic.main.activity_usdt_pay.llSelectEthWallet
import org.greenrobot.eventbus.EventBus
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.util.HashMap

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/31 13:58:11
 */

class UsdtPayActivity : BaseActivity(), UsdtPayContract.View {
    override fun sendUsdtSuccess(txid: String) {
        closeProgressDialog()
        toast(getString(R.string.success))
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun setTokens(ethWalletInfo: EthWalletInfo) {
        if (ethWalletInfo.data.eth != null && !"false".equals(ethWalletInfo.data.eth.balance.toString()) && !"-1".equals(ethWalletInfo.data.eth.balance.toString())) {
            ethCount = ethWalletInfo.data.eth.balance.toString().toDouble()
            if (gasEth.toDouble() > ethCount) {
                toast(getString(R.string.no_enough_eth))
            }
        } else {
            toast(getString(R.string.no_enough_eth))
        }
        ethWalletInfo.data.tokens.forEach {
            if ("USDT".equals(it.tokenInfo.symbol)) {
                usdtCount = it.balance / Math.pow(10.0, it.tokenInfo.decimals.toDouble())
                tvUsdtBalance.text = getString(R.string.balance) + ": $usdtCount USDT"
                return@forEach
            }
        }
    }

    override fun setEthPrice(tokenPrice: TokenPrice) {
        ethPrice = tokenPrice.data[0].price
        gasPrice = seekBar.progress + 6
        tvGwei.text = "$gasPrice gwei"
        val gas = Convert.toWei(gasPrice.toString() + "", Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        val f = gas.multiply(BigDecimal(gasLimit))
        val gasMoney = f.multiply(BigDecimal(ethPrice.toString() + ""))
        val f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()
        tvCostEth.text = gasEth + " ether ≈ " + ConstantValue.currencyBean.currencyImg + f1
    }

    @Inject
    internal lateinit var mPresenter: UsdtPayPresenter

    var isOpen = false
    /**
     * eth当前的市价
     */
    private var ethPrice: Double = 0.toDouble()

    private val gasLimit = 60000

    private var gasPrice = 6

    private var gasEth: String = "0.0004"

    var ethWallet: EthWallet? = null

    var ethCount = 0.toDouble()

    var usdtCount = 0.toDouble()
    var usdtAmount = 0.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_usdt_pay)
    }

    override fun initData() {
        title.text = getString(R.string.send_usdt)
        llOpen.setOnClickListener {
            toggleCost()
        }
        tvUsdtBalance.text = getString(R.string.balance) + ": -/- USDT"
        etEthTokenSendMemo.setText(getString(R.string.buy_qgas) + "( " + intent.getStringExtra("orderNumber") + " )")
        mPresenter.getEthPrice()
        tvReceiveAddress.text = intent.getStringExtra("receiveAddress")
        tvAmountUsdt.text = intent.getStringExtra("usdt")
        usdtAmount = intent.getStringExtra("usdt").toDouble()

        gasPrice = seekBar.progress + 6
        tvGwei.text = "$gasPrice gwei"
        val gas = Convert.toWei(gasPrice.toString() + "", Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        val f = gas.multiply(BigDecimal(gasLimit))
        val gasMoney = f.multiply(BigDecimal(ethPrice.toString() + ""))
        val f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()
        tvCostEth.text = gasEth + " ether ≈ " + ConstantValue.currencyBean.currencyImg + f1
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                gasPrice = progress + 6
                tvGwei.text = "$gasPrice gwei"
                val gas = Convert.toWei(gasPrice.toString() + "", Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
                val f = gas.multiply(BigDecimal(gasLimit))

                val gasMoney = f.multiply(BigDecimal(ethPrice.toString() + ""))
                val f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                KLog.i(f1)
                gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()
                tvCostEth.text = gasEth + " ether ≈ " + ConstantValue.currencyBean.currencyImg + f1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        llSelectEthWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.EthWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var ethWalletList = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        if (ethWalletList.size > 0) {
            ethWalletList.forEach {
                if (it.isCurrent()) {
                    ethWallet = it
                    tvEthWalletName.text = ethWallet!!.name
                    tvEthWalletAddess.text = ethWallet!!.address
                    val infoMap = HashMap<String, String>()
                    infoMap["address"] = ethWallet!!.address
                    mPresenter.getEthWalletDetail(infoMap)
                }
            }
        }
        tvSend.setOnClickListener {
            if (usdtAmount > usdtCount) {
                toast(getString(R.string.no_enough_usdt))
                return@setOnClickListener
            }
            if (gasEth.toDouble() > ethCount) {
                toast(getString(R.string.no_enough_usdt))
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
        val tvEthWalletName1 = view.findViewById<TextView>(R.id.tvEthWalletName1)
        val tvEthWalletAddess1 = view.findViewById<TextView>(R.id.tvEthWalletAddess1)
        val tvEthAmount1 = view.findViewById<TextView>(R.id.tvEthAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainEthAddress)
        val tvMemo = view.findViewById<TextView>(R.id.tvMemo)
        tvEthWalletName1.text = ethWallet!!.name
        tvEthWalletAddess1.text = ethWallet!!.address
        tvMemo.text = etEthTokenSendMemo.text.toString()
        tvEthAmount1.text = tvAmountUsdt.text.toString().trim() + " USDT"
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
            mPresenter.transferUsdt(ethWallet!!.address, tvReceiveAddress.text.toString(), usdtAmount.toString(), gasPrice, intent.getStringExtra("tradeOrderId"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AllWallet.WalletType.EthWallet.ordinal -> {
                    ethWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                    tvEthWalletName.text = ethWallet!!.name
                    tvEthWalletAddess.text = ethWallet!!.address

                    val infoMap = HashMap<String, String>()
                    infoMap["address"] = ethWallet!!.address
                    mPresenter.getEthWalletDetail(infoMap)

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
                        ethWallets.forEach {
                            if (it.address.equals(ethWallet!!.address)) {
                                it.setIsCurrent(true)
                                AppConfig.getInstance().daoSession.ethWalletDao.update(it)
                                EventBus.getDefault().post(ChangeCurrency())
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setupActivityComponent() {
        DaggerUsdtPayComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .usdtPayModule(UsdtPayModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: UsdtPayContract.UsdtPayContractPresenter) {
        mPresenter = presenter as UsdtPayPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    private fun toggleCost() {
        if (isOpen) {
            isOpen = false
            group.setVisibility(View.GONE)
            SpringAnimationUtil.endRotatoSpringViewAnimation(ivShow) { animation, canceled, value, velocity -> }
        } else {
            isOpen = true
            group.setVisibility(View.VISIBLE)
            SpringAnimationUtil.startRotatoSpringViewAnimation(ivShow) { animation, canceled, value, velocity -> }
        }
    }

}
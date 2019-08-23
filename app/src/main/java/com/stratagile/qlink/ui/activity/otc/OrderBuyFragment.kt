package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderBuyComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderBuyContract
import com.stratagile.qlink.ui.activity.otc.module.OrderBuyModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderBuyPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EosAccount
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.fragment_order_buy.*
import kotlinx.android.synthetic.main.fragment_order_buy.buyingToken
import kotlinx.android.synthetic.main.fragment_order_buy.buyingTokenPrice
import kotlinx.android.synthetic.main.fragment_order_buy.etAmount
import kotlinx.android.synthetic.main.fragment_order_buy.etMaxAmount
import kotlinx.android.synthetic.main.fragment_order_buy.etMinAmount
import kotlinx.android.synthetic.main.fragment_order_buy.etUnitPrice
import kotlinx.android.synthetic.main.fragment_order_buy.ivReceiveChain
import kotlinx.android.synthetic.main.fragment_order_buy.llBuyToken
import kotlinx.android.synthetic.main.fragment_order_buy.llSellToken
import kotlinx.android.synthetic.main.fragment_order_buy.sellingToken
import kotlinx.android.synthetic.main.fragment_order_buy.sellinngTokenQuantity
import kotlinx.android.synthetic.main.fragment_order_buy.tvNext
import kotlinx.android.synthetic.main.fragment_order_buy.tvReceiveWalletAddess
import kotlinx.android.synthetic.main.fragment_order_buy.tvReceiveWalletName
import neoutils.Neoutils
import qlc.mng.AccountMng
import qlc.mng.WalletMng
import java.math.BigDecimal

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/08 17:24:58
 */

class OrderBuyFragment : BaseFragment(), OrderBuyContract.View {
    override fun generateBuyQgasOrderSuccess() {
        toast(getString(R.string.success))
        closeProgressDialog()
        activity?.finish()
    }

    @Inject
    lateinit internal var mPresenter: OrderBuyPresenter

    var receiveQgasWallet : QLCAccount? = null

    val sendTokenType = 0
    val receiveTokenType = 1
    val selectPair = 11

    var receiveEthWallet : EthWallet? = null
    var receiveEosWallet : EosAccount? = null
    var receiveQlcWallet : QLCAccount? = null
    var receiveNeoWallet : Wallet? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_buy, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCreateWallet.setOnClickListener {
            startActivity(Intent(activity, SelectWalletTypeActivity::class.java))
        }
        tvNext.setOnClickListener {
            if ("".equals(etMinAmount.text.toString()) || "".equals(etMaxAmount.text.toString()) || "".equals(etAmount.text.toString()) || "".equals(etUnitPrice.text.toString())) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            if (etMinAmount.text.toString().trim().toBigDecimal() > etMaxAmount.text.toString().trim().toBigDecimal()) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            if (etMaxAmount.text.toString().trim().toBigDecimal() > etAmount.text.toString().trim().toBigDecimal()) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            when(OtcUtils.parseChain(selectedPair!!.tradeTokenChain)) {
                AllWallet.WalletType.QlcWallet -> {
                    if (!AccountMng.isValidAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EthWallet -> {
                    if (!ETHWalletUtils.isETHValidAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.NeoWallet -> {
                    if (!Neoutils.validateNEOAddress(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
                AllWallet.WalletType.EosWallet -> {
                    if (!EosUtil.isEosName(tvReceiveWalletAddess.text.toString().trim())) {
                        toast(getString(R.string.illegal_receipt_address))
                        return@setOnClickListener
                    }
                }
            }
            if (etUnitPrice.text.toString().toBigDecimal() == BigDecimal.ZERO) {
                toast(getString(R.string.illegal_value))
                return@setOnClickListener
            }
            var map = mutableMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
            map["pairsId"] = selectedPair!!.id
            map.put("type", ConstantValue.orderTypeBuy)
            map.put("unitPrice", etUnitPrice.text.toString().trim())
            map.put("totalAmount", etAmount.text.toString().trim())
            map.put("minAmount", etMinAmount.text.toString().trim())
            map.put("maxAmount", etMaxAmount.text.toString().trim())
            map.put("qgasAddress",tvReceiveWalletAddess.text.toString().trim())
//            map.put("usdtAddress", "")
//            map.put("fromAddress", "")
            map.put("txid", "")
            mPresenter.generateBuyQgasOrder(map)
        }
        etUnitPrice.filters = arrayOf<InputFilter>(MoneyValueFilter().setDigits(3))
        etUnitPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!"".equals(etUnitPrice.text.toString())) {
                    if (etUnitPrice.text.toString().length == 5 && etUnitPrice.text.toString().toBigDecimal() < 0.001.toBigDecimal()) {
                        etUnitPrice.setText("0.001")
                        etUnitPrice.setSelection(5)
                    }
                }
            }

        })
        llSelectReceiveWallet.setOnClickListener {
            if (selectedPair == null) {
                toast(getString(R.string.please_select_a_trade_pair))
                return@setOnClickListener
            }
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", OtcUtils.parseChain(selectedPair!!.tradeTokenChain).ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, receiveTokenType)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvReceiveWalletAddess.setOnClickListener {
            showEnterQlcWalletDialog()
        }
        var qlcAccounList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounList.size > 0) {
            qlcAccounList.forEach {
                if (it.isCurrent()) {
                    receiveQgasWallet = it
                    tvReceiveWalletName.text = it.accountName
                    tvReceiveWalletAddess.text = it.address
                    return@forEach
                }
            }
        }
        llSellToken.setOnClickListener {
            startActivityForResult(Intent(activity, SelectCurrencyActivity::class.java), selectPair)
        }
        llBuyToken.setOnClickListener {
            startActivityForResult(Intent(activity, SelectCurrencyActivity::class.java), selectPair)
        }
    }
    var selectedPair : TradePair.PairsListBean? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                receiveTokenType -> {
                    when(OtcUtils.parseChain(selectedPair!!.tradeTokenChain)) {
                        AllWallet.WalletType.EthWallet -> {
                            receiveEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                            tvReceiveWalletName.text = receiveEthWallet!!.name
                            tvReceiveWalletAddess.text = receiveEthWallet!!.address
                        }
                        AllWallet.WalletType.EosWallet -> {
                            receiveEosWallet = data!!.getParcelableExtra<EosAccount>("wallet")
                            tvReceiveWalletName.text = receiveEosWallet!!.walletName
                            tvReceiveWalletAddess.text = receiveEosWallet!!.accountName
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            receiveNeoWallet = data!!.getParcelableExtra<Wallet>("wallet")
                            tvReceiveWalletName.text = receiveNeoWallet!!.name
                            tvReceiveWalletAddess.text = receiveNeoWallet!!.address
                        }
                        AllWallet.WalletType.QlcWallet -> {
                            receiveQlcWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                            tvReceiveWalletName.text = receiveQlcWallet!!.accountName
                            tvReceiveWalletAddess.text = receiveQlcWallet!!.address
                        }
                    }
                }
                selectPair -> {
                    selectedPair = data!!.getParcelableExtra("pair")
                    buyingToken.text = selectedPair!!.tradeToken
                    buyingTokenPrice.text = selectedPair!!.tradeToken
                    sellingToken.text = selectedPair!!.payToken
                    sellinngTokenQuantity.text = selectedPair!!.payToken
                    ivReceiveChain.setImageDrawable(null)

                    tvReceiveWalletAddess.text = getString(R.string.input_wallet_address)
                    tvReceiveWalletName.text = getString(R.string.select_a_wallet_to_receive_token)

                    when(OtcUtils.parseChain(selectedPair!!.tradeTokenChain)) {
                        AllWallet.WalletType.QlcWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_qlc_wallet)
                        }
                        AllWallet.WalletType.EthWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_eth_wallet)
                        }
                        AllWallet.WalletType.NeoWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_neo_wallet)
                        }
                        AllWallet.WalletType.EosWallet -> {
                            ivReceiveChain.setImageResource(R.mipmap.icons_eos_wallet)
                        }
                    }
                }
            }
        }
    }

    fun showEnterQlcWalletDialog() {
        //
        val view = View.inflate(activity!!, R.layout.dialog_input_walletaddress_layout, null)
        val etContent = view.findViewById<View>(R.id.etContent) as EditText//输入内容
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)//取消按钮
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(activity)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        tvCancel.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
            if (AccountMng.isValidAddress(etContent.text.toString().trim())) {
                tvReceiveWalletName.text = etContent.text.toString().trim()
                tvReceiveWalletAddess.text = etContent.text.toString().trim()
            } else {
                toast(getString(R.string.illegal_receipt_address))
            }
            sweetAlertDialog.cancel()
        }
    }

    override fun setupFragmentComponent() {
        DaggerOrderBuyComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .orderBuyModule(OrderBuyModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: OrderBuyContract.OrderBuyContractPresenter) {
        mPresenter = presenter as OrderBuyPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
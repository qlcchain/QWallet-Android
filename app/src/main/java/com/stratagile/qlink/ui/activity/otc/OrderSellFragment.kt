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
import android.widget.ImageView
import android.widget.TextView

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderSellComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderSellContract
import com.stratagile.qlink.ui.activity.otc.module.OrderSellModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderSellPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.MoneyValueFilter
import com.stratagile.qlink.utils.UserUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.fragment_order_buy.*
import kotlinx.android.synthetic.main.fragment_order_sell.*
import kotlinx.android.synthetic.main.fragment_order_sell.etAmount
import kotlinx.android.synthetic.main.fragment_order_sell.etMaxAmount
import kotlinx.android.synthetic.main.fragment_order_sell.etMinAmount
import kotlinx.android.synthetic.main.fragment_order_sell.etUnitPrice
import kotlinx.android.synthetic.main.fragment_order_sell.llSelectQlcWallet
import kotlinx.android.synthetic.main.fragment_order_sell.tvNext
import kotlinx.android.synthetic.main.fragment_order_sell.tvQLCWalletAddess
import kotlinx.android.synthetic.main.fragment_order_sell.tvQLCWalletName
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/08 17:24:46
 */

class OrderSellFragment : BaseFragment(), OrderSellContract.View {
    override fun generateSellQgasOrderSuccess() {
        toast("success")
        closeProgressDialog()
        activity?.finish()
    }

    override fun generateSellQgasOrderFailed(content: String) {
        runOnUiThread {
            closeProgressDialog()
            toast(content)
        }
    }

    @Inject
    lateinit internal var mPresenter: OrderSellPresenter

    var qlcAccounList = mutableListOf<QLCAccount>()

    var receiveEthWallet : EthWallet? = null

    var sendQgasWallet : QLCAccount? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_sell, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNext.setOnClickListener {
            if ("".equals(etMinAmount.text.toString()) || "".equals(etMaxAmount.text.toString()) || "".equals(etAmount.text.toString()) || "".equals(etUnitPrice.text.toString())) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (etMinAmount.text.toString().toBigDecimal() < 1.toBigDecimal()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (etMinAmount.text.toString().trim().toInt() > etMaxAmount.text.toString().trim().toInt()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (etMaxAmount.text.toString().trim().toInt() > etAmount.text.toString().trim().toInt()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if ("".equals(tvQLCWalletAddess.text.toString())) {
                toast("Please select a qlc wallet to payment")
                return@setOnClickListener
            }
            if (etUnitPrice.text.toString().toBigDecimal() == BigDecimal.ZERO) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (!ETHWalletUtils.isETHValidAddress(tvWalletAddess.text.toString().trim())) {
                toast("USDT address error")
                return@setOnClickListener
            }
            showConfirmSellDialog()
        }
        llSelectEthWallet.setOnClickListener {
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal)
            intent1.putExtra("select", false)
            startActivityForResult(intent1, AllWallet.WalletType.EthWallet.ordinal)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
//            showSpinnerPopWindow()
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

        qlcAccounList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounList.size > 0) {
            qlcAccounList.forEach {
                if (it.isCurrent()) {
                    sendQgasWallet = it
                    tvQLCWalletAddess.text = it.address
                    tvQLCWalletName.text = it.accountName
                    return@forEach
                }
            }
        }
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvWalletAddess.setOnClickListener {
            showEnterEthWalletDialog()
        }
    }

    fun showConfirmSellDialog() {
        //
        val view = View.inflate(activity!!, R.layout.dialog_send_qgas_layout, null)
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvQLCWalletName1 = view.findViewById<TextView>(R.id.tvQLCWalletName1)
        val tvQLCWalletAddess1 = view.findViewById<TextView>(R.id.tvQLCWalletAddess1)
        val tvQgasAmount1 = view.findViewById<TextView>(R.id.tvQgasAmount1)
        val tvMainQgasAddress = view.findViewById<TextView>(R.id.tvMainQgasAddress)
        tvQLCWalletName1.text = sendQgasWallet!!.accountName
        tvQLCWalletAddess1.text = sendQgasWallet!!.address
        tvQgasAmount1.text = etAmount.text.toString().trim() + " QGAS"
        tvMainQgasAddress.text = ConstantValue.mainAddressData.qlcchian.address
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(activity!!)
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
            var map = mutableMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
            map.put("type", ConstantValue.orderTypeSell)
            map.put("unitPrice", etUnitPrice.text.toString().trim())
            map.put("totalAmount", etAmount.text.toString().trim())
            map.put("minAmount", etMinAmount.text.toString().trim())
            map.put("maxAmount", etMaxAmount.text.toString().trim())
            map.put("qgasAddress","")
            map.put("usdtAddress", tvWalletAddess.text.toString().trim())
            mPresenter.sendQgas(etAmount.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
        }
    }


    fun showEnterEthWalletDialog() {
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
            if (ETHWalletUtils.isETHValidAddress(etContent.text.toString().trim())) {
                tvWalletAddess.text = etContent.text.toString().trim()
                tvWalletName.text = etContent.text.toString().trim()
            } else {
                toast("Illegal Receipt Address")
            }
            sweetAlertDialog.cancel()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                AllWallet.WalletType.EthWallet.ordinal -> {
                    receiveEthWallet = data!!.getParcelableExtra<EthWallet>("wallet")
                    tvWalletName.text = receiveEthWallet!!.name
                    tvWalletAddess.text = receiveEthWallet!!.address
                }
                AllWallet.WalletType.QlcWallet.ordinal -> {
                    sendQgasWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                    tvQLCWalletName.text = sendQgasWallet!!.accountName
                    tvQLCWalletAddess.text = sendQgasWallet!!.address
                    val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
                    if (ethWallets.size != 0) {
                        ethWallets.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.ethWalletDao.update(it)
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

                    if (qlcAccounList != null && qlcAccounList.size != 0) {
                        qlcAccounList.forEach {
                            if (it.isCurrent()) {
                                it.setIsCurrent(false)
                                AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                return@forEach
                            }
                        }
                        qlcAccounList.forEach {
                            if (it.address.equals(sendQgasWallet!!.address)) {
                                it.setIsCurrent(true)
                                AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                EventBus.getDefault().post(ChangeCurrency())
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

//    private fun showSendQGASWalletPopWindow() {
//        if (qlcAccounList.size > 0) {
//            PopWindowUtil.showSharePopWindow(activity!!, sendWalletMore, qlcAccounList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
//                override fun onSelect(content: String) {
//                    if ("" != content) {
//                        etSendAddress.setText(content)
//                        val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
//                        if (ethWallets.size != 0) {
//                            ethWallets.forEach {
//                                if (it.isCurrent()) {
//                                    it.setIsCurrent(false)
//                                    AppConfig.getInstance().daoSession.ethWalletDao.update(it)
//                                    return@forEach
//                                }
//                            }
//                        }
//                        val neoWallets = AppConfig.getInstance().daoSession.walletDao.loadAll()
//                        if (neoWallets.size != 0) {
//                            neoWallets.forEach {
//                                if (it.isCurrent) {
//                                    it.setIsCurrent(false)
//                                    AppConfig.getInstance().daoSession.walletDao.update(it)
//                                    return@forEach
//                                }
//                            }
//                        }
//
//                        val wallets2 = AppConfig.getInstance().daoSession.eosAccountDao.loadAll()
//                        if (wallets2 != null && wallets2.size != 0) {
//                            wallets2.forEach {
//                                if (it.isCurrent) {
//                                    it.setIsCurrent(false)
//                                    AppConfig.getInstance().daoSession.eosAccountDao.update(it)
//                                    return@forEach
//                                }
//                            }
//                        }
//
//                        if (qlcAccounList != null && qlcAccounList.size != 0) {
//                            qlcAccounList.forEach {
//                                if (it.isCurrent()) {
//                                    it.setIsCurrent(false)
//                                    AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
//                                    return@forEach
//                                }
//                            }
//                            qlcAccounList.forEach {
//                                if (it.address.equals(content)) {
//                                    it.setIsCurrent(true)
//                                    AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
//                                    EventBus.getDefault().post(ChangeCurrency())
//                                    return@forEach
//                                }
//                            }
//                        }
//                    }
//                    SpringAnimationUtil.endRotatoSpringViewAnimation(sendWalletMore) { animation, canceled, value, velocity -> }
//                }
//            })
//            SpringAnimationUtil.startRotatoSpringViewAnimation(sendWalletMore) { animation, canceled, value, velocity -> }
//        }
//    }

//    private fun showSpinnerPopWindow() {
//        var ethWalletList = AppConfig.instance.daoSession.ethWalletDao.loadAll()
//        if (ethWalletList.size > 0) {
//            PopWindowUtil.showSharePopWindow(activity!!, walletMore, ethWalletList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
//                override fun onSelect(content: String) {
//                    if ("" != content) {
//                        etReceiveAddress.setText(content)
//                    }
//                    SpringAnimationUtil.endRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
//                }
//            })
//            SpringAnimationUtil.startRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
//        }
//    }

    override fun setupFragmentComponent() {
        DaggerOrderSellComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .orderSellModule(OrderSellModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: OrderSellContract.OrderSellContractPresenter) {
        mPresenter = presenter as OrderSellPresenter
    }

    override fun initDataFromLocal() {

    }

    fun generateEntrustSellOrder() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
package com.stratagile.qlink.ui.activity.otc

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.eventbus.ChangeCurrency
import com.stratagile.qlink.utils.PopWindowUtil
import com.stratagile.qlink.utils.SpringAnimationUtil
import com.stratagile.qlink.utils.UserUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import kotlinx.android.synthetic.main.activity_sell_qgas.*
import kotlinx.android.synthetic.main.fragment_order_sell.*
import kotlinx.android.synthetic.main.fragment_order_sell.etReceiveAddress
import kotlinx.android.synthetic.main.fragment_order_sell.etSendAddress
import kotlinx.android.synthetic.main.fragment_order_sell.sendWalletMore
import kotlinx.android.synthetic.main.fragment_order_sell.tvNext
import kotlinx.android.synthetic.main.fragment_order_sell.walletMore
import org.greenrobot.eventbus.EventBus

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
            if (etMinAmount.text.toString().trim().toInt() > etMaxAmount.text.toString().trim().toInt()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (etMaxAmount.text.toString().trim().toInt() > etAmount.text.toString().trim().toInt()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (!ETHWalletUtils.isETHValidAddress(etReceiveAddress.text.toString())) {
                toast("Illegal Receipt Address")
                return@setOnClickListener
            }
            if ("".equals(etSendAddress.text.toString())) {
                toast("Please select a qlc wallet to payment")
                return@setOnClickListener
            }
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
            map.put("usdtAddress", etReceiveAddress.text.toString())
            mPresenter.sendQgas(etAmount.text.toString(), ConstantValue.mainAddressData.qlcchian.address, map)
        }
        walletMore.setOnClickListener {
            showSpinnerPopWindow()
        }

        qlcAccounList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounList.size > 0) {
            qlcAccounList.forEach {
                if (it.isCurrent()) {
                    etSendAddress.text = it.address
                    return@forEach
                }
            }
        }
        sendWalletMore.setOnClickListener {
            showSendQGASWalletPopWindow()
        }
    }

    private fun showSendQGASWalletPopWindow() {
        if (qlcAccounList.size > 0) {
            PopWindowUtil.showSharePopWindow(activity!!, sendWalletMore, qlcAccounList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
                override fun onSelect(content: String) {
                    if ("" != content) {
                        etSendAddress.setText(content)
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
                                if (it.address.equals(content)) {
                                    it.setIsCurrent(true)
                                    AppConfig.getInstance().daoSession.qlcAccountDao.update(it)
                                    EventBus.getDefault().post(ChangeCurrency())
                                    return@forEach
                                }
                            }
                        }
                    }
                    SpringAnimationUtil.endRotatoSpringViewAnimation(sendWalletMore) { animation, canceled, value, velocity -> }
                }
            })
            SpringAnimationUtil.startRotatoSpringViewAnimation(sendWalletMore) { animation, canceled, value, velocity -> }
        }
    }

    private fun showSpinnerPopWindow() {
        var ethWalletList = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        if (ethWalletList.size > 0) {
            PopWindowUtil.showSharePopWindow(activity!!, walletMore, ethWalletList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
                override fun onSelect(content: String) {
                    if ("" != content) {
                        etReceiveAddress.setText(content)
                    }
                    SpringAnimationUtil.endRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
                }
            })
            SpringAnimationUtil.startRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
        }
    }

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
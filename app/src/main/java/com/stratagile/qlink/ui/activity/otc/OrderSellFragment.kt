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
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.UserUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import kotlinx.android.synthetic.main.fragment_order_sell.etAmount
import kotlinx.android.synthetic.main.fragment_order_sell.etMaxAmount
import kotlinx.android.synthetic.main.fragment_order_sell.etMinAmount
import kotlinx.android.synthetic.main.fragment_order_sell.etReceiveAddress
import kotlinx.android.synthetic.main.fragment_order_sell.etUnitPrice
import kotlinx.android.synthetic.main.fragment_order_sell.tvNext

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

    @Inject
    lateinit internal var mPresenter: OrderSellPresenter

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
package com.stratagile.qlink.ui.activity.otc

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
import com.stratagile.qlink.utils.UserUtils
import kotlinx.android.synthetic.main.fragment_order_buy.*
import kotlinx.android.synthetic.main.fragment_order_sell.*
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
            if (ConstantValue.mainAddressData == null) {
                toast("main address is null")
                return@setOnClickListener
            }
            showProgressDialog()
            var map = mutableMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
            map.put("type", ConstantValue.orderTypeSell)
            map.put("unitPrice", etUnitPrice.text.toString())
            map.put("totalAmount", etAmount.text.toString())
            map.put("minAmount", etMinAmount.text.toString())
            map.put("maxAmount", etMaxAmount.text.toString())
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
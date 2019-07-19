package com.stratagile.qlink.ui.activity.otc

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.utils.UserUtils
import kotlinx.android.synthetic.main.fragment_order_buy.*

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/08 17:24:58
 */

class OrderBuyFragment : BaseFragment(), OrderBuyContract.View {
    override fun generateBuyQgasOrderSuccess() {
        toast("success")
        closeProgressDialog()
        activity?.finish()
    }

    @Inject
    lateinit internal var mPresenter: OrderBuyPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_buy, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNext.setOnClickListener {
            if (ConstantValue.currentUser == null) {
                startActivity(Intent(activity, AccountActivity::class.java))
                return@setOnClickListener
            }
            var map = mutableMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
            map.put("type", ConstantValue.orderTypeBuy)
            map.put("unitPrice", etUnitPrice.text.toString().trim())
            map.put("totalAmount", etAmount.text.toString().trim())
            map.put("minAmount", etMinAmount.text.toString().trim())
            map.put("maxAmount", etMaxAmount.text.toString().trim())
            map.put("qgasAddress",etReceiveAddress.text.toString().trim())
            map.put("usdtAddress", "")
            map.put("fromAddress", "")
            map.put("txid", "")
            mPresenter.generateBuyQgasOrder(map)
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
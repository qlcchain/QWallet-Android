package com.stratagile.qlink.ui.activity.otc

import android.os.Bundle
import android.view.View
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.entity.otc.TradeOrderList
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderDetailComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderDetailContract
import com.stratagile.qlink.ui.activity.otc.module.OrderDetailModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderDetailPresenter
import com.stratagile.qlink.ui.adapter.otc.EntrustOrderTradeOrderListAdapter
import com.stratagile.qlink.utils.AccountUtil
import kotlinx.android.synthetic.main.activity_order_detail.*
import java.math.BigDecimal
import java.util.HashMap

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/10 10:03:30
 * 委托订单详情页面
 */

class OrderDetailActivity : BaseActivity(), OrderDetailContract.View {

    override fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo) {
        this.entrustOrderInfo = entrustOrderInfo
        tvQgasAmount.text = entrustOrderInfo.order.totalAmount.toString() + " QGAS"
        tvUnitPrice.text = entrustOrderInfo.order.unitPrice.toString() + " QGAS/USDT"
        tvQgasVolume.text = BigDecimal.valueOf(entrustOrderInfo.order.getMinAmount()).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(entrustOrderInfo.order.getMaxAmount()).stripTrailingZeros().toPlainString() + " QGAS"
        if (entrustOrderInfo.order.type.equals(ConstantValue.orderTypeBuy)) {
            tvOrderType.text = "委托BUY QGAS"
            tvDealQgasAmounnt.text = "+" + entrustOrderInfo.order.completeAmount.toString() + " QGAS"
            tvDealQgasAmounnt.setTextColor(resources.getColor(R.color.mainColor))
            tvOrderType.setTextColor(resources.getColor(R.color.mainColor))
            when (entrustOrderInfo.order.status) {
                "NORMAL" -> {
                    if (entrustOrderInfo.order.totalAmount - entrustOrderInfo.order.completeAmount < entrustOrderInfo.order.minAmount) {
                        //交易量不足
                        tvOrderState.text = "交易量不足"
                        tvOrderStateTip.text = "订单会按照剩余量全部交易"
                        llOrderState.setBackgroundColor(resources.getColor(R.color.color_d0021b))
                    } else {
                        tvOrderState.text = "挂单正常"
                        tvOrderStateTip.text = "正常"
                        llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    }
                }
                "END" -> {
                    tvOrderState.text = "委托完成"
                    tvOrderStateTip.text = "委托完成"
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_01b5ab))
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                }
                "CANCEL" -> {
                    tvOrderState.text = "已撤单"
                    tvOrderStateTip.text = "撤销剩余的委托"
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_808080))
                }
                else -> {
                }
            }
        } else {
            tvOrderType.text = "委托SELL QGAS"
            tvOrderType.setTextColor(resources.getColor(R.color.color_ff3669))
            tvDealQgasAmounnt.text = "-" + entrustOrderInfo.order.completeAmount.toString() + " QGAS"
            tvDealQgasAmounnt.setTextColor(resources.getColor(R.color.color_ff3669))
            when (entrustOrderInfo.order.status) {
                "NORMAL" -> {
                    if (entrustOrderInfo.order.totalAmount - entrustOrderInfo.order.completeAmount < entrustOrderInfo.order.minAmount) {
                        //交易量不足
                        tvOrderState.text = "交易量不足"
                        tvOrderStateTip.text = "订单会按照剩余量全部交易"
                        llOrderState.setBackgroundColor(resources.getColor(R.color.color_d0021b))
                    } else {
                        tvOrderState.text = "挂单正常"
                        tvOrderStateTip.text = "正常"
                        llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    }
                }
                "END" -> {
                    tvOrderState.text = "委托完成"
                    tvOrderStateTip.text = "委托完成"
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_01b5ab))
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                }
                "CANCEL" -> {
                    tvOrderState.text = "已撤单"
                    tvOrderStateTip.text = "撤销剩余的委托"
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_808080))
                }
                else -> {
                }
            }
        }
        getTradeOrderList()
    }

    fun getTradeOrderList() {
        val map = HashMap<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["page"] = "1"
        map["status"] = ""
        map["size"] = "20"
        map["entrustOrderId"] = entrustOrderInfo.order.id
        mPresenter.getTradeOrderList(map)
    }

    override fun setTradeOrderList(tradeOrderList: TradeOrderList) {
        entrustOrderTradeOrderListAdapter = EntrustOrderTradeOrderListAdapter(tradeOrderList.orderList)
        recyclerView.adapter = entrustOrderTradeOrderListAdapter
    }


    @Inject
    internal lateinit var mPresenter: OrderDetailPresenter

    lateinit var order : EntrustOrderList.OrderListBean
    lateinit var entrustOrderInfo : EntrustOrderInfo
    lateinit var entrustOrderTradeOrderListAdapter: EntrustOrderTradeOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_order_detail)
    }
    override fun initData() {
        title.text = "Detail"
        order = intent.getParcelableExtra("order")
        var map = hashMapOf<String, String>()
        map.put("entrustOrderId", order.id)
        mPresenter.getEntrustOrderDetail(map)
    }

    override fun setupActivityComponent() {
       DaggerOrderDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .orderDetailModule(OrderDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OrderDetailContract.OrderDetailContractPresenter) {
            mPresenter = presenter as OrderDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
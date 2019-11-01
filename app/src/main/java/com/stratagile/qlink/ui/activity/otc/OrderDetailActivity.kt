package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pawegio.kandroid.toast
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
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.activity_order_detail.llOpreate
import kotlinx.android.synthetic.main.activity_order_detail.llOrderState
import kotlinx.android.synthetic.main.activity_order_detail.tvOrderState
import kotlinx.android.synthetic.main.activity_order_detail.tvOrderStateTip
import kotlinx.android.synthetic.main.activity_order_detail.tvOrderType
import kotlinx.android.synthetic.main.activity_order_detail.tvQgasAmount
import kotlinx.android.synthetic.main.activity_order_detail.tvReceiveAddress
import kotlinx.android.synthetic.main.activity_order_detail.tvUnitPrice
import kotlinx.android.synthetic.main.activity_trade_order_detail.*
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
    override fun revokeOrderSuccess() {
        toast(getString(R.string.success))
        closeProgressDialog()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo) {
        this.entrustOrderInfo = entrustOrderInfo
        tvQgasAmount.text = entrustOrderInfo.order.totalAmount.toString() + " " + entrustOrderInfo.order.tradeToken
        tvUnitPrice.text = BigDecimal.valueOf(entrustOrderInfo.order.unitPrice).stripTrailingZeros().toPlainString() + " " + entrustOrderInfo.order.payToken
        tvQgasVolume.text = BigDecimal.valueOf(entrustOrderInfo.order.getMinAmount()).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(entrustOrderInfo.order.getMaxAmount()).stripTrailingZeros().toPlainString() + " QGAS"
        if (entrustOrderInfo.order.type.equals(ConstantValue.orderTypeBuy)) {
            tvOrderType.text = getString(R.string.buy) + " " + entrustOrderInfo.order.tradeToken
            tvReceiveAddress.text = entrustOrderInfo.order.qgasAddress
            tvDealQgasAmounnt.text = (entrustOrderInfo.order.totalAmount - entrustOrderInfo.order.completeAmount - entrustOrderInfo.order.lockingAmount).toString() + " " + entrustOrderInfo.order.tradeToken
            tvReceiveAddressTip.text = getString(R.string.receivable_address)
            tvDealQgasAmounnt.setTextColor(resources.getColor(R.color.mainColor))
            tvOrderType.setTextColor(resources.getColor(R.color.mainColor))
            when (entrustOrderInfo.order.status) {
                "NORMAL" -> {
                    tvOrderState.text = getString(R.string.active)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                }
                "TXID_ERROR" -> {
                    tvOrderState.text = getString(R.string.active)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                }
                "OVERTIME" -> {
                    tvOrderState.text = getString(R.string.active)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                }
                "PENDING" -> {
                    tvOrderState.text = getString(R.string.pending)
                    tvOrderStateTip.text = ""
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_808080))
                }
                "END" -> {
                    tvOrderState.text = getString(R.string.completed)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_01b5ab))
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                }
                "CANCEL" -> {
                    tvOrderState.text = getString(R.string.revoked)
                    tvOrderStateTip.text = getString(R.string.revoke)
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_808080))
                }
                else -> {
                }
            }
        } else {
            tvOrderType.text = getString(R.string.sell) + " " + entrustOrderInfo.order.tradeToken
            tvReceiveAddress.text = entrustOrderInfo.order.usdtAddress
            tvOrderType.setTextColor(resources.getColor(R.color.color_ff3669))
            tvDealQgasAmounnt.text = (entrustOrderInfo.order.totalAmount - entrustOrderInfo.order.completeAmount - entrustOrderInfo.order.lockingAmount).toString() + " " + entrustOrderInfo.order.tradeToken
            tvReceiveAddressTip.text = getString(R.string.receivable_address)
            tvDealQgasAmounnt.setTextColor(resources.getColor(R.color.color_ff3669))
            when (entrustOrderInfo.order.status) {
                "NORMAL" -> {
                    tvOrderState.text = getString(R.string.active)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                }
                "OVERTIME" -> {
                    tvOrderState.text = getString(R.string.active)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                }
                "TXID_ERROR" -> {
                    tvOrderState.text = getString(R.string.active)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                }
                "PENDING" -> {
                    tvOrderState.text = getString(R.string.pending)
                    tvOrderStateTip.text = ""
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_808080))
                }
                "END" -> {
                    tvOrderState.text = getString(R.string.completed)
                    tvOrderStateTip.text = ""
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_01b5ab))
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                }
                "CANCEL" -> {
                    tvOrderState.text = getString(R.string.revoked)
                    tvOrderStateTip.text = getString(R.string.revoke)
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_808080))
                    llOpreate.visibility = View.GONE
                    tvOpreate.visibility = View.GONE
                }
                else -> {
                }
            }
        }
        getTradeOrderList()
        tvOpreate.setOnClickListener {
            revokeOrder()
        }
    }

    /**
     * 撤销挂单
     */
    fun revokeOrder() {
        showProgressDialog()
        val map = HashMap<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["entrustOrderId"] = entrustOrderInfo.order.id
        mPresenter.revokeOrder(map)
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
        if (tradeOrderList.orderList.size == 0) {
            tvRecord.visibility = View.GONE
        }
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
        tvReceiveAddress.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvReceiveAddress.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
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
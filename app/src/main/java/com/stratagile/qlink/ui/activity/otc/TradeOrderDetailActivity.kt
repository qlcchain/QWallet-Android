package com.stratagile.qlink.ui.activity.otc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pawegio.kandroid.clipboardManager
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.QrEntity
import com.stratagile.qlink.entity.otc.TradeOrderDetail
import com.stratagile.qlink.ui.activity.otc.component.DaggerTradeOrderDetailComponent
import com.stratagile.qlink.ui.activity.otc.contract.TradeOrderDetailContract
import com.stratagile.qlink.ui.activity.otc.module.TradeOrderDetailModule
import com.stratagile.qlink.ui.activity.otc.presenter.TradeOrderDetailPresenter
import com.stratagile.qlink.ui.activity.wallet.WalletQRCodeActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.ToastUtil
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_trade_order_detail.*
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/17 11:14:13
 */

class TradeOrderDetailActivity : BaseActivity(), TradeOrderDetailContract.View {
    override fun setTradeOrderDetail(tradeOrderDetail: TradeOrderDetail) {
        this.mTradeOrderDetail = tradeOrderDetail
        tvNickName.text = tradeOrderDetail.order.nickname
        tvQgasAmount.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.qgasAmount).stripTrailingZeros().toPlainString() + " QGAS"
        tvAmountUsdt.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.usdtAmount).stripTrailingZeros().toPlainString() + " USDT"
        tvUnitPrice.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.unitPrice).stripTrailingZeros().toPlainString() + " QGAS/USDT"
        tvOrderId.text = tradeOrderDetail.order.id
        tvOrderTime.text = tradeOrderDetail.order.orderTime
        if (mTradeOrderDetail.order.buyerId.equals(ConstantValue.currentUser.userId)) {
            //我买
            tvOtherUser.text = "卖家"
            tvOrderType.text = ConstantValue.orderTypeBuy
            tvOrderType.setTextColor(resources.getColor(R.color.mainColor))
            when(tradeOrderDetail.order.status) {
                "QGAS_TO_PLATFORM"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_ff3669))
                    tvOrderState.text = "未付USDT"
                    tvOrderState1.text = "未付USDT"
                    tvOrderStateTip.text = "还剩30分钟，关闭订单"
                    receiveAddressTip.text = "GO-QLC Address to receive QGAS"
                    tvReceiveAddress.text = tradeOrderDetail.order.qgasToAddress
                    tvOpreate1.visibility = View.VISIBLE
                    tvOpreate2.visibility = View.VISIBLE
                    tvOpreate3.visibility = View.VISIBLE
                    viewLine.visibility = View.GONE
                    tvReceiveAddress.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvReceiveAddress.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    tvOpreate3.setOnClickListener {
                        val qrEntity = QrEntity(tradeOrderDetail.order.usdtToAddress, "USDT" + " Receivable Address", "usdt", 2)
                        val intent = Intent(this, UsdtReceiveAddressActivity::class.java)
                        intent.putExtra("qrentity", qrEntity)
                        startActivity(intent)
                    }
                    tvOpreate2.setOnClickListener {
                        markAsPaid()
                    }
                    var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.orderTime)) / 1000
                    remainTime = tradeOrderExistTime - remainTime
                    mdDisposable = Flowable.intervalRange(0, remainTime, 0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                var fen = (remainTime - it) / 60
                                var miao = (remainTime - it) % 60
                                if (fen > 0) {
                                    tvOrderStateTip.setText("还剩 " + fen + " : " + miao + " 分钟，关闭订单")
                                } else {
                                    tvOrderStateTip.setText("还剩 " + miao + " 秒，关闭订单")
                                }
                            }
                            .doOnComplete {
                                getTradeOrderDetail()
                            }
                            .subscribe();
                }
                "USDT_PAID" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    tvOrderState.text = "已确认付款"
                    tvOrderState1.text = "等待对方确认"
                    tvOrderStateTip.text = "等待对方确认收款，30分钟后可申请申诉"
                    receiveAddressTip.text = "GO-QLC Address to receive QGAS"
                    tvReceiveAddress.text = tradeOrderDetail.order.qgasToAddress
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.VISIBLE
                    viewLine.visibility = View.GONE
                    tvOpreate3.text = "我要申诉"
                    var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.orderTime)) / 1000
                    remainTime = tradeOrderExistTime - remainTime
                    if (remainTime > 0) {
                        mdDisposable = Flowable.intervalRange(0, remainTime, 0, 1, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    var fen = (remainTime - it) / 60
                                    var miao = (remainTime - it) % 60
                                    if (fen > 0) {
                                        tvOrderStateTip.setText("等待对方确认收款，" + fen + " : " + miao + " 分钟后可申请申诉")
                                    } else {
                                        tvOrderStateTip.setText("等待对方确认收款， " + miao + " 秒后可申请申诉")
                                    }
                                }
                                .doOnComplete {
                                    tvOrderStateTip.text = "等待对方确认收款，可申请申诉"
                                }
                                .subscribe();
                    } else {
                        tvOrderStateTip.text = "等待对方确认收款，可申请申诉"
                    }
                    tvOpreate3.setOnClickListener {

                    }
                }
                "OVERTIME" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_999))
                    tvOrderState.text = "Closed"
                    tvOrderState1.text = "Closed"
                    tvOrderStateTip.text = "The order is closed by the system"
                    receiveAddressTip.text = "GO-QLC Address to receive QGAS"
                    tvReceiveAddress.text = tradeOrderDetail.order.qgasToAddress
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.GONE
                    viewLine.visibility = View.GONE
                }
            }
        } else {
            //我卖
            tvOtherUser.text = "买家"
            tvOrderType.text = ConstantValue.orderTypeSell
            tvOrderType.setTextColor(resources.getColor(R.color.color_ff3669))
            when(tradeOrderDetail.order.status) {
                "QGAS_TO_PLATFORM"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    tvOrderState.text = "等待买家确认付款"
                    tvOrderState1.text = "等待买家确认付款"
                    tvOrderStateTip.text = "还剩30分钟，关闭订单"
                    receiveAddressTip.text = "ERC20 Address to receive USDT"
                    tvReceiveAddress.text = tradeOrderDetail.order.usdtToAddress
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.GONE
                    viewLine.visibility = View.GONE
                    var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.orderTime)) / 1000
                    remainTime = tradeOrderExistTime - remainTime
                    mdDisposable = Flowable.intervalRange(0, remainTime, 0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                var fen = (remainTime - it) / 60
                                var miao = (remainTime - it) % 60
                                if (fen > 0) {
                                    tvOrderStateTip.setText("还剩 " + fen + " : " + miao + " 分钟，关闭订单")
                                } else {
                                    tvOrderStateTip.setText("还剩 " + miao + " 秒，关闭订单")
                                }
                            }
                            .doOnComplete {
                                getTradeOrderDetail()
                            }
                            .subscribe();
                }
                "USDT_PAID"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    tvOrderState.text = "买家已确认付款"
                    tvOrderState1.text = "买家已确认付款"
                    tvOrderStateTip.text = "等待查收USDT，30分钟后可申请申诉"
                    receiveAddressTip.text = "ERC20 Address to receive USDT"
                    tvReceiveAddress.text = tradeOrderDetail.order.usdtToAddress
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.VISIBLE
                    tvOpreate3.visibility = View.VISIBLE
                    viewLine.visibility = View.VISIBLE
                    tvOpreate2.text = "我要申诉"
                    tvOpreate3.text = "确认查收"
                    llPayAddress.visibility = View.VISIBLE
                    tvPayAddressTip.text = "Buyer's ERC-20 address"
                    tvPayAddress.text = tradeOrderDetail.order.usdtFromAddress
                    tvPayAddress.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvPayAddress.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                }
            }
        }
    }

    /**
     * 买家标记为已经付款
     */
    fun markAsPaid() {
        var map = mutableMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["tradeOrderId"] = tradeOrderId
        map["txid"] = "0x45fc156524f34e15a116d27cb3f8374318b7a3ad6ea69c288847854a09068fca"
        mPresenter.markAsPaid(map)
    }

    override fun onDestroy() {
        mdDisposable?.dispose()
        super.onDestroy()
    }

    @Inject
    internal lateinit var mPresenter: TradeOrderDetailPresenter
    private var mdDisposable: Disposable? = null
    var tradeOrderId = ""
    lateinit var mTradeOrderDetail: TradeOrderDetail
    var tradeOrderExistTime = 30 * 60

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_trade_order_detail)
    }
    override fun initData() {
        title.text = "Detail"
        tradeOrderId = intent.getStringExtra("tradeOrderId")
        getTradeOrderDetail()
    }

    fun getTradeOrderDetail() {
        mdDisposable?.dispose()
        var map = mutableMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["tradeOrderId"] = tradeOrderId
        mPresenter.getTradeOrderDetail(map)
    }

    override fun setupActivityComponent() {
       DaggerTradeOrderDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .tradeOrderDetailModule(TradeOrderDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: TradeOrderDetailContract.TradeOrderDetailContractPresenter) {
            mPresenter = presenter as TradeOrderDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
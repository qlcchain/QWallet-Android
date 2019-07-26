package com.stratagile.qlink.ui.activity.otc

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
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
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.view.SweetAlertDialog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_trade_order_detail.*
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/17 11:14:13
 */

class TradeOrderDetailActivity : BaseActivity(), TradeOrderDetailContract.View {
    override fun markAsPaidSuccess() {
        closeProgressDialog()
        toast(getString(R.string.success))
        finish()
    }

    override fun confirmCheckSuccess() {
        closeProgressDialog()
        toast(getString(R.string.success))
        finish()
    }

    @SuppressLint("SetTextI18n")
    override fun setTradeOrderDetail(tradeOrderDetail: TradeOrderDetail) {
        this.mTradeOrderDetail = tradeOrderDetail
        tvNickName.text = tradeOrderDetail.order.nickname
        tvQgasAmount.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.qgasAmount).stripTrailingZeros().toPlainString() + " QGAS"
        tvAmountUsdt.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.usdtAmount).stripTrailingZeros().toPlainString() + " USDT"
        tvUnitPrice.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.unitPrice).stripTrailingZeros().toPlainString() + " QGAS/USDT"
        tvOrderId.text = tradeOrderDetail.order.number
        tvReceiveAddress.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvReceiveAddress.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
        tvAmountUsdt.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvAmountUsdt.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
        tvOrderTime.text = tradeOrderDetail.order.orderTime
        if (mTradeOrderDetail.order.buyerId.equals(ConstantValue.currentUser.userId)) {
            //我买
            tvOtherUser.text = getString(R.string.seller)
            tvOrderType.text = ConstantValue.orderTypeBuy
            tvAmountUsdt.setTextColor(resources.getColor(R.color.color_ff3669))
            tvOrderType.setTextColor(resources.getColor(R.color.mainColor))
            receiveAddressTip.text = "QLC Chain Address to receive QGAS"
            tvReceiveAddress.text = tradeOrderDetail.order.qgasToAddress
            when(tradeOrderDetail.order.status) {
                "QGAS_TO_PLATFORM"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_ff3669))
                    tvOrderState.text = getString(R.string.wait_buyer_payment1)
                    tvOrderState1.text = getString(R.string.wait_buyer_payment1)
                    tvOrderStateTip.text = "The order will be closed automatically, if no further confirmation within 30 minutes."
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
                        showEnterTxIdDialog()
                    }
                    var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.orderTime)) / 1000
                    remainTime = tradeOrderExistTime - remainTime
                    mdDisposable = Flowable.intervalRange(0, remainTime, 0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                var fen = (remainTime - it) / 60
                                var miao = (remainTime - it) % 60
                                if (fen > 0) {
                                    tvOrderStateTip.setText("The order will be closed automatically, if no further confirmation within " + fen + " : " + miao + " minutes.")
                                } else {
                                    tvOrderStateTip.setText("The order will be closed automatically, if no further confirmation within " + miao + " minutes.")
                                }
                            }
                            .doOnComplete {
                                getTradeOrderDetail()
                            }
                            .subscribe();
                }
                "USDT_PAID" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    tvOrderState.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderState1.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderStateTip.text = ""
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.VISIBLE
                    llOrderPayTime.visibility = View.VISIBLE
                    tvOrderPayTime.text = tradeOrderDetail.order.buyerConfirmDate
                    viewLine.visibility = View.GONE
                    llTxId.visibility = View.VISIBLE
                    tvTxId.text = tradeOrderDetail.order.txid
                    tvTxId.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvTxId.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                        tvOpreate3.text = getString(R.string.appeal)
                    } else {
                        if (tradeOrderDetail.order.appealerId.equals(ConstantValue.currentUser.userId)) {
                            tvOpreate3.text = getString(R.string.check_the_appeal_result)
                        } else {
                            tvOpreate3.text = getString(R.string.appealed)
                        }
                    }
                    tvOpreate3.setOnClickListener {
                        var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.buyerConfirmDate)) / 1000
                        remainTime = tradeOrderExistTime - remainTime
                        if (remainTime > 0) {
                            alert(getString(R.string.please_be_patiently_it_hasnot_been_30_minutes)) {
                                positiveButton("OK") { dismiss() }
                            }.show()
                        } else {
                            if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                                startActivityForResult(Intent(this, AppealActivity::class.java).putExtra("tradeOrder", mTradeOrderDetail.order), 0)
                            } else {
                                startActivityForResult(Intent(this, AppealDetailActivity::class.java).putExtra("tradeOrderId", mTradeOrderDetail.order.id), 0)
                            }
                        }
                    }
                }
                "USDT_PENDING" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    tvOrderState.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderState1.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderStateTip.text = ""
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.VISIBLE
                    llOrderPayTime.visibility = View.VISIBLE
                    tvOrderPayTime.text = tradeOrderDetail.order.buyerConfirmDate
                    viewLine.visibility = View.GONE
                    llTxId.visibility = View.VISIBLE
                    tvTxId.text = tradeOrderDetail.order.txid
                    tvTxId.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvTxId.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                        tvOpreate3.text = getString(R.string.appeal)
                    } else {
                        if (tradeOrderDetail.order.appealerId.equals(ConstantValue.currentUser.userId)) {
                            tvOpreate3.text = getString(R.string.check_the_appeal_result)
                        } else {
                            tvOpreate3.text = getString(R.string.appealed)
                        }
                    }
                    tvOpreate3.setOnClickListener {
                        var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.buyerConfirmDate)) / 1000
                        remainTime = tradeOrderExistTime - remainTime
                        if (remainTime > 0) {
                            alert(getString(R.string.please_be_patiently_it_hasnot_been_30_minutes)) {
                                positiveButton("OK") { dismiss() }
                            }.show()
                        } else {
                            if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                                startActivityForResult(Intent(this, AppealActivity::class.java).putExtra("tradeOrder", mTradeOrderDetail.order), 0)
                            } else {
                                startActivityForResult(Intent(this, AppealDetailActivity::class.java).putExtra("tradeOrderId", mTradeOrderDetail.order.id), 0)
                            }
                        }
                    }
                }
                "OVERTIME" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_999))
                    tvOrderState.text = getString(R.string.closed)
                    tvOrderState1.text = getString(R.string.closed)
                    tvOrderStateTip.text = getString(R.string.the_order_closed_by_the_system)
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.GONE
                    viewLine.visibility = View.GONE
                }
                "QGAS_PAID" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_4accaf))
                    tvOrderState.text = getString(R.string.successful_deal)
                    tvOrderState1.text = getString(R.string.successful_deal)
                    tvOrderStateTip.text = ""
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.GONE
                    llOpreate.visibility = View.GONE
                    viewLine.visibility = View.GONE

                    llOrderPayTime.visibility = View.VISIBLE
                    tvOrderPayTime.text = tradeOrderDetail.order.buyerConfirmDate

                    llTxId.visibility = View.VISIBLE
                    tvTxId.text = tradeOrderDetail.order.txid

                    llOrderSuccessTime.visibility = View.VISIBLE
                    tvOrderSuccessTime.text = tradeOrderDetail.order.sellerConfirmDate

                    llPayAddress.visibility = View.VISIBLE
                    tvPayAddressTip.text = "Buyer's ERC-20 address"
                    tvPayAddress.text = tradeOrderDetail.order.usdtFromAddress

                }
            }
        } else {
            //我卖
            tvOtherUser.text = getString(R.string.buyer)
            tvOrderType.text = ConstantValue.orderTypeSell
            tvAmountUsdt.setTextColor(resources.getColor(R.color.color_108ee9))
            tvOrderType.setTextColor(resources.getColor(R.color.color_ff3669))
            receiveAddressTip.text = "ERC20 Address to receive USDT"
            tvReceiveAddress.text = tradeOrderDetail.order.usdtToAddress
            when(tradeOrderDetail.order.status) {
                "QGAS_TO_PLATFORM"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.mainColor))
                    tvOrderState.text = getString(R.string.wait_buyer_payment1)
                    tvOrderState1.text = getString(R.string.wait_buyer_payment1)
                    tvOrderStateTip.text = "The order will be closed automatically, if no further confirmation within 30 minutes."
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
                                    tvOrderStateTip.setText("The order will be closed automatically, if no further confirmation within " + fen + " : " + miao + " minutes.")
                                } else {
                                    tvOrderStateTip.setText("The order will be closed automatically, if no further confirmation within " + miao + " minutes.")
                                }
                            }
                            .doOnComplete {
                                getTradeOrderDetail()
                            }
                            .subscribe();
                }
                "USDT_PAID"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_ff3669))
                    tvOrderState.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderState1.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderStateTip.text = getString(R.string.please_check_your_usdt_balance)
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.VISIBLE
                    tvOpreate3.visibility = View.VISIBLE
                    viewLine.visibility = View.VISIBLE
                    tvOpreate2.text = getString(R.string.appeal)
                    tvOpreate3.text = getString(R.string.i_have_received)
                    llPayAddress.visibility = View.VISIBLE
                    tvPayAddressTip.text = "Buyer's ERC-20 address"
                    tvPayAddress.text = tradeOrderDetail.order.usdtFromAddress
                    tvPayAddress.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvPayAddress.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    llTxId.visibility = View.VISIBLE
                    tvTxId.text = tradeOrderDetail.order.txid

                    llOrderPayTime.visibility = View.VISIBLE
                    tvOrderPayTime.text = tradeOrderDetail.order.buyerConfirmDate

                    tvTxId.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvTxId.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    tvOpreate3.setOnClickListener {
                        showReceivedDialog()
                    }
                    if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                        tvOpreate2.text = getString(R.string.appeal)
                    } else {
                        if (tradeOrderDetail.order.appealerId.equals(ConstantValue.currentUser.userId)) {
                            tvOpreate2.text = getString(R.string.check_the_appeal_result)
                        } else {
                            tvOpreate2.text = getString(R.string.appealed)
                        }
                    }
                    tvOpreate2.setOnClickListener {
                        var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.buyerConfirmDate)) / 1000
                        remainTime = tradeOrderExistTime - remainTime
                        if (remainTime > 0) {
                            alert(getString(R.string.please_be_patiently_it_hasnot_been_30_minutes)) {
                                positiveButton("OK") { dismiss() }
                            }.show()
                        } else {
                            if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                                startActivityForResult(Intent(this, AppealActivity::class.java).putExtra("tradeOrder", mTradeOrderDetail.order), 0)
                            } else {
                                startActivityForResult(Intent(this, AppealDetailActivity::class.java).putExtra("tradeOrderId", mTradeOrderDetail.order.id), 0)
                            }
                        }
                    }
                }
                "USDT_PENDING"-> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_ff3669))
                    tvOrderState.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderState1.text = getString(R.string.buyer_has_comfirmed_the_payment)
                    tvOrderStateTip.text = getString(R.string.please_check_your_usdt_balance)
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.VISIBLE
                    tvOpreate3.visibility = View.VISIBLE
                    viewLine.visibility = View.VISIBLE
                    tvOpreate2.text = getString(R.string.appeal)
                    tvOpreate3.text = getString(R.string.i_have_received)
                    llPayAddress.visibility = View.VISIBLE
                    tvPayAddressTip.text = "Buyer's ERC-20 address"
                    tvPayAddress.text = tradeOrderDetail.order.usdtFromAddress
                    tvPayAddress.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvPayAddress.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    llTxId.visibility = View.VISIBLE
                    tvTxId.text = tradeOrderDetail.order.txid

                    llOrderPayTime.visibility = View.VISIBLE
                    tvOrderPayTime.text = tradeOrderDetail.order.buyerConfirmDate

                    tvTxId.setOnClickListener {
                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", tvTxId.text.toString())
                        cm.primaryClip = mClipData
                        ToastUtil.displayShortToast(getString(R.string.copy_success))
                    }
                    tvOpreate3.setOnClickListener {
                        showReceivedDialog()
                    }
                    if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                        tvOpreate2.text = getString(R.string.appeal)
                    } else {
                        if (tradeOrderDetail.order.appealerId.equals(ConstantValue.currentUser.userId)) {
                            tvOpreate2.text = getString(R.string.check_the_appeal_result)
                        } else {
                            tvOpreate2.text = getString(R.string.appealed)
                        }
                    }
                    tvOpreate2.setOnClickListener {
                        var remainTime = (System.currentTimeMillis() - TimeUtil.timeStamp(tradeOrderDetail.order.buyerConfirmDate)) / 1000
                        remainTime = tradeOrderExistTime - remainTime
                        if (remainTime > 0) {
                            alert(getString(R.string.please_be_patiently_it_hasnot_been_30_minutes)) {
                                positiveButton("OK") { dismiss() }
                            }.show()
                        } else {
                            if (tradeOrderDetail.order.appealStatus.equals("NO")) {
                                startActivityForResult(Intent(this, AppealActivity::class.java).putExtra("tradeOrder", mTradeOrderDetail.order), 0)
                            } else {
                                startActivityForResult(Intent(this, AppealDetailActivity::class.java).putExtra("tradeOrderId", mTradeOrderDetail.order.id), 0)
                            }
                        }
                    }
                }
                "OVERTIME" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_999))
                    tvOrderState.text = getString(R.string.closed)
                    tvOrderState1.text = getString(R.string.closed)
                    tvOrderStateTip.text = getString(R.string.the_order_closed_by_the_system)
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.GONE
                    viewLine.visibility = View.GONE
                    llOpreate.visibility = View.GONE
                }
                "QGAS_PAID" -> {
                    llOrderState.setBackgroundColor(resources.getColor(R.color.color_4accaf))
                    tvOrderState.text = getString(R.string.successful_deal)
                    tvOrderState1.text = getString(R.string.successful_deal)
                    tvOrderStateTip.text = ""
                    tvOpreate1.visibility = View.GONE
                    tvOpreate2.visibility = View.GONE
                    tvOpreate3.visibility = View.GONE
                    llOpreate.visibility = View.GONE
                    viewLine.visibility = View.GONE

                    llOrderPayTime.visibility = View.VISIBLE
                    tvOrderPayTime.text = tradeOrderDetail.order.buyerConfirmDate

                    llTxId.visibility = View.VISIBLE
                    tvTxId.text = tradeOrderDetail.order.txid

                    llOrderSuccessTime.visibility = View.VISIBLE
                    tvOrderSuccessTime.text = tradeOrderDetail.order.sellerConfirmDate

                    llPayAddress.visibility = View.VISIBLE
                    tvPayAddressTip.text = "Buyer's ERC-20 address"
                    tvPayAddress.text = tradeOrderDetail.order.usdtFromAddress

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getTradeOrderDetail()
    }

    /**
     * 买家标记为已经付款
     */
    fun markAsPaid(txid : String) {
        showProgressDialog()
        var map = mutableMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["tradeOrderId"] = tradeOrderId
        map["txid"] = txid
        mPresenter.markAsPaid(map)
    }

    fun showEnterTxIdDialog() {
        //
        val view = View.inflate(this, R.layout.dialog_input_txid_layout, null)
        val etContent = view.findViewById<View>(R.id.etContent) as EditText//输入内容
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)//取消按钮
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(this)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        tvCancel.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
//            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            cm.primaryClip = ClipData.newPlainText("", etContent.text.toString().trim())
            sweetAlertDialog.cancel()
            markAsPaid(etContent.text.toString().trim())
        }
    }

    /**
     * 卖家确认已经收到usdt
     */
    fun confirmCheck() {
        showProgressDialog()
        var map = mutableMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["tradeOrderId"] = tradeOrderId
        mPresenter.confirmCheck(map)
    }

    fun showReceivedDialog() {
        alert("I've received USDT.") {
            positiveButton("OK") { confirmCheck() }
            negativeButton("Cancel") { dismiss() }
        }.show()
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
        var map = mutableMapOf<String, String>()
        if (ConstantValue.currentUser == null) {
            return
        }
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
package com.stratagile.qlink.ui.activity.otc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.otc.TradeOrderDetail
import com.stratagile.qlink.ui.activity.otc.component.DaggerAppealDetailComponent
import com.stratagile.qlink.ui.activity.otc.contract.AppealDetailContract
import com.stratagile.qlink.ui.activity.otc.module.AppealDetailModule
import com.stratagile.qlink.ui.activity.otc.presenter.AppealDetailPresenter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_appeal_detail.*
import kotlinx.android.synthetic.main.activity_appeal_detail.tvAmountUsdt
import kotlinx.android.synthetic.main.activity_appeal_detail.tvNickName
import kotlinx.android.synthetic.main.activity_appeal_detail.tvOrderId
import kotlinx.android.synthetic.main.activity_appeal_detail.tvOrderState1
import kotlinx.android.synthetic.main.activity_appeal_detail.tvOrderTime
import kotlinx.android.synthetic.main.activity_appeal_detail.tvOrderType
import kotlinx.android.synthetic.main.activity_appeal_detail.tvPayAddress
import kotlinx.android.synthetic.main.activity_appeal_detail.tvQgasAmount
import kotlinx.android.synthetic.main.activity_appeal_detail.tvTxId
import kotlinx.android.synthetic.main.activity_appeal_detail.tvUnitPrice
import java.math.BigDecimal

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/22 10:13:43
 */

class AppealDetailActivity : BaseActivity(), AppealDetailContract.View {

    override fun setTradeOrderDetail(tradeOrderDetail: TradeOrderDetail) {
        tvOrderAppealTime.text = tradeOrderDetail.order.appealDate
        if (tradeOrderDetail.order.appealerId.equals(tradeOrderDetail.order.sellerId)) {
            tvOrderAppealant.text = "Appellant：Seller"
        } else {
            tvOrderAppealant.text = "Appellant：Buyer"
        }

        llSubmit.setOnClickListener {
            startActivity(Intent(this, AppealSubmittedActivity::class.java).putExtra("orderDetail", tradeOrderDetail.order))
        }

        tvNickName.text = tradeOrderDetail.order.nickname
        tvQgasAmount.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.qgasAmount).stripTrailingZeros().toPlainString() + " QGAS"
        tvAmountUsdt.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.usdtAmount).stripTrailingZeros().toPlainString() + " USDT"
        tvUnitPrice.text = "" + BigDecimal.valueOf(tradeOrderDetail.order.unitPrice).stripTrailingZeros().toPlainString() + " QGAS/USDT"
        tvAmountUsdt.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvAmountUsdt.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
        tvOrderId.text = tradeOrderDetail.order.number
        tvOrderTime.text = tradeOrderDetail.order.orderTime
        tvTxId.text = tradeOrderDetail.order.txid
        tvTxId.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvTxId.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
        tvPayAddress.text = tradeOrderDetail.order.usdtFromAddress
        tvPayAddress.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", tvPayAddress.text.toString())
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }

        when(tradeOrderDetail.order.status) {
            "QGAS_TO_PLATFORM"-> {
                tvOrderState1.text = getString(R.string.wait_seller_confirmation)
            }
            "USDT_PAID"-> {
                tvOrderState1.text = getString(R.string.buyer_has_comfirmed_the_payment)
            }
            "OVERTIME" -> {
                tvOrderState1.text = getString(R.string.closed)
            }
            "QGAS_PAID" -> {
                tvOrderState1.text = getString(R.string.successful_deal)
            }
        }

        if (tradeOrderDetail.order.buyerId.equals(ConstantValue.currentUser.userId)) {
            //我买
            tvOrderType.text = ConstantValue.orderTypeBuy
            tvOrderType.setTextColor(resources.getColor(R.color.mainColor))
            tvOtherUser.text = getString(R.string.seller)
            tvAmountUsdt.setTextColor(resources.getColor(R.color.color_ff3669))
        } else {
            tvOrderType.text = ConstantValue.orderTypeSell
            tvOrderType.setTextColor(resources.getColor(R.color.color_ff3669))
            tvOtherUser.text = getString(R.string.buyer)
            tvAmountUsdt.setTextColor(resources.getColor(R.color.color_108ee9))
        }
        when(tradeOrderDetail.order.appealStatus) {
            "FAIL" ->{
                tvOrderAppealState.text = getString(R.string.appeal_failed)
                tvOrderAppealResult.visibility = View.VISIBLE
                tvOrderAppealResult.text = "Appeal Result：" + tradeOrderDetail.order.auditFeedback
            }
            "SUCCESS" -> {
                tvOrderAppealState.text = getString(R.string.successful_appeal)
                tvOrderAppealResult.visibility = View.VISIBLE
                tvOrderAppealResult.text = "Appeal Result：" + tradeOrderDetail.order.auditFeedback
            }
            "YES" -> {
                tvOrderAppealState.text = getString(R.string.wait_appeal_result)
                tvOrderAppealResult.visibility = View.GONE
            }

        }
    }

    @Inject
    internal lateinit var mPresenter: AppealDetailPresenter

    lateinit var mTradeOrderDetail: TradeOrderDetail
    var tradeOrderId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_appeal_detail)
    }
    override fun initData() {
        title.text = "Appeal Detail"
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
       DaggerAppealDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .appealDetailModule(AppealDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: AppealDetailContract.AppealDetailContractPresenter) {
            mPresenter = presenter as AppealDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
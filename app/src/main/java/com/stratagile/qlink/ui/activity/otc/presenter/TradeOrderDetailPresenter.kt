package com.stratagile.qlink.ui.activity.otc.presenter

import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.TradeOrderDetailContract
import com.stratagile.qlink.ui.activity.otc.TradeOrderDetailActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of TradeOrderDetailActivity
 * @date 2019/07/17 11:14:13
 */
class TradeOrderDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TradeOrderDetailContract.View) : TradeOrderDetailContract.TradeOrderDetailContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }

    fun getTradeOrderDetail(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeOrderInfo(map).subscribe({
            mView.setTradeOrderDetail(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun markAsPaid(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeBuyerConfirm(map).subscribe({
            mView.markAsPaidSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun tradeOrderCancel(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeOrderCancel(map).subscribe({
            mView.cancelOrderSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun confirmCheck(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeSellerConfirm(map).subscribe({
            mView.confirmCheckSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
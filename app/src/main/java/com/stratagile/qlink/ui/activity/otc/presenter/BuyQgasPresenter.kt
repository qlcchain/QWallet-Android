package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.BuyQgasContract
import com.stratagile.qlink.ui.activity.otc.BuyQgasActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of BuyQgasActivity
 * @date 2019/07/09 14:18:25
 */
class BuyQgasPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: BuyQgasContract.View) : BuyQgasContract.BuyQgasContractPresenter {
    override fun generateTradeBuyQgasOrder(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.generateTradeBuyQgasOrder(map).subscribe({
            mView.generateTradeBuyQgasOrderSuccess(it)
        }, {
            it.printStackTrace()
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getEntrustOrderDetail(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getEntrustOrderInfo(map).subscribe({
            mView.setEntrustOrder(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

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
}
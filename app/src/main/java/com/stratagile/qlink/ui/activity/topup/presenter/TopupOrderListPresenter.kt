package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.TopupOrderListContract
import com.stratagile.qlink.ui.activity.topup.TopupOrderListActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopupOrderListActivity
 * @date 2019/09/26 15:00:15
 */
class TopupOrderListPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopupOrderListContract.View) : TopupOrderListContract.TopupOrderListContractPresenter {

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

    fun getOderList(map: MutableMap<String, String>, page : Int) {
        mCompositeDisposable.add(httpAPIWrapper.getTopupOrderList(map).subscribe({
            mView.setOrderList(it, page)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun cancelOrder(map: MutableMap<String, String>, position : Int) {
        mCompositeDisposable.add(httpAPIWrapper.topupCancelOrder(map).subscribe({
            mView.cancelOrderSuccess(it, position)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
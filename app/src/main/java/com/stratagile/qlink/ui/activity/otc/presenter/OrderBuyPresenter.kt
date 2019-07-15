package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.newwinq.Product
import com.stratagile.qlink.ui.activity.otc.contract.OrderBuyContract
import com.stratagile.qlink.ui.activity.otc.OrderBuyFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of OrderBuyFragment
 * @date 2019/07/08 17:24:58
 */

class OrderBuyPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: OrderBuyContract.View) : OrderBuyContract.OrderBuyContractPresenter {

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

    fun generateBuyQgasOrder(map : Map<String, String>) {
        val disposable = httpAPIWrapper.generateBuyQgasOrder(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.closeProgressDialog()
                    mView.generateBuyQgasOrderSuccess()
                }, { mView.closeProgressDialog() }, {
                    //onComplete
                    KLog.i("onComplete")
                    mView.closeProgressDialog()
                })
        mCompositeDisposable.add(disposable)
    }
}
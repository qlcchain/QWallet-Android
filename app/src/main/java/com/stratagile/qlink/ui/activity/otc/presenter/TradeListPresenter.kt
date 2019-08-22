package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.otc.contract.TradeListContract
import com.stratagile.qlink.ui.activity.otc.TradeListFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of TradeListFragment
 * @date 2019/08/19 09:38:46
 */

class TradeListPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TradeListContract.View) : TradeListContract.TradeListContractPresenter {

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

    fun getOrderList(map: Map<*, *>, currentPage: Int) {
        val disposable = httpAPIWrapper.getEntrustOrderList(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setEntrustOrderList(baseBack.orderList, currentPage)
                }, { mView.getEutrustOrderError() }, {
                    //onComplete
                    KLog.i("onComplete")
                    mView.getEutrustOrderError()
                })
        mCompositeDisposable.add(disposable)
    }

}
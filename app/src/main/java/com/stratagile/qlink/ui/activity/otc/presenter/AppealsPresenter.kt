package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.AppealsContract
import com.stratagile.qlink.ui.activity.otc.AppealsFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of AppealsFragment
 * @date 2019/07/19 11:45:27
 */

class AppealsPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: AppealsContract.View) : AppealsContract.AppealsContractPresenter {

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

    fun getTradeOrderList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeOrderList(map).subscribe({
            mView.setTradeOrderList(it)
        }, {

        }, {

        }))
    }
}
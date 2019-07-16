package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.ProcessContract
import com.stratagile.qlink.ui.activity.otc.ProcessFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of ProcessFragment
 * @date 2019/07/16 17:52:45
 */

class ProcessPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: ProcessContract.View) : ProcessContract.ProcessContractPresenter {

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
        httpAPIWrapper.tradeOrderList(map).subscribe({
            mView.setTradeOrderList(it)
        }, {

        }, {

        })
    }
}
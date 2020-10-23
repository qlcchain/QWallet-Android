package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DappBrowserContract
import com.stratagile.qlink.ui.activity.defi.DappBrowserActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DappBrowserActivity
 * @date 2020/10/15 17:56:57
 */
class DappBrowserPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DappBrowserContract.View) : DappBrowserContract.DappBrowserContractPresenter {

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
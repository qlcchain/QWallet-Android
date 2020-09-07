package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.EthTransformContract
import com.stratagile.qlink.ui.activity.defi.EthTransformActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of EthTransformActivity
 * @date 2020/08/15 16:13:43
 */
class EthTransformPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EthTransformContract.View) : EthTransformContract.EthTransformContractPresenter {

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
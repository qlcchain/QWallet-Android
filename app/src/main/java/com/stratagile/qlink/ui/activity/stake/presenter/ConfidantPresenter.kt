package com.stratagile.qlink.ui.activity.stake.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.stake.contract.ConfidantContract
import com.stratagile.qlink.ui.activity.stake.ConfidantFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: presenter of ConfidantFragment
 * @date 2019/08/08 16:38:01
 */

class ConfidantPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: ConfidantContract.View) : ConfidantContract.ConfidantContractPresenter {

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
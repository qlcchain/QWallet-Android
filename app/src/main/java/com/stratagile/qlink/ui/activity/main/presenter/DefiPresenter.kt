package com.stratagile.qlink.ui.activity.main.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.main.contract.DefiContract
import com.stratagile.qlink.ui.activity.main.DefiFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: presenter of DefiFragment
 * @date 2020/05/25 11:29:00
 */

class DefiPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiContract.View) : DefiContract.DefiContractPresenter {

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
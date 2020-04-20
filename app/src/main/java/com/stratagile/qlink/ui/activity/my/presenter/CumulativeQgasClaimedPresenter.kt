package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.my.contract.CumulativeQgasClaimedContract
import com.stratagile.qlink.ui.activity.my.CumulativeQgasClaimedActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of CumulativeQgasClaimedActivity
 * @date 2020/04/15 09:37:37
 */
class CumulativeQgasClaimedPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: CumulativeQgasClaimedContract.View) : CumulativeQgasClaimedContract.CumulativeQgasClaimedContractPresenter {

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
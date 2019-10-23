package com.stratagile.qlink.ui.activity.stake.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.stake.contract.QlcPayContract
import com.stratagile.qlink.ui.activity.stake.QlcPayActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: presenter of QlcPayActivity
 * @date 2019/08/16 09:59:21
 */
class QlcPayPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: QlcPayContract.View) : QlcPayContract.QlcPayContractPresenter {

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
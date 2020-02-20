package com.stratagile.qlink.ui.activity.place.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.place.contract.SmsVourchContract
import com.stratagile.qlink.ui.activity.place.SmsVourchActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: presenter of SmsVourchActivity
 * @date 2020/02/20 22:53:22
 */
class SmsVourchPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: SmsVourchContract.View) : SmsVourchContract.SmsVourchContractPresenter {

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
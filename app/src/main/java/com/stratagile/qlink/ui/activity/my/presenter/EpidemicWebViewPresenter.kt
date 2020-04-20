package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.my.contract.EpidemicWebViewContract
import com.stratagile.qlink.ui.activity.my.EpidemicWebViewActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of EpidemicWebViewActivity
 * @date 2020/04/16 17:48:35
 */
class EpidemicWebViewPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EpidemicWebViewContract.View) : EpidemicWebViewContract.EpidemicWebViewContractPresenter {

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
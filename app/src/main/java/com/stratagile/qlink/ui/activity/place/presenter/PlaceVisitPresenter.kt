package com.stratagile.qlink.ui.activity.place.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.place.contract.PlaceVisitContract
import com.stratagile.qlink.ui.activity.place.PlaceVisitActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: presenter of PlaceVisitActivity
 * @date 2020/02/20 10:07:00
 */
class PlaceVisitPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: PlaceVisitContract.View) : PlaceVisitContract.PlaceVisitContractPresenter {

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

    fun smsReport(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.smsReport(map).subscribe({
            mView.reportBack(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
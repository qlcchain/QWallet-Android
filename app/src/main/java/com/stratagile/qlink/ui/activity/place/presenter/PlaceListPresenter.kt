package com.stratagile.qlink.ui.activity.place.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.place.contract.PlaceListContract
import com.stratagile.qlink.ui.activity.place.PlaceListActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: presenter of PlaceListActivity
 * @date 2020/02/21 21:39:44
 */
class PlaceListPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: PlaceListContract.View) : PlaceListContract.PlaceListContractPresenter {

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

    fun getPlaceList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.smsList(map).subscribe({
            mView.setReportList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.my.contract.EpidemicRedPaperContract
import com.stratagile.qlink.ui.activity.my.EpidemicRedPaperActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of EpidemicRedPaperActivity
 * @date 2020/04/13 17:05:33
 */
class EpidemicRedPaperPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EpidemicRedPaperContract.View) : EpidemicRedPaperContract.EpidemicRedPaperContractPresenter {

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

    fun getLocation(map: Map<*, *>?) {
        mCompositeDisposable.add(httpAPIWrapper.getLocation(map).subscribe({
            mView.setLocation(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
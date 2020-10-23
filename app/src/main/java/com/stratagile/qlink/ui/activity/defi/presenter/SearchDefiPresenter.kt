package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.SearchDefiContract
import com.stratagile.qlink.ui.activity.defi.SearchDefiActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of SearchDefiActivity
 * @date 2020/10/22 15:09:25
 */
class SearchDefiPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: SearchDefiContract.View) : SearchDefiContract.SearchDefiContractPresenter {

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
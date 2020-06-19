package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DefiNewsContract
import com.stratagile.qlink.ui.activity.defi.DefiNewsFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiNewsFragment
 * @date 2020/05/25 17:10:21
 */

class DefiNewsPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiNewsContract.View) : DefiNewsContract.DefiNewsContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getDefiNews(map: HashMap<String, String>, currentPage : Int) {
        val disposable = httpAPIWrapper.defiNewsList(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiNews(baseBack, currentPage)
                    //isSuccesse
                }, {
                    mView.closeProgressDialog()
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
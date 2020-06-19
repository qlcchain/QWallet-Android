package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DefiNewsDetailContract
import com.stratagile.qlink.ui.activity.defi.DefiNewsDetailActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiNewsDetailActivity
 * @date 2020/06/05 14:09:12
 */
class DefiNewsDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiNewsDetailContract.View) : DefiNewsDetailContract.DefiNewsDetailContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getDefiNewsDetail(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiNews(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setNewsDetail(baseBack)
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
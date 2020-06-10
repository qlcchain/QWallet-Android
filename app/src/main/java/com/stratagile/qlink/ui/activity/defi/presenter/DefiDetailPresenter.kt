package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DefiDetailContract
import com.stratagile.qlink.ui.activity.defi.DefiDetailActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiDetailActivity
 * @date 2020/05/29 09:13:19
 */
class DefiDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiDetailContract.View) : DefiDetailContract.DefiDetailContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }
    fun getDefiDetail(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiProject(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiDetail(baseBack)
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

    fun getDefiStateList(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiStatsList(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiStateList(baseBack)
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
package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DefiListContract
import com.stratagile.qlink.ui.activity.defi.DefiListFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiListFragment
 * @date 2020/05/25 17:10:05
 */

class DefiListPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiListContract.View) : DefiListContract.DefiListContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getCategoryList() {
        val disposable = httpAPIWrapper.defiCategoryList(hashMapOf<String, String>())
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setCategoryList(baseBack)
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
    fun getDefis(map: HashMap<String, String>, currentPage : Int) {
        val disposable = httpAPIWrapper.defiProjectList(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiList(baseBack, currentPage)
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

    fun getDefiStats(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiStasCache(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiStats(baseBack)
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
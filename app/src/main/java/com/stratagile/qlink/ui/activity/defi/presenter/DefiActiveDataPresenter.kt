package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.DefiActiveDataContract
import com.stratagile.qlink.ui.activity.defi.DefiActiveDataFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiActiveDataFragment
 * @date 2020/06/02 10:25:59
 */

class DefiActiveDataPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiActiveDataContract.View) : DefiActiveDataContract.DefiActiveDataContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getDefiStateList(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiStatsList(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setDefiStateListData(baseBack)
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
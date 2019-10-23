package com.stratagile.qlink.ui.activity.stake.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.stake.contract.StakeDetailContract
import com.stratagile.qlink.ui.activity.stake.StakeDetailActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: presenter of StakeDetailActivity
 * @date 2019/08/09 15:26:02
 */
class StakeDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: StakeDetailContract.View) : StakeDetailContract.StakeDetailContractPresenter {

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

    fun unLock(map : Map<String, String>) {
        val disposable = httpAPIWrapper.unLock(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.sign(baseBack)
                }, {
                    mView.closeProgressDialog()
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }
}
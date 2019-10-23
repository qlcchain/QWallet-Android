package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.otc.contract.SelectCurrencyContract
import com.stratagile.qlink.ui.activity.otc.SelectCurrencyActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of SelectCurrencyActivity
 * @date 2019/08/19 15:22:57
 */
class SelectCurrencyPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: SelectCurrencyContract.View) : SelectCurrencyContract.SelectCurrencyContractPresenter {

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

    fun getPairs() {
        val disposable = httpAPIWrapper.getPairs(HashMap<String, String>())
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setRemoteTradePairs(baseBack.pairsList)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }
}
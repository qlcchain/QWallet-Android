package com.stratagile.qlink.ui.activity.stake.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.stake.contract.MyStakeContract
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: presenter of MyStakeActivity
 * @date 2019/08/08 15:32:14
 */
class MyStakePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: MyStakeContract.View) : MyStakeContract.MyStakeContractPresenter {

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

    fun getClaimedTotal(map : Map<String, String>) {
        val disposable = httpAPIWrapper.getRewardTotal(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setClaimedTotal(baseBack)
                }, {
                    mView.closeProgressDialog()
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun rewardQlcAmount(map : Map<String, String>) {
        val disposable = httpAPIWrapper.qurryDict(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setRewardQlcAmount(baseBack)
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
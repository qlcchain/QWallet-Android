package com.stratagile.qlink.ui.activity.mining.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.mining.contract.MiningRewardContract
import com.stratagile.qlink.ui.activity.mining.MiningRewardActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: presenter of MiningRewardActivity
 * @date 2019/11/15 15:49:47
 */
class MiningRewardPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: MiningRewardContract.View) : MiningRewardContract.MiningRewardContractPresenter {

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

    fun claimQlc(map : Map<String, String>) {
        val disposable = httpAPIWrapper.claimQlc(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.claimQlcBack(baseBack)
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
}
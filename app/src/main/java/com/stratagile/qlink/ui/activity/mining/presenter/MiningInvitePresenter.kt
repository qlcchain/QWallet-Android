package com.stratagile.qlink.ui.activity.mining.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.mining.contract.MiningInviteContract
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: presenter of MiningInviteActivity
 * @date 2019/11/14 09:43:06
 */
class MiningInvitePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: MiningInviteContract.View) : MiningInviteContract.MiningInviteContractPresenter {

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

    fun getMiningRewardList(map : Map<String, String>) {
        val disposable = httpAPIWrapper.getTradeMiningIndex(map)
                .subscribe({ miningAct ->
                    //isSuccesse
                    mView.setRewardRank(miningAct)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }
}
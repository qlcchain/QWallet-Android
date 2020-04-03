package com.stratagile.qlink.ui.activity.reward.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.reward.contract.ClaimRewardContract
import com.stratagile.qlink.ui.activity.reward.ClaimRewardActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.reward
 * @Description: presenter of ClaimRewardActivity
 * @date 2019/10/10 15:28:24
 */
class ClaimRewardPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: ClaimRewardContract.View) : ClaimRewardContract.ClaimRewardContractPresenter {

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

    fun getVCode(map: Map<String, String>) {
        val disposable = httpAPIWrapper.getVcode(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setInviteCode(baseBack)
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

    fun claimQgas(map : Map<String, String>) {
        val disposable = httpAPIWrapper.claimQgas(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.claimQgasBack(baseBack)
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

    fun claiminviteQgas(map : Map<String, String>) {
        val disposable = httpAPIWrapper.claimInviteQgas(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.claimQgasBack(baseBack)
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
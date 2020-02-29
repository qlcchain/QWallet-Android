package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.ui.activity.my.contract.VoteContract
import com.stratagile.qlink.ui.activity.my.VoteActivity
import io.reactivex.Observer
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of VoteActivity
 * @date 2020/02/26 10:34:02
 */
class VotePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: VoteContract.View) : VoteContract.VoteContractPresenter {

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

    fun sysVote(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.sysVote(map).subscribe({
            mView.voteBack()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun sysVoteResult(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.sysVoteResult(map).subscribe({
            mView.setVoteResult(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getAppDict(map: MutableMap<String, String>) {
        httpAPIWrapper.qurryDict(map).subscribe(object : Observer<Dict?> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(dict: Dict) {
                mView.setAppDict(dict)
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }
}
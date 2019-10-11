package com.stratagile.qlink.ui.activity.reward.presenter
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.reward.contract.MyClaimContract
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.claim
 * @Description: presenter of MyClaimActivity
 * @date 2019/10/09 11:57:31
 */
class MyClaimPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: MyClaimContract.View) : MyClaimContract.MyClaimContractPresenter {

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
    fun getCanClaimTotal(map : Map<String, String>) {
        val disposable = httpAPIWrapper.getRewardTotal(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setCanClaimTotal(baseBack)
                }, {
                    mView.closeProgressDialog()
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
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

    fun getRewardList(map : Map<String, String>, page : Int) {
        val disposable = httpAPIWrapper.getRewardList(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setRewardList(baseBack, page)
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
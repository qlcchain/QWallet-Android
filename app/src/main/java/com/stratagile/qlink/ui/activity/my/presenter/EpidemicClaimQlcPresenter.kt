package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.my.contract.EpidemicClaimQlcContract
import com.stratagile.qlink.ui.activity.my.EpidemicClaimQlcActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of EpidemicClaimQlcActivity
 * @date 2020/04/16 15:57:15
 */
class EpidemicClaimQlcPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EpidemicClaimQlcContract.View) : EpidemicClaimQlcContract.EpidemicClaimQlcContractPresenter {

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
}
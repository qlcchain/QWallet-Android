package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.my.contract.EpidemicInviteNowContract
import com.stratagile.qlink.ui.activity.my.EpidemicInviteNowActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of EpidemicInviteNowActivity
 * @date 2020/04/17 10:25:30
 */
class EpidemicInviteNowPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EpidemicInviteNowContract.View) : EpidemicInviteNowContract.EpidemicInviteNowContractPresenter {

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
}
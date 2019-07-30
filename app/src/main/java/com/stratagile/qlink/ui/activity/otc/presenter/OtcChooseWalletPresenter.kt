package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.OtcChooseWalletContract
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of OtcChooseWalletActivity
 * @date 2019/07/29 11:54:14
 */
class OtcChooseWalletPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: OtcChooseWalletContract.View) : OtcChooseWalletContract.OtcChooseWalletContractPresenter {

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
package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.UsdtReceiveAddressContract
import com.stratagile.qlink.ui.activity.otc.UsdtReceiveAddressActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of UsdtReceiveAddressActivity
 * @date 2019/07/17 16:50:34
 */
class UsdtReceiveAddressPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: UsdtReceiveAddressContract.View) : UsdtReceiveAddressContract.UsdtReceiveAddressContractPresenter {

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
package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.TopUpFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopUpFragment
 * @date 2019/09/23 15:54:17
 */

class TopUpPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopUpContract.View) : TopUpContract.TopUpContractPresenter {

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

    fun getProductList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getTopupProductList(map).subscribe({
            mView.setProductList(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
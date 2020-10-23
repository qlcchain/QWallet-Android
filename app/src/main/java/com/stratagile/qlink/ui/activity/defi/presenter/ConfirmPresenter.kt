package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.ConfirmContract
import com.stratagile.qlink.ui.activity.defi.ConfirmActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of ConfirmActivity
 * @date 2020/10/14 16:59:28
 */
class ConfirmPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: ConfirmContract.View) : ConfirmContract.ConfirmContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getEthGasPrice(map: java.util.HashMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.ethGasPrice(map).subscribe({
            mView.setEthGasPrice(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
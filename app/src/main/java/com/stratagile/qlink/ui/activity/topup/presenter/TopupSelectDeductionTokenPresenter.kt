package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.TopupSelectDeductionTokenContract
import com.stratagile.qlink.ui.activity.topup.TopupSelectDeductionTokenActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopupSelectDeductionTokenActivity
 * @date 2020/02/13 20:41:09
 */
class TopupSelectDeductionTokenPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopupSelectDeductionTokenContract.View) : TopupSelectDeductionTokenContract.TopupSelectDeductionTokenContractPresenter {

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

    fun getPayToken() {
        mCompositeDisposable.add(httpAPIWrapper.payToken(hashMapOf<String, String>()).subscribe({
            mView.setPayToken(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
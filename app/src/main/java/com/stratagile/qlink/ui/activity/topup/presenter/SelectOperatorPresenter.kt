package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.SelectOperatorContract
import com.stratagile.qlink.ui.activity.topup.SelectOperatorActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of SelectOperatorActivity
 * @date 2019/12/25 10:21:32
 */
class SelectOperatorPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: SelectOperatorContract.View) : SelectOperatorContract.SelectOperatorContractPresenter {

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

    fun getIspList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getIspList(map).subscribe({
            mView.setIsp(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
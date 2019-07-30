package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.AppealContract
import com.stratagile.qlink.ui.activity.otc.AppealActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of AppealActivity
 * @date 2019/07/19 11:44:36
 */
class AppealPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: AppealContract.View) : AppealContract.AppealContractPresenter {

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

    fun generateAppeal(account : RequestBody?, token :RequestBody?, tradeOrderId : RequestBody?, reason : RequestBody?, photo1 : MultipartBody.Part? ,photo2 : MultipartBody.Part? , photo3 : MultipartBody.Part? ,photo4 : MultipartBody.Part?) {
        mCompositeDisposable.add(httpAPIWrapper.generateAppeal(account, token, tradeOrderId, reason, photo1, photo2, photo3, photo4).subscribe({
            mView.generateAppealSuccess()
        }, {
            it.printStackTrace()
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
}
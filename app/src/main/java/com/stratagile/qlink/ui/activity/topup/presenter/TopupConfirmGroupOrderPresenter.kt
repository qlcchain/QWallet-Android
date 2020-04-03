package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.TopupConfirmGroupOrderContract
import com.stratagile.qlink.ui.activity.topup.TopupConfirmGroupOrderActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopupConfirmGroupOrderActivity
 * @date 2020/02/13 16:15:05
 */
class TopupConfirmGroupOrderPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopupConfirmGroupOrderContract.View) : TopupConfirmGroupOrderContract.TopupConfirmGroupOrderContractPresenter {

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

    fun topupJoinGroup(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.topupJoinGroup(map).subscribe({
            mView.joinGroupBack(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun createTopupOrder(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.topupCreateOrder(map).subscribe({
            mView.createTopupOrderSuccess(it)
        }, {
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }, {
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }))
    }
}
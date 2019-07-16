package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.otc.contract.PostedContract
import com.stratagile.qlink.ui.activity.otc.PostedFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.ArrayList

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of PostedFragment
 * @date 2019/07/16 17:52:28
 */

class PostedPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: PostedContract.View) : PostedContract.PostedContractPresenter {

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

    fun getOrderList(map: Map<*, *>) {
        val disposable = httpAPIWrapper.getEntrustOrderList(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.setEntrustOrderList(baseBack.orderList)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }
}
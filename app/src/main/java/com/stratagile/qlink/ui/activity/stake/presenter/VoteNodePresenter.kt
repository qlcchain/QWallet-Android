package com.stratagile.qlink.ui.activity.stake.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.stake.contract.VoteNodeContract
import com.stratagile.qlink.ui.activity.stake.VoteNodeFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: presenter of VoteNodeFragment
 * @date 2019/08/08 16:37:41
 */

class VoteNodePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: VoteNodeContract.View) : VoteNodeContract.VoteNodeContractPresenter {

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

    fun getNeoWalletDetail(map: HashMap<String, String>, address: String) {
        val disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe({ baseBack ->
                    mView.setNeoDetail(baseBack)
//                    getUtxo(address)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }
}
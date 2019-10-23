package com.stratagile.qlink.ui.activity.stake.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.stake.contract.StakeExplainContract
import com.stratagile.qlink.ui.activity.stake.StakeExplainActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: presenter of StakeExplainActivity
 * @date 2019/08/09 15:25:43
 */
class StakeExplainPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: StakeExplainContract.View) : StakeExplainContract.StakeExplainContractPresenter {

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
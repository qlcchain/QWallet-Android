package com.stratagile.qlink.ui.activity.recommend.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.recommend.contract.GroupExplainContract
import com.stratagile.qlink.ui.activity.recommend.GroupExplainActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: presenter of GroupExplainActivity
 * @date 2020/01/17 13:37:58
 */
class GroupExplainPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: GroupExplainContract.View) : GroupExplainContract.GroupExplainContractPresenter {

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
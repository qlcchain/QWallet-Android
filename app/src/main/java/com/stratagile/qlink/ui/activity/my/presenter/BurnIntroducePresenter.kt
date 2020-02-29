package com.stratagile.qlink.ui.activity.my.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.my.contract.BurnIntroduceContract
import com.stratagile.qlink.ui.activity.my.BurnIntroduceActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of BurnIntroduceActivity
 * @date 2020/02/29 17:33:46
 */
class BurnIntroducePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: BurnIntroduceContract.View) : BurnIntroduceContract.BurnIntroduceContractPresenter {

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
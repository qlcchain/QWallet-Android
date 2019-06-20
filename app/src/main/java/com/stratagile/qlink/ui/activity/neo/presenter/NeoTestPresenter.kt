package com.stratagile.qlink.ui.activity.neo.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.neo.contract.NeoTestContract
import com.stratagile.qlink.ui.activity.neo.NeoTestActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.neo
 * @Description: presenter of NeoTestActivity
 * @date 2019/06/10 10:30:03
 */
class NeoTestPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: NeoTestContract.View) : NeoTestContract.NeoTestContractPresenter {

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
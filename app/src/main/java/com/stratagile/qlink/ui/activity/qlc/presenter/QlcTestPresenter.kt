package com.stratagile.qlink.ui.activity.qlc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.qlc.contract.QlcTestContract
import com.stratagile.qlink.ui.activity.qlc.QlcTestActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: presenter of QlcTestActivity
 * @date 2019/05/05 16:24:30
 */
class QlcTestPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: QlcTestContract.View) : QlcTestContract.QlcTestContractPresenter {

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
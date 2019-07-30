package com.stratagile.qlink.ui.activity.qlc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.qlc.contract.QlcMnemonicbackupContract
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicbackupActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: presenter of QlcMnemonicbackupActivity
 * @date 2019/06/05 18:37:03
 */
class QlcMnemonicbackupPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: QlcMnemonicbackupContract.View) : QlcMnemonicbackupContract.QlcMnemonicbackupContractPresenter {

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
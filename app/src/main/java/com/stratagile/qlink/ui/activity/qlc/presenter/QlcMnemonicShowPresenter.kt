package com.stratagile.qlink.ui.activity.qlc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.qlc.contract.QlcMnemonicShowContract
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicShowActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: presenter of QlcMnemonicShowActivity
 * @date 2019/06/05 18:36:43
 */
class QlcMnemonicShowPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: QlcMnemonicShowContract.View) : QlcMnemonicShowContract.QlcMnemonicShowContractPresenter {

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
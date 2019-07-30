package com.stratagile.qlink.ui.activity.qlc.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcMnemonicContract
import com.stratagile.qlink.ui.activity.qlc.ImportQlcMnemonicFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: presenter of ImportQlcMnemonicFragment
 * @date 2019/06/06 10:37:31
 */

class ImportQlcMnemonicPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: ImportQlcMnemonicContract.View) : ImportQlcMnemonicContract.ImportQlcMnemonicContractPresenter {

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
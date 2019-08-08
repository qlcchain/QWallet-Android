package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.entity.MainAddress
import com.stratagile.qlink.ui.activity.otc.contract.NewOrderContract
import com.stratagile.qlink.ui.activity.otc.NewOrderActivity
import com.stratagile.qlink.utils.SpUtil
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of NewOrderActivity
 * @date 2019/07/08 16:00:52
 */
class NewOrderPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: NewOrderContract.View) : NewOrderContract.NewOrderContractPresenter {

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

    fun getMainAddress() {
        mCompositeDisposable.add(httpAPIWrapper.getMainAddress(HashMap<String, String>()).subscribe({
            KLog.i("onSuccesse")
            ConstantValue.mainAddress = it.data.neo.address
            ConstantValue.ethMainAddress = it.data.eth.address
            ConstantValue.mainAddressData = it.data
        }, {

        }, {

        }))
    }

}
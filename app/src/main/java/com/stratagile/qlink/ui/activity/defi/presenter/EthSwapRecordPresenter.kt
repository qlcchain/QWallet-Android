package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.EthSwapRecordContract
import com.stratagile.qlink.ui.activity.defi.EthSwapRecordFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of EthSwapRecordFragment
 * @date 2020/09/02 13:58:17
 */

class EthSwapRecordPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EthSwapRecordContract.View) : EthSwapRecordContract.EthSwapRecordContractPresenter {

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

    fun getEthGasPrice(map: java.util.HashMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.ethGasPrice(map).subscribe({
            mView.setEthGasPrice(it.gasPrice)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun getEthPrice() {
        val infoMap = HashMap<String, Any>()
        val tokens = arrayListOf<String>("ETH")
        infoMap["symbols"] = tokens
        infoMap["coin"] = "USD"
        mCompositeDisposable.add(httpAPIWrapper.getTokenPrice(infoMap).subscribe({
            mView.setEthPrice(it)
        }, {

        }, {

        }))
    }
}
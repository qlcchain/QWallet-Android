package com.stratagile.qlink.ui.activity.defi.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.defi.contract.EthSwapContract
import com.stratagile.qlink.ui.activity.defi.EthSwapFragment
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of EthSwapFragment
 * @date 2020/08/15 16:40:33
 */

class EthSwapPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: EthSwapContract.View) : EthSwapContract.EthSwapContractPresenter {

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
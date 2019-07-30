package com.stratagile.qlink.ui.activity.otc.presenter
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.otc.contract.CloasedOrderContract
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of CloasedOrderFragment
 * @date 2019/07/17 14:39:46
 */

class CloasedOrderPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: CloasedOrderContract.View) : CloasedOrderContract.CloasedOrderContractPresenter {

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

    fun getTradeOrderList(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeOrderList(map).subscribe({
            mView.setTradeOrderList(it)
        }, {

        }, {

        }))
    }
}
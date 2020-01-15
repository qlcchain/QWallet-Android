package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.topup.contract.TopupPayNeoChainContract
import com.stratagile.qlink.ui.activity.topup.TopupPayNeoChainActivity
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopupPayNeoChainActivity
 * @date 2019/12/26 16:34:35
 */
class TopupPayNeoChainPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopupPayNeoChainContract.View) : TopupPayNeoChainContract.TopupPayNeoChainContractPresenter {

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

    fun getNeoWalletDetail(map: HashMap<String, String>, address: String) {
        val disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe({ baseBack ->
                    //isSuccesse
//                    getNeoTokensInfo(baseBack)
                    mView.setNeoDetail(baseBack)
//                    getUtxo(address)
                }, { }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun savePayTokenTxid(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.savePayTokenTxid(map).subscribe({
            mView.savePayTokenTxidBack(it)
        }, {
            mView.savePayTokenTxidError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }, {
            mView.savePayTokenTxidError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }))
    }

    fun saveItemPayTokenTxid(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.saveItemPayTokenTxid(map).subscribe({
            mView.saveItemPayTokenTxidBack(it)
        }, {
            mView.saveItemPayTokenError()
        }, {
            mView.saveItemPayTokenError()
        }))
    }
}
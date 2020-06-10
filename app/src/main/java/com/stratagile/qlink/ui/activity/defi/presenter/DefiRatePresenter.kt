package com.stratagile.qlink.ui.activity.defi.presenter

import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.QLCAPI.BalanceInter
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.ui.activity.defi.contract.DefiRateContract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: presenter of DefiRateActivity
 * @date 2020/06/03 14:17:18
 */
class DefiRatePresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: DefiRateContract.View) : DefiRateContract.DefiRateContractPresenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    fun getRatingInfo(map: HashMap<String, String>) {
        val disposable = httpAPIWrapper.defiRatingInfo(map)
                .subscribe({ baseBack ->
                    mView.closeProgressDialog()
                    mView.setRatingInfo(baseBack)
                    //isSuccesse
                }, {
                    mView.closeProgressDialog()
                }, {
                    //onComplete
                    mView.closeProgressDialog()
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun getNeoQlcCount(list: MutableList<Wallet>) {
        var count = 0
        Observable
                .fromIterable(list)
                .observeOn(Schedulers.io())
                .map(Function<Wallet, Observable<NeoWalletInfo>>() {
                    try {
                        Thread.sleep(500)
                        var map = hashMapOf<String, String>()
                        map["address"] = it.address
                        httpAPIWrapper.getNeoWalletInfo(map)
                    } catch (e : Exception) {
                        e.printStackTrace()
                        Observable.just(NeoWalletInfo())
                    }
                })
                .onErrorReturnItem(Observable.just(NeoWalletInfo()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    KLog.i("请求返回了。")
                    it.forEach {
                        it.subscribe({
                            count++
                            KLog.i(count)
                            if (it.data.address != null) {
                                if (it.data.balance.size > 0) {
                                    var hasQlc = false
                                    it.data.balance.forEach {
                                        if ("QLC".equals(it.asset_symbol)) {
                                            hasQlc = true
                                            mView.setNeoQLcAmount(it.amount, count == list.size)
                                        }
                                    }
                                    if (!hasQlc) {
                                        mView.setNeoQLcAmount(0.toDouble(), count == list.size)
                                    }
                                } else {
                                    mView.setNeoQLcAmount(0.toDouble(), count == list.size)
                                }
                            } else {
                                mView.setNeoQLcAmount(0.toDouble(), count == list.size)
                            }
                        })
                    }
                }, {
                    it.printStackTrace()
                })
    }

    fun getQlcChainQlcCount(list: MutableList<QLCAccount>) {
        var count = 0
        Observable
                .fromIterable(list)
                .observeOn(Schedulers.io())
                .map(Function<QLCAccount, String>() {
                    QLCAPI().walletGetBalance(it.getAddress(), "", object : BalanceInter {
                        override fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: Error?) {
                            KLog.i(baseResult)
                            count++
                            if (error == null) {
                                var hasQlc = false
                                baseResult?.forEach {
                                    if (it.symbol.equals("QLC")) {
                                        hasQlc = true
                                        KLog.i(it.balance)
                                        mView.setQLCQLCAmount(it.vote.toDouble(), count == list.size)
                                    }
                                }
                                if (!hasQlc) {
                                    mView.setQLCQLCAmount(0.toDouble(), count == list.size)
                                }
                            } else {
                                mView.setQLCQLCAmount(0.toDouble(), count == list.size)
                            }
                        }
                    })
                    ""
                })
                .onErrorReturnItem("")
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {

                })
    }

    fun getNeoWalletDetail(address: String, map: Map<*, *>?) {
        val disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe({
                    if (it.data.address != null) {
                        if (it.data.balance.size > 0) {
                            var hasQlc = false
                            it.data.balance.forEach {
                                if ("QLC".equals(it.asset_symbol)) {
                                    hasQlc = true
                                    mView.setNeoQlcAmount(it.amount, address)
                                }
                            }
                            if (!hasQlc) {
                                mView.setNeoQlcAmount(0.toDouble(), address)
                            }
                        } else {
                            mView.setNeoQlcAmount(0.toDouble(), address)
                        }
                    } else {
                        mView.setNeoQlcAmount(0.toDouble(), address)
                    }
                }, {
                    it.printStackTrace()
                    getNeoWalletDetail(address, map)
                })
        mCompositeDisposable.add(disposable)
    }

    fun ratingDefi(map: Map<*, *>?) {
        val disposable = httpAPIWrapper.defiRating(map).subscribe({
            mView.closeProgressDialog()
            mView.defiRatingBack(it)
        }, {
            mView.ratingError()
        })
        mCompositeDisposable.add(disposable)
    }

    override fun unsubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
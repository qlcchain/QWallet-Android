package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.ui.activity.otc.contract.SellQgasContract
import com.stratagile.qlink.ui.activity.otc.SellQgasActivity
import com.stratagile.qlink.utils.QlcReceiveUtils
import com.stratagile.qlink.utils.SendBack
import com.stratagile.qlink.utils.ToastUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.lang.Error
import java.math.BigDecimal

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of SellQgasActivity
 * @date 2019/07/09 14:18:11
 */
class SellQgasPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: SellQgasContract.View) : SellQgasContract.SellQgasContractPresenter {

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

    fun getEntrustOrderDetail(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getEntrustOrderInfo(map).subscribe({
            mView.setEntrustOrder(it)
        }, {

        }, {

        }))
    }

    fun sendQgas(amount: String, receiveAddress: String, map: MutableMap<String, String>) {
        var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        var qlcAccount: QLCAccount
        if (qlcAccounts.filter { it.isCurrent() }.size == 1) {
            qlcAccount = qlcAccounts.filter { it.isCurrent() }.get(0)
        } else {
            ToastUtil.displayShortToast("Please Switch to QLC Chain Wallet")
            mView.closeProgressDialog()
            return
        }
        var disposable = Observable.create(ObservableOnSubscribe<QLCAccount> {
            KLog.i("发射1")
            it.onNext(qlcAccount)
            it.onComplete()
        }).subscribeOn(Schedulers.io()).map { qlcAccount1 ->
            var qlcTokenbalances: ArrayList<QlcTokenbalance>? = null
            Observable.create(ObservableOnSubscribe<String> {
                KLog.i("开始查询qgas。。")
                QLCAPI().walletGetBalance(qlcAccount1.address, "", object : QLCAPI.BalanceInter {
                    override fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: Error?) {
                        if (error == null) {
                            KLog.i("发射2")
                            qlcTokenbalances = baseResult
                            if (qlcTokenbalances!!.filter { it.symbol.equals("QGAS") }.size > 0) {
                                if (qlcTokenbalances!!.filter { it.symbol.equals("QGAS") }[0].balance.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros() >= amount.toBigDecimal()) {
                                    QlcReceiveUtils.sendQGas(qlcAccount, receiveAddress, amount, "SELL QGAS", object : SendBack {
                                        override fun send(suceess: String) {
                                            if ("".equals(suceess)) {
                                                mView.generateSellQgasOrderFailed("send qgas error")
                                                it.onComplete()
                                            } else {
                                                KLog.i(suceess)
                                                it.onNext(suceess)
                                                it.onComplete()
                                            }
                                        }

                                    })
                                } else {
                                    mView.generateSellQgasOrderFailed("Not enough QGAS")
                                    it.onComplete()
                                }
                            }
                        } else {
                            mView.generateSellQgasOrderFailed("send qgas error")
                            it.onComplete()
                        }
                    }
                })
            })
        }.concatMap {
            map.put("fromAddress", qlcAccount.address)
            map.put("txid", it.blockingFirst())
            httpAPIWrapper.generateTradeSellOrder(map)
        }.subscribe({ baseBack ->
            //isSuccesse
            mView.closeProgressDialog()
            mView.generateBuyQgasOrderSuccess()
        }, { mView.closeProgressDialog() }, {
            //onComplete
            KLog.i("onComplete")
            mView.closeProgressDialog()
        })
        mCompositeDisposable.add(disposable)
    }
}
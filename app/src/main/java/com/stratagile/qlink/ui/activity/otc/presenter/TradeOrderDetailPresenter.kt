package com.stratagile.qlink.ui.activity.otc.presenter

import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.R
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.BuySellSellTodo
import com.stratagile.qlink.db.BuySellSellTodoDao
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.ui.activity.otc.contract.TradeOrderDetailContract
import com.stratagile.qlink.ui.activity.otc.TradeOrderDetailActivity
import com.stratagile.qlink.utils.AccountUtil
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
 * @Description: presenter of TradeOrderDetailActivity
 * @date 2019/07/17 11:14:13
 */
class TradeOrderDetailPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TradeOrderDetailContract.View) : TradeOrderDetailContract.TradeOrderDetailContractPresenter {

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

    fun getTradeOrderDetail(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeOrderInfo(map).subscribe({
            mView.setTradeOrderDetail(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun markAsPaid(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeBuyerConfirm(map).subscribe({
            mView.markAsPaidSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun tradeOrderCancel(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeOrderCancel(map).subscribe({
            mView.cancelOrderSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun confirmCheck(map: Map<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.tradeSellerConfirm(map).subscribe({
            mView.confirmCheckSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }
    fun getSysTime() {
        mCompositeDisposable.add(httpAPIWrapper.getServerTime(hashMapOf<String, String>()).subscribe({
            mView.setServerTime(it.data.sysTime)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

    fun sendQgas(amount: String, receiveAddress: String, map: MutableMap<String, String>, message : String) {
        var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        var qlcAccount: QLCAccount
        if (qlcAccounts.filter { it.isCurrent() }.size == 1) {
            qlcAccount = qlcAccounts.filter { it.isCurrent() }.get(0)
        } else {
            ToastUtil.displayShortToast(AppConfig.instance.getString(R.string.please_switch_to_qlc_chain_wallet))
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
                                    QlcReceiveUtils.sendQGas(qlcAccount, receiveAddress, amount, message, false, object : SendBack {
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
            map.put("txid", it.blockingFirst())
            httpAPIWrapper.tradeSellOrderTxid(map)
        }.subscribe({ baseBack ->
            //isSuccesse
            mView.closeProgressDialog()
            mView.tradeOrderTxidSuccess()
        }, {
            mView.closeProgressDialog()
            if (map["txid"] != null) {
                BuySellSellTodo.createBuySellSellTodo(map)
                sysbackUp(map["txid"]!!, "TRADE_ORDER", "", "", "")
            }
        }, {
            //onComplete
            KLog.i("onComplete")
            mView.closeProgressDialog()
            if (map["txid"] != null) {
                BuySellSellTodo.createBuySellSellTodo(map)
                sysbackUp(map["txid"]!!, "TRADE_ORDER", "", "", "")
            }
        })
        mCompositeDisposable.add(disposable)
    }

    fun confirmTradeOrderTxid(txid: String, map: MutableMap<String, String>) {
        map["txid"] = txid
        mCompositeDisposable.add(httpAPIWrapper.tradeSellOrderTxid(map).subscribe({
            mView.closeProgressDialog()
            mView.tradeOrderTxidSuccess()
        }, {
            BuySellSellTodo.createBuySellSellTodo(map)
            sysbackUp(txid, "TRADE_ORDER", "", "", "")
        }, {
            BuySellSellTodo.createBuySellSellTodo(map)
            sysbackUp(txid, "TRADE_ORDER", "", "", "")
        }))
    }

    fun sysbackUp(txid: String, type: String, chain: String, tokenName: String, amount: String) {
        val infoMap = java.util.HashMap<String, Any>()
        infoMap["account"] = ConstantValue.currentUser.account
        infoMap["token"] = AccountUtil.getUserToken()
        infoMap["type"] = type
        infoMap["chain"] = chain
        infoMap["tokenName"] = tokenName
        infoMap["amount"] = amount
        infoMap["platform"] = "Android"
        infoMap["txid"] = txid
        httpAPIWrapper.sysBackUp(infoMap).subscribe(object : HttpObserver<BaseBack<*>>() {
            override fun onNext(baseBack: BaseBack<*>) {
                onComplete()
                var list = AppConfig.instance.daoSession.buySellSellTodoDao.queryBuilder().where(BuySellSellTodoDao.Properties.Txid.eq(txid)).list()
                if (list.size > 0) {
                    AppConfig.instance.daoSession.buySellSellTodoDao.delete(list[0])
                }
            }
        })
    }
}
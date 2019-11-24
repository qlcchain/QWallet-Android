package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.BuySellBuyTodo
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.ui.activity.otc.contract.OtcQlcChainPayContract
import com.stratagile.qlink.ui.activity.otc.OtcQlcChainPayActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.QlcReceiveUtils
import com.stratagile.qlink.utils.SendBack
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import org.apache.commons.lang3.StringUtils
import java.lang.Error
import java.math.BigDecimal

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of OtcQlcChainPayActivity
 * @date 2019/08/23 12:01:58
 */
class OtcQlcChainPayPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: OtcQlcChainPayContract.View) : OtcQlcChainPayContract.OtcQlcChainPayContractPresenter {

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

    fun getQlcWalletDetail(qlcAccount : QLCAccount) {
        QLCAPI().walletGetBalance(qlcAccount.address, "", object : QLCAPI.BalanceInter {
            override fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: Error?) {
                if (error == null) {
                    KLog.i("发射2")
                    mView.setQlcChainToken(baseResult!!)
                } else {
                }
            }
        })
    }

    fun getTxidByHex(txid : String): String {
        if (StringUtils.isBlank(txid)) {
            return txid
        }
        try {
            var addStr = ""
            var addCount = 63 - txid.length
            if (addCount > 0) {
                for (i in 0..addCount) {
                    addStr+= "0"
                }
            }
            return addStr + txid
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return txid
    }

    fun sendQlcChainToken(qlcTokenbalance : QlcTokenbalance, qlcAccount: QLCAccount, receiveAddress : String, amount : String, message : String, tradeOrderId : String) {
        QlcReceiveUtils.sendQlcChainToken(qlcAccount, receiveAddress, amount, message, qlcTokenbalance.symbol, 8, object : SendBack {
            override fun send(suceess: String) {
                if ("".equals(suceess)) {
                    mView.sendTokenFailed()
                } else {
                    KLog.i(suceess)
                    var map = hashMapOf<String, String>()
                    map["account"] = ConstantValue.currentUser.account
                    map["token"] = AccountUtil.getUserToken()
                    map["tradeOrderId"] = tradeOrderId
                    map["txid"] = getTxidByHex(suceess)
                    httpAPIWrapper.tradeBuyerConfirm(map).subscribe({
                        mView.sendTokenSuccess(suceess)
                    }, {
                        mView.closeProgressDialog()
                        BuySellBuyTodo.createBuySellBuyTodo(map)
                        sysbackUp(getTxidByHex(suceess), "TRADE_ORDER", "", "", "")
                    }, {
                        mView.closeProgressDialog()
                        BuySellBuyTodo.createBuySellBuyTodo(map)
                        sysbackUp(getTxidByHex(suceess), "TRADE_ORDER", "", "", "")
                    })
                }
            }

        })
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
            }
        })
    }
}
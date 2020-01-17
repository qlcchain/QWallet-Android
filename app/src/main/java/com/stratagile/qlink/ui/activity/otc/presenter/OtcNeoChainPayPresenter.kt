package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.Account
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.api.transaction.SendCallBack
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.NeoCallBack
import com.stratagile.qlink.data.NeoNodeRPC
import com.stratagile.qlink.data.UTXO
import com.stratagile.qlink.data.UTXOS
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.BuySellBuyTodo
import com.stratagile.qlink.db.BuySellBuyTodoDao
import com.stratagile.qlink.entity.AssetsWarpper
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.entity.NeoTransfer
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.ui.activity.otc.contract.OtcNeoChainPayContract
import com.stratagile.qlink.ui.activity.otc.OtcNeoChainPayActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.txutils.model.core.Transaction
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil
import io.reactivex.Observer
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.nio.ByteBuffer

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of OtcNeoChainPayActivity
 * @date 2019/08/21 15:07:05
 */
class OtcNeoChainPayPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: OtcNeoChainPayContract.View) : OtcNeoChainPayContract.OtcNeoChainPayContractPresenter {

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

    fun sendNep5Token(address : String, amount : String, toAddress : String, dataBean : NeoWalletInfo.DataBean.BalanceBean, remark : String, tradeOrderId: String) {
        val neoNodeRPC = NeoNodeRPC("")
        neoNodeRPC.sendNEP5Token(assets, Account.getWallet()!!, dataBean.asset_hash, address, toAddress, amount.toDouble(), remark, object : NeoCallBack{
            override fun NeoTranscationResult(jsonBody: String?) {
                val tx = getTxid(jsonBody!!)
                val map = java.util.HashMap<String, String>()
                map["addressFrom"] = address
                map["addressTo"] = toAddress
                map["symbol"] = dataBean.asset_symbol
                map["amount"] = amount
                map["tx"] = jsonBody
                sendRow(tx.getHash().toReverseHexString(), tradeOrderId, map)
            }
        })
    }

    fun sendRow(txid: String, tradeOrderId : String, map: HashMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.neoTokenTransaction(map).subscribe({
            markAsPaid(txid, tradeOrderId)
        }, {

        }, {

        }) )
    }

    fun markAsPaid(txid : String, tradeOrderId: String) {
        var map = hashMapOf<String, String>()
        map["account"] = ConstantValue.currentUser.account
        map["token"] = AccountUtil.getUserToken()
        map["tradeOrderId"] = tradeOrderId
        map["txid"] = txid
        httpAPIWrapper.tradeBuyerConfirm(map).subscribe({
            mView.sendUsdtSuccess(txid)
        }, {
            mView.closeProgressDialog()
            BuySellBuyTodo.createBuySellBuyTodo(map)
            sysbackUp(txid, "TRADE_ORDER", "", "", "")
        }, {
            mView.closeProgressDialog()
            BuySellBuyTodo.createBuySellBuyTodo(map)
            sysbackUp(txid, "TRADE_ORDER", "", "", "")
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
                var list = AppConfig.instance.daoSession.buySellBuyTodoDao.queryBuilder().where(BuySellBuyTodoDao.Properties.Txid.eq(txid)).list()
                if (list.size > 0) {
                    AppConfig.instance.daoSession.buySellBuyTodoDao.delete(list[0])
                }
            }
        })
    }

    /**
     * 获取交易id的方法
     *
     * @param hex sendrow的参数，一串很长的参数
     * @return 返回该笔交易的id
     */
    private fun getTxid(hex: String): Transaction {
        val ba: ByteArray
        ba = ModelUtil.decodeHex(hex)
        return Transaction(ByteBuffer.wrap(ba))
    }

    private var assets: UTXOS? = null
    fun getUtxo(address: String) {
        val map = java.util.HashMap<String, String>()
        map["address"] = address
        val disposable = httpAPIWrapper.getMainUnspentAsset(map)
                .subscribe({ unspent ->
                    //isSuccesse
                    KLog.i("onSuccesse")
                    val utxos = arrayOfNulls<UTXO>(unspent.data.size)
                    assets = UTXOS(unspent.data)
                    //                        assets.copy(unspent.getData());
                }, { throwable ->
                    //onError
                    KLog.i("onError")
                    throwable.printStackTrace()
                }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

}
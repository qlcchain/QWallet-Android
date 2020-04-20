package com.stratagile.qlink.ui.activity.otc.presenter

import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.ColdWallet
import com.stratagile.qlink.R
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.BuySellBuyTodo
import com.stratagile.qlink.db.BuySellBuyTodoDao
import com.stratagile.qlink.db.BuySellSellTodoDao
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.Raw
import com.stratagile.qlink.ui.activity.otc.contract.UsdtPayContract
import com.stratagile.qlink.ui.activity.otc.UsdtPayActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils.derivePrivateKey
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ChainId
import org.web3j.utils.Convert
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of UsdtPayActivity
 * @date 2019/07/31 13:58:11
 */
class UsdtPayPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: UsdtPayContract.View) : UsdtPayContract.UsdtPayContractPresenter {

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

    fun getEthPrice() {
        val infoMap = HashMap<String, Any>()
        val tokens = arrayListOf<String>("ETH")
        infoMap["symbols"] = tokens
        infoMap["coin"] = ConstantValue.currencyBean.name
        mCompositeDisposable.add(httpAPIWrapper.getTokenPrice(infoMap).subscribe({
            mView.setEthPrice(it)
        }, {

        }, {

        }))
    }

    fun getEthWalletDetail(map: HashMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getEthWalletInfo(map).subscribe({
            mView.setTokens(it)
        }, {

        }, {

        }))
    }

    fun transferUsdt(walletAddress: String, toAddress: String, amount: String, price: Int, tradeOrderId: String, tokenInfo: EthWalletInfo.DataBean.TokensBean) {
//        generateTransaction(walletAddress, "0xdac17f958d2ee523a2206206994597c13d831ec7", toAddress, derivePrivateKey(walletAddress)!!, amount, 6000, price, 6)
        var disposable = Observable.create(ObservableOnSubscribe<String> { it ->
            it.onNext(
                    generateTransaction(walletAddress, tokenInfo.tokenInfo.address, toAddress, derivePrivateKey(walletAddress)!!, amount, 60000, price, tokenInfo.tokenInfo.decimals.toInt()))
        })
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if ("".equals(it)) {
                        ToastUtil.displayShortToast(AppConfig.getInstance().resources.getString(R.string.error2))
                        mView.closeProgressDialog()
                    } else {
                        markAsPaid(it, tradeOrderId)
                        KLog.i("transaction Hash: $it")
                    }
                }, {
                    mView.closeProgressDialog()
                }, {
                    KLog.i("complete")
                    mView.closeProgressDialog()
                })
        mCompositeDisposable.add(disposable)

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

    private fun derivePrivateKey(address: String): String? {
        val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
        var ethWallet = EthWallet()
        for (i in ethWallets.indices) {
            if (ethWallets[i].address.toLowerCase().equals(address.toLowerCase())) {
                ethWallet = ethWallets[i]
                break
            }
        }
        return ETHWalletUtils.derivePrivateKey(ethWallet.id!!)
    }

    private fun generateTransaction(fromAddress: String, contractAddress: String, toAddress: String, privateKey: String, amount: String, limit: Int, price: Int, decimals: Int): String {
        val web3j = Web3j.build(HttpService(ConstantValue.ethNodeUrl))
        try {
            return testTokenTransaction(web3j, fromAddress, privateKey, contractAddress, toAddress, amount, decimals, limit, price)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun testTokenTransaction(web3j: Web3j, fromAddress: String, privateKey: String, contractAddress: String, toAddress: String, amount: String, decimals: Int, limit: Int, price: Int): String {
        val nonce: BigInteger
        var ethGetTransactionCount: EthGetTransactionCount? = null
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (ethGetTransactionCount == null) {
            return ""
        }
        nonce = ethGetTransactionCount.transactionCount
        println("nonce $nonce")
        val gasPrice = Convert.toWei(BigDecimal.valueOf(price.toLong()), Convert.Unit.GWEI).toBigInteger()
        val gasLimit = BigInteger.valueOf(limit.toLong())
        val value = BigInteger.ZERO
        KLog.i("Arrays")
        val function = Function(
                "transfer",
                Arrays.asList(Address(toAddress), Uint256(baseToSubunit(amount, decimals))) as List<Type<*>>?,
                Arrays.asList<TypeReference<*>>(object : TypeReference<Type<*>>() {}))

        val encodedFunction = FunctionEncoder.encode(function)


        KLog.i(encodedFunction)
        val chainId = ChainId.MAINNET
        val signedData: String?
        try {
            signedData = ColdWallet.signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, encodedFunction, chainId, privateKey)
            if (signedData != null) {
                KLog.i(signedData)
                //如果客户端发送的话，就把下面三行打开
                val ethSendTransaction = web3j.ethSendRawTransaction(signedData).send()
                if (ethSendTransaction.hasError()) {
                    KLog.i(ethSendTransaction.error.message)
                } else {
                }
                println("交易的hash为：" + ethSendTransaction.transactionHash)
                return ethSendTransaction.transactionHash
                //                return signedData;
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }

        return ""
    }

    /**
     * Base - taken to mean default unit for a currency e.g. ETH, DOLLARS
     * Subunit - taken to mean subdivision of base e.g. WEI, CENTS
     *
     * @param baseAmountStr - decimal amonut in base unit of a given currency
     * @param decimals - decimal places used to convert to subunits
     * @return amount in subunits
     */
    fun baseToSubunit(baseAmountStr: String, decimals: Int): BigInteger {
        assert(decimals >= 0)
        val baseAmount = BigDecimal(baseAmountStr)
        val subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(decimals))
        try {
            return subunitAmount.toBigIntegerExact()
        } catch (ex: ArithmeticException) {
            assert(false)
            return subunitAmount.toBigInteger()
        }

    }
}
package com.stratagile.qlink.ui.activity.otc.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.ColdWallet
import com.stratagile.qlink.R
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.EntrustTodo
import com.stratagile.qlink.db.EntrustTodoDao
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.newwinq.Product
import com.stratagile.qlink.ui.activity.otc.contract.OrderBuyContract
import com.stratagile.qlink.ui.activity.otc.OrderBuyFragment
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.QlcReceiveUtils
import com.stratagile.qlink.utils.SendBack
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.StringUtils
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
import java.lang.Error
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: presenter of OrderBuyFragment
 * @date 2019/07/08 17:24:58
 */

class OrderBuyPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: OrderBuyContract.View) : OrderBuyContract.OrderBuyContractPresenter {

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
                                    QlcReceiveUtils.sendQGas(qlcAccount, receiveAddress, amount, "SELL QGAS", false, object : SendBack {
                                        override fun send(suceess: String) {
                                            if ("".equals(suceess)) {
                                                mView.generatebuyQgasOrderFailed("send qgas error")
                                                it.onComplete()
                                            } else {
                                                KLog.i(suceess)
                                                it.onNext(suceess)
                                                it.onComplete()
                                            }
                                        }

                                    })
                                } else {
                                    mView.generatebuyQgasOrderFailed("Not enough QGAS")
                                    it.onComplete()
                                }
                            }
                        } else {
                            mView.generatebuyQgasOrderFailed("send qgas error")
                            it.onComplete()
                        }
                    }
                })
            })
        }.concatMap {
            map.put("fromAddress", qlcAccount.address)
            map.put("txid", it.blockingFirst())
            httpAPIWrapper.generateBuyQgasOrder(map)
        }.subscribe({ baseBack ->
            //isSuccesse
            mView.closeProgressDialog()
            mView.generateBuyQgasOrderSuccess()
        }, {
            mView.closeProgressDialog()
        }, {
            //onComplete
            KLog.i("onComplete")
            mView.closeProgressDialog()
        })
        mCompositeDisposable.add(disposable)
    }

    fun generateEntrustBuyOrder(txid: String, fromAddress: String, map: MutableMap<String, String>) {
        map.put("fromAddress", fromAddress)
        map.put("txid", getTxidByHex(txid))
        mCompositeDisposable.add(httpAPIWrapper.generateBuyQgasOrder(map).subscribe({
            mView.closeProgressDialog()
            mView.generateBuyQgasOrderSuccess()
        }, {
            mView.closeProgressDialog()
            EntrustTodo.createEntrustTodo(map)
            sysbackUp(txid, "ENTRUST_ORDER", "", "", "")
        }, {
            mView.closeProgressDialog()
            EntrustTodo.createEntrustTodo(map)
            sysbackUp(txid, "ENTRUST_ORDER", "", "", "")
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
                var list = AppConfig.instance.daoSession.entrustTodoDao.queryBuilder().where(EntrustTodoDao.Properties.Txid.eq(txid)).list()
                if (list.size > 0) {
                    AppConfig.instance.daoSession.entrustTodoDao.delete(list[0])
                }
            }
        })
    }

    fun sendEthToken(walletAddress: String, toAddress: String, amount: String, price: Int, tokenInfo: EthWalletInfo.DataBean.TokensBean, map: MutableMap<String, String>) {
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
                        generateEntrustBuyOrder(it, walletAddress, map)
                        KLog.i("transaction Hash: $it")
                    }
                }, {
                    mView.closeProgressDialog()
//                    EntrustTodo.createEntrustTodo(map)
                }, {
                    KLog.i("complete")
                    mView.closeProgressDialog()
//                    EntrustTodo.createEntrustTodo(map)
                })
        mCompositeDisposable.add(disposable)
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
        val web3j = Web3j.build(HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"))
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

    fun generateBuyQgasOrder(map : Map<String, String>) {
        val disposable = httpAPIWrapper.generateBuyQgasOrder(map)
                .subscribe({ baseBack ->
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

    fun getEthWalletDetail(map: java.util.HashMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.getEthWalletInfo(map).subscribe({
            mView.setEthTokens(it)
        }, {
            mView.closeProgressDialog()
        }, {
            mView.closeProgressDialog()
        }))
    }

}
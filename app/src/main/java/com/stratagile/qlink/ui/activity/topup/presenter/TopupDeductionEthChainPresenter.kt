package com.stratagile.qlink.ui.activity.topup.presenter
import android.support.annotation.NonNull
import com.socks.library.KLog
import com.stratagile.qlink.ColdWallet
import com.stratagile.qlink.R
import com.stratagile.qlink.api.HttpObserver
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.TopupTodoListDao
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.entity.topup.GroupItemList
import com.stratagile.qlink.entity.topup.TopupJoinGroup
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.eth.presenter.EthTransferPresenter
import com.stratagile.qlink.ui.activity.topup.contract.TopupDeductionEthChainContract
import com.stratagile.qlink.ui.activity.topup.TopupDeductionEthChainActivity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import io.reactivex.Observable
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
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: presenter of TopupDeductionEthChainActivity
 * @date 2019/12/27 11:59:29
 */
class TopupDeductionEthChainPresenter @Inject
constructor(internal var httpAPIWrapper: HttpAPIWrapper, private val mView: TopupDeductionEthChainContract.View) : TopupDeductionEthChainContract.TopupDeductionEthChainContractPresenter {

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

    fun getMainAddress() {
        mCompositeDisposable.add(httpAPIWrapper.getMainAddress(HashMap<String, String>()).subscribe({
            KLog.i("onSuccesse")
            ConstantValue.mainAddress = it.data.neo.address
            ConstantValue.ethMainAddress = it.data.eth.address
            ConstantValue.mainAddressData = it.data
            mView.setMainAddress()
        }, {

        }, {

        }))
    }

    fun createTopupOrder(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.topupCreateOrder(map).subscribe({
            mView.createTopupOrderSuccess(it)
        }, {
            mView.createTopupOrderError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }, {
            mView.createTopupOrderError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }))
    }

    fun saveDeductionTokenTxid(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.saveDeductionTokenTxid(map).subscribe({
            mView.saveDeductionTokenTxidBack(it)
        }, {
            mView.createTopupOrderError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
        }, {
            mView.createTopupOrderError()
//            TopupTodoList.createTodoList(map)
//            sysbackUp(map["txid"]!!, "TOPUP", "", "", "")
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
                var list = AppConfig.instance.daoSession.topupTodoListDao.queryBuilder().where(TopupTodoListDao.Properties.Txid.eq(txid)).list()
                if (list.size > 0) {
                    AppConfig.instance.daoSession.topupTodoListDao.delete(list[0])
                }
            }
        })
    }



    fun topupOrderConfirm(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.topupOrderConfirm(map).subscribe({
            mView.topupOrderStatus(it)
        }, {
            var topupOrder = TopupOrder()
            var orderBean = TopupOrder.OrderBean()
            topupOrder.order = orderBean
            topupOrder.order.id = map["orderId"]
            topupOrder.order.status = "NEW"
            mView.topupOrderStatus(topupOrder)
        }, {
            var topupOrder = TopupOrder()
            var orderBean = TopupOrder.OrderBean()
            topupOrder.order = orderBean
            topupOrder.order.id = map["orderId"]
            topupOrder.order.status = "NEW"
            mView.topupOrderStatus(topupOrder)
        }))
    }

    fun getETHWalletDetail(address: String, map: Map<*, *>) {
        val disposable = httpAPIWrapper.getEthWalletInfo(map)
                .subscribe({ baseBack ->
                    //isSuccesse
                    mView.getEthWalletBack(baseBack)
                }, { throwable -> throwable.printStackTrace() }, {
                    //onComplete
                    KLog.i("onComplete")
                })
        mCompositeDisposable.add(disposable)
    }

    fun transaction(walletAddress: String, tokenAddress: String, tokenDecimal: Int, toAddress: String, amount: String, limit: Int, price: Int) {
        Observable.create(ObservableOnSubscribe<String>{ emitter ->
            emitter.onNext(generateTransaction(walletAddress, tokenAddress, toAddress, derivePrivateKey(walletAddress)!!, amount, limit, price, tokenDecimal))
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(s: String) {
                        mView.closeProgressDialog()
                        if ("" == s) {
                            ToastUtil.displayShortToast(AppConfig.getInstance().resources.getString(R.string.error2))
                        } else {
                            mView.sendPayTokenSuccess(s)
                        }
                        KLog.i("transaction Hash: $s")
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

    }

    private fun generateTransaction(fromAddress: String, contractAddress: String, toAddress: String, privateKey: String, amount: String, limit: Int, price: Int, decimals: Int): String {
        val web3j = Web3j.build(HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"))
        KLog.i("generateTransaction")
        try {
            return testTokenTransaction(web3j, fromAddress, privateKey, contractAddress, toAddress, amount, decimals, limit, price)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun derivePrivateKey(address: String): String? {
        val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
        var ethWallet = EthWallet()
        for (i in ethWallets.indices) {
            if (ethWallets[i].address.equals(address, true)) {
                ethWallet = ethWallets[i]
                break
            }
        }
        return ETHWalletUtils.derivePrivateKey(ethWallet.id!!)
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
        KLog.i("nonce $nonce")
        val gasPrice = Convert.toWei(BigDecimal.valueOf(price.toLong()), Convert.Unit.GWEI).toBigInteger()
        val gasLimit = BigInteger.valueOf(limit.toLong())
        val value = BigInteger.ZERO

        val function = Function(
                "transfer",
                Arrays.asList(Address(toAddress), Uint256(EthTransferPresenter.baseToSubunit(amount, decimals))),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Type<*>>() {

                }))

        val encodedFunction = FunctionEncoder.encode(function)


        KLog.i(encodedFunction)
        val chainId = ChainId.MAINNET
        val signedData: String?
        KLog.i("开始发送")
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
                KLog.i("交易的hash为：" + ethSendTransaction.transactionHash)
                return ethSendTransaction.transactionHash
                //                return signedData;
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""
    }

    fun saveItemDeductionTokenTxid(map: MutableMap<String, String>) {
        mCompositeDisposable.add(httpAPIWrapper.saveItemDeductionTokenTxid(map).subscribe({
            mView.saveItemDeductionTokenTxidBack(it)
        }, {
            mView.createTopupOrderError()
        }, {
            mView.createTopupOrderError()
        }))
    }

    fun itemDeductionTokenConfirm(map: MutableMap<String, String>, item : GroupItemList.ItemListBean) {
        mCompositeDisposable.add(httpAPIWrapper.itemDeductionTokenConfirm(map).subscribe({
            mView.itemOrderStatus(it)
        }, {
            var topupOrder = TopupJoinGroup()
            topupOrder.item = item
            mView.itemOrderStatus(topupOrder)
        }, {
            var topupOrder = TopupJoinGroup()
            topupOrder.item = item
            mView.itemOrderStatus(topupOrder)
        }))
    }
}
package com.stratagile.qlink.ui.activity.defi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.C
import com.stratagile.qlink.ColdWallet
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.entity.defi.EthGasPrice
import com.stratagile.qlink.entity.defi.ServerEthPrice
import com.stratagile.qlink.ui.activity.defi.component.DaggerConfirmComponent
import com.stratagile.qlink.ui.activity.defi.contract.ConfirmContract
import com.stratagile.qlink.ui.activity.defi.module.ConfirmModule
import com.stratagile.qlink.ui.activity.defi.presenter.ConfirmPresenter
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.web3.entity.Web3Transaction
import kotlinx.android.synthetic.main.activity_confirm.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ChainId
import org.web3j.utils.Convert
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/10/14 16:59:28
 */

class ConfirmActivity : BaseActivity(), ConfirmContract.View {

    @Inject
    internal lateinit var mPresenter: ConfirmPresenter
    lateinit var web3Transaction: Web3Transaction
    var amount = 0.toBigDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_confirm)
        mPresenter.getEthGasPrice(hashMapOf())
    }
    override fun initData() {
        title.text = getString(R.string.confirm_transaction)
        web3Transaction = intent.getParcelableExtra(C.EXTRA_WEB3TRANSACTION)
        text_from.text = intent.getStringExtra(C.EXTRA_ADDRESS)
        text_to.text = web3Transaction.recipient.toString()
        text_gas_estimate.text = web3Transaction.gasLimit.toString()
        amount = try {
            BigDecimal(intent.getStringExtra(C.EXTRA_AMOUNT))
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }
        progress_gas_estimate.visibility = View.GONE
        text_gas_limit.text = "600000"
        send_button.setOnClickListener {
            showProgressDialog()
            thread {
                var hashData = signTransaction(intent.getStringExtra(C.EXTRA_ADDRESS), derivePrivateKey(intent.getStringExtra(C.EXTRA_ADDRESS)), web3Transaction.recipient.toString(), web3Transaction.payload, web3Transaction.gasLimit.toInt(), ethGasPrice.result.proposeGasPrice.toInt())
                var resultIntent = Intent()
                runOnUiThread {
                    closeProgressDialog()
                }
                if ("".equals(hashData)) {
                    KLog.i("交易失败")
                    resultIntent.putExtra(C.EXTRA_WEB3TRANSACTION, web3Transaction)
                    setResult(1, resultIntent)
                    finish()
                } else {
                    resultIntent.putExtra(C.EXTRA_TRANSACTION_DATA, hashData)
                    resultIntent.putExtra(C.EXTRA_WEB3TRANSACTION, web3Transaction)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        var resultIntent = Intent()
        resultIntent.putExtra(C.EXTRA_WEB3TRANSACTION, web3Transaction)
        setResult(1, resultIntent)
        finish()
    }

    private fun signTransaction(fromAddress: String, privateKey: String, contractAddress: String, data : String, limit: Int, price: Int): String? {
        KLog.i(ConstantValue.ethNodeUrl)
        val web3j = Web3j.build(HttpService(ConstantValue.ethNodeUrl))
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
//        val value = BigInteger.ZERO

        val chainId = ChainId.MAINNET
        val signedData: String?
        try {
            signedData = ColdWallet.signTransaction(nonce, gasPrice, gasLimit, contractAddress, amount.toBigInteger(), data, chainId, privateKey)
            //如果客户端发送的话，就把下面三行打开
            val ethSendTransaction = web3j.ethSendRawTransaction(signedData).send()
            if (ethSendTransaction.hasError()) {
                KLog.i(ethSendTransaction.error.message)
                runOnUiThread {
                    toast(ethSendTransaction.error.message)
                }
                return ""
            } else {
            }
            println("交易的hash为：" + ethSendTransaction.transactionHash)
            return ethSendTransaction.transactionHash
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun derivePrivateKey(address: String): String {
        val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
        var ethWallet = EthWallet()
        for (i in ethWallets.indices) {
            if (ethWallets[i].address.equals(address, true)) {
                ethWallet = ethWallets[i]
                break
            }
        }
        return ETHWalletUtils.derivePrivateKey(ethWallet.id)
    }

    override fun setupActivityComponent() {
       DaggerConfirmComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .confirmModule(ConfirmModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: ConfirmContract.ConfirmContractPresenter) {
            mPresenter = presenter as ConfirmPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
    lateinit var ethGasPrice : EthGasPrice
    override fun setEthGasPrice(serverEthPrice: ServerEthPrice) {
        var ethGasPrice1 = Gson().fromJson<EthGasPrice>(serverEthPrice.gasPrice, EthGasPrice::class.java)
        ethGasPrice = ethGasPrice1
        text_gas_price.text = ethGasPrice.result.proposeGasPrice
        val averageGas = Convert.toWei(ethGasPrice.result.proposeGasPrice, Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        var averageEth = averageGas * web3Transaction.gasLimit.toBigDecimal()
        text_network_fee.text = averageEth.stripTrailingZeros().toPlainString() + " ETH"
        var amount1 = Convert.toWei(amount, Convert.Unit.WEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        text_value.text = (amount1 + averageEth).stripTrailingZeros().toPlainString()
        progress_network_fee.visibility = View.GONE
    }

}
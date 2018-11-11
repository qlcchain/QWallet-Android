package com.stratagile.qlink.data

import android.util.Log
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.stratagile.qlink.*
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.utils.SpUtil
import neoutils.Neoutils
import neoutils.Wallet
import unsigned.toUByte
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by huzhipeng on 2018/5/2.
 */
class NeoNodeRPC {
    var nodeURL = "http://seed2.neo.org:10332"
//    var nodeURL = "http://seed1.o3node.org:10332"

    //var nodeURL = "http://seed3.neo.org:20332" //TESTNET
    enum class Asset() {
        NEO,
        GAS,
        QLC;

        fun assetID(): String {
            if (this == GAS) {
                return "602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7"
            } else if (this == NEO) {
                return "c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b"
            } else if (this == QLC) {
                if (SpUtil.getBoolean(AppConfig.instance, ConstantValue.isMainNet, false)) {
                    return "0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5"
                } else {
                    return "b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263"
                }
            }
            return ""
        }
    }

    constructor(url: String = "http://seed3.neo.org:10332") {
        this.nodeURL = url
    }

    enum class RPC() {
        GETBLOCKCOUNT,
        GETCONNECTIONCOUNT,
        VALIDATEADDRESS,
        GETACCOUNTSTATE,
        SENDRAWTRANSACTION,
        INVOKEFUNCTION;

        fun methodName(): String {
            return this.name.toLowerCase()
        }
    }

    fun sendNativeAssetTransaction(assets: Assets, wallet: Wallet, tokenContractHash: Asset, fromAddress: String, toAddress: String, amount: Double, callBack: NeoCallBack) {
        val payload = generateSendTransactionPayload(wallet, tokenContractHash, amount, toAddress, assets!!, null)
        val jsonBody = sendRawTransaction(payload)
        callBack.NeoTranscationResult(jsonBody)
    }

    private fun generateSendTransactionPayload(wallet: Wallet, asset: Asset, amount: Double, toAddress: String, assets: Assets, attributes: Array<TransactionAttritbute>?): ByteArray {
        var error: Error?
        val inputData = getInputsNecessaryToSendAsset(asset, amount, assets)
        val payloadPrefix = byteArrayOf(0x80.toUByte(), 0x00.toByte())
        val rawTransaction = packRawTransactionBytes(payloadPrefix, wallet, asset, inputData.payload!!, inputData.totalAmount!!, amount, toAddress, attributes)
        val privateKeyHex = wallet.privateKey.toHex()
        val signatureData = Neoutils.sign(rawTransaction, privateKeyHex)
        val finalPayload = concatenatePayloadData(wallet, rawTransaction, signatureData)
        Log.d("PAYLAOD:", finalPayload.toHex())
        return finalPayload
    }

    fun sendNEP5Token(assets: Assets, wallet: Wallet, tokenContractHash: String, fromAddress: String, toAddress: String, amount: Double, callBack: NeoCallBack) {
        val scriptBytes = buildNEP5TransferScript(tokenContractHash, fromAddress, toAddress, amount)
        val finalPayload = generateInvokeTransactionPayload(wallet, assets, scriptBytes.toHex(), tokenContractHash)
        val jsonBody = sendRawTransaction(finalPayload)
        callBack.NeoTranscationResult(jsonBody)
    }



    /**
     * 转账，接口
     */
    private fun sendRawTransaction(data: ByteArray) :String {
        var string = jsonArray(data.toHex())
//        Log.i("ddd", nodeURL)
//        val dataJson = jsonObject(
//                "jsonrpc" to "2.0",
//                "method" to RPC.SENDRAWTRANSACTION.methodName(),
//                "params" to string,
//                "id" to 3
//        )
        return data.toHex()
    }

    fun buildNEP5TransferScript(scriptHash: String, fromAddress: String, toAddress: String, amount: Double): ByteArray {
        val amountToSendInMemory: Long = (amount * 100000000).toLong()
        val fromAddressHash = fromAddress.hashFromAddress()
        val toAddressHash = toAddress.hashFromAddress()
        val scriptBuilder = ScriptBuilder()
        scriptBuilder.pushContractInvoke(scriptHash, operation = "transfer",
                args = arrayOf(amountToSendInMemory, toAddressHash, fromAddressHash)
        )
        var script = scriptBuilder.getScriptHexString()
        return byteArrayOf((script.length / 2).toUByte()) + script.hexStringToByteArray()
    }

    private fun generateInvokeTransactionPayload(wallet: Wallet, assets: Assets, script: String, contractAddress: String): ByteArray {
        val inputData = getInputsNecessaryToSendAsset(NeoNodeRPC.Asset.GAS, 0.00000001, assets)
        val payloadPrefix = byteArrayOf(0xd1.toUByte(), 0x00.toUByte()) + script.hexStringToByteArray()
        var rawTransaction = packRawTransactionBytes(payloadPrefix, wallet, Asset.GAS,
                inputData.payload!!, inputData.totalAmount!!, 0.00000001,
                Account?.getWallet()?.address!!, null)

        val privateKeyHex = wallet.privateKey.toHex()
        val signature = Neoutils.sign(rawTransaction, privateKeyHex)
        var finalPayload = concatenatePayloadData(wallet, rawTransaction, signature)
        finalPayload = finalPayload + contractAddress.hexStringToByteArray()
        return finalPayload

    }

    data class SendAssetReturn(val totalAmount: Double?, val payload: ByteArray?, val error: Error?)
    data class TransactionAttritbute(val messaeg: String?)

    private fun getInputsNecessaryToSendAsset(asset: Asset, amount: Double, assets: Assets): SendAssetReturn {
        var sortedUnspents: List<Unspent>
        var neededForTransaction: MutableList<Unspent> = arrayListOf()

        if (asset == Asset.NEO) {
            if (assets.NEO.balance < amount) {
                return SendAssetReturn(null, null, Error("insufficient balance"))
            }
            sortedUnspents = assets.NEO.unspent.sortedBy { it.value }
        } else {
            if (assets.GAS.balance < amount) {
                return SendAssetReturn(null, null, Error("insufficient balance"))
            }
            sortedUnspents = assets.GAS.unspent.sortedBy { it.value }
        }
        var runningAmount = 0.0
        var index = 0
        var count: Int = 0
        //Assume we always have enough balance to do this, prevent the check for bal
        while (runningAmount < amount) {
            neededForTransaction.add(sortedUnspents[index])
            runningAmount += sortedUnspents[index].value
            index += 1
            count += 1
        }
        var inputData: ByteArray = byteArrayOf(count.toByte())
        for (t: Unspent in neededForTransaction) {
            val data = hexStringToByteArray(t.txid)
            val reversedBytes = data.reversedArray()
            inputData = inputData + reversedBytes + ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(t.index.toShort()).array()
        }
        return SendAssetReturn(runningAmount, inputData, null)
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    private fun packRawTransactionBytes(payloadPrefix: ByteArray, wallet: Wallet, asset: Asset, inputData: ByteArray,
                                        runningAmount: Double, toSendAmount: Double, toAddress: String,
                                        attributes: Array<TransactionAttritbute>?): ByteArray {
        var inputDataBytes = inputData
        val needsTwoOutputTransactions = runningAmount != toSendAmount

        var numberOfAttributes: Byte = 0x00.toByte()
        var attributesPayload: ByteArray = ByteArray(0)
        //TODO add attribute
//        if attributes != nil {
//            for (attribute in attributes!!) {
//            if attribute.data != nil {
//                attributesPayload = attributesPayload + attribute.data!
//                numberOfAttributes = numberOfAttributes + 1
//            }
//        }
//        }

        var payload: ByteArray = payloadPrefix + numberOfAttributes
        payload = payload + attributesPayload + inputDataBytes
        if (needsTwoOutputTransactions) {
            //Transaction To Reciever
            payload = payload + byteArrayOf(0x02.toByte()) + asset.assetID().hexStringToByteArray().reversedArray()
            val amountToSendInMemory: Long = (toSendAmount * 100000000).toLong()
            payload += to8BytesArray(amountToSendInMemory)
            //reciever addressHash
            payload += toAddress.hashFromAddress().hexStringToByteArray()
            //Transaction To Sender
            payload += asset.assetID().hexStringToByteArray().reversedArray()
            val amountToGetBackInMemory = (runningAmount * 100000000).toLong() - (toSendAmount * 100000000).toLong()
            payload += to8BytesArray(amountToGetBackInMemory)
            payload += wallet.hashedSignature

        } else {
            payload = payload + byteArrayOf(0x01.toByte()) + asset.assetID().hexStringToByteArray().reversedArray()
            val amountToSendInMemory = (toSendAmount * 100000000).toLong()
            payload += to8BytesArray(amountToSendInMemory)
            payload += toAddress.hashFromAddress().hexStringToByteArray()
        }
        return payload
    }

    private fun concatenatePayloadData(wallet: Wallet, txData: ByteArray, signatureData: ByteArray): ByteArray {
        var payload = txData + byteArrayOf(0x01.toByte())                        // signature number
        payload += byteArrayOf(0x41.toByte())                              // signature struct length
        payload += byteArrayOf(0x40.toByte())                                 // signature data length
        payload += signatureData                   // signature
        payload += byteArrayOf(0x23.toByte())                                 // contract data length
        payload = payload + byteArrayOf(0x21.toByte()) + wallet.publicKey + byteArrayOf(0xac.toByte()) // NeoSigned publicKey
        return payload
    }
}
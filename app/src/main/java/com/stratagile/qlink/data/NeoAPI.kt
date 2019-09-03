package com.stratagile.qlink.data

import android.util.Log
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.socks.library.KLog
import com.stratagile.qlink.*
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.ClaimData
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.txutils.model.neo.TransactionAttribute
import neoutils.Neoutils
import neoutils.Wallet
import unsigned.toUByte
import java.math.BigDecimal
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

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

    fun sendNativeAssetTransaction(assets: UTXOS, wallet: Wallet, tokenContractHash: Asset, fromAddress: String, toAddress: String, amount: Double, callBack: NeoCallBack) {
        val payload = generateSendTransactionPayload(wallet, tokenContractHash, amount, toAddress, assets!!, null)
        val jsonBody = sendRawTransaction(payload)
        callBack.NeoTranscationResult(jsonBody)
    }

    private fun generateSendTransactionPayload(wallet: Wallet, asset: Asset, amount: Double, toAddress: String, assets: UTXOS, attributes: Array<TransactionAttritbute>?): ByteArray {
        var error: Error?
        val inputData = getInputsNecessaryToSendAsset1(wallet, asset, amount, assets, toAddress)
//        val payloadPrefix = byteArrayOf(0x80.toUByte(), 0x00.toByte())
//        val rawTransaction = packRawTransactionBytes(payloadPrefix, wallet, asset, inputData.payload!!, inputData.totalAmount!!.toDouble(), amount, toAddress, attributes)
//        val privateKeyHex = wallet.privateKey.toHex()
//        val signatureData = Neoutils.sign(rawTransaction, privateKeyHex)
//        val finalPayload = concatenatePayloadData(wallet, rawTransaction, signatureData)
//        Log.d("PAYLAOD:", finalPayload.toHex())
//        return finalPayload
        return inputData
    }

    fun sendNEP5Token(assets: UTXOS?, wallet: Wallet, tokenContractHash: String, fromAddress: String, toAddress: String, amount: Double, remark: String, callBack: NeoCallBack) {
        val scriptBytes = buildNEP5TransferScript(tokenContractHash, fromAddress, toAddress, amount)
        val finalPayload = generateInvokeTransactionPayload(wallet, assets, scriptBytes.toHex(), tokenContractHash, remark)
        val jsonBody = sendRawTransaction(finalPayload)
        callBack.NeoTranscationResult(jsonBody)
    }



    /**
     * 转账，接口
     */
    private fun sendRawTransaction(data: ByteArray) :String {
        var string = jsonArray(data.toHex())
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

    private fun generateInvokeTransactionPayload(wallet: Wallet, utxos: UTXOS?, script: String, contractAddress: String, remark : String): ByteArray {
//        val inputData = getInputsNecessaryToSendAsset(NeoNodeRPC.Asset.GAS, 0.00000001, assets)
//        val payloadPrefix = byteArrayOf(0xd1.toUByte(), 0x00.toUByte()) + script.hexStringToByteArray()
//        var rawTransaction = packRawTransactionBytes(payloadPrefix, wallet, Asset.GAS,
//                inputData.inputPayload!!, inputData.totalAmount!!.toDouble(), 0.00000001,
//                Account?.getWallet()?.address!!, null)
//
//        val privateKeyHex = wallet.privateKey.toHex()
//        val signature = Neoutils.sign(rawTransaction, privateKeyHex)
//        var finalPayload = concatenatePayloadData(wallet, rawTransaction, signature)
//        finalPayload = finalPayload + contractAddress.hexStringToByteArray()
//        return finalPayload
        val payloadPrefix = byteArrayOf(0xd1.toUByte(), 0x00.toUByte()) + script.hexStringToByteArray()
        var neoInput: SendAssetReturn? = null
        var gasInput: SendAssetReturn? = null
        var neoOutput: Pair<ByteArray, Int>? = null
        var gasOutput: Pair<ByteArray, Int>? = null
        var attachedNEOAmount = BigDecimal.ZERO
        var attachedGasAmount = BigDecimal.ZERO

        if (attachedNEOAmount > BigDecimal.ZERO) {
            neoInput = getInputsNecessaryToSendNEO(attachedNEOAmount, utxos)
        }

        if (attachedGasAmount > BigDecimal.ZERO || "0.00000001".toBigDecimal() > BigDecimal.ZERO ) {
            val toSendGasAmount = if (attachedGasAmount == BigDecimal.ZERO) BigDecimal(0.00000001) else attachedGasAmount
            gasInput = getInputsNecessaryToSendGAS(toSendGasAmount, utxos, "0.00000001".toBigDecimal())
        }

        if (neoInput != null) {
            neoOutput = getOutputDataPayload(wallet,
                    Asset.NEO, neoInput.totalAmount!!,
                    attachedNEOAmount, contractAddress.hexStringToByteArray().reversedArray().toHex(), neoInput.fee)
        }

        if (gasInput != null) {
            gasOutput = getOutputDataPayload(wallet,
                    Asset.GAS, gasInput.totalAmount!!,
                    attachedGasAmount, contractAddress.hexStringToByteArray().reversedArray().toHex(), gasInput.fee)
        }

        var finalAttributes = arrayOf<TransactionAttribute>(
                TransactionAttribute().scriptAttribute(wallet.address.hashFromAddress()),
                TransactionAttribute().remarkAttribute(String.format(remark, Date().time.toString())),
                TransactionAttribute().hexDescriptionAttribute(contractAddress))  + arrayOf()
        val totalInputCount = (neoInput?.inputCount ?: 0) + (gasInput?.inputCount ?: 0)
        val finalInputPayload = (neoInput?.inputPayload ?: byteArrayOf()) + (gasInput?.inputPayload ?: byteArrayOf())

        val totalOutputCount = (neoOutput?.second ?: 0) + (gasOutput?.second ?: 0)
        val finalOutputPayload = (neoOutput?.first ?: byteArrayOf()) + (gasOutput?.first ?: byteArrayOf())

        val rawTransaction = payloadPrefix +
                getAttributesPayload(finalAttributes) +
                totalInputCount.toByte() + finalInputPayload!! +
                totalOutputCount.toByte() + finalOutputPayload

        val privateKeyHex = wallet.privateKey.toHex()
        val signature = Neoutils.sign(rawTransaction, privateKeyHex)
        var finalPayload = concatenatePayloadData(wallet, rawTransaction, signature)
        finalPayload = finalPayload + contractAddress.hexStringToByteArray()
//        return Pair(finalPayload, rawTransaction)
        Log.i("payload3: ", finalPayload.toHex())
        return finalPayload
    }

    fun getAttributesPayload(attributes: Array<TransactionAttribute>?): ByteArray {
        var numberOfAttributes: Int = 0
        var attributesPayload: ByteArray = ByteArray(0)
        if (attributes != null) {
            for (attribute in attributes) {
                if (attribute.data != null) {
                    attributesPayload += attribute.data!!
                    numberOfAttributes += 1
                }
            }
        }

        var payload: ByteArray = byteArrayOf(numberOfAttributes.toUByte())
        payload = payload + attributesPayload
        return payload
    }


    fun signStr(message: String) : String{
        return Neoutils.bytesToHex(Neoutils.sign(message.hexStringToByteArray(), Account?.getWallet()?.privateKey?.toHex()))
    }

    data class SendAssetReturn(val totalAmount: BigDecimal?,
                               val inputCount: Int,
                               val inputPayload: ByteArray?,
                               val fee: BigDecimal,
                               val error: Error?)

    data class SendAssetReturn1(val totalAmount: Double?, val payload: ByteArray?, val error: Error?)
    data class TransactionAttritbute(val messaeg: String?)
    private fun getInputsNecessaryToSendAsset1(wallet: Wallet, asset: Asset, amount: Double, utxos: UTXOS, toAddress: String): ByteArray {
        val mainInput: SendAssetReturn
        var optionalFeeInput: SendAssetReturn? = null
        val payloadPrefix = byteArrayOf(0x80.toUByte(), 0x00.toByte())

        if (asset == Asset.GAS) {
            mainInput = getInputsNecessaryToSendGAS(amount.toSafeDecimal(), utxos, BigDecimal.ZERO)
        } else {
            mainInput = getInputsNecessaryToSendNEO(amount.toSafeDecimal(), utxos)
        }

        var mainOutputData = getOutputDataPayload(wallet,
                        asset, mainInput.totalAmount!!,
                        amount.toSafeDecimal(), toAddress.hashFromAddress(), mainInput.fee)

        var optionalFeeOutputData: Pair<ByteArray, Int>? = null
        if (optionalFeeInput != null) {
            optionalFeeOutputData = getOutputDataPayload(wallet,
                    Asset.GAS, optionalFeeInput.totalAmount!!,
                    BigDecimal(0.00000001), wallet.address.hashFromAddress(), BigDecimal.ZERO)
        }

        val totalInputCount = mainInput.inputCount + (optionalFeeInput?.inputCount ?: 0)
        val finalInputPayload = mainInput.inputPayload!! + (optionalFeeInput?.inputPayload ?: byteArrayOf())

        val totalOutputCount = mainOutputData.second + (optionalFeeOutputData?.second ?: 0)
        val finalOutputPayload = mainOutputData.first + (optionalFeeOutputData?.first ?: byteArrayOf())

        val rawTransaction = payloadPrefix +
                getAttributesPayload(null) +
                totalInputCount.toByte() + finalInputPayload +
                totalOutputCount.toByte() + finalOutputPayload

        val privateKeyHex = wallet.privateKey.toHex()
        val signatureData = Neoutils.sign(rawTransaction, privateKeyHex)
        val finalPayload = concatenatePayloadData(wallet, rawTransaction, signatureData)
        return finalPayload

    }

    private fun getOutputDataPayload(wallet: Wallet, asset: Asset,
                                     runningAmount: BigDecimal, toSendAmount: BigDecimal, toAddressHash: String,
                                     fee: BigDecimal): Pair<ByteArray, Int> {
        val needsTwoOutputTransactions = runningAmount.toSafeMemory(8) != (toSendAmount + fee).toSafeMemory(8) && toSendAmount > BigDecimal.ZERO

        var outputCount: Int = 0
        var payload = byteArrayOf()
        //assetless send
        if(runningAmount == BigDecimal.ZERO && fee == BigDecimal.ZERO) {
            payload += byteArrayOf()
            return Pair(payload, 0)
        }

        //this assumes only one type of input neo or gas per transaction
        if (needsTwoOutputTransactions) {
            //Transaction To Reciever
            outputCount = 2
            payload = payload + asset.assetID().hexStringToByteArray().reversedArray()
            val amountToSendInMemory: Long = toSendAmount.toSafeMemory(8)
            payload += to8BytesArray(amountToSendInMemory)
            //reciever addressHash
            payload += toAddressHash.hexStringToByteArray()
            //Transaction To Sender
            payload += asset.assetID().hexStringToByteArray().reversedArray()
            val amountToGetBackInMemory = runningAmount.toSafeMemory(8) - toSendAmount.toSafeMemory(8) - fee.toSafeMemory(8)
            payload += to8BytesArray(amountToGetBackInMemory)
            payload += wallet.hashedSignature

        } else if (toSendAmount > BigDecimal.ZERO){
            outputCount = 1
            payload = payload + asset.assetID().hexStringToByteArray().reversedArray()
            val amountToSendInMemory = toSendAmount.toSafeMemory(8)
            payload += to8BytesArray(amountToSendInMemory)
            payload += toAddressHash.hexStringToByteArray()
            // just paying a fee, need to return all change back to myself
        } else {
            outputCount = 1
            payload = payload + asset.assetID().hexStringToByteArray().reversedArray()
            val amountToSendInMemory = runningAmount.toSafeMemory(8) - toSendAmount.toSafeMemory(8) - fee.toSafeMemory(8)
            payload += to8BytesArray(amountToSendInMemory)
            payload += wallet.hashedSignature
        }

        return Pair(payload, outputCount)
    }

    private fun getInputsNecessaryToSendNEO(amount: BigDecimal, utxos: UTXOS?): SendAssetReturn {
        if (utxos == null) {
            return SendAssetReturn(BigDecimal.ZERO, 0, byteArrayOf(), BigDecimal.ZERO, null)
        }

        var sortedUnspents  = getSortedUnspents(Asset.NEO, utxos.data)
        var neededForTransaction: MutableList<UTXO> = arrayListOf()
        var decimalSum = BigDecimal(0)
        for (utxo in sortedUnspents) {
            decimalSum += (utxo.value.toSafeDecimal())
        }
        if (decimalSum < amount) {
            return SendAssetReturn(null, 0, null, BigDecimal.ZERO, Error("insufficient balance"))
        }

        var runningAmount = BigDecimal(0.0)
        var index = 0
        var count: Int = 0
        //Assume we always have enough balance to do this, prevent the check for bal
        while (runningAmount < amount) {
            neededForTransaction.add(sortedUnspents[index])
            runningAmount += (sortedUnspents[index].value.toSafeDecimal())
            index += 1
            count += 1
        }
        var inputData: ByteArray = byteArrayOf()
        for (t: UTXO in neededForTransaction) {
            val data = hexStringToByteArray(t.txid.removePrefix("0x"))
            val reversedBytes = data.reversedArray()
            inputData = inputData + reversedBytes + ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(t.index.toShort()).array()
        }
        return SendAssetReturn(runningAmount, count, inputData, BigDecimal.ZERO, null)
    }

    private fun getInputsNecessaryToSendGAS(amount: BigDecimal, utxos: UTXOS?, fee: BigDecimal = BigDecimal.ZERO): SendAssetReturn {
        if (utxos == null) {
            return SendAssetReturn(BigDecimal.ZERO, 0, byteArrayOf(), BigDecimal.ZERO, null)
        }

        var sortedUnspents  = getSortedUnspents(Asset.GAS, utxos.data)
        var neededForTransaction: MutableList<UTXO> = arrayListOf()
        var decimalSum = BigDecimal(0)
        for (utxo in sortedUnspents) {
            decimalSum += (utxo.value.toSafeDecimal())
        }
        if (decimalSum.toSafeMemory(8) < (amount + fee).toSafeMemory(8)) {
            return SendAssetReturn(null, 0, null, fee, Error("insufficient balance"))
        }

        var runningAmount = BigDecimal(0.0)
        var index = 0
        var count: Int = 0
        //Assume we always have enough balance to do this, prevent the check for bal
        while (runningAmount.toSafeMemory(8) < (amount + fee).toSafeMemory(8)) {
            neededForTransaction.add(sortedUnspents[index])
            runningAmount += (sortedUnspents[index].value.toSafeDecimal())
            index += 1
            count += 1
        }
        var inputData: ByteArray = byteArrayOf()
        for (t: UTXO in neededForTransaction) {
            val data = hexStringToByteArray(t.txid.removePrefix("0x"))
            val reversedBytes = data.reversedArray()
            inputData = inputData + reversedBytes + ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(t.index.toShort()).array()
        }
        return SendAssetReturn(runningAmount, count,  inputData, fee, null)
    }

    private fun getSortedUnspents(asset: Asset, utxos: Array<UTXO>): List<UTXO> {
        if (asset == Asset.NEO) {
            val unsorted = utxos.filter { it.asset.contains(Asset.NEO.assetID()) }
            return unsorted.sortedBy { it.value.toDouble() }
        } else {
            val unsorted = utxos.filter { it.asset.contains(Asset.GAS.assetID()) }
            return unsorted.sortedBy { it.value.toDouble() }
        }
    }


    private fun getInputsNecessaryToSendAsset(asset: Asset, amount: Double, assets: Assets): SendAssetReturn {
        if (true) {
            return SendAssetReturn(BigDecimal.ZERO, 0, byteArrayOf(), BigDecimal.ZERO, null)
        }
        var sortedUnspents: List<Unspent>
        var neededForTransaction: MutableList<Unspent> = arrayListOf()

        if (asset == Asset.NEO) {
            if (assets.NEO.balance < amount) {
                return SendAssetReturn(BigDecimal.ZERO, 0, byteArrayOf(), BigDecimal.ZERO, null)
            }
            sortedUnspents = assets.NEO.unspent.sortedBy { it.value }
        } else {
            if (assets.GAS.balance < amount) {
                return SendAssetReturn(BigDecimal.ZERO, 0, byteArrayOf(), BigDecimal.ZERO, null)
            }
            sortedUnspents = assets.GAS.unspent.sortedBy { it.value }
        }
        var runningAmount = BigDecimal(0.0)
        var index = 0
        var count: Int = 0
        //Assume we always have enough balance to do this, prevent the check for bal
        while (runningAmount < amount.toSafeDecimal()) {
            neededForTransaction.add(sortedUnspents[index])
            runningAmount += sortedUnspents[index].value.toSafeDecimal()
            index += 1
            count += 1
        }
        var inputData: ByteArray = byteArrayOf(count.toByte())
        for (t: Unspent in neededForTransaction) {
            val data = hexStringToByteArray(t.txid)
            val reversedBytes = data.reversedArray()
            inputData = inputData + reversedBytes + ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(t.index.toShort()).array()
        }
        return SendAssetReturn(runningAmount, count, inputData, BigDecimal.ZERO, null)
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

    fun claimGAS(wallet: Wallet, storedClaims: ClaimData, gas : String) : String {
        val payload = generateClaimTransactionPayload(wallet, storedClaims!!, gas)
        return sendRawTransaction(payload)
    }

    private fun generateClaimTransactionPayload(wallet: Wallet, claims: ClaimData, gas : String): ByteArray {
        val rawClaim = generateClaimInputData(wallet, claims, gas)
        val privateKeyHex = wallet.privateKey.toHex()
        val signature = Neoutils.sign(rawClaim, privateKeyHex)
        val finalPayload = concatenatePayloadData(wallet, rawClaim, signature)
        return finalPayload
    }

    private fun generateClaimInputData(wallet: Wallet, claims: ClaimData, gas : String): ByteArray {
        var payload: ByteArray = byteArrayOf(0x02.toByte()) // Claim Transaction Type
        payload += byteArrayOf(0x00.toByte()) // Version
        val claimsCount = claims.data.claims.count().toByte()
        payload += byteArrayOf(claimsCount)
        for (claim: ClaimData.DataBean.ClaimsBean in claims.data.claims) {
            payload += hexStringToByteArray(claim.txid.removePrefix("0x")).reversedArray()
            payload += ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(claim.index.toShort()).array()
        }
        payload += byteArrayOf(0x00.toByte()) // Attributes
        payload += byteArrayOf(0x00.toByte()) // Inputs
        payload += byteArrayOf(0x01.toByte()) // Output Count
        payload += hexStringToByteArray(NeoNodeRPC.Asset.GAS.assetID()).reversedArray()

        val claimIntermediate = BigDecimal(gas)
        val claimLong = claimIntermediate.multiply(BigDecimal(100000000)).toLong()
        payload += ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(claimLong).array()
        payload += wallet.hashedSignature
        Log.d("Claim Payload", payload.toHex())
        return payload
    }

}
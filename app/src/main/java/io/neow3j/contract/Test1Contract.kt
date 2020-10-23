package io.neow3j.contract

import android.os.Build
import android.support.annotation.RequiresApi
import com.socks.library.KLog
import com.stratagile.qlink.Account
import com.stratagile.qlink.constant.ConstantValue
import io.neow3j.constants.OpCode
import io.neow3j.crypto.transaction.RawScript
import io.neow3j.crypto.transaction.RawTransactionAttribute
import io.neow3j.model.types.StackItemType
import io.neow3j.model.types.TransactionAttributeUsageType
import io.neow3j.protocol.Neow3j
import io.neow3j.protocol.core.methods.response.InvocationResult
import io.neow3j.protocol.core.methods.response.StackItem
import io.neow3j.protocol.exceptions.ErrorResponseException
import io.neow3j.utils.Numeric
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import qlc.utils.TimeUtil
import java.io.IOException
import java.math.BigInteger
import java.util.*
import kotlin.concurrent.thread

object Test1Contract {
    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun testContract(neow3j: Neow3j?) {

        val neo = ScriptHash("0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5")
        try {
            var wallet = io.neow3j.wallet.Account.fromWIF("KzviPYuqHtQvw4T6vkbxJGnyoRGo1yYULAAn6WbTLwpQboHEkXcW").build()
//            var scriptHash = ScriptHash("0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5")
            var scriptHash = ScriptHash("b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263")
            val ofParam = ContractParameter.hash160(ScriptHash.fromAddress("AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK"))
            KLog.i(Account.getWallet()!!.address)
            KLog.i(ofParam)
            KLog.i(wallet.ecKeyPair.exportAsWIF())
            KLog.i(Numeric.toHexStringNoPrefix(wallet.ecKeyPair.privateKey))
            KLog.i(Numeric.toHexStringNoPrefix(wallet.publicKey))

            val builder: ContractInvocation.Builder = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(scriptHash)
                    .function("balanceOf")
            var parameter = ContractParameter.byteArrayFromAddress(Account.getWallet()!!.address)
            var result = builder.parameter(parameter).build().testInvoke()
            KLog.i(streamResponse(result)!!.asByteArray().asNumber)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun userLock(neow3j: Neow3j, hash: String, fromAddress: String, privateKey: String, amount: BigInteger, wrapperNeoAddress: String, overTimeBlocks: Int, contractAddress: String): String {
        var wallet = io.neow3j.wallet.Account.fromWIF(privateKey).build()
        KLog.i(fromAddress)
        KLog.i(privateKey)
        KLog.i(amount)
        KLog.i(wrapperNeoAddress)
        KLog.i(overTimeBlocks)
        KLog.i(contractAddress)
        try {
            var nep5ScriptHash = ScriptHash(contractAddress)
            val params: MutableList<ContractParameter> = ArrayList()
            params.add(ContractParameter.byteArray(hash))
            params.add(ContractParameter.byteArrayFromAddress(fromAddress))
            params.add(ContractParameter.integer(amount * 100000000.toBigInteger()))
            params.add(ContractParameter.byteArrayFromAddress(wrapperNeoAddress))
            params.add(ContractParameter.integer(overTimeBlocks))
            var contractInvocation = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(nep5ScriptHash)
                    .function("userLock")
                    .parameters(params)
                    .account(wallet)
                    .build()
                    .sign()
                    .invoke()
            KLog.i(contractInvocation.response.result)
            return contractInvocation.transaction.txId
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun deploy(neow3j: Neow3j) {
        var wallet = io.neow3j.wallet.Account.fromWIF("L46exNKHs4McEGg1c7hzbwsUt6LBxCe6CxpTBRWkDVFnKZDfDktg").build()
        try {
            var nep5ScriptHash = ScriptHash("1248666b69d75772e7f9df07bc258448f022abde")
            val params: MutableList<ContractParameter> = ArrayList()
            var contractInvocation = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(nep5ScriptHash)
                    .function("deploy")
                    .parameters(params)
                    .account(wallet)
                    .build()
                    .sign()
                    .invoke()
            KLog.i(contractInvocation.response.result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun querySwapInfo(neow3j: Neow3j, hash: String, contractAddress: String) {
        checkErc20Transaction()
//        try {
//            var nep5ScriptHash = ScriptHash(contractAddress)
//            val params: MutableList<ContractParameter> = ArrayList()
//            params.add(ContractParameter.byteArray(hash))
//            var contractInvocation = ContractInvocation.Builder(neow3j)
//                    .contractScriptHash(nep5ScriptHash)
//                    .function("querySwapInfo")
//                    .parameters(params)
//                    .build()
//                    .testInvoke()
//            contractInvocation.stack.forEach {
//                print(it)
////                KLog.i(it.toString())
////                if (it.type == StackItemType.BYTE_ARRAY) {
////                    KLog.i(Numeric.toHexStringNoPrefix(it.asByteArray().value))
////                    KLog.i(Numeric.hexToString(Numeric.toHexStringNoPrefix(it.asByteArray().value)))
////                } else if (it.type == StackItemType.ARRAY) {
////                    for (i in 0..it.asArray().size() - 1) {
////                        if (StackItemType.BYTE_ARRAY == it.asArray()[i].type) {
////                            KLog.i(Numeric.toHexStringNoPrefix(it.asArray()[i].asByteArray().value))
////                        } else if (StackItemType.BOOLEAN == it.asArray()[i].type) {
////                            KLog.i(it.asArray()[i].asBoolean().value)
////                        } else if (StackItemType.INTEGER == it.asArray()[i].type) {
////                            KLog.i(it.asArray()[i].asInteger().value)
////                        }
////                    }
////                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }
    @JvmStatic
    fun checkErc20Transaction() {
        thread {
            try {
                val web3j = Web3j.build(HttpService(ConstantValue.ethNodeUrl))
                var transaction = web3j.ethGetTransactionByHash("0x6edafaaf2a31bc97f68bd2693fe3b68a0e4e5efbd1fcd6298674a5fdaabad92e").send()
                KLog.i(transaction.transaction)
                if (transaction.hasError()) {
                    KLog.i(transaction.error.message)
                    if ("Unknown transaction".equals(transaction.error.message)) {
                    }
                } else {

                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    private fun print(item: StackItem) {
        if (StackItemType.ARRAY == item.type) {
            val itemArr = item.asArray()
            for (i in 0 until itemArr.size()) {
                if (StackItemType.BYTE_ARRAY == itemArr[i].type) {
                    if (i == 12) {
                        println(Numeric.toBigInt(itemArr[i].asByteArray().value))
                    } else {
                        if (i == 3) {
                            println(Numeric.toHexString(itemArr[i].asByteArray().value))
                        } else {
                            val str = Numeric.toHexStringNoPrefix(itemArr[i].asByteArray().value)
                            if (str.length == 40) {
                                println(itemArr[i].asByteArray().asAddress)
                            } else if (str.length == 64) {
                                println(Numeric.reverseHexString(str))
                            } else if (str.length == 8) {
                                println(Numeric.fromFixed8ToDecimal(Numeric.reverseHexString(str)))
                            } else {
                                println(str)
                            }
                        }
                    }
                } else if (StackItemType.BOOLEAN == itemArr[i].type) {
                    println(itemArr[i].asBoolean().value)
                } else if (StackItemType.INTEGER == itemArr[i].type) {
                    val num = itemArr[i].asInteger().value
                    if (BigInteger("1590000000").compareTo(num) < 0) {
                        System.out.println(com.stratagile.qlink.utils.TimeUtil.getTransactionTime(num.toLong()))
                    } else {
                        println(num)
                    }
                }
            }
        } else if (StackItemType.BYTE_ARRAY == item.type) {
            println(Numeric.hexToString(Numeric.toHexStringNoPrefix(item.asByteArray().value)))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun refundUser(neow3j: Neow3j, hash: String, userAddress: String, privateKey: String, contractAddress: String): String {
        var wallet = io.neow3j.wallet.Account.fromWIF(privateKey).build()
        wallet.updateAssetBalances(neow3j)
        val contractScripthash = ScriptHash(contractAddress)
        try {

            val attributes = Arrays.asList(
                    RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT, wallet.getScriptHash().toArray()),
                    RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT, contractScripthash.toArray())
            )

            var contractInvocation = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(contractScripthash)
                    .function("refundUser")
                    .parameter(ContractParameter.string(hash))
                    .parameter(ContractParameter.byteArrayFromAddress(userAddress))
                    .attributes(attributes)
                    .account(wallet)
                    .build()

            var tx = contractInvocation.transaction
            val witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), wallet.getECKeyPair())
            val witnessFrom = RawScript(ScriptBuilder().pushData(hash).pushInteger(1).opCode(OpCode.PACK).pushData("refundUser").toArray(), contractScripthash)
            tx.addScript(witnessTo)
            tx.addScript(witnessFrom)
            contractInvocation.invoke()

            KLog.i(contractInvocation.response.result)
            KLog.i(contractInvocation.transaction.txId)
            return contractInvocation.transaction.txId
//            contractInvocation.testInvoke().stack.forEach {
//                KLog.i(it.toString())
//                KLog.i(Numeric.toHexStringNoPrefix(it.asByteArray().value))
//                return Numeric.toHexStringNoPrefix(it.asByteArray().value)
//            }
        } catch (e: ErrorResponseException) {
            KLog.i(e.getError().getMessage())
            e.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun userUnlock(neow3j: Neow3j, orginHash: String, userAddress: String, privateKey: String, contractAddress: String): String {
        var wallet = io.neow3j.wallet.Account.fromWIF(privateKey).build()
        wallet.updateAssetBalances(neow3j)
        val contractScripthash = ScriptHash(contractAddress)
        try {

            val attributes = Arrays.asList(
                    RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT, wallet.getScriptHash().toArray()),
                    RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT, contractScripthash.toArray())
            )

            var contractInvocation = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(contractScripthash)
                    .function("userUnlock")
                    .parameter(ContractParameter.string(orginHash))
                    .parameter(ContractParameter.byteArrayFromAddress(userAddress))
                    .attributes(attributes)
                    .account(wallet)
                    .build()

            var tx = contractInvocation.transaction
            val witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), wallet.getECKeyPair())
            val witnessFrom = RawScript(ScriptBuilder().pushData(orginHash).pushInteger(1).opCode(OpCode.PACK).pushData("userUnlock").toArray(), contractScripthash)
            tx.addScript(witnessTo)
            tx.addScript(witnessFrom)
            contractInvocation.invoke()

            KLog.i(contractInvocation.response.result)
            KLog.i(contractInvocation.transaction.txId)
            return contractInvocation.transaction.txId
        } catch (e: ErrorResponseException) {
            KLog.i(e.getError().getMessage())
            e.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun sha2561(neow3j: Neow3j, hash: String, contractAddress: String): String {
        try {
            var nep5ScriptHash = ScriptHash(contractAddress)
            val params: MutableList<ContractParameter> = ArrayList()
            params.add(ContractParameter.string(hash))
            var contractInvocation = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(nep5ScriptHash)
                    .function("sha256")
//                    .function("name")
                    .parameters(params)
                    .build()
                    .testInvoke()
            contractInvocation.stack.forEach {
                KLog.i(it.toString())
                KLog.i(Numeric.toHexStringNoPrefix(it.asByteArray().value))
                return Numeric.toHexStringNoPrefix(it.asByteArray().value)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmStatic
    fun sha2562(neow3j: Neow3j, hash: String, privateKey: String, contractAddress: String): String {
        try {
            var nep5ScriptHash = ScriptHash(contractAddress)
            val params: MutableList<ContractParameter> = ArrayList()
            params.add(ContractParameter.string(hash))
            var contractInvocation = ContractInvocation.Builder(neow3j)
                    .contractScriptHash(nep5ScriptHash)
                    .function("sha2562")
                    .parameters(params)
                    .build()
                    .testInvoke()
            contractInvocation.stack.forEach {
                KLog.i(it.toString())
                KLog.i(Numeric.toHexStringNoPrefix(it.asByteArray().value))
                return Numeric.toHexStringNoPrefix(it.asByteArray().value)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.N)
    fun streamResponse(invocResult: InvocationResult): StackItem? {
        return Optional.ofNullable(invocResult.stack)
                .orElseGet { emptyList() }
                .stream()
                .findFirst()
                .orElse(null)
    }

    @JvmStatic
    fun byteArrayToString(byteArray: String) {
        KLog.i(Numeric.hexToString(byteArray))
    }

    @JvmStatic
    fun byteArrayToString2(byteArray: String) {
        KLog.i(Numeric.hexToInteger(byteArray))
    }
}
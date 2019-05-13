package com.stratagile.qlc

import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.socks.library.KLog
import com.stratagile.qlc.QLCDataModels.AccountCreate
import com.stratagile.qlc.QLCDataModels.BaseResult
import com.stratagile.qlink.utils.txutils.model.CommandEnum
import io.eblock.eos4j.Test

class QLCAPI {
//    private var nodeURL = "http://47.103.40.20:19735"
    private var nodeURL = "http://192.168.1.134:19735"
    constructor(url: String = "http://192.168.1.134:19735") {
        this.nodeURL = url
    }
    companion object {
        fun getSeed(): String {
           return "1234567890123456789012345678901234567890123456789012345678908890"
        }
        @JvmStatic
        fun main(args: Array<String>) {
            val nodeURL = "http://127.0.0.1:19735"
//            val nodeURL = "http://47.103.40.20:19735"
            val dataJson = jsonObject(
                    "jsonrpc" to "2.0",
                    "method" to RPC.account_create.methodName(),
                    "params" to jsonArray(getSeed()),
                    "id" to 3
            )
            System.out.println(dataJson.toString())
            System.out.println(nodeURL)
            var request = nodeURL.httpPost().body(dataJson.toString())
            request.headers["Content-Type"] = "application/json"
            request.responseString { _, _, result ->
                val (data, error) = result
                if (error == null) {
                    System.out.println(data!!)
                    val gson = Gson()
                    val nodeResponse = gson.fromJson<AccountCreate>(data!!)
                } else {
                    System.out.println(error.localizedMessage)
                }
            }
        }
    }
    enum class RPC {
        /**
         * 通过seed和index创建一个新的账户，返回账户的公钥和私钥
         */
        account_create,

        /**
         * 通过账户公钥返回账户的地址
         */
        account_forPublicKey,

        /**
         * 通过账户地址返回账户公钥
         */
        account_publicKey,

        /**
         * 检测账户地址是否合法
         */
        account_validate,

        /**
         * 返回钱包里每种token的余额
         */
        wallet_getBalances,
        /**
         * 返回钱包账户的私钥和公钥
         */
        wallet_getRawKey,
        /**
         * 创建新的seed
         */
        wallet_newSeed,
        /**
         * 创建新的钱包并返回主地址
         */
        wallet_newWallet,
        /**
         * 修改钱包密码
         */
        wallet_changePassword,

        /**
         * 返回账户的详细信息，包含该账户下的每个token
         */
        ledger_accountInfo,

        /**
         * 返回已确认和未确认的区块总数
         */
        ledger_blocksCount,
        ;

        fun methodName(): String {
            return this.name
        }
    }

    fun accountCreate(seed : String, completion: (Pair<AccountCreate?, Error?>) -> Unit) {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.account_create.methodName(),
                "params" to jsonArray(seed),
                "id" to 3
        )
        KLog.i(dataJson.toString())
        KLog.i(nodeURL)
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->

            val (data, error) = result
            if (error == null) {
                val gson = Gson()
                val nodeResponse = gson.fromJson<AccountCreate>(data!!)
                completion(Pair<AccountCreate?, Error?>(nodeResponse, null))
            } else {
                KLog.i(error.localizedMessage)
                completion(Pair<AccountCreate?, Error?>(null, Error(error.localizedMessage)))
            }
        }
    }

    fun walletNewSeed(completion: (Pair<BaseResult?, Error?>) -> Unit) {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.wallet_newSeed.methodName(),
                "params" to jsonArray(),
                "id" to 3
        )
        KLog.i(dataJson.toString())
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->

            val (data, error) = result
            if (error == null) {
                val gson = Gson()
                val nodeResponse = gson.fromJson<BaseResult>(data!!)
                completion(Pair<BaseResult?, Error?>(nodeResponse, null))
            } else {
                KLog.i(error.localizedMessage)
                completion(Pair<BaseResult?, Error?>(null, Error(error.localizedMessage)))
            }
        }
    }

    fun walletNewWallet(password : String, seed : String = "", completion: (Pair<BaseResult?, Error?>) -> Unit) {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.wallet_newWallet.methodName(),
                "params" to jsonArray(password, seed),
                "id" to 3
        )
        KLog.i(dataJson.toString())
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->
            KLog.i(result.component1())
            KLog.i(result.component2())
            val (data, error) = result
            if (error == null) {
                val gson = Gson()
                val nodeResponse = gson.fromJson<BaseResult>(data!!)
                completion(Pair<BaseResult?, Error?>(nodeResponse, null))
            } else {
                KLog.i(error.localizedMessage)
                completion(Pair<BaseResult?, Error?>(null, Error(error.localizedMessage)))
            }
        }
    }
    fun ledgerAccountInfo(address : String, completion: (Pair<BaseResult?, Error?>) -> Unit) {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.ledger_accountInfo.methodName(),
                "params" to jsonArray(address),
                "id" to 3
        )
        KLog.i(dataJson.toString())
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->

            val (data, error) = result
            KLog.i(result.component1())
            KLog.i(result.component2())
            if (error == null) {
                val gson = Gson()
                val nodeResponse = gson.fromJson<BaseResult>(data!!)
                completion(Pair<BaseResult?, Error?>(nodeResponse, null))
            } else {
                KLog.i(error.localizedMessage)
                completion(Pair<BaseResult?, Error?>(null, Error(error.localizedMessage)))
            }
        }
    }

    fun walletGetBalance(address : String, password : String, completion: (Pair<BaseResult?, Error?>) -> Unit) {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.wallet_getBalances.methodName(),
                "params" to jsonArray(address, password),
                "id" to 3
        )
        KLog.i(dataJson.toString())
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->

            val (data, error) = result
            KLog.i(result.component1())
            KLog.i(result.component2())
            if (error == null) {
                val gson = Gson()
                val nodeResponse = gson.fromJson<BaseResult>(data!!)
                completion(Pair<BaseResult?, Error?>(nodeResponse, null))
            } else {
                KLog.i(error.localizedMessage)
                completion(Pair<BaseResult?, Error?>(null, Error(error.localizedMessage)))
            }
        }
    }

    /**
     * 获取区块数量
     */
    fun getBlockCount() {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.ledger_blocksCount.methodName(),
                "id" to 3
        )
        KLog.i(dataJson.toString())
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->

            val (data, error) = result
            KLog.i(result.component1())
            KLog.i(result.component2())
            if (error == null) {
                val gson = Gson()
            } else {
                KLog.i(error.localizedMessage)
            }
        }
    }
    fun test(seed : String) {
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.account_create.methodName(),
                "params" to jsonArray(seed),
                "id" to 3
        )
        System.out.println(dataJson.toString())
        System.out.println(nodeURL)
        var request = nodeURL.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->

            val (data, error) = result
            if (error == null) {
                val gson = Gson()
                val nodeResponse = gson.fromJson<AccountCreate>(data!!)
            } else {
                System.out.println(error.localizedMessage)
            }
        }
    }
}

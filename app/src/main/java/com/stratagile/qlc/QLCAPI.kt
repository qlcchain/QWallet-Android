package com.stratagile.qlc

import com.alibaba.fastjson.JSONObject
import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.socks.library.KLog
import com.stratagile.qlc.entity.BaseResult
import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import kotlin.random.Random

class QLCAPI {
    companion object {
        fun getQlcWalletName() : String {
            var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
            if (qlcAccounts == null || qlcAccounts.size == 0) {
                return "QLC Chain-Wallet 01"
            } else if (qlcAccounts.size < 9){
                return "QLC Chain-Wallet 0" + (qlcAccounts.size + 1)
            } else {
                return "QLC Chain-Wallet " + (qlcAccounts.size + 1)
            }
        }
    }
    enum class RPC {

        /**
         * 返回账户的详细信息，包含该账户下的每个token
         */
        ledger_accountInfo,

        ledger_accountsBalance,
        ;

        fun methodName(): String {
            return this.name
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
        var request = ConstantValue.qlcNode.httpPost().body(dataJson.toString())
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

    fun walletGetBalance(address : String, password : String, balanceInter: BalanceInter) {
        val array = JsonArray()
        array.add(jsonArray(address))
        val dataJson = jsonObject(
                "jsonrpc" to "2.0",
                "method" to RPC.ledger_accountsBalance.methodName(),
                "params" to array,
                "id" to Random.nextInt(10000)
        )
        KLog.i(dataJson.toString())
        var request = ConstantValue.qlcNode.httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->
            val (data, error) = result
            if (error == null) {
                try {
                    KLog.i(data)
                    val jsonObject = JSONObject.parseObject(data)
                    val tokenJson = jsonObject.getJSONObject("result").getJSONObject(address)
                    var tokenList = arrayListOf<QlcTokenbalance>()
                    if (tokenJson == null) {
                        balanceInter.onBack(tokenList, null)
                    } else {
                        tokenJson.entries.forEach {
                            var tokenbalance = QlcTokenbalance()
                            tokenbalance.symbol = it.key
                            tokenbalance.balance = JSONObject.parseObject(it.value.toString()).getString("balance")
                            tokenbalance.pending = JSONObject.parseObject(it.value.toString()).getString("pending")
                            tokenbalance.vote = JSONObject.parseObject(it.value.toString()).getString("vote")
                            tokenbalance.address = address
                            tokenList.add(tokenbalance)
                        }
                    }
                    balanceInter.onBack(tokenList, null)
                } catch (e : Exception) {
                    balanceInter.onBack(null, Error("no tokens"))
                    e.printStackTrace()
                }
            } else {
                balanceInter.onBack(null, Error(error.localizedMessage))
            }
        }
    }

    interface BalanceInter {
        fun onBack(baseResult: ArrayList<QlcTokenbalance>?, error: java.lang.Error?)
    }

}
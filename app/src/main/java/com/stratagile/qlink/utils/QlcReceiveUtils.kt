package com.stratagile.qlink.utils

import android.app.Activity
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlc.utils.QlcUtil
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.TokenInfo
import com.stratagile.qlink.entity.eventbus.ChangeWallet
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import qlc.bean.StateBlock
import qlc.mng.BlockMng
import qlc.mng.LedgerMng
import qlc.mng.TransactionMng
import qlc.mng.WalletMng
import qlc.network.QlcClient
import qlc.network.QlcException
import qlc.rpc.impl.LedgerRpc
import qlc.utils.Helper
import qlc.utils.WorkUtil

fun recevive(qlcClient: QlcClient, byteArray: ByteArray, qlcAccount: QLCAccount, rpc : LedgerRpc, receiveBack: ReceiveBack){
    KLog.i("开始接收")
    val sendBlock = LedgerMng.getBlockInfoByHash(qlcClient, byteArray)
    var isBendi = true
    //QlcUtil.hexStringToByteArray(qlcAccount.getPrivKey()
    val receiveBlockJson = TransactionMng.receiveBlock(qlcClient, sendBlock, qlcAccount.address, null)
    val aaaa = JSONArray()
    var stateBlock = Gson().fromJson<StateBlock>(receiveBlockJson.toJSONString(), StateBlock::class.java)
    var root = BlockMng.getRoot(stateBlock)
    var params = mutableListOf<Pair<String, String>>()
    params.add(Pair("root", root))
    var standaloneCoroutine = launch {
        KLog.i("协程启动。")
        delay(30000)
        isBendi = false
        KLog.i("走远程work逻辑")
        var request = "http://pow1.qlcchain.org/work".httpGet(params)
        request.responseString { _, _, result ->
            KLog.i("远程work返回、、")
            val (data, error) = result
            try {
                if (error == null) {
                    stateBlock.work = data
                    var hash = BlockMng.getHash(stateBlock)
                    var signature = WalletMng.sign(hash, Helper.hexStringToBytes(drivePrivateKey(qlcAccount.address).substring(0, 64)))
                    val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(drivePublicKey(qlcAccount.address)))
                    if (!signCheck) {
                        KLog.i("签名验证失败")
                        receiveBack.recevie(false)
                        return@responseString
                    }
                    stateBlock.setSignature(Helper.byteToHexString(signature))
                    aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
                } else {

                }
                var result = rpc.process(aaaa)
                KLog.i(result.toJSONString())
                if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                    receiveBack.recevie(true)
                    return@responseString
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        KLog.i(aaaa)
        return@launch
    }
    var work = WorkUtil.generateWork(Helper.hexStringToBytes(BlockMng.getRoot(stateBlock)))
    standaloneCoroutine.cancel()
    try {
        if (!isBendi) {
            return
        }
        KLog.i("走本地work逻辑")
        stateBlock.work = work
        var hash = BlockMng.getHash(stateBlock)
        var signature = WalletMng.sign(hash, Helper.hexStringToBytes(drivePrivateKey(qlcAccount.address).substring(0, 64)))
        val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(drivePublicKey(qlcAccount.address)))
        if (!signCheck) {
            KLog.i("签名验证失败")
            receiveBack.recevie(false)
            return
        }
        stateBlock.setSignature(Helper.byteToHexString(signature))
        aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
        var result = rpc.process(aaaa)
        KLog.i(result.toJSONString())
        if (result.getString("result") != null && !"".equals(result.getString("result"))) {
            receiveBack.recevie(true)
            return
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return
}

private fun drivePrivateKey(address: String): String {
    val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
    qlcAccounts.forEach {
        if (it.address.toLowerCase() == address) {
            return it.privKey
        }
    }
    return ""
}

private fun drivePublicKey(address: String): String {
    val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
    qlcAccounts.forEach {
        if (it.address.toLowerCase() == address) {
            return it.pubKey
        }
    }
    return ""
}

interface ReceiveBack {
    fun recevie(suceess : Boolean)
}
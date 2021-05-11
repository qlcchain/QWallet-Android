package com.stratagile.qlink.utils

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.socks.library.KLog
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import qlc.bean.StateBlock
import qlc.mng.BlockMng
import qlc.mng.LedgerMng
import qlc.mng.TransactionMng
import qlc.mng.WalletMng
import qlc.network.QlcClient
import qlc.rpc.impl.LedgerRpc
import qlc.utils.Constants
import qlc.utils.Helper
import qlc.utils.WorkUtil
import java.math.BigDecimal

fun recevive(qlcClient: QlcClient, byteArray: ByteArray, qlcAccount: QLCAccount, rpc : LedgerRpc, receiveBack: ReceiveBack){
    KLog.i("开始接收")
    var sendBlock = LedgerMng.getBlockInfoByHash(qlcClient, byteArray)
    var isBendi = true
    //QlcUtil.hexStringToByteArray(qlcAccount.getPrivKey()
    KLog.i("得到发送块")
    val receiveBlockJson = TransactionMng.receiveBlock(qlcClient, sendBlock, qlcAccount.address, null)
    if (receiveBlockJson == null){
        receiveBack.recevie(false)
        return
    }

    KLog.i("得到接收块")
    val aaaa = JSONArray()
    var stateBlock = Gson().fromJson<StateBlock>(receiveBlockJson.toJSONString(), StateBlock::class.java)
    if (Constants.BLOCK_TYPE_CONTRACTREWARD.equals(stateBlock.type) || Constants.LINNK_TYPE_AIRDORP.equals(stateBlock.type)) {
        KLog.i("走不要算work的逻辑")
        try {
            aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
            KLog.i("接收传过去的参数：" + aaaa)
            var result = rpc.process(aaaa)
            KLog.i(result.toJSONString())
            if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                receiveBack.recevie(true)
                return
            } else {
                receiveBack.recevie(true)
            }
        } catch (e : Exception) {
            e.printStackTrace()
            receiveBack.recevie(false)
        }
        return
    }
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
                    KLog.i("算work正确。")
                    stateBlock.work = data
                    var hash = BlockMng.getHash(stateBlock)
                    var signature = WalletMng.sign(hash, Helper.hexStringToBytes(QlcReceiveUtils.drivePrivateKey(qlcAccount.address).substring(0, 64)))
                    val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(QlcReceiveUtils.drivePublicKey(qlcAccount.address)))
                    if (!signCheck) {
                        KLog.i("签名验证失败")
                        receiveBack.recevie(false)
                        return@responseString
                    }
                    stateBlock.setSignature(Helper.byteToHexString(signature))
                    aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
                } else {
                    KLog.i("算work出错了。")
                    error!!.exception.printStackTrace()
                    receiveBack.recevie(false)
                    return@responseString
                }
                KLog.i("接收传过去的参数：" + aaaa)
                var result = rpc.process(aaaa)
                KLog.i(result.toJSONString())
                if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                    receiveBack.recevie(true)
                    return@responseString
                }
            } catch (e: Exception) {
                e.printStackTrace()
                receiveBack.recevie(false)
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
        var signature = WalletMng.sign(hash, Helper.hexStringToBytes(QlcReceiveUtils.drivePrivateKey(qlcAccount.address).substring(0, 64)))
        val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(QlcReceiveUtils.drivePublicKey(qlcAccount.address)))
        if (!signCheck) {
            KLog.i("签名验证失败")
            receiveBack.recevie(false)
            return
        }
        stateBlock.setSignature(Helper.byteToHexString(signature))
        aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
        KLog.i("接收传过去的参数：" + aaaa)
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

object QlcReceiveUtils {
    fun sendQGas(qlcAccount: QLCAccount, receiveAddress : String, amount : String, message : String, isRemotePow: Boolean = false, sendBack: SendBack) {
        try {
            val qlcClient = QlcClient(ConstantValue.qlcNode)
            val rpc = LedgerRpc(qlcClient)
            var bendi = true
            var jsonObject = TransactionMng.sendBlock(qlcClient, qlcAccount.address, "QGAS", receiveAddress, amount.toBigDecimal().multiply(BigDecimal.TEN.pow(8)).toBigInteger(), null, null, null, message, null)
            var aaaa = JSONArray()
            var stateBlock = Gson().fromJson<StateBlock>(jsonObject.toJSONString(), StateBlock::class.java)
            var root = BlockMng.getRoot(stateBlock)
            var params = mutableListOf<Pair<String, String>>()
            params.add(Pair("root", root))
            var standaloneCoroutine = launch {
                KLog.i("协程启动")
                if (!isRemotePow) {
                    delay(30000)
                }
                bendi = false
                KLog.i("走远程work逻辑, root为：" + root)
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
                                sendBack.send("")
                                return@responseString
                            }
                            stateBlock.setSignature(Helper.byteToHexString(signature))
                            aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))

                        } else {

                        }
                        var result = rpc.process(aaaa)
                        KLog.i(result.toJSONString())
                        if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                            sendBack.send(result.getString("result"))
                            return@responseString
                        } else {
                            sendBack.send("")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        sendBack.send("")
                    }
                }
                KLog.i(aaaa)
                return@launch
            }
            if (!isRemotePow) {
                var work = WorkUtil.generateWork(Helper.hexStringToBytes(BlockMng.getRoot(stateBlock)))
                standaloneCoroutine.cancel()
                try {
                    if (!bendi) {
                        return
                    }
                    KLog.i("走本地work逻辑")
                    stateBlock.work = work
                    var hash = BlockMng.getHash(stateBlock)
                    var signature = WalletMng.sign(hash, Helper.hexStringToBytes(drivePrivateKey(qlcAccount.address).substring(0, 64)))
                    val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(drivePublicKey(qlcAccount.address)))
                    if (!signCheck) {
                        KLog.i("签名验证失败")
                        sendBack.send("")
                        return
                    }
                    stateBlock.setSignature(Helper.byteToHexString(signature))
                    aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
                    var result = rpc.process(aaaa)
                    KLog.i(result.toJSONString())
                    if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                        sendBack.send(result.getString("result"))
                    }
                } catch (e: Exception) {
                    sendBack.send("")
                    e.printStackTrace()
                }
            }
        } catch (e : Exception) {
            sendBack.send("")
            e.printStackTrace()
        }
    }

    fun getMemoHash(hexStr: String): String {
        var string = StringBuilder()
        if (hexStr.length < 64) {
            for (i in 0..64 - hexStr.length - 1) {
                string.append("0")
            }
        }
        return (string.toString() + hexStr)
    }

    fun sendQlcChainToken(qlcAccount: QLCAccount, receiveAddress : String, amount : String, message : String, tokenName : String, decimal : Int, sendBack: SendBack) {
        val qlcClient = QlcClient(ConstantValue.qlcNode)
        val rpc = LedgerRpc(qlcClient)
        var bendi = true
        //getMemoHash(Helper.byteToHexString(message.toByteArray()))
        var jsonObject = TransactionMng.sendBlock(qlcClient, qlcAccount.address, tokenName, receiveAddress, amount.toBigDecimal().multiply(BigDecimal.TEN.pow(decimal)).toBigInteger(), null, null, null, message, null)
        var aaaa = JSONArray()
        var stateBlock = Gson().fromJson<StateBlock>(jsonObject.toJSONString(), StateBlock::class.java)
        var root = BlockMng.getRoot(stateBlock)
        var params = mutableListOf<Pair<String, String>>()
        params.add(Pair("root", root))
        var standaloneCoroutine = launch {
            KLog.i("协程启动")
            delay(30000)
            bendi = false
            KLog.i("走远程work逻辑, root为：" + root)
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
                            sendBack.send("")
                            return@responseString
                        }
                        stateBlock.setSignature(Helper.byteToHexString(signature))
                        aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))

                    } else {

                    }
                    var result = rpc.process(aaaa)
                    KLog.i(result.toJSONString())
                    if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                        sendBack.send(result.getString("result"))
                        return@responseString
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    sendBack.send("")
                }
            }
            KLog.i(aaaa)
            return@launch
        }
        var work = WorkUtil.generateWork(Helper.hexStringToBytes(BlockMng.getRoot(stateBlock)))
        standaloneCoroutine.cancel()
        try {
            if (!bendi) {
                return
            }
            KLog.i("走本地work逻辑")
            stateBlock.work = work
            var hash = BlockMng.getHash(stateBlock)
            var signature = WalletMng.sign(hash, Helper.hexStringToBytes(drivePrivateKey(qlcAccount.address).substring(0, 64)))
            val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(drivePublicKey(qlcAccount.address)))
            if (!signCheck) {
                KLog.i("签名验证失败")
                sendBack.send("")
                return
            }
            stateBlock.setSignature(Helper.byteToHexString(signature))
            aaaa.add(JSONObject.parseObject(Gson().toJson(stateBlock)))
            var result = rpc.process(aaaa)
            KLog.i(result.toJSONString())
            if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                sendBack.send(result.getString("result"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun drivePrivateKey(address: String): String {
        val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
        qlcAccounts.forEach {
            if (it.address.toLowerCase().equals(address)) {
//            KLog.i(it.privKey)
                return it.privKey
            }
        }
        return ""
    }

    fun drivePublicKey(address: String): String {
        val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
        qlcAccounts.forEach {
            if (it.address.toLowerCase() == address) {
                return it.pubKey
            }
        }
        return ""
    }
}


interface ReceiveBack {
    fun recevie(suceess : Boolean)
}

interface SendBack {
    fun send(suceess : String)
}
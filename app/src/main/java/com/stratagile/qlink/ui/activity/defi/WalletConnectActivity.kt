package com.stratagile.qlink.ui.activity.defi

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.C
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.DWCSession
import com.stratagile.qlink.db.DWCSessionDao
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.entity.defi.ConfirmationType
import com.stratagile.qlink.entity.walletconnect.*
import com.stratagile.qlink.service.WalletConnectService
import com.stratagile.qlink.ui.activity.defi.component.DaggerWalletConnectComponent
import com.stratagile.qlink.ui.activity.defi.contract.WalletConnectContract
import com.stratagile.qlink.ui.activity.defi.module.WalletConnectModule
import com.stratagile.qlink.ui.activity.defi.presenter.WalletConnectPresenter
import com.stratagile.qlink.utils.MessageUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.walletconnect.WCClient
import com.stratagile.qlink.walletconnect.WCSession
import com.stratagile.qlink.walletconnect.entity.WCEthereumSignMessage
import com.stratagile.qlink.walletconnect.entity.WCEthereumTransaction
import com.stratagile.qlink.walletconnect.entity.WCPeerMeta
import com.stratagile.qlink.web3.entity.Address
import com.stratagile.qlink.web3.entity.Web3Transaction
import com.stratagile.qlink.web3j.StructuredDataEncoder
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_wallet_connect.*
import okhttp3.OkHttpClient
import org.web3j.utils.Convert
import org.web3j.utils.Numeric.cleanHexPrefix
import wallet.core.jni.Hash
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/09/15 16:03:09
 */

class WalletConnectActivity : BaseActivity(), WalletConnectContract.View {

    @Inject
    internal lateinit var mPresenter: WalletConnectPresenter
    lateinit var connectSession : DWCSession
    var sessionStr = ""
    lateinit var ethWallet: EthWallet
    private val clientBuffer = HashMap<String, WCClient>()

    private var walletConnectService: WalletConnectService? = null
    private var serviceConnection: ServiceConnection? = null

    private var client: WCClient? = null
    private var session: WCSession? = null
    private var peerMeta: WCPeerMeta? = null

    private var httpClient: OkHttpClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_wallet_connect)
    }
    override fun initData() {
        getEthWallet()
        title.text = "WalletConnect"
        var data = intent.data
        if (data != null) {
            KLog.i("进入1")
            sessionStr = data.toString()
        } else {
            KLog.i("进入2")
            sessionStr = intent.getStringExtra("session")
        }
        KLog.i(sessionStr)
        startService()
    }

    fun initClient() {
        var sessionId = UUID.randomUUID().toString()
        var connectionId = ""
        session = WCSession.from(sessionStr)
        if (session == null) {
            var replace = sessionStr.replace("wc:", "").replace("@1", "")
            KLog.i(replace)
            connectSession = AppConfig.instance.daoSession.dwcSessionDao.queryBuilder().where(DWCSessionDao.Properties.SessionId.eq(replace)).list()[0]
            progress.setVisibility(View.GONE)
            layout_info.setVisibility(View.VISIBLE)
            Glide.with(this)
                    .load(connectSession.remotePeerData1.icons[0])
                    .into(icon)
            peer_name.setText(connectSession.remotePeerData1.name)
            peer_url.setText(connectSession.remotePeerData1.url)
            address.setText(connectSession.walletAccount)
            tx_count.text = connectSession.usageCount.toString()
            client = walletConnectService!!.getClient(replace)
            if (client == null) {
                toast(getString(R.string.session_has_been_terminated))
                return
//                session = connectSession.session
//                sessionId = connectSession.sessionId
//                connectionId = connectSession.remotePeerId
            }

        } else {
            client = walletConnectService!!.getClient(session!!.topic)
        }
        if (client == null) {
            initWalletConnectPeerMeta()
            initWalletConnectClient()
            initWalletConnectSession(sessionId, connectionId)
        }
    }

    fun getEthWallet() {
        var ethWallets = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        var walletList = ethWallets.filter { it.isCurrent() }
        if (walletList.size > 0) {
            ethWallet = walletList[0]
        }
    }

    private fun initWalletConnectPeerMeta() {
        val name = "QWallet"
        val url = "https://www.Qwallet.com"
        val description: String = ethWallet.address
        val icons = arrayOf("https://hk.qwallet.network/favicon.ico")
        peerMeta = WCPeerMeta(
                name,
                url,
                description,
                Arrays.asList(*icons)
        )
    }

    private fun initWalletConnectClient() {
        httpClient = OkHttpClient.Builder()
                .connectTimeout(7, TimeUnit.SECONDS)
                .readTimeout(7, TimeUnit.SECONDS)
                .writeTimeout(7, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build()
        client = WCClient(GsonBuilder(), OkHttpClient())
        client!!.onWCOpen = { peerId ->
            putClient(getSessionId(), client)
            KLog.i("On Open: $peerId")
        }
        client!!.onDisconnect = { code, reason ->
            KLog.i("Terminate session?")
        }
    }

    private fun getSessionId(): String {
        return if (!"".equals(sessionStr)) {
            val uriString: String = sessionStr.replace("wc:", "wc://")
            Uri.parse(uriString).userInfo
        } else session!!.topic
    }

    fun startService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                walletConnectService = (service as WalletConnectService.LocalBinder).getService()
                KLog.i("Service connected")
                for (sessionId in clientBuffer.keys) {
                    KLog.i("put from buffer: $sessionId")
                    val c: WCClient = clientBuffer.get(sessionId)!!
                    walletConnectService!!.putClient(sessionId, c)
                }
                clientBuffer.clear()
                initClient()
            }

            override fun onServiceDisconnected(name: ComponentName) {
                walletConnectService = null
                KLog.i("Service disconnected")
            }
        }
        val i = Intent(this, WalletConnectService::class.java)
        startService(i)
        bindService(i, serviceConnection, Context.BIND_ABOVE_CLIENT)
    }

    override fun onPause() {
        super.onPause()
        stopMessageCheck()
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        stopMessageCheck()
        super.onDestroy()
    }

    fun getClient(sessionId: String): WCClient? {
        //may fail.
        return if (walletConnectService == null) {
            null
        } else {
            KLog.i("fetch: $sessionId")
            walletConnectService!!.getClient(sessionId)
        }
    }

    fun putClient(sessionId: String, client: WCClient?) {
        if (walletConnectService == null) {
            clientBuffer[sessionId] = client!!
            KLog.i("buffering: $sessionId")
        } else {
            KLog.i("put sessionId")
            KLog.i("put: $sessionId")
            walletConnectService!!.putClient(sessionId, client)
        }
    }

    private fun initWalletConnectSession(sessionId: String, connectionId: String?) {
        if (session == null) {
            //error situation!
            KLog.i("错误的连接")
            return
        }
        KLog.i("Connect: " + getSessionId() + " (" + connectionId + ")")
        client!!.connect(session!!, peerMeta!!, sessionId, connectionId)
    }

    override fun onResume() {
        super.onResume()
        startMessageCheck()
    }

    lateinit var messageCheck: Disposable
    private fun startMessageCheck() {
        if (this::messageCheck.isInitialized && !messageCheck.isDisposed()) messageCheck.dispose()
        messageCheck = Observable.interval(0, 500, TimeUnit.MILLISECONDS).doOnNext { l: Long? -> checkMessages() }.subscribe()
    }

    private fun stopMessageCheck() {
        if (messageCheck != null && !messageCheck.isDisposed()) messageCheck.dispose()
    }

    private fun checkMessages() {
//        KLog.i(getSessionId())
        var rq: WCRequest? = getPendingRequest(getSessionId())
        if (rq != null) {
            when (rq.type) {
                SignType.MESSAGE -> runOnUiThread {
                    onEthSign(rq.id, rq.sign)
                }
                SignType.SIGN_TX -> runOnUiThread {
                    KLog.i("签名交易")
                    onEthSignTransaction(rq.id, rq.tx)
                }
                SignType.SEND_TX -> runOnUiThread {
                    KLog.i("发送交易")
                    onEthSendTransaction(rq.id, rq.tx)
                }
                SignType.FAILURE -> runOnUiThread {
                    onFailure(rq.throwable)
                }
                SignType.SESSION_REQUEST -> {
                    KLog.i("On Request: " + rq.peer.name)
                    runOnUiThread { onSessionRequest(rq.id, rq.peer) }
                }
            }
        }
    }

    private fun hexToUtf8(hex: String): String {
        var hex = hex
        hex = cleanHexPrefix(hex)
        val buff = ByteBuffer.allocate(hex.length / 2)
        var i = 0
        while (i < hex.length) {
            buff.put(hex.substring(i, i + 2).toInt(16).toByte())
            i += 2
        }
        buff.rewind()
        val cb = StandardCharsets.UTF_8.decode(buff)
        return cb.toString()
    }

    fun onEthSign(id: Long, sign : WCEthereumSignMessage) {
        lastId = id
        val builder = AlertDialog.Builder(this)
        var signable : Signable? = null
        when(sign.type) {
            WCEthereumSignMessage.WCSignType.MESSAGE -> {
                signable = EthereumMessage(sign.data, connectSession.remotePeerData1.url, id)
            }
            WCEthereumSignMessage.WCSignType.PERSONAL_MESSAGE -> {
                signable = EthereumMessage(sign.data, connectSession.remotePeerData1.url, id, true)
            }
            WCEthereumSignMessage.WCSignType.TYPED_MESSAGE -> {
                //See TODO in SignCallbackJSInterface, refactor duplicate code
                try {
                    try {
                        val rawData: Array<ProviderTypedData> = Gson().fromJson(sign.data, Array<ProviderTypedData>::class.java)
                        val writeBuffer = ByteArrayOutputStream()
                        writeBuffer.write(Hash.keccak256(MessageUtils.encodeParams(rawData)))
                        writeBuffer.write(Hash.keccak256(MessageUtils.encodeValues(rawData)))
                        val signMessage: CharSequence = MessageUtils.formatTypedMessage(rawData)
                        signable = EthereumTypedMessage(writeBuffer.toByteArray(), signMessage, connectSession.remotePeerData1.url, id)
                    } catch (e: JsonSyntaxException) {
                        val eip721Object = StructuredDataEncoder(sign.data)
                        val signMessage: CharSequence = MessageUtils.formatEIP721Message(eip721Object)
                        signable = EthereumTypedMessage(eip721Object.getStructuredData(), signMessage, connectSession.remotePeerData1.url, id)
                    }
                } catch (e: IOException) {
                    client!!.rejectRequest(id, "")
                }
            }
        }

        val dialog: AlertDialog = builder.setTitle(getString(R.string.sign))
                .setMessage(signable!!.userMessage)
                .setPositiveButton(getString(R.string.confirm), { d, w ->
                    sign(signable!!.prehash)
                })
                .setNegativeButton(getString(R.string.cancel), { d, w ->

                })
                .setCancelable(false)
                .create()
        dialog.show()
    }

    fun sign(sign : ByteArray) {
        showProgressDialog()
        var resultSign = ETHWalletUtils.sign(ETHWalletUtils.derivePrivateKey(ethWallet.id), sign)
        approveRequest(getSessionId(), lastId, resultSign)
        updateSignCount()
        closeProgressDialog()
        finish()
    }

    fun updateSignCount() {
        connectSession.usageCount = connectSession.usageCount + 1
        AppConfig.instance.daoSession.dwcSessionDao.update(connectSession)
        tx_count.text = connectSession.usageCount.toString()
    }

    var lastId = 0L
    fun onEthSendTransaction(id: Long, transaction: WCEthereumTransaction) {
        try {
            lastId = id
            KLog.i(lastId)
            if (transaction.to!!.equals(Address.EMPTY) && transaction.data != null // Constructor
                    || !transaction.to.equals(Address.EMPTY) && (transaction.data != null || transaction.value != null)) {
                confirmTransaction(transaction, connectSession.remotePeerData1.url, ConstantValue.currentChainId, id)
            }
        } catch (e :Exception) {
            e.printStackTrace()
        }
    }

    fun confirmTransaction(transaction: WCEthereumTransaction?, requesterURL: String?, chainId: Int, callbackId: Long?) {
        val w3tx = Web3Transaction(transaction, callbackId!!)
        val intent = Intent(this, ConfirmActivity::class.java)
        intent.putExtra(C.EXTRA_WEB3TRANSACTION, w3tx)
        intent.putExtra(C.EXTRA_ADDRESS, ethWallet.address)
        intent.putExtra(C.EXTRA_AMOUNT, Convert.fromWei(w3tx.value.toString(), Convert.Unit.WEI).toString())
        intent.putExtra(C.TOKEN_TYPE, ConfirmationType.WEB3TRANSACTION.ordinal)
        intent.putExtra(C.EXTRA_ACTION_NAME, requesterURL)
        intent.putExtra(C.EXTRA_NETWORKID, chainId)
        intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        startActivityForResult(intent, C.REQUEST_TRANSACTION_CALLBACK)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == C.REQUEST_TRANSACTION_CALLBACK) {
                val hashData = data!!.getStringExtra(C.EXTRA_TRANSACTION_DATA)
                approveRequest(getSessionId(), lastId, hashData)
                connectSession.usageCount = connectSession.usageCount + 1
                tx_count.text = connectSession.usageCount.toString()
                AppConfig.instance.daoSession.dwcSessionDao.update(connectSession)
                finish()
            }
        } else {
            if (requestCode == C.REQUEST_TRANSACTION_CALLBACK) {
                KLog.i("签名失败。。。")
                rejectRequest(getSessionId(), lastId, "Authentication failed")
            }
        }
    }

    private fun onFailure(throwable: Throwable) {
        throwable.printStackTrace()
        val builder = AlertDialog.Builder(this)
        val dialog: AlertDialog = builder.setTitle(getString(R.string.error))
                .setMessage(throwable.message)
                .setPositiveButton(getString(R.string.re_try), { d, w ->  d.dismiss()})
                .setNeutralButton(getString(R.string.cancel), { d, w ->
                    finish()
                })
                .setCancelable(false)
                .create()
        dialog.show()
    }

    private fun onEthSignTransaction(id: Long, transaction: WCEthereumTransaction) {
        lastId = id
        val w3Tx = Web3Transaction(transaction, id)
        val builder = AlertDialog.Builder(this)
        val dialog: AlertDialog = builder.setTitle(getString(R.string.sign))
                .setMessage(w3Tx.getFormattedTransaction(this, ConstantValue.currentChainId))
                .setPositiveButton(getString(R.string.confirm), { d, w ->  })
                .setNegativeButton(getString(R.string.cancel), { d, w ->

                })
                .setCancelable(false)
                .create()
        dialog.show()
    }

    private fun onSessionRequest(id: Long, peer: WCPeerMeta) {
        lastId = id
        val accounts = arrayOf<String>(ethWallet.address)
        Glide.with(this)
                .load(peer.icons.get(0))
                .into(icon)
        peer_name.setText(peer.name)
        peer_url.setText(peer.url)
        tx_count.setText("")
        tx_count.text = "0"
        address.setText(ethWallet.address)
        var dwcSession = DWCSession()
        dwcSession.sessionData = Gson().toJson(session)
        dwcSession.remotePeerData = Gson().toJson(peer)
        dwcSession.walletAccount = ethWallet.address
        dwcSession.peerId = client!!.peerId
        dwcSession.remotePeerId = client!!.remotePeerId
        dwcSession.sessionId = session!!.topic
        connectSession = dwcSession
        AppConfig.instance.daoSession.dwcSessionDao.insert(dwcSession)
        val builder = AlertDialog.Builder(this)
        val dialog: AlertDialog = builder
                .setIcon(icon.getDrawable())
                .setTitle(peer.name)
                .setMessage(peer.url)
                .setPositiveButton(getString(R.string.approve), { d, w ->
                    client!!.approveSession(Arrays.asList(*accounts), ConstantValue.currentChainId)
//                    createNewSession(getSessionId(), client!!.peerId, client!!.remotePeerId, Gson().toJson(session), Gson().toJson(peer))
                    progress.setVisibility(View.GONE)
                    layout_info.setVisibility(View.VISIBLE)
                    finish()
                })
                .setNegativeButton(getString(R.string.Reject), { d, w ->
                    client!!.rejectSession(getString(R.string.rejected))
                    finish()
                })
                .setCancelable(false)
                .create()
        dialog.show()
    }

    fun getPendingRequest(sessionId: String?): WCRequest? {
        return if (walletConnectService != null) walletConnectService!!.getPendingRequest(sessionId) else null
    }

    fun rejectRequest(sessionId: String?, id: Long, message: String?) {
        if (walletConnectService != null) walletConnectService!!.rejectRequest(sessionId, id, message)
    }

    fun approveRequest(sessionId: String?, id: Long, message: String?) {
        if (walletConnectService != null) walletConnectService!!.approveRequest(sessionId, id, message)
    }

    fun getConnectionCount(): Int {
        return if (walletConnectService != null) walletConnectService!!.connectionCount else 0
    }

    override fun setupActivityComponent() {
        DaggerWalletConnectComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .walletConnectModule(WalletConnectModule(this))
                .build()
                .inject(this)
    }
    override fun setPresenter(presenter: WalletConnectContract.WalletConnectContractPresenter) {
        mPresenter = presenter as WalletConnectPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }



}
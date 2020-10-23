package com.stratagile.qlink.ui.activity.defi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.blockchain.cypto.digest.Sha256
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.SwapRecord
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.defi.CheckHubState
import com.stratagile.qlink.entity.defi.EthGasPrice
import com.stratagile.qlink.entity.defi.FetchBack
import com.stratagile.qlink.entity.swap.WrapperOnline
import com.stratagile.qlink.ui.activity.defi.component.DaggerEthSwapComponent
import com.stratagile.qlink.ui.activity.defi.contract.EthSwapContract
import com.stratagile.qlink.ui.activity.defi.module.EthSwapModule
import com.stratagile.qlink.ui.activity.defi.presenter.EthSwapPresenter
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import io.neow3j.protocol.Neow3j
import io.neow3j.protocol.http.HttpService
import kotlinx.android.synthetic.main.fragment_ethswap.*
import org.json.JSONArray
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.utils.Convert
import wendu.dsbridge.OnReturnValue
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/08/15 16:40:33
 */

class EthSwapFragment : BaseFragment(), EthSwapContract.View {

    @Inject
    lateinit internal var mPresenter: EthSwapPresenter
    lateinit var swapRecord: SwapRecord

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ethswap, null)
//        AppConfig.instance.daoSession.swapRecordDao.deleteAll()
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    var sweetAlertDialogSwap: SweetAlertDialog? = null
    var llDot: View? = null
    var tvDot: TextView? = null
    var isClose = false

    fun showSwapPup() {
        isClose = false
        val view = layoutInflater.inflate(R.layout.alert_swap, null, false)
        llDot = view.findViewById(R.id.llDot)
        tvDot = view.findViewById(R.id.tvDot)
        var ivClose = view.findViewById<ImageView>(R.id.ivClose)

        tvDot!!.text = "1"
        sweetAlertDialogSwap = SweetAlertDialog(activity)
        sweetAlertDialogSwap!!.setView(view)
        ivClose.setOnClickListener {
            isClose = true
            getErc20TokenBalance()
            etStakeQlcAmount.setText("")
            var newSwapRecord = SwapRecord()
            newSwapRecord.isMainNet = SpUtil.getBoolean(activity, ConstantValue.isMainNet, true)
            newSwapRecord.wrpperEthAddress = wrapperOnline.ethAddress
            newSwapRecord.ethContractAddress = wrapperOnline.ethContract
            newSwapRecord.wrapperNeoAddress = wrapperOnline.neoAddress
            newSwapRecord.neoContractAddress = wrapperOnline.neoContract
            newSwapRecord.type = SwapRecord.SwapType.typeErc20ToNep5.ordinal
            newSwapRecord.fromAddress = swapRecord.fromAddress
            newSwapRecord.toAddress = swapRecord.toAddress
            swapRecord = newSwapRecord
            sweetAlertDialogSwap!!.dismissWithAnimation()
        }
        sweetAlertDialogSwap!!.show()
        scaleAnimationTo1(llDot!!)
    }

    fun scaleAnimationTo1(imageView: View) {
        sa1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationToHalf(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(sa1)
    }

    fun scaleAnimationToHalf(imageView: View) {
        saHalf.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                scaleAnimationTo1(imageView)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        imageView.startAnimation(saHalf)
    }

    lateinit var saHalf: ScaleAnimation
    lateinit var sa1: ScaleAnimation

    var leastSwapQlc = 5
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webview.loadUrl("file:///android_asset/eth.html")
        neow3j = Neow3j.build(HttpService(ConstantValue.neoNode))
        getEthWallet()
        swapRecord = SwapRecord()
        swapRecord.isMainNet = SpUtil.getBoolean(activity, ConstantValue.isMainNet, true)
        swapRecord.type = SwapRecord.SwapType.typeErc20ToNep5.ordinal
        swapRecord.fromAddress = ethWallet.address
        miniSwapQlc.text = getString(R.string.the_minimum_stake_amount_is_1_s_qlc, leastSwapQlc.toString())
        etStakeQlcAmount.hint = ""
        invoke.setOnClickListener {
            if (!this::wrapperOnline.isInitialized) {
                checkWrapperOnline()
                toast(getString(R.string.pleasewait))
                return@setOnClickListener
            }
            if (wrapperOnline.isWithdrawLimit) {
                toast(getString(R.string.is_withdraw_limit))
                return@setOnClickListener
            }
            if (wrapperOnline.ethBalance < 0.01) {
                toast(getString(R.string.is_withdraw_limit))
                return@setOnClickListener
            }
            if ("".equals(balance)) {
                getErc20TokenBalance()
                toast(getString(R.string.pleasewait))
                return@setOnClickListener
            }
            if (etStakeQlcAmount.text.toString().toFloat() > balance.toFloat()) {
                toast("No enough QLC to swap")
                return@setOnClickListener
            }
            if (etStakeQlcAmount.text.toString().toFloat() < wrapperOnline.minWithdrawAmount.toLong()/ 100000000) {
                toast(getString(R.string.the_minimum_stake_amount_is_1_s_qlc, (wrapperOnline.minWithdrawAmount.toLong()/ 100000000).toString()))
                return@setOnClickListener
            }
            if (ethPrice == 0.toDouble()) {
                mPresenter.getEthPrice()
                toast(getString(R.string.pleasewait))
                return@setOnClickListener
            }
            if (ethBalance == 0.toBigInteger()) {
                getEthbalance()
                toast(getString(R.string.pleasewait))
                return@setOnClickListener
            }

            if (!this::ethGasPrice.isInitialized) {
                mPresenter.getEthGasPrice(hashMapOf())
                toast(getString(R.string.pleasewait))
                return@setOnClickListener
            }
            if (!"".equals(etStakeQlcAmount.text.toString()) && this::neoWallet.isInitialized) {
                if (etStakeQlcAmount.text.toString().toInt() < 5) {
                    return@setOnClickListener
                }
                showEthFee()
            }
        }

        llSelectETHWallet.setOnClickListener {
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.NeoWallet.ordinal)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        thread {
            try {
                checkWrapperOnline()
            } catch (e :java.lang.Exception) {
                e.printStackTrace()
            }
        }
        etStakeQlcAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (p0.toString().length == 0) {
                    invoke.setBackgroundResource(R.drawable.unable_bt_bg)
                } else {
                    if (this@EthSwapFragment::neoWallet.isInitialized) {
                        invoke.setBackgroundResource(R.drawable.main_color_bt_bg)
                    } else {
                        invoke.setBackgroundResource(R.drawable.unable_bt_bg)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        saHalf = ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        saHalf.setDuration(400)

        sa1 = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(1000)

    }

    var isFinish = false
    override fun onDestroy() {
        isFinish = true
        isClose = true
        var records = AppConfig.instance.daoSession.swapRecordDao.loadAll()
        val saveData = Gson().toJson(records)
        FileUtil.savaData("/Qwallet/swapRecord.json", saveData)
        super.onDestroy()
    }

    var checkStatusTime = 5000.toLong()
    fun checkLcokState() {
        if (isClose) {
            return
        }
        val dataJson = jsonObject(
                "type" to swapRecord.type.toString(),
                "hash" to swapRecord.rHash
        )

        var list = arrayListOf<Pair<String, String>>(Pair("value", swapRecord.rHash))
        KLog.i(dataJson.toString())
        var request = (ConstantValue.qlcHubEndPoint + "/info/lockerInfo").httpGet(list)
        DefiUtil.addRequestHeader(request)
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                /**
                 * // withdraw
                WithDrawEthLockedDone
                WithDrawNeoLockedPending
                WithDrawNeoLockedDone
                WithDrawNeoUnLockedDone
                WithDrawEthUnlockPending
                WithDrawEthUnlockDone
                WithDrawNeoFetchPending
                WithDrawNeoFetchDone
                WithDrawEthFetchDone

                Failed
                Invalid
                 */
                var checkHubState = Gson().fromJson<CheckHubState>(data, CheckHubState::class.java)
                if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawInit.ordinal) {
                    //WithDrawEthLockedDone == 10
                    swapRecord.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "1"
                    }
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawEthLockedDone.ordinal) {
                    //WithDrawEthLockedDone == 10
                    swapRecord.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "2"
                    }
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoLockedPending.ordinal) {
                    //WithDrawNeoLockedPending == 11
                    swapRecord.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "3"
                    }
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoLockedDone.ordinal) {
                    //WithDrawNeoLockedDone == 12
                    swapRecord.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "4"
                    }
                    if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                        Thread.sleep(checkStatusTime)
                        userUnlocNep5Qlc()
                    } else {
                        Thread.sleep(checkStatusTime)
                        checkLcokState()
                    }
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoUnLockedPending.ordinal) {
                    //WithDrawNeoLockedDone == 12
                    swapRecord.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "5"
                    }
                    if (swapRecord.swaptxHash == null || "".equals(swapRecord.swaptxHash)) {
                        Thread.sleep(checkStatusTime)
                        userUnlocNep5Qlc()
                    } else {
                        Thread.sleep(checkStatusTime)
                        checkLcokState()
                    }
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoUnLockedDone.ordinal) {
                    //claim成功
                    //WithDrawNeoUnLockedDone == 13
                    runOnUiThread {
                        swapRecord.state = checkHubState.state.toInt()
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                        var newSwapRecord = SwapRecord()
                        newSwapRecord.isMainNet = SpUtil.getBoolean(activity, ConstantValue.isMainNet, true)
                        newSwapRecord.wrpperEthAddress = wrapperOnline.ethAddress
                        newSwapRecord.ethContractAddress = wrapperOnline.ethContract
                        newSwapRecord.wrapperNeoAddress = wrapperOnline.neoAddress
                        newSwapRecord.neoContractAddress = wrapperOnline.neoContract
                        newSwapRecord.type = SwapRecord.SwapType.typeNep5ToErc20.ordinal
                        newSwapRecord.fromAddress = swapRecord.fromAddress
                        swapRecord = newSwapRecord
                        sweetAlertDialogSwap!!.dismissWithAnimation()
                        toast(getString(R.string.success))
                        activity!!.finish()
                    }

                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawEthUnlockPending.ordinal) {
                    //WithDrawEthUnlockPending == 14
                    //可以作为app端的最终状态
                    runOnUiThread {
                        swapRecord.state = checkHubState.state.toInt()
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                        var newSwapRecord = SwapRecord()
                        newSwapRecord.isMainNet = SpUtil.getBoolean(activity, ConstantValue.isMainNet, true)
                        newSwapRecord.wrpperEthAddress = wrapperOnline.ethAddress
                        newSwapRecord.ethContractAddress = wrapperOnline.ethContract
                        newSwapRecord.wrapperNeoAddress = wrapperOnline.neoAddress
                        newSwapRecord.neoContractAddress = wrapperOnline.neoContract
                        newSwapRecord.type = SwapRecord.SwapType.typeNep5ToErc20.ordinal
                        newSwapRecord.fromAddress = swapRecord.fromAddress
                        swapRecord = newSwapRecord
                        sweetAlertDialogSwap!!.dismissWithAnimation()
                        toast(getString(R.string.success))
                        activity!!.finish()
                    }
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawEthUnlockDone.ordinal) {
                    //DepositNeoUnLockedPending == 5
                }
            } else {
                error.exception.printStackTrace()
                Thread.sleep(checkStatusTime)
                checkLcokState()
            }
        }
    }

    var ethBalance = 0.toBigInteger()
    fun getEthbalance() {
        thread {
            try {
                val web3j = Web3j.build(org.web3j.protocol.http.HttpService(ConstantValue.ethNodeUrl))
                var balance = web3j.ethGetBalance(ethWallet.address, DefaultBlockParameter.valueOf("latest")).send()
                ethBalance = balance.balance
                KLog.i(balance.balance)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    lateinit var neow3j: Neow3j

    /**
     * 抵押erc20成功，准备领取nep5 qlc
     */
    fun userUnlocNep5Qlc() {
        KLog.i("开始claim")
        try {
            thread {
                nep5QLCClaim()
//                var wallet = AppConfig.instance.daoSession.walletDao.queryBuilder().where(WalletDao.Properties.Address.eq(swapRecord.toAddress)).list()[0]
//                var txid = Test1Contract.userUnlock(neow3j, swapRecord.rOrign, swapRecord.toAddress, wallet.wif, wrapperOnline.neoContract)
//                if ("".equals(txid)) {
//                    runOnUiThread {
//                        toast(getString(R.string.please_retry))
//                    }
//                    sweetAlertDialogSwap!!.dismissWithAnimation()
//                    return@thread
//                } else {
//                    swapRecord.swaptxHash = txid
//                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
//                    checkNep5Transaction()
//                    runOnUiThread {
//                        tvDot!!.text = "5"
//                    }
//
//                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                toast(e.message!!)
            }
        }
    }

    /**
     * 检查erc20 claim的状态
     */
    fun checkErc20Transaction() {
        KLog.i("检查eth交易状态")
        thread {
            try {
                val web3j = Web3j.build(org.web3j.protocol.http.HttpService(ConstantValue.ethNodeUrl))
                var transaction = web3j.ethGetTransactionByHash(swapRecord.txHash).send()
                if (transaction.hasError()) {
                    KLog.i(transaction.error.message)
                    if ("Unknown transaction".equals(transaction.error.message)) {
                        Thread.sleep(checkStatusTime)
                        checkErc20Transaction()
                    }
                } else {
                    KLog.i("eth链上交易已经确认")
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun withDrawApiLock(gasPrice: String, gasLimit: Int) {
        if (isClose) {
            return
        }

        val orginHash = UUID.randomUUID().toString().replace("-", "")
        KLog.i(orginHash)
        val hashSha256 = Sha256.from(orginHash.toByteArray()).toString()
        KLog.i(hashSha256)

        val dataJson = jsonObject(
                "value" to hashSha256
        )
//        var list = arrayListOf<Pair<String, String>>(Pair("value", rHash))
//        KLog.i(list)
        var request = (ConstantValue.qlcHubEndPoint + "/withdraw/lock").httpPost().body(dataJson.toString())
        DefiUtil.addRequestHeader(request)
        var result1 = false
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                Thread.sleep(checkStatusTime)
                destroyLock(gasPrice, gasLimit, orginHash, hashSha256)
            } else {

            }
        }
    }

    /**
     * 检查发出的交易是否已经上链
     */
    fun checkNep5Transaction() {
        var log = neow3j.getApplicationLog(swapRecord.swaptxHash).send()
        if (log.hasError()) {
            KLog.i(log.error.message)
            if ("Unknown transaction".equals(log.error.message)) {
                Thread.sleep(5000)
                checkNep5Transaction()
            }
        } else {
            KLog.i(log.applicationLog.transactionId)
            log.applicationLog.executions.forEach {
                KLog.i(it.state)
            }
            nep5QLCClaim()
        }
    }

    /**
     * 这个方法现在为claim方法
     */
    fun nep5QLCClaim() {
        val dataJson = jsonObject(
                "rOrigin" to swapRecord.rOrign,
                "userNep5Addr" to swapRecord.toAddress
        )
        KLog.i(dataJson.toString())
        var request = (ConstantValue.qlcHubEndPoint + "/withdraw/claim").httpPost().body(dataJson.toString())
        DefiUtil.addRequestHeader(request)
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                var fetchBack = Gson().fromJson<FetchBack>(data, FetchBack::class.java)
                swapRecord!!.swaptxHash = fetchBack.value
                AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                Thread.sleep(checkStatusTime)
                checkLcokState()
            } else {
                Thread.sleep(checkStatusTime)
                nep5QLCClaim()
            }
        }
    }

    override fun setNeoDetail(neoWalletInfo: NeoWalletInfo) {
        closeProgressDialog()
        neoWalletInfo.data.balance.forEach {
            if (it.asset_symbol.equals("QLC")) {
                neoQlcTokenInfo = it
                tvQlcBalance.text = getString(R.string.balance) + ": ${it.amount} QLC"
            }
        }
    }

    var balance = ""
    private fun getErc20TokenBalance() {
        val arrays = arrayOfNulls<Any>(3)
        arrays[0] = ethWallet.address
        arrays[1] = ethWallet.address
        arrays[2] = wrapperOnline.ethContract
        Thread(Runnable {
            Thread.sleep(500)
            webview.callHandler<Any>("ethSmartContract.getErc20TokenBalance", arrays, object : OnReturnValue<Any?> {
                fun onValue(retValue: String) {
                    val result = java.lang.StringBuilder().append("call succeed,return value is ")
                    KLog.i(result.append(retValue).toString())
                    balance = BigDecimal.valueOf(retValue.toLong()).divide(100000000.toBigDecimal(), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
                    tvQlcBalance?.text = getString(R.string.balance) + ": ${balance} QLC"
                    mPresenter.getEthGasPrice(hashMapOf())
                }

                override fun onValue(retValue: Any?) {
                    this.onValue(retValue as String)
                }
            })
        }).start()
    }

    var ethPrice = 0.toDouble()
    override fun setEthPrice(tokenPrice: TokenPrice) {
        ethPrice = tokenPrice.data[0].price
        getEthbalance()
    }

    lateinit var ethGasPrice: EthGasPrice
    var gasLimit = 200000
    override fun setEthGasPrice(string: String) {
        var ethGasPrice1 = Gson().fromJson<EthGasPrice>(string, EthGasPrice::class.java)
        ethGasPrice = ethGasPrice1
        KLog.i(ethGasPrice)
        mPresenter.getEthPrice()
    }

    var neoQlcTokenInfo: NeoWalletInfo.DataBean.BalanceBean? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AllWallet.WalletType.NeoWallet.ordinal && resultCode == Activity.RESULT_OK) {
            neoWallet = data!!.getParcelableExtra("wallet")
            tvNeoWalletName.text = neoWallet!!.name
            tvNeoWalletAddess.text = neoWallet!!.address
            swapRecord.toAddress = neoWallet!!.address
            if (!"".equals(etStakeQlcAmount.text.toString())) {
                invoke.setBackgroundResource(R.drawable.main_color_bt_bg)
            } else {
                invoke.setBackgroundResource(R.drawable.unable_bt_bg)
            }
        }
    }

    lateinit var ethWallet: EthWallet
    lateinit var neoWallet: Wallet
    lateinit var wrapperOnline: WrapperOnline
    fun getEthWallet() {
        var ethWallets = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        ethWallet = ethWallets.filter { it.isCurrent() }[0]
        tvEthWalletAddess.text = ethWallet.address
        tvEthWalletName.text = ethWallet.name
    }

    fun checkWrapperOnline() {
        var list = arrayListOf<Pair<String, String>>(Pair("value", ethWallet.address))
        var request = (ConstantValue.qlcHubEndPoint + "/info/ping").httpGet(list)
        DefiUtil.addRequestHeader(request)
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (isFinish) {
                return@responseString
            }
            if (error == null) {
                try {
                    wrapperOnline = Gson().fromJson<WrapperOnline>(data, WrapperOnline::class.java)
                    KLog.i(wrapperOnline.toString())
                    swapRecord.wrpperEthAddress = wrapperOnline.ethAddress
                    swapRecord.ethContractAddress = wrapperOnline.ethContract
                    swapRecord.wrapperNeoAddress = wrapperOnline.neoAddress
                    swapRecord.neoContractAddress = wrapperOnline.neoContract
                    runOnUiThread {
                        etStakeQlcAmount.hint = getString(R.string.from_1_s_qlc, (wrapperOnline.minWithdrawAmount.toLong() / 100000000).toString())
                        miniSwapQlc.text = getString(R.string.the_minimum_stake_amount_is_1_s_qlc, (wrapperOnline.minWithdrawAmount.toLong() / 100000000).toString())
                    }
                    try {
                        getErc20TokenBalance()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {

            }
        }
    }


    var sweetAlertDialog: SweetAlertDialog? = null
    fun showEthFee() {
        val view = layoutInflater.inflate(R.layout.show_eth_fee, null, false)
        var tvOk = view.findViewById<View>(R.id.tvOk)
        var ivClose = view.findViewById<View>(R.id.ivClose)
        var ivSendChain = view.findViewById<ImageView>(R.id.ivSendChain)

        var tvEthWalletName1 = view.findViewById<TextView>(R.id.tvEthWalletName1)
        var tvEthWalletAddess1 = view.findViewById<TextView>(R.id.tvEthWalletAddess1)
        var tvToAddress = view.findViewById<TextView>(R.id.tvToAddress)
        var tvEthAmount1 = view.findViewById<TextView>(R.id.tvEthAmount1)
        var llSlow = view.findViewById<LinearLayout>(R.id.llSlow)
        var llAverage = view.findViewById<LinearLayout>(R.id.llAverage)
        var llFast = view.findViewById<LinearLayout>(R.id.llFast)

        var slowCostEth = view.findViewById<TextView>(R.id.slowCostEth)
        var slowCostUsd = view.findViewById<TextView>(R.id.slowCostUsd)
        val slowGas = Convert.toWei(ethGasPrice.result.safeGasPrice, Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        var slowEth = slowGas * gasLimit.toBigDecimal()
        var slowUsd = (slowGas * gasLimit.toBigDecimal()).multiply(ethPrice.toBigDecimal())
        slowCostEth.text = slowEth.stripTrailingZeros().toPlainString() + " ETH"
        slowCostUsd.text = "$ " + slowUsd.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()

        var averageCostEth = view.findViewById<TextView>(R.id.averageCostEth)
        var averageCostUsd = view.findViewById<TextView>(R.id.averageCostUsd)
        val averageGas = Convert.toWei(ethGasPrice.result.proposeGasPrice, Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        var averageEth = averageGas * gasLimit.toBigDecimal()
        var averageUsd = (averageGas * gasLimit.toBigDecimal()).multiply(ethPrice.toBigDecimal())
        averageCostEth.text = averageEth.stripTrailingZeros().toPlainString() + " ETH"
        averageCostUsd.text = "$ " + averageUsd.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()

        var fastCostEth = view.findViewById<TextView>(R.id.fastCostEth)
        var fastCostUsd = view.findViewById<TextView>(R.id.fastCostUsd)
        val fastGas = Convert.toWei(ethGasPrice.result.fastGasPrice, Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
        var fastEth = fastGas * gasLimit.toBigDecimal()
        var fastUsd = (fastGas * gasLimit.toBigDecimal()).multiply(ethPrice.toBigDecimal())
        fastCostEth.text = fastEth.stripTrailingZeros().toPlainString() + " ETH"
        fastCostUsd.text = "$ " + fastUsd.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()


        llSlow.isSelected = false
        llAverage.isSelected = true
        llFast.isSelected = false
        // https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey=YourApiKeyToken
        var gasPrice = ethGasPrice.result.safeGasPrice

        llSlow.setOnClickListener {
            gasPrice = ethGasPrice.result.safeGasPrice
            llSlow.isSelected = true
            llAverage.isSelected = false
            llFast.isSelected = false
        }
        llAverage.setOnClickListener {
            gasPrice = ethGasPrice.result.proposeGasPrice
            llSlow.isSelected = false
            llAverage.isSelected = true
            llFast.isSelected = false
        }
        llFast.setOnClickListener {
            gasPrice = ethGasPrice.result.fastGasPrice
            llSlow.isSelected = false
            llAverage.isSelected = false
            llFast.isSelected = true
        }

        tvEthWalletAddess1.text = ethWallet.address
        tvEthWalletName1.text = ethWallet.name
        ivSendChain.setImageResource(R.mipmap.icons_eth_wallet)
        tvToAddress.text = neoWallet.address

        tvEthAmount1.text = etStakeQlcAmount.text.toString() + " QLC"
        sweetAlertDialog = SweetAlertDialog(activity)
        val window = sweetAlertDialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog!!.setView(view)
        sweetAlertDialog!!.show()
        ivClose.setOnClickListener {
            sweetAlertDialog!!.cancel()
        }
        tvOk.setOnClickListener {
            val needGas = Convert.toWei(gasPrice, Convert.Unit.GWEI).divide(Convert.toWei(1.toString() + "", Convert.Unit.ETHER))
            var needEth = needGas * gasLimit.toBigDecimal()
            if (Convert.toWei(needEth, Convert.Unit.ETHER) > ethBalance.toBigDecimal()) {
                toast(getString(R.string.no_enough_eth))
                return@setOnClickListener
            }
            sweetAlertDialog!!.cancel()
            showSwapPup()
            thread {
                withDrawApiLock(gasPrice, gasLimit)
            }
        }
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

    fun destroyLock(gasPrice: String, gasLimit: Int, orginHash : String, hashSha256 : String) {
        swapRecord.rOrign = orginHash
        swapRecord.rHash = hashSha256
        swapRecord.lockTime = System.currentTimeMillis()
        swapRecord.amount = etStakeQlcAmount.text.toString().toInt()
        val arrays = arrayOfNulls<Any>(9)
        arrays[0] = ConstantValue.ethNodeUrl
        arrays[1] = derivePrivateKey(swapRecord.fromAddress)
        arrays[2] = swapRecord.fromAddress
        arrays[3] = wrapperOnline.ethAddress
        arrays[5] = etStakeQlcAmount.text.toString().toInt()
        arrays[4] = swapRecord.ethContractAddress
        arrays[6] = "0x" + swapRecord.rHash
        arrays[7] = gasPrice
        arrays[8] = gasLimit

        arrays.forEach {
            KLog.i(it)
        }
        thread {
            webview.callHandler<Any>("ethSmartContract.destoryLock", arrays, object : OnReturnValue<Any> {
                override fun onValue(retValue: Any) {
                    this.onValue(retValue as JSONArray)
                    swapRecord.txHash = retValue[1].toString()
                    swapRecord.state = -1
                    AppConfig.instance.daoSession.swapRecordDao.insert(swapRecord)
                    checkErc20Transaction()
                }

                fun onValue(retValue: JSONArray) {
                    val result = StringBuilder().append("call succeed,return value is ")
                    KLog.i(result.append(retValue).toString())
                    if (!"".equals(retValue)) {

                    }
                }

            })
        }
    }

//    fun destroyUnLock(hash: String, orginHash: String) {
//        val orginHash = UUID.randomUUID().toString().replace("-", "")
//        KLog.i(orginHash)
//        val hashSha256 = Sha256.from(orginHash.toByteArray()).toString()
//        KLog.i(hashSha256)
//
//        val arrays = arrayOfNulls<Any>(6)
//        arrays[0] = "1c70c79c8f0c0ba5c700663256360230327f6d3688859c688b4106c890676440"
//        arrays[1] = userAddress
////        arrays[2] = "0x" + wrapperOnline.ethAddress
//        arrays[2] = onwerAddress
////        arrays[3] = "0x" + wrapperOnline.ethContract
//        arrays[3] = contractAddress
//        arrays[4] = "2"
//        arrays[5] = "0x" + hashSha256
//        thread {
//            webview.callHandler<Any>("ethSmartContract.destoryLock", arrays, object : OnReturnValue<Any> {
//                override fun onValue(retValue: Any) {
//                    this.onValue(retValue as String)
//                }
//
//                fun onValue(retValue: String) {
//                    val result = StringBuilder().append("call succeed,return value is ")
//                    KLog.i(result.append(retValue).toString())
//                }
//
//            })
//        }
//    }

    override fun initDataFromNet() {
    }

    override fun setupFragmentComponent() {
        DaggerEthSwapComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .ethSwapModule(EthSwapModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: EthSwapContract.EthSwapContractPresenter) {
        mPresenter = presenter as EthSwapPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
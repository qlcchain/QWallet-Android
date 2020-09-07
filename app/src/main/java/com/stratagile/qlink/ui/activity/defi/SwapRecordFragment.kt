package com.stratagile.qlink.ui.activity.defi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.getSystemService
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
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.db.SwapRecord
import com.stratagile.qlink.db.SwapRecordDao
import com.stratagile.qlink.db.WalletDao
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.defi.CheckHubState
import com.stratagile.qlink.entity.defi.EthGasPrice
import com.stratagile.qlink.ui.activity.defi.component.DaggerSwapRecordComponent
import com.stratagile.qlink.ui.activity.defi.contract.SwapRecordContract
import com.stratagile.qlink.ui.activity.defi.module.SwapRecordModule
import com.stratagile.qlink.ui.activity.defi.presenter.SwapRecordPresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.defi.SwapListAdapter
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import io.neow3j.contract.Test1Contract.refundUser
import io.neow3j.protocol.Neow3j
import io.neow3j.protocol.http.HttpService
import kotlinx.android.synthetic.main.activity_swap_detail.*
import kotlinx.android.synthetic.main.fragment_swap_record.*
import org.json.JSONArray
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import wendu.dsbridge.OnReturnValue
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/08/12 15:49:25
 */

class SwapRecordFragment : BaseFragment(), SwapRecordContract.View {

    @Inject
    lateinit internal var mPresenter: SwapRecordPresenter
    lateinit var swapListAdapter: SwapListAdapter
    var currentAddress = ""
    lateinit var neow3j: Neow3j
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_swap_record, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        currentAddress = mBundle!!.getString("address")
        neow3j = Neow3j.build(HttpService(ConstantValue.neoNode))
        saHalf = ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        saHalf.setDuration(400)

        sa1 = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(1000)

        return view
    }

    object SwapRecordFragmentInstance{
        @JvmStatic
        fun getInstance(address: String) : SwapRecordFragment{
            var bundle = Bundle()
            bundle.putString("address", address)
            var swapRecordFragment = SwapRecordFragment()
            swapRecordFragment.arguments = bundle
            return swapRecordFragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webview.loadUrl("file:///android_asset/eth.html")
        swapListAdapter = SwapListAdapter(arrayListOf())
        swapListAdapter.setEmptyView(R.layout.empty_layout, refreshLayout)
        recyclerView.adapter = swapListAdapter
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(8f, activity)))
        getSwapHistory()
        refreshLayout.setOnRefreshListener {
            getSwapHistory()
            refreshLayout.isRefreshing = false
        }
        swapListAdapter.setOnItemClickListener { adapter, view, position ->
            KLog.i(swapListAdapter.data[position].toString())
            startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapListAdapter.data[position]))
        }
        swapListAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (swapListAdapter.data[position].type == 1) {
                if (swapListAdapter.data[position].state == -1) {
                    swapRecord = swapListAdapter.data[position]
                    swapRecord!!.index = position
                    showProgressDialog()
                    thread {
                        try {
                            checkNep5Transaction()
                        } catch (e : Exception) {
                            e.printStackTrace()
                            checkNep5Transaction()
                        }
                    }
                } else if (swapListAdapter.data[position].state == 8) {
                    //nep5 -> erc20 抵押超时，赎回nep5
                    swapRecord = swapListAdapter.data[position]
                    swapRecord!!.index = position
                    if (swapRecord!!.swaptxHash != null && !"".equals(swapRecord!!.swaptxHash)) {
                        showProgressDialog()
                        nep5unLockNotice()
                    } else {
                        showProgressDialog()
                        unLockNepQlc()
                    }
                } else if (swapListAdapter.data[position].state == 3) {
                    if (swapListAdapter.data[position].swaptxHash == null || "".equals(swapListAdapter.data[position].swaptxHash)) {
                        //nep5 -> erc20 抵押中断，继续抵押nep5， 这里处理领取erc20的qlc
                        swapRecord = swapListAdapter.data[position]
                        swapRecord!!.index = position
                        mPresenter.getEthGasPrice(hashMapOf())
                        showProgressDialog()
                    }
                } else if (swapListAdapter.data[position].state == 4) {
                    if (swapListAdapter.data[position].swaptxHash == null || "".equals(swapListAdapter.data[position].swaptxHash)) {
                        //nep5 -> erc20 抵押中断，继续抵押nep5， 这里处理领取erc20的qlc
                        swapRecord = swapListAdapter.data[position]
                        swapRecord!!.index = position
                        mPresenter.getEthGasPrice(hashMapOf())
                        showProgressDialog()
                    }
                }
            } else {
//                revokeEthQlc(position, swapListAdapter.data[position])
            }
        }
    }

    /**
     * 检查发出的交易是否已经上链
     */
    fun checkNep5Transaction() {
        var log = neow3j.getApplicationLog(swapRecord!!.txHash).send()
        if (log.hasError()) {
            KLog.i(log.error.message)
            if ("Unknown transaction".equals(log.error.message)) {
                Thread.sleep(checkStatusTime)
                checkNep5Transaction()
            }
        } else {
            nep5LockNotice()
            KLog.i(log.applicationLog.transactionId)
            log.applicationLog.executions.forEach {
                KLog.i(it.state)
            }
        }
    }

    fun nep5LockNotice() {
        if (isClose) {
            return
        }
        val dataJson = jsonObject(
                "amount" to (swapRecord!!.amount.toLong() * 100000000).toString(),
                "rHash" to swapRecord!!.rHash,
                "nep5TxHash" to swapRecord!!.txHash,
                "addr" to swapRecord!!.wrpperEthAddress
        )
        KLog.i(dataJson.toString())
        var request = (ConstantValue.qlcHubEndPoint + "/deposit/lock").httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                Thread.sleep(checkStatusTime)
                mPresenter.getEthGasPrice(hashMapOf())
            } else {
                error.exception.printStackTrace()
                Thread.sleep(checkStatusTime)
                nep5LockNotice()
            }
        }
    }

    var swapRecord : SwapRecord? = null


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

    var sweetAlertDialog: SweetAlertDialog? = null
    var gasPrice = ""
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
        gasPrice = ethGasPrice.result.safeGasPrice

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

        tvEthWalletAddess1.text = swapRecord!!.fromAddress
        tvEthWalletName1.text = ""
        ivSendChain.setImageResource(R.mipmap.icons_neo_wallet)
        tvToAddress.text = swapRecord!!.toAddress
        tvEthAmount1.text = swapRecord!!.amount.toString() + " QLC"
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
            checkLcokState()
            showSwapPup()
        }
    }

    var sweetAlertDialogSwap: SweetAlertDialog? = null
    var llDot: View? = null
    var tvDot: TextView? = null

    fun showSwapPup() {
        val view = layoutInflater.inflate(R.layout.alert_swap, null, false)
        llDot = view.findViewById(R.id.llDot)
        tvDot = view.findViewById(R.id.tvDot)
        tvDot!!.text = "1"
        sweetAlertDialogSwap = SweetAlertDialog(activity)
        var ivClose = view.findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            swapRecord = null
            isClose = true
            sweetAlertDialogSwap!!.dismissWithAnimation()
        }

        sweetAlertDialogSwap!!.setView(view)
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

    //用户claim qlc到eth钱包
    fun userClaimQlc() {
        val arrays = arrayOfNulls<Any>(8)
        arrays[0] = ConstantValue.ethNodeUrl
        arrays[1] = derivePrivateKey(swapRecord!!.toAddress)
        arrays[2] = swapRecord!!.toAddress
        arrays[3] = "0x" + swapRecord!!.rHash
        arrays[4] = "0x" + Numeric.toHexStringNoPrefix(swapRecord!!.rOrign.toByteArray())
        arrays[5] = swapRecord!!.ethContractAddress
        arrays[6] = gasPrice
        arrays[7] = gasLimit

        arrays.forEach {
            KLog.i(it)
        }
        thread {
            webview.callHandler<Any>("ethSmartContract.issueUnlock", arrays, object : OnReturnValue<Any> {
                override fun onValue(retValue: Any) {
                    this.onValue(retValue as JSONArray)
                    swapRecord!!.swaptxHash = retValue[1].toString()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    checkErc20Transaction()
                }

                fun onValue(retValue: JSONArray) {
                    val result = StringBuilder().append("call succeed,return value is ")
                    KLog.i(result.append(retValue).toString())
//                    runOnUiThread {
//                        val cm = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                        val mClipData = ClipData.newPlainText("Label", result)
//                        cm.primaryClip = mClipData
//                        ToastUtil.displayShortToast(result.toString())
//                    }
                }

            })
        }
    }

    /**
     * 检查erc20 claim的状态
     */
    fun checkErc20Transaction() {
        thread {
            try {
                val web3j = Web3j.build(org.web3j.protocol.http.HttpService(ConstantValue.ethNodeUrl))
                var transaction = web3j.ethGetTransactionByHash(swapRecord!!.swaptxHash).send()
                if (transaction.hasError()) {
                    KLog.i(transaction.error.message)
                    if ("Unknown transaction".equals(transaction.error.message)) {
                        Thread.sleep(checkStatusTime)
                        checkErc20Transaction()
                    }
                } else {
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                }
            } catch (e : Exception) {
                Thread.sleep(checkStatusTime)
                checkLcokState()
                e.printStackTrace()
            }
        }
    }

    var ethBalance = 0.toBigInteger()
    fun getEthbalance() {
        thread {
            try {
                val web3j = Web3j.build(org.web3j.protocol.http.HttpService(ConstantValue.ethNodeUrl))
                var balance = web3j.ethGetBalance(swapRecord!!.toAddress, DefaultBlockParameter.valueOf("latest")).send()
                ethBalance = balance.balance
                KLog.i(balance.balance)
                runOnUiThread {
                    closeProgressDialog()
                    showEthFee()
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }




    private var neow: Neow3j? = null
    fun unLockNepQlc() {
        showProgressDialog()
        thread {
            neow = Neow3j.build(HttpService(ConstantValue.neoNode))
            var wallet = AppConfig.instance.daoSession.walletDao.queryBuilder().where(WalletDao.Properties.Address.eq(swapRecord!!.fromAddress)).list()[0]
            val result = refundUser(neow!!, swapRecord!!.rOrign, swapRecord!!.fromAddress, wallet.wif, swapRecord!!.neoContractAddress)
            swapRecord!!.swaptxHash = result
            AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
            KLog.i(result)
            nep5unLockNotice()
        }
    }

    var isClose = true
    var checkStatusTime = 5000.toLong()
    override fun onDestroy() {
        isClose = true
        var records = AppConfig.instance.daoSession.swapRecordDao.loadAll()
        val saveData = Gson().toJson(records)
        FileUtil.savaData("/Qwallet/swapRecord.json", saveData)
        super.onDestroy()
    }

    var unLockNoticeTimes = 0
    fun nep5unLockNotice() {
        if (isClose) {
            return
        }
        val dataJson = jsonObject(
                "rHash" to swapRecord!!.rHash,
                "nep5TxHash" to swapRecord!!.swaptxHash
        )
        KLog.i(dataJson.toString())
        var request = (ConstantValue.qlcHubEndPoint + "/deposit/fetchNotice").httpPost().body(dataJson.toString())
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                checkLcokState()
                runOnUiThread {
//                    if (!isClose) {
//                        toast(getString(R.string.success))
//                    }
                    closeProgressDialog()
                }
            } else {
                if (unLockNoticeTimes > 3) {
                    unLockNoticeTimes = 0
                    checkLcokState()
                    runOnUiThread {
                        closeProgressDialog()
                    }
                    return@responseString
                } else {
                    unLockNoticeTimes++
                    error.exception.printStackTrace()
                    Thread.sleep(checkStatusTime)
                    nep5unLockNotice()
                }
            }
        }
    }

    private fun derivePrivateKey(address: String): String? {
        val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
        var ethWallet = EthWallet()
        for (i in ethWallets.indices) {
            if (ethWallets[i].address.toLowerCase() == address.toLowerCase()) {
                ethWallet = ethWallets[i]
                break
            }
        }
        return ETHWalletUtils.derivePrivateKey(ethWallet.id)
    }

    fun getSwapHistory() {
        var lsit = AppConfig.instance.daoSession.swapRecordDao.loadAll()
        var list = AppConfig.instance.daoSession.swapRecordDao.queryBuilder().where(SwapRecordDao.Properties.FromAddress.eq(currentAddress), SwapRecordDao.Properties.IsMainNet.eq(SpUtil.getBoolean(activity, ConstantValue.isMainNet, true))).list()
        list.sortBy { - it.lockTime }
        swapListAdapter.setNewData(list)
        thread {
            list.forEachIndexed { index, swapRecord ->
                if (swapRecord.type == SwapRecord.SwapType.typeNep5ToErc20.ordinal) {
                    //查找nep5到erc20抵押的状态
                    //
                    if (swapRecord.state != 6 && swapRecord.state != 9) {
                        checkLcokState(index, swapRecord)
                    }
                } else {
                    //查找erc20到nep5抵押的状态
//                    checkLcokState(index, swapRecord)
                }
            }
        }
    }

    fun checkLcokState() {
        if (isClose) {
            return
        }
        val dataJson = jsonObject(
                "value" to swapRecord!!.rHash
        )

        var list = arrayListOf<Pair<String, String>>(Pair("value", swapRecord!!.rHash))
        KLog.i(dataJson.toString())
        var request = (ConstantValue.qlcHubEndPoint + "/info/lockerInfo").httpGet(list)
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                var checkHubState = Gson().fromJson<CheckHubState>(data, CheckHubState::class.java)
                if (checkHubState.state.toInt() == 0) {
                    //DepositInit == 0
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "0"
                    }
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == 1){
                    //DepositNeoLockedDone == 1
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "1"
                    }
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == 2){
                    //DepositEthLockedPending == 2
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "2"
                    }
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == 3) {
                    //开始调eth合约
                    //DepositEthLockedDone == 3
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        tvDot!!.text = "3"
                    }
                    if (swapRecord!!.swaptxHash == null || "".equals(swapRecord!!.swaptxHash)) {
                        Thread.sleep(checkStatusTime)
                        userClaimQlc()
                    } else {
                        Thread.sleep(checkStatusTime)
                        checkLcokState()
                    }
                } else if (checkHubState.state.toInt() == 4) {
                    //DepositEthUnLockedDone == 4
                    //可以作为app端的最终状态
                    runOnUiThread {
                        swapRecord!!.state = 4
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                        sweetAlertDialogSwap!!.dismissWithAnimation()
                        swapListAdapter.notifyItemChanged(swapRecord!!.index)
                    }
                } else if (checkHubState.state.toInt() == 5) {
                    //DepositNeoUnLockedPending == 5
                    runOnUiThread {
                        swapRecord!!.state = 5
                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                        sweetAlertDialogSwap!!.dismissWithAnimation()
                        swapListAdapter.notifyItemChanged(swapRecord!!.index)
                    }
                } else if (checkHubState.state.toInt() == 6) {
                    //wrapper已经拿到了nep5代币
                    //DepositNeoUnLockedDone == 6
                    runOnUiThread {
                        swapRecord!!.state = 6
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                        sweetAlertDialogSwap!!.dismissWithAnimation()
                        swapListAdapter.notifyItemChanged(swapRecord!!.index)
                    }
                } else if (checkHubState.state.toInt() == 8) {
                    //wrapper已经拿到了nep5代币
                    //DepositNeoUnLockedDone == 6
                    runOnUiThread {
                        swapRecord!!.state = 8
                        swapListAdapter.notifyItemChanged(swapRecord!!.index)
                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    }
                } else if (checkHubState.state.toInt() == 9) {
                    //wrapper已经拿到了nep5代币
                    //DepositNeoUnLockedDone == 6
                    runOnUiThread {
                        swapRecord!!.state = 9
                        swapListAdapter.notifyItemChanged(swapRecord!!.index)
                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    }
                }
            } else {
                error.exception.printStackTrace()
                Thread.sleep(checkStatusTime)
                checkLcokState()
            }
        }
    }

    fun checkLcokState(index: Int, swapRecord: SwapRecord) {
//        if (swapRecord.state == 8 && (swapRecord.swaptxHash != null && !"".equals(swapRecord.swaptxHash))) {
//            swapRecord.index = index
//            this.swapRecord = swapRecord
//            nep5unLockNotice()
//            return
//        }
        val dataJson = jsonObject(
                "type" to swapRecord.type.toString(),
                "hash" to swapRecord.rHash
        )

        var list = arrayListOf<Pair<String, String>>(Pair("value", swapRecord.rHash))
        KLog.i(dataJson.toString())
        var request = (ConstantValue.qlcHubEndPoint + "/info/lockerInfo").httpGet(list)
        request.headers["Content-Type"] = "application/json"
        request.responseString { _, _, result ->
            val (data, error) = result
            KLog.i(data)
            if (error == null) {
                var checkSwapHubState = Gson().fromJson<CheckHubState>(data, CheckHubState::class.java)
                swapRecord.state = checkSwapHubState.state.toInt()
                swapRecord.neoTimeout = checkSwapHubState.isNeoTimeout
                swapRecord.ethTimeout = checkSwapHubState.isEthTimeout
                swapRecord.fail = checkSwapHubState.isFail
                AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                runOnUiThread {
                    swapListAdapter.notifyItemChanged(index)
                }
            } else {

            }
        }
    }

    override fun initDataFromNet() {
        getSwapHistory()
    }


    override fun setupFragmentComponent() {
        DaggerSwapRecordComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .swapRecordModule(SwapRecordModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: SwapRecordContract.SwapRecordContractPresenter) {
        mPresenter = presenter as SwapRecordPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        isClose = false
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }
}
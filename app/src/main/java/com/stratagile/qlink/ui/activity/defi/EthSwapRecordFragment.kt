package com.stratagile.qlink.ui.activity.defi

import android.content.Intent
import android.os.Bundle
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
import com.stratagile.qlink.db.SwapRecord.SwapState
import com.stratagile.qlink.db.SwapRecordDao
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.defi.CheckHubState
import com.stratagile.qlink.entity.defi.EthGasPrice
import com.stratagile.qlink.entity.defi.FetchBack
import com.stratagile.qlink.ui.activity.defi.component.DaggerEthSwapRecordComponent
import com.stratagile.qlink.ui.activity.defi.contract.EthSwapRecordContract
import com.stratagile.qlink.ui.activity.defi.module.EthSwapRecordModule
import com.stratagile.qlink.ui.activity.defi.presenter.EthSwapRecordPresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.defi.SwapListAdapter
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.utils.eth.ETHWalletUtils
import com.stratagile.qlink.view.SweetAlertDialog
import io.neow3j.protocol.Neow3j
import io.neow3j.protocol.http.HttpService
import kotlinx.android.synthetic.main.fragment_swap_record.*
import org.json.JSONArray
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.utils.Convert
import wendu.dsbridge.OnReturnValue
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/09/02 13:58:17
 */

class EthSwapRecordFragment : BaseFragment(), EthSwapRecordContract.View {

    @Inject
    lateinit internal var mPresenter: EthSwapRecordPresenter
    lateinit var swapListAdapter: SwapListAdapter
    var currentAddress = ""
    lateinit var neow3j: Neow3j
    var swapRecord : SwapRecord? = null
    lateinit var saHalf: ScaleAnimation
    lateinit var sa1: ScaleAnimation

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

                    }
                } else if (swapListAdapter.data[position].state == 8) {
                    //erc20 -> nep5 抵押超时，赎回erc20
                } else if (swapListAdapter.data[position].state == 3) {
                    if (swapListAdapter.data[position].swaptxHash == null || "".equals(swapListAdapter.data[position].swaptxHash)) {
                        //erc20 -> nep5 抵押中断，继续抵押erc20， 这里处理领取nep5的qlc
                        swapRecord = swapListAdapter.data[position]
                        swapRecord!!.index = position
                        showProgressDialog()
                    }
                }
            } else {
                if (swapListAdapter.data[position].state == -1) {
                    swapRecord = swapListAdapter.data[position]
                    swapRecord!!.index = position
                    showProgressDialog()
                    thread {

                    }
                } else if (swapListAdapter.data[position].state == 14) {
                    swapRecord = swapListAdapter.data[position]
                    swapRecord!!.index = position
                    showProgressDialog()
                    isClose = false
                    if (swapListAdapter.data[position].swaptxHash == null || "".equals(swapListAdapter.data[position].swaptxHash)) {
                        userUnlocNep5Qlc()
                    } else {
                        thread {
                            try {
                                checkNep5Transaction()
                            } catch (e : Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } else if (swapListAdapter.data[position].state == 20) {
                    swapRecord = swapListAdapter.data[position]
                    swapRecord!!.index = position
                    showProgressDialog()
                    mPresenter.getEthGasPrice(hashMapOf())

                }
            }
        }

        progressDialog.setOnDismissListener {
            KLog.i("弹窗关闭")
            isClose = true
        }
    }

    var isClose = false

    override fun onDestroy() {
        isClose = true
        super.onDestroy()
    }

    var checkStatusTime = 5000.toLong()
    fun checkLcokState() {
        if (isClose) {
            return
        }
        val dataJson = jsonObject(
                "type" to swapRecord!!.type.toString(),
                "hash" to swapRecord!!.rHash
        )

        var list = arrayListOf<Pair<String, String>>(Pair("value", swapRecord!!.rHash))
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
                if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawEthLockedDone.ordinal) {
                    //WithDrawEthLockedDone == 10
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoLockedPending.ordinal){
                    //WithDrawNeoLockedPending == 11
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    Thread.sleep(checkStatusTime)
                    checkLcokState()
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoLockedDone.ordinal){
                    //WithDrawNeoLockedDone == 12
                    swapRecord!!.state = checkHubState.state.toInt()
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    if (swapRecord!!.swaptxHash == null || "".equals(swapRecord!!.swaptxHash)) {
                        Thread.sleep(checkStatusTime)
//                        userUnlocNep5Qlc()
                    } else {
                        Thread.sleep(checkStatusTime)
                        checkLcokState()
                    }
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoUnLockedPending.ordinal) {
                    //开始调eth合约
                    //WithDrawNeoUnLockedDone == 13
                    runOnUiThread {
                        closeProgressDialog()
                        swapRecord!!.state = checkHubState.state.toInt()
                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                    }
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawNeoUnLockedDone.ordinal) {
                    //WithDrawEthUnlockPending == 14
                    //可以作为app端的最终状态
                    runOnUiThread {
                        closeProgressDialog()
                        swapRecord!!.state = checkHubState.state.toInt()
                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                        swapListAdapter.notifyItemChanged(swapRecord!!.index)
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                    }
                } else if (checkHubState.state.toInt() == SwapRecord.SwapState.WithDrawEthUnlockPending.ordinal) {
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
            val web3j = Web3j.build(org.web3j.protocol.http.HttpService(ConstantValue.ethNodeUrl))
            var balance = web3j.ethGetBalance(swapRecord!!.fromAddress, DefaultBlockParameter.valueOf("latest")).send()
            ethBalance = balance.balance
            KLog.i(balance.balance)
            runOnUiThread {
                closeProgressDialog()
                showEthFee()
            }
        }
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

        tvEthWalletAddess1.text = swapRecord!!.fromAddress
        tvEthWalletName1.text = ""
        ivSendChain.setImageResource(R.mipmap.icons_eth_wallet)
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
            showProgressDialog()
            thread {
                try {
                    revokeEthQlc(gasPrice, gasLimit)
                } catch (e :java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //erc20 -> nep5 超时了，需要原路拿回qlc
    fun revokeEthQlc(gasPrice: String, gasLimit: Int) {
        showProgressDialog()
        val arrays = arrayOfNulls<Any>(7)
        arrays[0] = ConstantValue.ethNodeUrl
        arrays[1] = derivePrivateKey(swapRecord!!.fromAddress)
        arrays[2] = swapRecord!!.fromAddress
        arrays[3] = "0x" + swapRecord!!.rHash
        arrays[4] = swapRecord!!.ethContractAddress
        arrays[5] = gasPrice
        arrays[6] = gasLimit
        arrays.forEach {
            KLog.i(it)
        }
        thread {
            try {
                Thread.sleep(500)
                webview.callHandler<Any>("ethSmartContract.destoryFetch", arrays, object : OnReturnValue<Any?> {
                    fun onValue(retValue: JSONArray) {
                        val result = StringBuilder().append("call succeed,return value is ")
                        KLog.i(result.append(retValue).toString())
                        KLog.i(retValue[1].toString())
                        swapRecord!!.swaptxHash = retValue[1].toString()
                        swapRecord!!.state = 21
                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                        startActivity(Intent(activity, SwapDetailActivity::class.java).putExtra("swapRecord", swapRecord))
                        runOnUiThread {
                            closeProgressDialog()
                            swapListAdapter.notifyItemChanged(swapRecord!!.index)
                        }

                    }

                    override fun onValue(retValue: Any?) {
                        this.onValue(retValue as JSONArray)
                    }
                })
            } catch (e :Exception) {
                e.printStackTrace()
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

    /**
     * 抵押erc20成功，准备领取nep5 qlc
     */
    fun userUnlocNep5Qlc() {
        KLog.i("开始claim")
        try {
            thread {
                try {
                    nep5unLockNotice()
//                    var wallet = AppConfig.instance.daoSession.walletDao.queryBuilder().where(WalletDao.Properties.Address.eq(swapRecord!!.toAddress)).list()[0]
//                    var txid = Test1Contract.userUnlock(neow3j, swapRecord!!.rOrign, swapRecord!!.toAddress, wallet.wif, swapRecord!!.neoContractAddress)
//                    if ("".equals(txid)) {
//                        runOnUiThread {
//                            toast(getString(R.string.please_retry))
//                        }
//                        return@thread
//                    } else {
//                        swapRecord!!.swaptxHash = txid
//                        AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
//                        checkNep5Transaction()
//                        runOnUiThread {
//                        }
//
//                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }

            }
        } catch (e : Exception) {
            e.printStackTrace()
            runOnUiThread {
                toast(e.message!!)
            }
        }
    }

    /**
     * 检查发出的交易是否已经上链
     */
    fun checkNep5Transaction() {
        if (isClose) {
            return
        }
        var log = neow3j.getApplicationLog(swapRecord!!.swaptxHash).send()
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
            nep5unLockNotice()
        }
    }

    fun nep5unLockNotice() {
        if (isClose) {
            return
        }
        val dataJson = jsonObject(
                "rOrigin" to swapRecord!!.rOrign,
                "userNep5Addr" to swapRecord!!.toAddress
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
                runOnUiThread {
                    swapListAdapter.notifyItemChanged(swapRecord!!.index)
                }
                Thread.sleep(checkStatusTime)
                checkLcokState()
            } else {
                Thread.sleep(checkStatusTime)
                nep5unLockNotice()
            }
        }
    }

    fun getSwapHistory() {
        var list = AppConfig.instance.daoSession.swapRecordDao.queryBuilder().where(SwapRecordDao.Properties.FromAddress.eq(currentAddress), SwapRecordDao.Properties.IsMainNet.eq(SpUtil.getBoolean(activity, ConstantValue.isMainNet, true))).list()
        list.sortBy { - it.lockTime }
        swapListAdapter.setNewData(list)
        thread {
            list.forEachIndexed { index, swapRecord ->
                if (swapRecord.type == SwapRecord.SwapType.typeNep5ToErc20.ordinal) {
                    //查找nep5到erc20抵押的状态
                    //
                    if (swapRecord.state != 6) {

                    }
                } else {
                    //查找erc20到nep5抵押的状态
                    if (swapRecord.state != 21 && swapRecord.state != 18) {
                        try {
                            KLog.i("index= " + index)
                            checkLcokState(index, swapRecord)
                        } catch (e :Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    fun checkLcokState(index: Int, swapRecord: SwapRecord) {
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
                var checkSwapHubState = Gson().fromJson<CheckHubState>(data, CheckHubState::class.java)
                swapRecord.state = checkSwapHubState.state.toInt()
                swapRecord.neoTimeout = checkSwapHubState.isNeoTimeout
                swapRecord.ethTimeout = checkSwapHubState.isEthTimeout
                swapRecord.fail = checkSwapHubState.isFail
                AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                runOnUiThread {
                    KLog.i("index= " + index)
                    swapListAdapter.notifyItemChanged(index)
                }
            } else {
                error.exception.printStackTrace()
                if ("HTTP Exception 500 Internal Server Error".equals(error.exception.message)) {
                    swapRecord.state = -1
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        KLog.i("index= " + index)
                        swapListAdapter.notifyItemChanged(index)
                    }
                } else {
                    swapRecord.state = -2
                    AppConfig.instance.daoSession.swapRecordDao.update(swapRecord)
                    runOnUiThread {
                        KLog.i("index= " + index)
                        swapListAdapter.notifyItemChanged(index)
                    }
                }
                KLog.i(error.exception.message)
            }
        }
    }

    object EthSwapRecordFragmentInstance{
        @JvmStatic
        fun getInstance(address: String) : EthSwapRecordFragment{
            var bundle = Bundle()
            bundle.putString("address", address)
            var swapRecordFragment = EthSwapRecordFragment()
            swapRecordFragment.arguments = bundle
            return swapRecordFragment
        }
    }

    override fun setupFragmentComponent() {
        DaggerEthSwapRecordComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .ethSwapRecordModule(EthSwapRecordModule(this))
                .build()
                .inject(this)
    }

    override fun initDataFromNet() {
        getSwapHistory()
    }

    override fun setPresenter(presenter: EthSwapRecordContract.EthSwapRecordContractPresenter) {
        mPresenter = presenter as EthSwapRecordPresenter
    }

    override fun initDataFromLocal() {

    }

    override fun showProgressDialog() {
        isClose = false
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        isClose = true
        progressDialog.hide()
    }
}
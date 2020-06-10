package com.stratagile.qlink.ui.activity.stake

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.stake.component.DaggerTokenMintageComponent
import com.stratagile.qlink.ui.activity.stake.contract.TokenMintageContract
import com.stratagile.qlink.ui.activity.stake.module.TokenMintageModule
import com.stratagile.qlink.ui.activity.stake.presenter.TokenMintagePresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.db.Wallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.stake.LockResult
import com.stratagile.qlink.entity.stake.NeoBlock
import com.stratagile.qlink.entity.stake.StakeType
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.utils.QlcReceiveUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.fragment_token_mintage.*
import kotlinx.android.synthetic.main.fragment_vote_node.*
import kotlinx.android.synthetic.main.fragment_vote_node.etStakeDays
import kotlinx.android.synthetic.main.fragment_vote_node.etStakeQlcAmount
import kotlinx.android.synthetic.main.fragment_vote_node.invoke
import kotlinx.android.synthetic.main.fragment_vote_node.llSelectNeoWallet
import kotlinx.android.synthetic.main.fragment_vote_node.llSelectQlcWallet
import kotlinx.android.synthetic.main.fragment_vote_node.tvNeoWalletAddess
import kotlinx.android.synthetic.main.fragment_vote_node.tvNeoWalletName
import kotlinx.android.synthetic.main.fragment_vote_node.tvQLCWalletAddess
import kotlinx.android.synthetic.main.fragment_vote_node.tvQLCWalletName
import kotlinx.android.synthetic.main.fragment_vote_node.tvQlcBalance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import qlc.bean.StateBlock
import qlc.mng.BlockMng
import qlc.mng.WalletMng
import qlc.network.QlcClient
import qlc.utils.Helper
import java.util.HashMap
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/08 16:38:20
 */

class TokenMintageFragment : BaseFragment(), TokenMintageContract.View {
    override fun initDataFromNet() {

    }
    override fun setNeoDetail(neoWalletInfo: NeoWalletInfo) {
        closeProgressDialog()
        this.neoWalletInfo = neoWalletInfo
        neoWalletInfo.data.balance.forEach {
            if (it.asset_symbol.equals("QLC")) {
                neoQlcTokenInfo = it
                tvQlcBalance.text = getString(R.string.balance) + ": ${it.amount} QLC"
            }
        }
    }

    @Inject
    lateinit internal var mPresenter: TokenMintagePresenter
    var neoWallet: Wallet? = null
    var qlcWallet: QLCAccount? = null
    var neoWalletInfo: NeoWalletInfo? = null
    var neoQlcTokenInfo: NeoWalletInfo.DataBean.BalanceBean? = null
    lateinit var stakeViewModel: NewStakeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_token_mintage, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvQlcBalance.text = getString(R.string.balance) + ": -/- QLC"
        stakeViewModel = ViewModelProviders.of(activity!!).get<NewStakeViewModel>(NewStakeViewModel::class.java)
        stakeViewModel.lockResult.observe(this, Observer<LockResult> {
            if (it!!.stakeType.type == 2) {
                getNep5Txid(it)
            }
        })
        invoke.isEnabled = false
        invoke.setOnClickListener {
            if (neoWallet == null) {
                return@setOnClickListener
            }
            if (qlcWallet == null) {
                return@setOnClickListener
            }
            if (neoQlcTokenInfo == null) {
                val infoMap = HashMap<String, String>()
                infoMap["address"] = neoWallet!!.address
                mPresenter.getNeoWalletDetail(infoMap, neoWallet!!.address)
                toast(getString(R.string.pleasewait))
                return@setOnClickListener
            }
            if ("".equals(etStakeQlcAmount.text.toString().trim())) {
                return@setOnClickListener
            }
            if ("".equals(etStakeDays.text.toString().trim())) {
                return@setOnClickListener
            }
            if (etStakeQlcAmount.text.toString().trim().toInt() > neoQlcTokenInfo!!.amount) {
                toast(getString(R.string.no_enough) + " QLC")
                return@setOnClickListener
            }
            if ("".equals(etTokenSymbol.text.toString().trim())) {
                return@setOnClickListener
            }
            if ("".equals(etTokenSupply.text.toString().trim())) {
                return@setOnClickListener
            }
            if ("".equals(etTokenName.text.toString().trim())) {
                return@setOnClickListener
            }
            if ("".equals(etTokenDecimals.text.toString().trim())) {
                return@setOnClickListener
            }
            showStakingPup()
            var stakeQLcAmount = etStakeQlcAmount.text.toString().trim()
            var stakeQlcDays = etStakeDays.text.toString().trim()
            var priKey = neoWallet!!.privateKey
            var fromAddress = neoWallet!!.address
            var qlcchainAddress = qlcWallet!!.address
            var neoPubKey = neoWallet!!.publicKey

            var stakeType = StakeType(2)
            stakeType.fromNeoAddress = fromAddress
            stakeType.neoPriKey = priKey
            stakeType.neoPubKey = neoPubKey
            stakeType.qlcchainAddress = qlcchainAddress
            stakeType.stakeQLcAmount = stakeQLcAmount
            stakeType.stakeQlcDays = stakeQlcDays
            EventBus.getDefault().post(stakeType)
        }


        llSelectNeoWallet.setOnClickListener {
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.NeoWallet.ordinal)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        etTokenDecimals.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if ("".equals(p0!!.toString())) {
                    return
                }
                if (p0!!.toString().toInt() >= 0) {
                    if (etStakeQlcAmount.text.toString().trim().toInt() >= 1 && neoWallet != null && qlcWallet != null && !"".equals(etTokenSymbol.text.toString().trim()) && !"".equals(etTokenName.text.toString().trim()) && !"".equals(etTokenSupply.text.toString().trim())) {
                        invoke.background = resources.getDrawable(R.drawable.main_color_bt_bg)
                        invoke.isEnabled = true
                    } else {
                        invoke.background = resources.getDrawable(R.drawable.unable_bt_bg)
                        invoke.isEnabled = false
                    }
                } else {
                    invoke.background = resources.getDrawable(R.drawable.unable_bt_bg)
                    invoke.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    var sweetAlertDialog: SweetAlertDialog? = null

    fun showStakingPup() {
        val view = layoutInflater.inflate(R.layout.alert_staking, null, false)
        sweetAlertDialog = SweetAlertDialog(activity)
        sweetAlertDialog!!.setView(view)
        sweetAlertDialog!!.show()
    }

    fun getNep5Txid(lockResult: LockResult) {
        thread {
            val dataJson = jsonObject(
                    "jsonrpc" to "2.0",
                    "method" to "getnep5transferbytxid",
                    "params" to jsonArray(lockResult.txid),
                    "id" to 3
            )
            var request = "https://api.nel.group/api/mainnet".httpPost().body(dataJson.toString())
            request.headers["Content-Type"] = "application/json"
            request.responseString { _, _, result ->
                val (data, error) = result
                if (error == null) {
                    val gson = Gson()
                    KLog.i(data!!)
                    val nodeResponse = gson.fromJson<NeoBlock>(data, object : TypeToken<NeoBlock>() {}.type)
                    KLog.i(nodeResponse.toString())
                    if (nodeResponse.error == null && nodeResponse.result != null) {
                        prePareMintagePledge(lockResult = lockResult)
                    } else {
                        launch {
                            delay(3000)
                            getNep5Txid(lockResult)
                        }
                    }
                } else {
                    launch {
                        delay(3000)
                        getNep5Txid(lockResult)
                    }
                    KLog.i(error.localizedMessage)
                }
            }
        }
    }

    fun prePareMintagePledge(lockResult: LockResult) {
        val client = QlcClient("https://nep5.qlcchain.online")
        val params = JSONArray()
        val paramOne = JSONObject()
        paramOne["beneficial"] = lockResult.stakeType.qlcchainAddress
//        paramOne["amount"] = lockResult.stakeType.stakeQLcAmount.toBigDecimal().multiply(10.toBigDecimal().pow(8)).stripTrailingZeros().toPlainString()
//        paramOne["pType"] = "vote"
        paramOne["tokenName"] = etTokenName.text.toString()
        paramOne["tokenSymbol"] = etTokenSymbol.text.toString()
        paramOne["totalSupply"] = etTokenSupply.text.toString()
        paramOne["decimals"] = etTokenDecimals.text.toString().toInt()
        params.add(paramOne)

        val paramTwo = JSONObject()
        paramTwo["multiSigAddress"] = lockResult.stakeType.toAddress
        paramTwo["publicKey"] = lockResult.stakeType.neoPubKey
        paramTwo["lockTxId"] = lockResult.txid
        params.add(paramTwo)

        val result = client.call("nep5_prePareMintagePledge", params)
        KLog.i(result)
        mintagePledge(lockResult = lockResult)
    }

    fun mintagePledge(lockResult: LockResult) {
        val client = QlcClient("https://nep5.qlcchain.online")
        val params = JSONArray()
        params.add(lockResult.txid)
        var result = client.call("nep5_mintagePledge", params)
        KLog.i(result.toString())
        var stateBlock = Gson().fromJson(result.getJSONObject("result").toString(), StateBlock::class.java)

        var root = BlockMng.getRoot(stateBlock)
        var params1 = mutableListOf<Pair<String, String>>()
        params1.add(Pair("root", root))
        var request = "http://pow1.qlcchain.org/work".httpGet(params1)
        request.responseString { _, _, result ->
            KLog.i("远程work返回、、")
            val (data, error) = result
            try {
                if (error == null) {
                    stateBlock.work = data
                    var hash = BlockMng.getHash(stateBlock)
                    var signature = WalletMng.sign(hash, Helper.hexStringToBytes(QlcReceiveUtils.drivePrivateKey(lockResult.stakeType.qlcchainAddress).substring(0, 64)))
                    val signCheck = WalletMng.verify(signature, hash, Helper.hexStringToBytes(QlcReceiveUtils.drivePublicKey(lockResult.stakeType.qlcchainAddress)))
                    if (!signCheck) {
                        KLog.i("签名验证失败")
                        return@responseString
                    }
                    stateBlock.setSignature(Helper.byteToHexString(signature))
                    lockResult.stateBlock = stateBlock
                    process(lockResult)
                } else {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun process(lockResult: LockResult) {
        val client = QlcClient("https://nep5.qlcchain.online")
        val params = JSONArray()
        params.add(JSONObject.parse(Gson().toJson(lockResult.stateBlock)))
        params.add(lockResult.txid)

        println(params.toJSONString())

        val result = client.call("ledger_process", params)
        println(result)
        runOnUiThread {
            sweetAlertDialog?.dismissWithAnimation()
            launch {
                delay(300)
                activity!!.setResult(Activity.RESULT_OK)
                activity!!.finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AllWallet.WalletType.NeoWallet.ordinal -> {
                    neoWallet = data!!.getParcelableExtra("wallet")
                    tvNeoWalletName.text = neoWallet!!.name
                    tvNeoWalletAddess.text = neoWallet!!.address
                    val infoMap = HashMap<String, String>()
                    infoMap["address"] = neoWallet!!.address
                    mPresenter.getNeoWalletDetail(infoMap, neoWallet!!.address)
                }
                AllWallet.WalletType.QlcWallet.ordinal -> {
                    qlcWallet = data!!.getParcelableExtra("wallet")
                    tvQLCWalletName.text = qlcWallet!!.accountName
                    tvQLCWalletAddess.text = qlcWallet!!.address
                }
            }
        }
    }


    override fun setupFragmentComponent() {
        DaggerTokenMintageComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .tokenMintageModule(TokenMintageModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TokenMintageContract.TokenMintageContractPresenter) {
        mPresenter = presenter as TokenMintagePresenter
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
package com.stratagile.qlink.ui.activity.qlc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout

import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.TokenInfo
import com.stratagile.qlink.ui.activity.qlc.component.DaggerQlcTransferComponent
import com.stratagile.qlink.ui.activity.qlc.contract.QlcTransferContract
import com.stratagile.qlink.ui.activity.qlc.module.QlcTransferModule
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcTransferPresenter
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity
import com.stratagile.qlink.utils.PopWindowUtil
import com.stratagile.qlink.utils.SpringAnimationUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.view.CustomPopWindow

import java.math.BigDecimal
import java.util.ArrayList

import javax.inject.Inject

import com.alibaba.fastjson.JSONArray
import com.google.gson.Gson
import com.pawegio.kandroid.toast
import com.stratagile.qlc.entity.AccountInfo
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.eventbus.ChangeWallet
import com.stratagile.qlink.utils.DecimalDigitsInputFilter
import kotlinx.android.synthetic.main.activity_qlc_transfer.*
import org.greenrobot.eventbus.EventBus
import qlc.mng.*
import qlc.network.QlcClient
import qlc.network.QlcException
import qlc.rpc.impl.LedgerRpc
import qlc.utils.Helper
import java.nio.charset.Charset
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/05/20 18:05:32
 */

class QlcTransferActivity : BaseActivity(), QlcTransferContract.View {

    @Inject
    internal lateinit var mPresenter: QlcTransferPresenter

    private var tokenInfo: TokenInfo? = null

    private var tokenInfoArrayList: ArrayList<TokenInfo>? = null

    private var list: ArrayList<String>? = null

    private var isChanged = false//申明一个flag 来判断是否已经改变


    override fun initView() {
        setContentView(R.layout.activity_qlc_transfer)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setupActivityComponent() {
        DaggerQlcTransferComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .qlcTransferModule(QlcTransferModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: QlcTransferContract.QlcTransferContractPresenter) {
        mPresenter = presenter as QlcTransferPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initData() {
        KLog.i(getMemoHash(Helper.byteToHexString("哈哈哈哈".toByteArray())))
        list = ArrayList()
        tokenInfoArrayList = intent.getParcelableArrayListExtra("tokens")
        if (intent.hasExtra("tokenInfo")) {
            tokenInfo = intent.getParcelableExtra("tokenInfo")
        } else {
            tokenInfo = tokenInfoArrayList!![0]
        }
        if (intent.hasExtra("walletAddress")) {
            etQlcTokenSendAddress!!.setText(intent.getStringExtra("walletAddress"))
        }
        for (i in tokenInfoArrayList!!.indices) {
            list!!.add(tokenInfoArrayList!![i].tokenSymol)

        }
        setTitle("Send " + tokenInfo!!.tokenSymol)
        tvQlcTokenName!!.text = tokenInfo!!.tokenSymol
        tvQlcTokenValue!!.text = "Balance: " + BigDecimal.valueOf(tokenInfo!!.tokenValue).divide(BigDecimal.TEN.pow(tokenInfo!!.getTokenDecimals()), tokenInfo!!.getTokenDecimals(), BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString() + ""

        etQlcTokenSendValue.filters = arrayOf(DecimalDigitsInputFilter(tokenInfo!!.tokenDecimals))

        etQlcTokenSendValue!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if ("" == charSequence.toString()) {
                    return
                }
                if ("." == charSequence.toString()) {
                    etQlcTokenSendValue!!.setText("")
                    return
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                try {
                    if (isChanged) {//必须在修改内容前调用
                        return
                    }
                    if (editable != null && "" != editable.toString() && "." != editable.toString()) {
                        val toSendQlcCount = editable.toString().toBigDecimal()
                        KLog.i(toSendQlcCount)
                        if (toSendQlcCount.multiply(BigDecimal.TEN.pow(tokenInfo!!.getTokenDecimals())) > BigDecimal.valueOf(tokenInfo!!.tokenValue)) {
                            etQlcTokenSendValue!!.setText(BigDecimal.valueOf(tokenInfo!!.tokenValue).divide(BigDecimal.TEN.pow(tokenInfo!!.getTokenDecimals()), tokenInfo!!.getTokenDecimals(), BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString())
                            isChanged = true
                            etQlcTokenSendValue!!.setSelection(BigDecimal.valueOf(tokenInfo!!.tokenValue).divide(BigDecimal.TEN.pow(tokenInfo!!.getTokenDecimals()), tokenInfo!!.getTokenDecimals(), BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString().length)
                            KLog.i(etQlcTokenSendValue.text.toString())
                        } else {
                            KLog.i("进入else")
                            etQlcTokenSendValue!!.setSelection(editable.toString().length)
                        }
                        isChanged = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isChanged = true
                    etQlcTokenSendValue!!.setText("")
                    isChanged = false
                }

            }
        })
        tvQlcTokenName!!.post {
            val layoutParams = LinearLayout.LayoutParams(tvQlcTokenName!!.width, resources.getDimension(R.dimen.x1).toInt())
            viewLine!!.layoutParams = layoutParams
        }
        tvSend.setOnClickListener {
            if ("" == etQlcTokenSendValue!!.text.toString().trim { it <= ' ' }) {
                ToastUtil.displayShortToast("illegal value")
                return@setOnClickListener
            }
            if (java.lang.Float.parseFloat(etQlcTokenSendValue!!.text.toString()) <= 0) {
                ToastUtil.displayShortToast("illegal value")
                return@setOnClickListener
            }
            if ("" == etQlcTokenSendAddress!!.text.toString().trim { it <= ' ' }) {
                ToastUtil.displayShortToast("please enter Qlc wallet address")
                return@setOnClickListener
            }
            if (!AccountMng.isValidAddress(etQlcTokenSendAddress!!.text.toString().trim { it <= ' ' })) {
                ToastUtil.displayShortToast("please enter Qlc wallet address")
                return@setOnClickListener
            }
            if (tokenInfo!!.walletAddress == etQlcTokenSendAddress!!.text.toString()) {
                ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self))
                return@setOnClickListener
            }
            showProgressDialog()
            getAccountInfo()
        }
        tvQlcTokenName.setOnClickListener {
            showSpinnerPopWindow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.qrcode_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.qrcode) {
            startActivityForResult(Intent(this, ScanQrCodeActivity::class.java), 1)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            etQlcTokenSendAddress!!.setText(data?.getStringExtra("result"))
        }
    }

    var accountInfo: AccountInfo? = null
    fun getAccountInfo() {
        QLCAPI().ledgerAccountInfo(tokenInfo!!.walletAddress) {
            KLog.i(it.first!!.result.toString())
            var accountInfo = Gson().fromJson<AccountInfo>(it.first!!.result.toString(), AccountInfo::class.java)
            this.accountInfo = accountInfo
            generateSendBlock()
        }
    }

    fun getMemoHash(hexStr : String) : String{
        var string = StringBuilder()
        if (hexStr.length < 64) {
            for (i in 0.. 64 - hexStr.length - 1) {
               string.append("0")
            }
        }
        return (string.toString() + hexStr)
    }

    private fun generateSendBlock() {
        thread {
            accountInfo!!.tokens.forEach {
                if (it.tokenName.equals(tokenInfo!!.tokenName)) {
                    if (BigDecimal.valueOf(it.balance.toDouble()) >= BigDecimal.TEN.pow(tokenInfo!!.tokenDecimals).multiply(BigDecimal(etQlcTokenSendValue.text.toString().trim())) ) {
                        val qlcClient = QlcClient(ConstantValue.qlcNode)
                        val rpc = LedgerRpc(qlcClient)
                        var jsonObject = TransactionMng.sendBlock(qlcClient, tokenInfo!!.walletAddress, tokenInfo!!.tokenSymol, etQlcTokenSendAddress.text.toString().trim(), BigDecimal(etQlcTokenSendValue.text.toString().trim()).multiply(BigDecimal.TEN.pow(tokenInfo!!.tokenDecimals)).toBigInteger(), null, null, if (!"".equals(etEthTokenSendMemo.text.toString().trim())) {getMemoHash(Helper.byteToHexString(etEthTokenSendMemo.text.toString().trim().toByteArray()))} else {null}, Helper.hexStringToBytes(drivePrivateKey(tokenInfo!!.walletAddress)))
                        var aaaa = JSONArray()
                        aaaa.add(jsonObject)
                        KLog.i(aaaa)
                        try {
                            var result = rpc.process (aaaa)
                            KLog.i(result.toJSONString())
                            if (result.getString("result") != null && !"".equals(result.getString("result"))) {
                                runOnUiThread {
                                    toast("success")
                                }
                                EventBus.getDefault().post(ChangeWallet())
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                            closeProgressDialog()
                        } catch (e : QlcException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
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

    private fun showSpinnerPopWindow() {
        PopWindowUtil.showSharePopWindow(this, ivArrow!!, list!!, object : PopWindowUtil.OnItemSelectListener {
            override fun onSelect(content: String) {
                if ("" != content) {
                    for (i in tokenInfoArrayList!!.indices) {
                        if (tokenInfoArrayList!![i].tokenSymol == content) {
                            tokenInfo = tokenInfoArrayList!![i]
                            setTitle("Send " + tokenInfo!!.tokenSymol)
                            tvQlcTokenName!!.text = tokenInfo!!.tokenSymol
                            etQlcTokenSendValue.filters = arrayOf(DecimalDigitsInputFilter(tokenInfo!!.tokenDecimals))
                            etQlcTokenSendValue!!.setText("")
                            tvQlcTokenValue!!.text = "Balance: " + BigDecimal.valueOf(tokenInfo!!.tokenValue)
                            val layoutParams = LinearLayout.LayoutParams(tvQlcTokenName!!.width, resources.getDimension(R.dimen.x1).toInt())
                            viewLine!!.layoutParams = layoutParams
                        }
                    }
                }
                SpringAnimationUtil.endRotatoSpringViewAnimation(ivArrow) { animation, canceled, value, velocity -> }
            }
        })
        SpringAnimationUtil.startRotatoSpringViewAnimation(ivArrow) { animation, canceled, value, velocity -> }
    }

    override fun onBackPressed() {
        if (CustomPopWindow.onBackPressed()) {
            SpringAnimationUtil.endRotatoSpringViewAnimation(ivArrow) { animation, canceled, value, velocity -> }
        } else {
            super.onBackPressed()
        }
    }
}
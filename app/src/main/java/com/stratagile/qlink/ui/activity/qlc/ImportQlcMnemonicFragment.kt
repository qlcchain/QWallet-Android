package com.stratagile.qlink.ui.activity.qlc

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.qlc.component.DaggerImportQlcMnemonicComponent
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcMnemonicContract
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcMnemonicModule
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcMnemonicPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.utils.QlcUtil
import com.stratagile.qlink.R
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.ui.activity.eth.ImportViewModel
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_import_qlc_mnemonic.*
import qlc.mng.AccountMng
import qlc.network.QlcException
import qlc.rpc.AccountRpc
import qlc.utils.Helper

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/06/06 10:37:31
 */

class ImportQlcMnemonicFragment : BaseFragment(), ImportQlcMnemonicContract.View {

    @Inject
    lateinit internal var mPresenter: ImportQlcMnemonicPresenter
    private var viewModel: ImportViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_import_qlc_mnemonic, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        viewModel = ViewModelProviders.of(activity!!).get(ImportViewModel::class.java)
        viewModel!!.qrCode.observe(this, Observer<String> { s ->
            if (userVisibleHint) {
                etMnemonic.setText(s)
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btImport.setOnClickListener {
            if ("" == etMnemonic.getText().toString().trim({ it <= ' ' })) {
                ToastUtil.displayShortToast(getString(R.string.please_type_mnemonic))
                return@setOnClickListener
            }

            showProgressDialog()
            try {
                var jsonArray = JSONArray()
                jsonArray.add(etMnemonic.text.toString().trim())
                var seed = AccountRpc.mnemonicsToSeed(jsonArray)
                var jsonObject: JSONObject? = null
                jsonObject = AccountMng.keyPairFromSeed(Helper.hexStringToBytes(seed), 0)
                val priKey = jsonObject!!.getString("privKey")
                val pubKey = jsonObject.getString("pubKey")
                val address = QlcUtil.publicToAddress(pubKey).toLowerCase()
                val qlcAccount = QLCAccount()
                qlcAccount.privKey = priKey.toLowerCase()
                qlcAccount.pubKey = pubKey
                qlcAccount.address = address
                qlcAccount.mnemonic = etMnemonic.text.toString().trim()
                qlcAccount.setIsCurrent(true)
                qlcAccount.seed = seed
                qlcAccount.accountName = QLCAPI.getQlcWalletName()
                val wallets3 = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
                if (wallets3!!.size == 0) {
                    qlcAccount.isAccountSeed = true
                }

                if (wallets3 != null && wallets3.size != 0) {
                    for (i in wallets3.indices) {
                        if (wallets3[i].address.toLowerCase() == qlcAccount.address) {
                            ToastUtil.displayShortToast(getString(R.string.wallet_exist))
                            closeProgressDialog()
                            return@setOnClickListener
                        }
                    }
                }
                AppConfig.instance.daoSession.qlcAccountDao.insert(qlcAccount)
                closeProgressDialog()
                viewModel!!.walletAddress.postValue(qlcAccount.address)
            } catch (e: Exception) {
                ToastUtil.displayShortToast(e.message)
                e.printStackTrace()
                closeProgressDialog()
            }

        }
    }


    override fun setupFragmentComponent() {
        DaggerImportQlcMnemonicComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .importQlcMnemonicModule(ImportQlcMnemonicModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: ImportQlcMnemonicContract.ImportQlcMnemonicContractPresenter) {
        mPresenter = presenter as ImportQlcMnemonicPresenter
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
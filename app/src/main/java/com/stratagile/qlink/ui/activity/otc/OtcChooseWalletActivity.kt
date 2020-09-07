package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.socks.library.KLog
import com.stratagile.qlink.Account
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.EthWallet
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.ui.activity.otc.component.DaggerOtcChooseWalletComponent
import com.stratagile.qlink.ui.activity.otc.contract.OtcChooseWalletContract
import com.stratagile.qlink.ui.activity.otc.module.OtcChooseWalletModule
import com.stratagile.qlink.ui.activity.otc.presenter.OtcChooseWalletPresenter
import com.stratagile.qlink.view.SelectWalletAdapter
import kotlinx.android.synthetic.main.activity_choose_wallet.*
import java.util.ArrayList

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/29 11:54:14
 */

class OtcChooseWalletActivity : BaseActivity(), OtcChooseWalletContract.View {

    @Inject
    internal lateinit var mPresenter: OtcChooseWalletPresenter
    internal var allWallets = ArrayList<AllWallet>()
    lateinit var downCheckAdapter: SelectWalletAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_choose_wallet)
        title.text = getString(R.string.choose_wallet)
    }
    override fun initData() {
        KLog.i("    " + intent.getIntExtra("walletType", 0))
        when(intent.getIntExtra("walletType", 0)){
            AllWallet.WalletType.NeoWallet.ordinal -> {
                val neoWallets = AppConfig.getInstance().daoSession.walletDao.loadAll()
                if (neoWallets.size != 0) {
                    for (i in neoWallets.indices) {

                        if (neoWallets[i].privateKey.equals(neoWallets[i].publicKey, ignoreCase = true)) {
                            Account.fromWIF(neoWallets[i].wif)
//                            neoWallets[i].publicKey = Numeric.toHexStringWithPrefix(Account.getWallet()!!.publicKey)
                            neoWallets[i].publicKey = Account.byteArray2String(Account.getWallet()!!.publicKey).toLowerCase()
                            AppConfig.getInstance().daoSession.walletDao.update(neoWallets[i])
                        }

                        val allWallet = AllWallet()
                        allWallet.wallet = neoWallets[i]
                        allWallet.walletAddress = neoWallets[i].address
                        allWallet.walletType = AllWallet.WalletType.NeoWallet
                        if (neoWallets[i].name == null || "" == neoWallets[i].name) {
                            allWallet.walletName = neoWallets[i].address
                        } else {
                            allWallet.walletName = neoWallets[i].name
                        }
                        allWallets.add(allWallet)
                    }
                }
            }
            AllWallet.WalletType.EthWallet.ordinal -> {
                val ethWallets = AppConfig.getInstance().daoSession.ethWalletDao.loadAll()
                if (ethWallets.size != 0) {
                    for (i in ethWallets.indices) {
                        val allWallet = AllWallet()
                        allWallet.ethWallet = ethWallets[i]
                        allWallet.walletType = AllWallet.WalletType.EthWallet
                        allWallet.walletAddress = ethWallets[i].address
                        allWallet.walletName = ethWallets[i].name
                        allWallets.add(allWallet)
                    }
                }
            }
            AllWallet.WalletType.EosWallet.ordinal -> {
                val wallets2 = AppConfig.getInstance().daoSession.eosAccountDao.loadAll()
                if (wallets2 != null && wallets2.size != 0) {
                    for (i in wallets2.indices) {
                        if (wallets2[i].accountName == null) {
                            continue
                        }
                        if (wallets2[i].isCreating) {
                            continue
                        }
                        val allWallet = AllWallet()
                        allWallet.eosAccount = wallets2[i]
                        allWallet.walletAddress = wallets2[i].accountName
                        allWallet.walletName = if (wallets2[i].walletName == null) wallets2[i].accountName else wallets2[i].walletName
                        allWallet.walletType = AllWallet.WalletType.EosWallet
                        allWallets.add(allWallet)
                    }
                }
            }
            AllWallet.WalletType.QlcWallet.ordinal -> {
                val qlcAccounts = AppConfig.getInstance().daoSession.qlcAccountDao.loadAll()
                if (qlcAccounts != null && qlcAccounts.size != 0) {
                    for (i in qlcAccounts.indices) {
                        if (qlcAccounts[i].accountName == null) {
                            continue
                        }
                        val allWallet = AllWallet()
                        allWallet.qlcAccount = qlcAccounts[i]
                        allWallet.walletAddress = qlcAccounts[i].address
                        allWallet.walletName = if (qlcAccounts[i].accountName == null) qlcAccounts[i].accountName else qlcAccounts[i].accountName
                        allWallet.walletType = AllWallet.WalletType.QlcWallet
                        allWallets.add(allWallet)
                    }
                }
            }
        }
        downCheckAdapter = SelectWalletAdapter(allWallets)
        recyclerView.setAdapter(downCheckAdapter)
        downCheckAdapter.setOnItemClickListener { adapter, view, position ->
            when(intent.getIntExtra("walletType", 0)){
                AllWallet.WalletType.EthWallet.ordinal -> {
                   var returnItent  = Intent()
                    returnItent.putExtra("wallet", downCheckAdapter.data[position].ethWallet)
                    setResult(Activity.RESULT_OK, returnItent)
                    finish()
                }
                AllWallet.WalletType.QlcWallet.ordinal -> {
                   var returnItent  = Intent()
                    returnItent.putExtra("wallet", downCheckAdapter.data[position].qlcAccount)
                    setResult(Activity.RESULT_OK, returnItent)
                    finish()
                }
                AllWallet.WalletType.NeoWallet.ordinal -> {
                   var returnItent  = Intent()
                    returnItent.putExtra("wallet", downCheckAdapter.data[position].wallet)
                    setResult(Activity.RESULT_OK, returnItent)
                    finish()
                }
                AllWallet.WalletType.EosWallet.ordinal -> {
                   var returnItent  = Intent()
                    returnItent.putExtra("wallet", downCheckAdapter.data[position].eosAccount)
                    setResult(Activity.RESULT_OK, returnItent)
                    finish()
                }
            }
        }
    }

    override fun setupActivityComponent() {
       DaggerOtcChooseWalletComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .otcChooseWalletModule(OtcChooseWalletModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OtcChooseWalletContract.OtcChooseWalletContractPresenter) {
            mPresenter = presenter as OtcChooseWalletPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
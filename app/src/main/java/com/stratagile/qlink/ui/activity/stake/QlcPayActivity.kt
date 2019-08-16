package com.stratagile.qlink.ui.activity.stake

import android.content.Intent
import android.os.Bundle
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.stake.component.DaggerQlcPayComponent
import com.stratagile.qlink.ui.activity.stake.contract.QlcPayContract
import com.stratagile.qlink.ui.activity.stake.module.QlcPayModule
import com.stratagile.qlink.ui.activity.stake.presenter.QlcPayPresenter
import kotlinx.android.synthetic.main.activity_qlc_pay.*
import kotlinx.android.synthetic.main.activity_usdt_pay.*
import java.util.HashMap

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: $description
 * @date 2019/08/16 09:59:21
 */

class QlcPayActivity : BaseActivity(), QlcPayContract.View {

    @Inject
    internal lateinit var mPresenter: QlcPayPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_qlc_pay)
    }
    var qlcAccount : QLCAccount? = null
    override fun initData() {
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(this, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }

        var qlcAccounts = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounts.size > 0) {
            qlcAccounts.forEach {
                if (it.isCurrent()) {
                    qlcAccount = it
                    tvQlcWalletName.text = qlcAccount!!.accountName
                    tvQlcWalletAddess.text = qlcAccount!!.address
                    val infoMap = HashMap<String, String>()
                    infoMap["address"] = qlcAccount!!.address
                }
            }
        }
    }

    override fun setupActivityComponent() {
       DaggerQlcPayComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .qlcPayModule(QlcPayModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: QlcPayContract.QlcPayContractPresenter) {
            mPresenter = presenter as QlcPayPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
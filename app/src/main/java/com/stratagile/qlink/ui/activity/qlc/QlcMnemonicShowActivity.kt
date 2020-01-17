package com.stratagile.qlink.ui.activity.qlc

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.ui.activity.eth.EthMnemonicbackupActivity
import com.stratagile.qlink.ui.activity.qlc.component.DaggerQlcMnemonicShowComponent
import com.stratagile.qlink.ui.activity.qlc.contract.QlcMnemonicShowContract
import com.stratagile.qlink.ui.activity.qlc.module.QlcMnemonicShowModule
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcMnemonicShowPresenter
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_qlc_mnemonic_show.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/06/05 18:36:43
 */

class QlcMnemonicShowActivity : BaseActivity(), QlcMnemonicShowContract.View {

    @Inject
    internal lateinit var mPresenter: QlcMnemonicShowPresenter

    private var qlcAccount: QLCAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_qlc_mnemonic_show)
    }
    override fun initData() {
        qlcAccount = intent.getParcelableExtra("wallet")
        tvMnemonic.setText(qlcAccount?.getMnemonic())
        setTitle(getString(R.string.qlc_wallet))
        tvMnemonic.text = qlcAccount?.mnemonic
        tvSeed.text = qlcAccount?.seed
        tvMnemonic.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", tvMnemonic.text.toString())
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
        tvPublicAddress.text = qlcAccount?.address
        tvSeed.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", tvSeed.text.toString())
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }

        tvConfirm.setOnClickListener {
            if (checkBox.isChecked) {
                qlcAccount?.isBackUp = true
                AppConfig.getInstance().daoSession.qlcAccountDao.update(qlcAccount)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun showBackupDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.careful))
        tvContent.text = "The exposure of the mnemonic code will cause the loss of the assets, please copy carefully, do not take a screenshot! "
        val sweetAlertDialog = SweetAlertDialog(this)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)
        val tvOk = view.findViewById<TextView>(R.id.tvOpreate)
        tvOk.text = "OK"
        tvOk.setOnClickListener {
            startActivityForResult(Intent(this@QlcMnemonicShowActivity, QlcMnemonicbackupActivity::class.java).putExtra("wallet", qlcAccount), 0)
            sweetAlertDialog.cancel()
        }
        ivClose.setOnClickListener { sweetAlertDialog.cancel() }
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
    }

    override fun setupActivityComponent() {
       DaggerQlcMnemonicShowComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .qlcMnemonicShowModule(QlcMnemonicShowModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: QlcMnemonicShowContract.QlcMnemonicShowContractPresenter) {
            mPresenter = presenter as QlcMnemonicShowPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.qlc

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.ui.activity.qlc.component.DaggerQlcMnemonicbackupComponent
import com.stratagile.qlink.ui.activity.qlc.contract.QlcMnemonicbackupContract
import com.stratagile.qlink.ui.activity.qlc.module.QlcMnemonicbackupModule
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcMnemonicbackupPresenter
import com.stratagile.qlink.ui.adapter.eth.EthSelectedMnemonicAdapter
import com.stratagile.qlink.ui.adapter.eth.EthUnSelectedMnemonicAdapter
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_qlc_mnemonicbackup.*
import java.util.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/06/05 18:37:03
 */

class QlcMnemonicbackupActivity : BaseActivity(), QlcMnemonicbackupContract.View {

    @Inject
    internal lateinit var mPresenter: QlcMnemonicbackupPresenter

    private var qlcAccount: QLCAccount? = null

    private var ethSelectedMnemonicAdapter: EthSelectedMnemonicAdapter? = null
    private var ethUnSelectedMnemonicAdapter: EthUnSelectedMnemonicAdapter? = null
    private val mnemonicList = ArrayList<String>()
    private val selectedMnemonicList = ArrayList<String>()
    private val unSelectedMnemonicList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_qlc_mnemonicbackup)
    }
    override fun initData() {
        qlcAccount = intent.getParcelableExtra("wallet")
        setTitle(getString(R.string.backup_mnemonic))
        val words = qlcAccount!!.getMnemonic().split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (i in words.indices) {
            mnemonicList.add(words[i])
        }
        unSelectedMnemonicList.addAll(mnemonicList)
        Collections.shuffle(unSelectedMnemonicList)
        ethSelectedMnemonicAdapter = EthSelectedMnemonicAdapter(selectedMnemonicList)
        ethUnSelectedMnemonicAdapter = EthUnSelectedMnemonicAdapter(unSelectedMnemonicList)
        val flexboxLayoutManager1 = FlexboxLayoutManager(this)
        val flexboxLayoutManager2 = FlexboxLayoutManager(this)
        recyclerView1.setLayoutManager(flexboxLayoutManager1)
        recyclerView2.setLayoutManager(flexboxLayoutManager2)
        recyclerView1.setAdapter(ethSelectedMnemonicAdapter)
        recyclerView2.setAdapter(ethUnSelectedMnemonicAdapter)
        ethUnSelectedMnemonicAdapter!!.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            ethSelectedMnemonicAdapter!!.getData().add(ethUnSelectedMnemonicAdapter!!.getData()[position])
            ethUnSelectedMnemonicAdapter!!.getData().removeAt(position)

            ethUnSelectedMnemonicAdapter!!.notifyDataSetChanged()
            ethSelectedMnemonicAdapter!!.notifyDataSetChanged()
        })
        ethSelectedMnemonicAdapter!!.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            ethUnSelectedMnemonicAdapter!!.getData().add(ethSelectedMnemonicAdapter!!.getData()[position])
            ethSelectedMnemonicAdapter!!.getData().removeAt(position)

            ethUnSelectedMnemonicAdapter!!.notifyDataSetChanged()
            ethSelectedMnemonicAdapter!!.notifyDataSetChanged()
        })
        btBackup.setOnClickListener {
            if (selectedMnemonicList.size != mnemonicList.size) {
                ToastUtil.displayShortToast(getString(R.string.please_select_all_mnemonic))
                return@setOnClickListener
            }
            for (i in mnemonicList.indices) {
                if (mnemonicList[i] != selectedMnemonicList[i]) {
                    ToastUtil.displayShortToast(getString(R.string.incorrect_sequence))
                    return@setOnClickListener
                }
            }
            qlcAccount!!.isBackUp = true
            AppConfig.getInstance().daoSession.qlcAccountDao.update(qlcAccount)
            showSuccessDialog()
        }
    }

    private fun showSuccessDialog() {
        val view = layoutInflater.inflate(R.layout.alert_dialog_tip, null, false)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val imageView = view.findViewById<ImageView>(R.id.ivTitle)
        imageView.setImageDrawable(resources.getDrawable(R.mipmap.op_success))
        tvContent.text = getString(R.string.backup_success)
        val sweetAlertDialog = SweetAlertDialog(this)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        btBackup.postDelayed({
            sweetAlertDialog.cancel()
            btBackup.postDelayed({
                setResult(Activity.RESULT_OK)
                finish()
            }, 200)
        }, 2000)
    }

    override fun setupActivityComponent() {
       DaggerQlcMnemonicbackupComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .qlcMnemonicbackupModule(QlcMnemonicbackupModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: QlcMnemonicbackupContract.QlcMnemonicbackupContractPresenter) {
            mPresenter = presenter as QlcMnemonicbackupPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.otc.component.DaggerOrderBuyComponent
import com.stratagile.qlink.ui.activity.otc.contract.OrderBuyContract
import com.stratagile.qlink.ui.activity.otc.module.OrderBuyModule
import com.stratagile.qlink.ui.activity.otc.presenter.OrderBuyPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.utils.PopWindowUtil
import com.stratagile.qlink.utils.SpringAnimationUtil
import com.stratagile.qlink.utils.UserUtils
import com.stratagile.qlink.view.SweetAlertDialog
import kotlinx.android.synthetic.main.fragment_order_buy.*
import kotlinx.android.synthetic.main.fragment_order_buy.etAmount
import kotlinx.android.synthetic.main.fragment_order_buy.etMaxAmount
import kotlinx.android.synthetic.main.fragment_order_buy.etMinAmount
import kotlinx.android.synthetic.main.fragment_order_buy.etUnitPrice
import kotlinx.android.synthetic.main.fragment_order_buy.llSelectQlcWallet
import kotlinx.android.synthetic.main.fragment_order_buy.tvNext
import kotlinx.android.synthetic.main.fragment_order_buy.tvQLCWalletAddess
import kotlinx.android.synthetic.main.fragment_order_buy.tvQLCWalletName
import kotlinx.android.synthetic.main.fragment_order_sell.*
import qlc.mng.AccountMng
import qlc.mng.WalletMng

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/08 17:24:58
 */

class OrderBuyFragment : BaseFragment(), OrderBuyContract.View {
    override fun generateBuyQgasOrderSuccess() {
        toast("success")
        closeProgressDialog()
        activity?.finish()
    }

    @Inject
    lateinit internal var mPresenter: OrderBuyPresenter

    var receiveQgasWallet : QLCAccount? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_buy, null)
        ButterKnife.bind(this, view)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCreateWallet.setOnClickListener {
            startActivity(Intent(activity, SelectWalletTypeActivity::class.java))
        }
        tvNext.setOnClickListener {
            if ("".equals(etMinAmount.text.toString()) || "".equals(etMaxAmount.text.toString()) || "".equals(etAmount.text.toString()) || "".equals(etUnitPrice.text.toString())) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (etMinAmount.text.toString().trim().toInt() > etMaxAmount.text.toString().trim().toInt()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (etMaxAmount.text.toString().trim().toInt() > etAmount.text.toString().trim().toInt()) {
                toast("illegal value")
                return@setOnClickListener
            }
            if (!AccountMng.isValidAddress(tvQLCWalletAddess.text.toString())) {
                toast("Illegal Receipt Address")
                return@setOnClickListener
            }
            var map = mutableMapOf<String, String>()
            map.put("account", ConstantValue.currentUser.account)
            map.put("token", UserUtils.getUserToken(ConstantValue.currentUser))
            map.put("type", ConstantValue.orderTypeBuy)
            map.put("unitPrice", etUnitPrice.text.toString().trim())
            map.put("totalAmount", etAmount.text.toString().trim())
            map.put("minAmount", etMinAmount.text.toString().trim())
            map.put("maxAmount", etMaxAmount.text.toString().trim())
            map.put("qgasAddress",tvQLCWalletAddess.text.toString().trim())
            map.put("usdtAddress", "")
            map.put("fromAddress", "")
            map.put("txid", "")
            mPresenter.generateBuyQgasOrder(map)
        }
        llSelectQlcWallet.setOnClickListener {
            var intent1 = Intent(activity, OtcChooseWalletActivity::class.java)
            intent1.putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal)
            intent1.putExtra("select", true)
            startActivityForResult(intent1, AllWallet.WalletType.QlcWallet.ordinal)
            activity!!.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out)
        }
        tvQLCWalletAddess.setOnClickListener {
            showEnterQlcWalletDialog()
        }
        var qlcAccounList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        if (qlcAccounList.size > 0) {
            qlcAccounList.forEach {
                if (it.isCurrent()) {
                    receiveQgasWallet = it
                    tvQLCWalletName.text = it.accountName
                    tvQLCWalletAddess.text = it.address
                    return@forEach
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AllWallet.WalletType.QlcWallet.ordinal -> {
                    receiveQgasWallet = data!!.getParcelableExtra<QLCAccount>("wallet")
                    tvQLCWalletName.text = receiveQgasWallet!!.accountName
                    tvQLCWalletAddess.text = receiveQgasWallet!!.address
                }
            }
        }
    }

    fun showEnterQlcWalletDialog() {
        //
        val view = View.inflate(activity!!, R.layout.dialog_input_walletaddress_layout, null)
        val etContent = view.findViewById<View>(R.id.etContent) as EditText//输入内容
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)//取消按钮
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        //取消或确定按钮监听事件处l
        val sweetAlertDialog = SweetAlertDialog(activity)
        val window = sweetAlertDialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        sweetAlertDialog.setView(view)
        sweetAlertDialog.show()
        tvCancel.setOnClickListener {
            sweetAlertDialog.cancel()
        }
        tvOk.setOnClickListener {
            if (AccountMng.isValidAddress(etContent.text.toString().trim())) {
                tvQLCWalletName.text = etContent.text.toString().trim()
                tvQLCWalletAddess.text = etContent.text.toString().trim()
            } else {
                toast("Illegal Receipt Address")
            }
            sweetAlertDialog.cancel()
        }
    }

//    private fun showSpinnerPopWindow() {
//        var ethWalletList = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
//        if (ethWalletList.size > 0) {
//            PopWindowUtil.showSharePopWindow(activity!!, walletMore, ethWalletList.map { it.address }, object : PopWindowUtil.OnItemSelectListener {
//                override fun onSelect(content: String) {
//                    if ("" != content) {
//                        etReceiveAddress.setText(content)
//                    }
//                    SpringAnimationUtil.endRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
//                }
//            })
//            SpringAnimationUtil.startRotatoSpringViewAnimation(walletMore) { animation, canceled, value, velocity -> }
//        }
//    }

    override fun setupFragmentComponent() {
        DaggerOrderBuyComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .orderBuyModule(OrderBuyModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: OrderBuyContract.OrderBuyContractPresenter) {
        mPresenter = presenter as OrderBuyPresenter
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
package com.stratagile.qlink.ui.activity.recommend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import com.pawegio.kandroid.alert
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.topup.GroupItemList
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.recommend.component.DaggerMyTopupGroupComponent
import com.stratagile.qlink.ui.activity.recommend.contract.MyTopupGroupContract
import com.stratagile.qlink.ui.activity.recommend.module.MyTopupGroupModule
import com.stratagile.qlink.ui.activity.recommend.presenter.MyTopupGroupPresenter
import com.stratagile.qlink.ui.activity.topup.TopupDeductionEthChainActivity
import com.stratagile.qlink.ui.activity.topup.TopupDeductionQlcChainActivity
import com.stratagile.qlink.ui.activity.topup.TopupPayNeoChainActivity
import com.stratagile.qlink.ui.activity.topup.VoucherDetailActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.topup.TopupGroupItemAdapter
import com.stratagile.qlink.utils.FileUtil
import com.stratagile.qlink.utils.OtcUtils
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.activity_topup_order_list.*
import java.io.File
import java.util.*

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: $description
 * @date 2020/01/15 16:21:51
 */

class MyTopupGroupActivity : BaseActivity(), MyTopupGroupContract.View {

    @Inject
    internal lateinit var mPresenter: MyTopupGroupPresenter
    lateinit var topupGroupItemAdapter: TopupGroupItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_order_list)
    }
    override fun initData() {
        title.text = getString(R.string.group_order_list)
        topupGroupItemAdapter = TopupGroupItemAdapter(arrayListOf())
        topupGroupItemAdapter.setEnableLoadMore(true)
        recyclerView.adapter = topupGroupItemAdapter
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, this)))
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
        topupGroupItemAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.cancelOrder -> {
                    showCancelDialog(position)
                }
                R.id.llVoucher -> {
                    OtcUtils.gotoBlockBrowser(this, topupGroupItemAdapter.data[position].deductionTokenChain, topupGroupItemAdapter.data[position].deductionTokenInTxid)
                }
                R.id.voucherDetail -> {

                    var topupOrderBean = TopupOrder.OrderBean()
                    var groupItemList = topupGroupItemAdapter.data[position]
                    topupOrderBean.payTokenSymbol = groupItemList.payToken
                    topupOrderBean.symbol = groupItemList.deductionToken
                    topupOrderBean.id = groupItemList.id
                    topupOrderBean.userId = groupItemList.userId
                    topupOrderBean.chain = groupItemList.deductionTokenChain
                    topupOrderBean.payTokenChain = groupItemList.payTokenChain
                    topupOrderBean.qgasAmount = groupItemList.deductionTokenAmount
                    topupOrderBean.payTokenAmount = groupItemList.payTokenAmount.toString()
                    topupOrderBean.discountPrice = groupItemList.payFiatMoney
                    var orderIntent = Intent(this, VoucherDetailActivity::class.java).putExtra("orderBean", topupOrderBean)
                    startActivity(orderIntent)
                }
                R.id.tvPayNow -> {
                    if ("".equals(topupGroupItemAdapter.data[position].deductionTokenInTxid)) {
                        when(OtcUtils.parseChain(topupGroupItemAdapter.data[position].deductionTokenChain)) {
                            AllWallet.WalletType.QlcWallet -> {
                                var payIntent = Intent(this, TopupDeductionQlcChainActivity::class.java)
                                payIntent.putExtra("groupBean", topupGroupItemAdapter.data[position])
                                payIntent.putExtra("isGroup", true)
                                startActivityForResult(payIntent, 1)
                            }
                            AllWallet.WalletType.EthWallet -> {
                                var payIntent = Intent(this, TopupDeductionEthChainActivity::class.java)
                                payIntent.putExtra("groupBean", topupGroupItemAdapter.data[position])
                                payIntent.putExtra("isGroup", true)
                                startActivityForResult(payIntent, 1)
                            }
                        }
                    } else {
                        if ("".equals(topupGroupItemAdapter.data[position].payTokenInTxid) && topupGroupItemAdapter.data[position].status.equals("DEDUCTION_TOKEN_PAID")) {
                            when(OtcUtils.parseChain(topupGroupItemAdapter.data[position].payTokenChain)) {
                                AllWallet.WalletType.NeoWallet -> {
                                    var payIntent = Intent(this, TopupPayNeoChainActivity::class.java)
                                    payIntent.putExtra("groupBean", topupGroupItemAdapter.data[position])
                                    payIntent.putExtra("isGroup", true)
                                    startActivityForResult(payIntent, 1)
                                }
                            }
                        }
                    }
                }
            }
        }
        getOrderList()

        refreshLayout.setOnRefreshListener {
            currentPage = 1
            refreshLayout.isRefreshing = false
            topupGroupItemAdapter.setNewData(ArrayList())
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["userId"] = ConstantValue.currentUser.userId
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)
        }
    }
    var paymentOk = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            paymentOk = true
            finish()
        }
    }

    var currentPage = 0
    fun getOrderList(){
        currentPage = 1
        var map = hashMapOf<String, String>()
        if (ConstantValue.currentUser != null) {
            map["userId"] = ConstantValue.currentUser.userId
        }
        map["page"] = currentPage.toString()
        map["size"] = "20"
        mPresenter.getOderList(map, currentPage)
    }

    override fun setupActivityComponent() {
       DaggerMyTopupGroupComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .myTopupGroupModule(MyTopupGroupModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: MyTopupGroupContract.MyTopupGroupContractPresenter) {
            mPresenter = presenter as MyTopupGroupPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    override fun setOrderList(groupItemList: GroupItemList, page: Int) {
        if (page == 1) {
            topupGroupItemAdapter.setNewData(ArrayList())
        }
        topupGroupItemAdapter.addData(groupItemList.itemList)
        if (page != 1) {
            topupGroupItemAdapter.loadMoreComplete()
        }
        if (groupItemList.itemList.size == 0) {
            topupGroupItemAdapter.loadMoreEnd(true)
        }
    }

    fun showCancelDialog(position: Int) {
        alert(getString(R.string.are_you_sure_want_to_cancel_the_order)) {
            negativeButton(getString(R.string.cancel)) { dismiss() }
            positiveButton(getString(R.string.confirm)) {
                cancelOrder(position)
            }
        }.show()
    }

    fun cancelOrder(position: Int) {
        showProgressDialog()
        var map = hashMapOf<String, String>()
        if (ConstantValue.currentUser != null) {
            map["account"] = ConstantValue.currentUser.account
        }
        map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
//        map["orderId"] = topupOrderListAdapter.data[position].id
//        mPresenter.cancelOrder(map, position)
    }

}
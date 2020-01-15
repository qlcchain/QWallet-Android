package com.stratagile.qlink.ui.activity.recommend

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.topup.GroupItemList
import com.stratagile.qlink.ui.activity.recommend.component.DaggerMyTopupGroupComponent
import com.stratagile.qlink.ui.activity.recommend.contract.MyTopupGroupContract
import com.stratagile.qlink.ui.activity.recommend.module.MyTopupGroupModule
import com.stratagile.qlink.ui.activity.recommend.presenter.MyTopupGroupPresenter
import com.stratagile.qlink.ui.activity.topup.TopupDeductionEthChainActivity
import com.stratagile.qlink.ui.activity.topup.TopupDeductionQlcChainActivity
import com.stratagile.qlink.ui.activity.topup.TopupPayNeoChainActivity
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
        title.text = "Order List"
        topupGroupItemAdapter = TopupGroupItemAdapter(arrayListOf())
        recyclerView.adapter = topupGroupItemAdapter
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, this)))
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
        topupGroupItemAdapter.setOnItemClickListener { adapter, view, position ->
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
        getOrderList()
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
        topupGroupItemAdapter.setNewData(groupItemList.itemList)
    }

}
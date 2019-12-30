package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.WebSettings
import androidx.work.impl.Scheduler
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.API
import com.stratagile.qlink.data.api.HttpInfoInterceptor
import com.stratagile.qlink.data.api.RequestBodyInterceptor
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.BaseBack
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopupOrderListComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopupOrderListContract
import com.stratagile.qlink.ui.activity.topup.module.TopupOrderListModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopupOrderListPresenter
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.topup.TopupOrderListAdapter
import com.stratagile.qlink.utils.*
import com.stratagile.topup.PayService
import com.stratagile.topup.TopupInterceptor
import com.stratagile.topup.TopupRequestInterceptor
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_qurry_mobile.*
import kotlinx.android.synthetic.main.activity_topup_order_list.*
import kotlinx.android.synthetic.main.activity_topup_order_list.recyclerView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/26 15:00:15
 */

class TopupOrderListActivity : BaseActivity(), TopupOrderListContract.View {
    override fun cancelOrderSuccess(topupOrder: TopupOrder, position: Int) {
        closeProgressDialog()
        topupOrderListAdapter.data[position].status = topupOrder.order.status
        toast(getString(R.string.qgas_will_be_returned_to_the_payment_address))
        topupOrderListAdapter.notifyItemChanged(position)
    }

    override fun setOrderList(topupOrderList: TopupOrderList, page: Int) {
        if (page == 1) {
            topupOrderListAdapter.setNewData(ArrayList())
        }
        topupOrderListAdapter.addData(topupOrderList.orderList)
        if (page != 1) {
            topupOrderListAdapter.loadMoreComplete()
        }
        if (topupOrderList.orderList.size == 0) {
            topupOrderListAdapter.loadMoreEnd(true)
        }
    }

    var currentPage = 1

    @Inject
    internal lateinit var mPresenter: TopupOrderListPresenter
    lateinit var topupOrderListAdapter: TopupOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_topup_order_list)
    }

    override fun initData() {
        title.text = getString(R.string.order_list)
        topupOrderListAdapter = TopupOrderListAdapter(ArrayList())
        topupOrderListAdapter.setEnableLoadMore(true)
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, this)))
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
        recyclerView.adapter = topupOrderListAdapter
        currentPage = 1
        var map = hashMapOf<String, String>()
        if (ConstantValue.currentUser != null) {
            map["account"] = ConstantValue.currentUser.account
        }
        var topUpP2pId = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
        if ("".equals(topUpP2pId)) {
            var saveP2pId = FileUtil.readData("/Qwallet/p2pId.txt")
            if ("".equals(saveP2pId)) {
                val uuid = UUID.randomUUID()
                var p2pId = ""
                p2pId += uuid.toString().replace("-", "")
                topUpP2pId = p2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, p2pId)

                val file = File(Environment.getExternalStorageDirectory().toString() + "/Qwallet/p2pId.txt")
                if (file.exists()) {
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                } else {
                    file.createNewFile()
                    FileUtil.savaData("/Qwallet/p2pId.txt", topUpP2pId)
                }

            } else {
                topUpP2pId = saveP2pId
                SpUtil.putString(this, ConstantValue.topUpP2pId, topUpP2pId)
            }
        }

        map["p2pId"] = topUpP2pId
        map["page"] = currentPage.toString()
        map["size"] = "20"
        mPresenter.getOderList(map, currentPage)

        topupOrderListAdapter.setOnLoadMoreListener({
            currentPage++
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["account"] = ConstantValue.currentUser.account
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)

        }, recyclerView)
        topupOrderListAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.cancelOrder -> {
                    showCancelDialog(position)
                }
                R.id.llVoucher -> {
                    OtcUtils.gotoBlockBrowser(this, topupOrderListAdapter.data[position].chain, topupOrderListAdapter.data[position].txid)
                }
                R.id.voucherDetail -> {
                    startActivity(Intent(this, VoucherDetailActivity::class.java).putExtra("orderBean", topupOrderListAdapter.data[position]))
                }
                R.id.tvPayNow -> {
                    if ("TOKEN".equals(topupOrderListAdapter.data[position].payWay)) {
                        if ("".equals(topupOrderListAdapter.data[position].txid)) {
                            when(OtcUtils.parseChain(topupOrderListAdapter.data[position].chain)) {
                                AllWallet.WalletType.QlcWallet -> {
                                    var payIntent = Intent(this, TopupDeductionQlcChainActivity::class.java)
                                    payIntent.putExtra("order", topupOrderListAdapter.data[position])
                                    startActivityForResult(payIntent, 1)
                                }
                                AllWallet.WalletType.EthWallet -> {
                                    var payIntent = Intent(this, TopupDeductionEthChainActivity::class.java)
                                    payIntent.putExtra("order", topupOrderListAdapter.data[position])
                                    startActivityForResult(payIntent, 1)
                                }
                            }
                        } else {
                            if ("".equals(topupOrderListAdapter.data[position].payTokenInTxid)) {
                                when(OtcUtils.parseChain(topupOrderListAdapter.data[position].payTokenChain)) {
                                    AllWallet.WalletType.NeoWallet -> {
                                        var payIntent = Intent(this, TopupPayNeoChainActivity::class.java)
                                        payIntent.putExtra("order", topupOrderListAdapter.data[position])
                                        startActivityForResult(payIntent, 1)
                                    }
                                }
                            }
                        }
                    } else {
                        if ("QGAS_PAID".equals(topupOrderListAdapter.data[position].status)) {
                            var url = "https://shop.huagaotx.cn/vendor/third_pay/index.html?mobile=${topupOrderListAdapter.data[position].phoneNumber}&uid=1000001&sid=8a51FmcnWGH-j2F-g9Ry2KT4FyZ_Rr5xcKdt7i96&trace_id=mm_1000001_${topupOrderListAdapter.data[position].userId}_${topupOrderListAdapter.data[position].id}&package=${topupOrderListAdapter.data[position].originalPrice.toBigDecimal().stripTrailingZeros().toPlainString()}"
                            KLog.i(url)
                            paymentOk = false
                            val intent = Intent(this, WebViewActivity::class.java)
                            intent.putExtra("url", url)
                            intent.putExtra("title", getString(R.string.payment))
                            startActivityForResult(intent, 1)
                            finish()
                        }
                    }
                }
            }
        }
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            refreshLayout.isRefreshing = false
            topupOrderListAdapter.setNewData(ArrayList())
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["account"] = ConstantValue.currentUser.account
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)
        }
    }
    var paymentOk = false;

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            paymentOk = true
            finish()
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
        map["orderId"] = topupOrderListAdapter.data[position].id
        mPresenter.cancelOrder(map, position)
    }

    override fun onResume() {
        super.onResume()
        if (paymentOk) {
            currentPage = 1
            refreshLayout.isRefreshing = false
            topupOrderListAdapter.setNewData(ArrayList())
            var map = hashMapOf<String, String>()
            if (ConstantValue.currentUser != null) {
                map["account"] = ConstantValue.currentUser.account
            }
            map["p2pId"] = SpUtil.getString(this, ConstantValue.topUpP2pId, "")
            map["page"] = currentPage.toString()
            map["size"] = "20"
            mPresenter.getOderList(map, currentPage)
        }
    }

    override fun setupActivityComponent() {
        DaggerTopupOrderListComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .topupOrderListModule(TopupOrderListModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: TopupOrderListContract.TopupOrderListContractPresenter) {
        mPresenter = presenter as TopupOrderListPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
package com.stratagile.qlink.ui.activity.qlc

import android.os.Bundle
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.google.gson.Gson
import com.socks.library.KLog
import com.stratagile.qlc.QLCAPI
import com.stratagile.qlc.QlcWebSocketLister
import com.stratagile.qlc.ReceiveMessage
import com.stratagile.qlc.entity.AccountInfo
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.db.QLCAccount
import com.stratagile.qlink.ui.activity.qlc.component.DaggerQlcTestComponent
import com.stratagile.qlink.ui.activity.qlc.contract.QlcTestContract
import com.stratagile.qlink.ui.activity.qlc.module.QlcTestModule
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcTestPresenter
import kotlinx.android.synthetic.main.activity_qlc_test.*
import qlc.network.QlcClient
import qlc.network.QlcWebSocketClient
import qlc.rpc.impl.LedgerRpc

import javax.inject.Inject;
import kotlin.concurrent.thread

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/05/05 16:24:30
 */

class QlcTestActivity : BaseActivity(), QlcTestContract.View {

    @Inject
    internal lateinit var mPresenter: QlcTestPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var qlcWebSocketClient : QlcWebSocketClient? = null

    override fun initView() {
        setContentView(R.layout.activity_qlc_test)
    }
    override fun initData() {
        var  list = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        list.forEach {
            KLog.i(it)
        }
        getAccountInfo.setOnClickListener {
            list.forEach {
                KLog.i(it)
                it.address.let {
                }
            }
        }
        clearAccount.setOnClickListener {
            AppConfig.instance.daoSession.qlcAccountDao.deleteAll()
            list = AppConfig.instance.daoSession.qlcAccountDao.loadAll()
        }
        disconnectWebsockt.setOnClickListener {
        }
        connectWebsockt.setOnClickListener {

        }
        getBlockCount.setOnClickListener {
            thread {
                try {
                    val jsonArray = JSONArray()
                    val jsonArray1 = JSONArray()
                    jsonArray1.add("qlc_19r1dsrniopuyzfgfb9ym97oqt35ezp67sfhswro6ozb5m3hgnbeoi68ns6o")
                    jsonArray.add(jsonArray1)
                    jsonArray.add(-1)
                    val qlcClient = QlcClient(ConstantValue.qlcNode)
                    val rpc = LedgerRpc(qlcClient)
                    val jsonObject = rpc.accountsPending(jsonArray)
                    QLCAPI().ledgerAccountInfo("qlc_19r1dsrniopuyzfgfb9ym97oqt35ezp67sfhswro6ozb5m3hgnbeoi68ns6o") {
                        KLog.i(it.first!!.result.toString())
                        var accountInfo = Gson().fromJson<AccountInfo>(it.first!!.result.toString(), AccountInfo::class.java)
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }

        }
    }


    override fun setupActivityComponent() {
       DaggerQlcTestComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .qlcTestModule(QlcTestModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: QlcTestContract.QlcTestContractPresenter) {
            mPresenter = presenter as QlcTestPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
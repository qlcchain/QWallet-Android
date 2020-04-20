package com.stratagile.qlink.ui.activity.my

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.View
import android.widget.TextView
import com.pawegio.kandroid.runDelayedOnUiThread
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.ui.activity.my.component.DaggerCumulativeQgasClaimedComponent
import com.stratagile.qlink.ui.activity.my.contract.CumulativeQgasClaimedContract
import com.stratagile.qlink.ui.activity.my.module.CumulativeQgasClaimedModule
import com.stratagile.qlink.ui.activity.my.presenter.CumulativeQgasClaimedPresenter
import com.stratagile.qlink.ui.adapter.my.EpidemicRedPaperAwardedAdapter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.UIUtils
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepService
import com.today.step.net.EpidemicList
import kotlinx.android.synthetic.main.activity_cumulative_qgas_claimed.*
import kotlinx.android.synthetic.main.activity_cumulative_qgas_claimed.recyclerView
import kotlinx.android.synthetic.main.activity_cumulative_qgas_claimed.refreshLayout
import kotlinx.android.synthetic.main.activity_trade_order_detail.*
import kotlinx.android.synthetic.main.fragment_topup.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.zip.Inflater

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/04/15 09:37:37
 */

class CumulativeQgasClaimedActivity : BaseActivity(), CumulativeQgasClaimedContract.View {

    @Inject
    internal lateinit var mPresenter: CumulativeQgasClaimedPresenter

    private var iSportStepInterface: ISportStepInterface? = null
    lateinit var serviceConnection : ServiceConnection
    lateinit var epidemicRedPaperAwardedAdapter: EpidemicRedPaperAwardedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_cumulative_qgas_claimed)
    }
    override fun initData() {
        title.text = getString(R.string.cumulative_qgas_claimed)


        epidemicRedPaperAwardedAdapter = EpidemicRedPaperAwardedAdapter(arrayListOf())
        recyclerView.adapter = epidemicRedPaperAwardedAdapter
        epidemicRedPaperAwardedAdapter.emptyView = View.inflate(this, R.layout.empty_layout, null)
        refreshLayout.setOnRefreshListener {
            getNoRewardEpidemicList()
        }
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
        epidemicRedPaperAwardedAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id) {
                R.id.tvTransactionHash -> {
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val mClipData = ClipData.newPlainText("Label", (view as TextView).text.toString())
                    cm.setPrimaryClip(mClipData)
                    ToastUtil.displayShortToast(getString(R.string.copy_success))
                }
            }
        }
        EventBus.getDefault().register(this)
        //开启计步Service，同时绑定Activity进行aidl通信
        val intent = Intent(this, TodayStepService::class.java)
        startService(intent)
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                runDelayedOnUiThread(300) {
                    iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                    getNoRewardEpidemicList()
                }

//                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH)

            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun epidemicListBack(epidemicList: EpidemicList) {
        KLog.i("列表返回")
        refreshLayout.isRefreshing = false
        when(epidemicList.status) {
            "AWARDED" -> {
                epidemicRedPaperAwardedAdapter.setNewData(epidemicList.recordList)
            }
        }
    }

    fun getNoRewardEpidemicList() {
        iSportStepInterface!!.getEpidemicList(ConstantValue.currentUser.account, AccountUtil.getUserToken(), "AWARDED")
    }

    override fun setupActivityComponent() {
       DaggerCumulativeQgasClaimedComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .cumulativeQgasClaimedModule(CumulativeQgasClaimedModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: CumulativeQgasClaimedContract.CumulativeQgasClaimedContractPresenter) {
            mPresenter = presenter as CumulativeQgasClaimedPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
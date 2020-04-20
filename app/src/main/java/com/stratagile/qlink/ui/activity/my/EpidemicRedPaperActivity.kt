package com.stratagile.qlink.ui.activity.my

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.runDelayedOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.Location
import com.stratagile.qlink.entity.MyAsset
import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.my.component.DaggerEpidemicRedPaperComponent
import com.stratagile.qlink.ui.activity.my.contract.EpidemicRedPaperContract
import com.stratagile.qlink.ui.activity.my.module.EpidemicRedPaperModule
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicRedPaperPresenter
import com.stratagile.qlink.ui.adapter.my.EpidemicOutSideAdapter
import com.stratagile.qlink.ui.adapter.my.EpidemicRedPaperAdapter
import com.stratagile.qlink.ui.adapter.my.OutSideBean
import com.stratagile.qlink.utils.AccountUtil
import com.today.step.lib.ISportStepInterface
import com.today.step.lib.TodayStepData
import com.today.step.lib.TodayStepService
import com.today.step.net.BaseBack
import com.today.step.net.CHttpApiWrapper
import com.today.step.net.CreateRecord
import com.today.step.net.EpidemicList
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_epidemic_red_paper.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/04/13 17:05:33
 */

class EpidemicRedPaperActivity : BaseActivity(), EpidemicRedPaperContract.View {

    @Inject
    internal lateinit var mPresenter: EpidemicRedPaperPresenter
    private var iSportStepInterface: ISportStepInterface? = null
    lateinit var serviceConnection: ServiceConnection
    private var mStepSum: Int = 0
    lateinit var epidemicRedPaperAdapter: EpidemicRedPaperAdapter
    var stepArrayList = arrayListOf<TodayStepData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun setLocation(location: Location) {
        closeProgressDialog()
        focusEpidemic()
        if ("domestic".equals(location.location)) {
            val intent = Intent(this, EpidemicWebViewActivity::class.java)
            intent.putExtra("url", ConstantValue.guoneiEpidemic)
            intent.putExtra("title", "COVID-19 Live Updates")
            startActivity(intent)
        } else {
            val intent = Intent(this, EpidemicWebViewActivity::class.java)
            intent.putExtra("url", ConstantValue.haiwaiEpidemic)
            intent.putExtra("title", "COVID-19 Live Updates")
            startActivity(intent)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_epidemic_red_paper)
        ivRefresh.setOnClickListener {
            var animation = RotateAnimation(0f, 359f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
            animation.duration = 1000
            animation.repeatCount = 0
            animation.fillAfter = true
            ivRefresh.startAnimation(animation)
            mStepSum = iSportStepInterface!!.getCurrentTimeSportStep()

            var stepArray = Gson().fromJson<ArrayList<TodayStepData>>(iSportStepInterface!!.todaySportStepArray, object : TypeToken<ArrayList<TodayStepData>>() {}.type)
            stepArrayList = stepArray
            stepArrayList.reverse()

            updateStepCount()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handlerError(throwable: Throwable) {
        closeProgressDialog()
        toast(throwable.message!!)
    }

    override fun initData() {
        title.text = getString(R.string.covid_19_fighter_bounty)
        EventBus.getDefault().register(this)
        //开启计步Service，同时绑定Activity进行aidl通信
        val intent = Intent(this, TodayStepService::class.java)
        startService(intent)
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Activity和Service通过aidl进行通信
                runDelayedOnUiThread(300) {
                    iSportStepInterface = ISportStepInterface.Stub.asInterface(service)
                    firstEnter()
                    try {
                        mStepSum = iSportStepInterface!!.getCurrentTimeSportStep()
                        var stepArray = Gson().fromJson<ArrayList<TodayStepData>>(iSportStepInterface!!.todaySportStepArray, object : TypeToken<ArrayList<TodayStepData>>() {}.type)
                        stepArrayList = stepArray
                        stepArrayList.reverse()
                        KLog.i(stepArray)
                        updateStepCount()
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }

//                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH)

            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        epidemicRedPaperAdapter = EpidemicRedPaperAdapter(arrayListOf())
        recyclerView.adapter = epidemicRedPaperAdapter
        epidemicRedPaperAdapter.setOnItemClickListener { adapter, view, position ->
            var intent = Intent(this, EpidemicClaimActivity::class.java)
            intent.putExtra("bean", epidemicRedPaperAdapter.data[position])
            startActivityForResult(intent, 0)
        }
        tvCumulativeQgas.setOnClickListener {
            startActivity(Intent(this, CumulativeQgasClaimedActivity::class.java))
        }
        tvFocus.setOnClickListener {
            showProgressDialog()
            mPresenter.getLocation(hashMapOf<String, String>())
        }
        tvNoOutSide.setOnClickListener {
            if (createRecord.isolationDays >= 14 && this::createRecord.isInitialized && !"1".equals(createRecord.subsidised.toString())) {
                var intent = Intent(this, EpidemicClaimQlcActivity::class.java).putExtra("qlc", createRecord.rewardQlcAmount.toString())
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            getNoRewardEpidemicList()
            firstEnter()
        }
    }

    lateinit var createRecord: CreateRecord
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun createRecordBack(createRecord: CreateRecord) {
        this.createRecord = createRecord
        KLog.i(createRecord.toString())
        if (createRecord.isCreate) {
            getNewEpidemicList()
            tvFocusContent.text = getString(R.string.claim_daily_qgas_bounty_simply_by_checking_out_the_covid_19_live_updates_page_for_1_consecutive_day, (createRecord.isolationDays + 1).toString(), ((createRecord.isolationDays + 1) * 100).toString())
        } else {
            llFocus.visibility = View.GONE
            getNoRewardEpidemicList()
        }
        tvAllQlc.text = createRecord.claimedQgas.toString()


        recyclerViewAll.layoutManager = GridLayoutManager(this, 7)
        var dayList = arrayListOf<OutSideBean>()
        for (i in 1..createRecord.rewardQlcDays) {
            if (i <= createRecord.isolationDays) {
                dayList.add(OutSideBean(i, true))
            } else {
                dayList.add(OutSideBean(i, false))
            }
        }
        updateStepCount()
        recyclerViewAll.adapter = EpidemicOutSideAdapter(dayList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun epidemicListBack(epidemicList: EpidemicList) {
        when (epidemicList.status) {
            "NEW" -> {
                if (epidemicList.recordList.size == 0) {
                    llFocus.visibility = View.GONE
                } else {
                    tvFocusContent.text = getString(R.string.claim_daily_qgas_bounty_simply_by_checking_out_the_covid_19_live_updates_page_for_1_consecutive_day, (epidemicList.recordList[0].days).toString(), ((epidemicList.recordList[0].days) * 100).toString())
                    llFocus.visibility = View.VISIBLE
                    tvNoRedPaper.visibility = View.GONE
                }
                getNoRewardEpidemicList()
            }
            "NO_AWARD" -> {
                if (epidemicList.recordList.size > 0) {
                    tvNoRedPaper.visibility = View.GONE
                } else {
                    if (llFocus.visibility != View.VISIBLE) {
                        tvNoRedPaper.visibility = View.VISIBLE
                    }
                }
                epidemicRedPaperAdapter.setNewData(epidemicList.recordList)
            }
        }
    }

    /**
     * 关注疫情，关注之后才能领取
     */
    fun focusEpidemic() {
        iSportStepInterface!!.focusEpidemic(ConstantValue.currentUser.account, AccountUtil.getUserToken())
    }

    /**
     * 进页面拉数据
     */
    fun firstEnter() {
        KLog.i("开始首次进入请求")
        iSportStepInterface!!.createRecord(ConstantValue.currentUser.account, AccountUtil.getUserToken())
    }

    fun getNewEpidemicList() {
        iSportStepInterface!!.getEpidemicList(ConstantValue.currentUser.account, AccountUtil.getUserToken(), "NEW")
    }

    fun getNoRewardEpidemicList() {
        iSportStepInterface!!.getEpidemicList(ConstantValue.currentUser.account, AccountUtil.getUserToken(), "NO_AWARD")
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        unbindService(serviceConnection)
        super.onDestroy()
    }

    private fun updateStepCount() {
        KLog.i("updateStepCount : $mStepSum")
        if (this::createRecord.isInitialized) {
            tvDailyStep.text = mStepSum.toString()
            var allStep = 0L
            if (createRecord.isolationDays > 0) {
                for (i in 0 until createRecord.isolationDays) {
                    if (stepArrayList.size > i) {
                        allStep += stepArrayList[i].step
                    }
                }
            }
            tvQlcAmount.text = getString(R.string.claim_100_qlc_when_your_mission_is_completed, createRecord.rewardQlcAmount.toString())
            tvIntroduce.text = getString(R.string._1_how_to_claim_qlc_for_staking_daily_bounty_check_out_the_covid_19_live_updates_page_at_least_once_a_day_you_are_entitled_to_claim_daily_qgas_bounty_in_your_qgas_wallet_the_qgas_amount_is_calculated_based_on_that_earned_from_100_qlc_for_staking_2_how_to_claim_social_distancing_bounty_check_out_the_covid_19_live_updates_page_at_least_once_a_day_with_your_daily_steps_no_greater_than_1000_for_consecutive_14_days_you_are_entitled_to_claim_100_qlc_the_reward_will_be_sent_to_your_nep_5_wallet_when_claimed, createRecord.pledgeQlcBase.toString(), createRecord.rewardQlcDays.toString(), createRecord.rewardQlcAmount.toString())
            tvDoneDays.text = Html.fromHtml(getString(R.string.you_have_checked_out_the_covid_19_live_updates_page_for_1_consecutive_day_your_cumulative_steps_now_is_59, " ${createRecord.isolationDays.toString()} ", " ${allStep.toString()} "))
        }
        if (this::createRecord.isInitialized && "1".equals(createRecord.subsidised.toString())) {
            tvNoOutSide.background = resources.getDrawable(R.drawable.oval_eceff5_bg)
            tvNoOutSide.setTextColor(resources.getColor(R.color.color_808080))
            tvNoOutSide.text = getString(R.string.claimed_epidemic)
        } else if (this::createRecord.isInitialized){
            if (createRecord.isolationDays >= createRecord.rewardQlcDays) {
                tvNoOutSide.background = resources.getDrawable(R.drawable.oval_maincolor_stroke_bg)
                tvNoOutSide.setTextColor(resources.getColor(R.color.mainColor))
                tvNoOutSide.text = getString(R.string.claim_epidemic)
            } else {
                tvNoOutSide.background = resources.getDrawable(R.drawable.oval_eceff5_bg)
                tvNoOutSide.setTextColor(resources.getColor(R.color.color_808080))
                tvNoOutSide.text = getString(R.string._14_days_left, (createRecord.rewardQlcDays - createRecord.isolationDays).toString())
            }
        }
    }

    override fun setupActivityComponent() {
        DaggerEpidemicRedPaperComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .epidemicRedPaperModule(EpidemicRedPaperModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: EpidemicRedPaperContract.EpidemicRedPaperContractPresenter) {
        mPresenter = presenter as EpidemicRedPaperPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
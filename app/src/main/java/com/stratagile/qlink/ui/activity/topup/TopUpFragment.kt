package com.stratagile.qlink.ui.activity.topup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopUpComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.module.TopUpModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopUpPresenter

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.pawegio.kandroid.alert
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.chart.MyAxisValueFormatter
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.InviteList
import com.stratagile.qlink.entity.KLine
import com.stratagile.qlink.entity.TokenInfo
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.eventbus.Logout
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity
import com.stratagile.qlink.ui.activity.main.MainViewModel
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration
import com.stratagile.qlink.ui.adapter.finance.InvitedAdapter
import com.stratagile.qlink.ui.adapter.topup.TopupShowProductAdapter
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.ToastUtil
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.view.ScaleCircleNavigator
import com.vondear.rxtools.RxDeviceTool
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_person.*
import kotlinx.android.synthetic.main.fragment_topup.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */

class TopUpFragment : BaseFragment(), TopUpContract.View {
    override fun setQlcPrice(tokenPrice: TokenPrice) {
        if("USD".equals(ConstantValue.currencyBean.name)) {
            tvQlcPrice.text = "$ " + tokenPrice.data[0].price
        } else {
            tvQlcPrice.text = "¥ " + tokenPrice.data[0].price
        }
    }

    override fun setOneFriendReward(dict: Dict) {
        var map = mutableMapOf<String, String>()
        map.put("page", "1")
        map.put("size", "20")
        mPresenter.getProductList(map)

        oneFirendClaimQgas = dict.data.value.toFloat()
    }

    override fun setInviteRank(inviteList: InviteList) {

        val invitedAdapter = InvitedAdapter(inviteList.top5, oneFirendClaimQgas)
        recyclerViewInvite.adapter = invitedAdapter
    }

    override fun setProductList(topupProduct: TopupProduct) {
        topupShowProductAdapter.setNewData(topupProduct.productList)
        getInviteRank()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun logOut(logout: Logout) {
        llReferralCode.visibility = View.GONE
    }

    @Inject
    lateinit internal var mPresenter: TopUpPresenter
    private var oneFirendClaimQgas = 0f
    private var viewModel: MainViewModel? = null
    internal var viewList: MutableList<View> = ArrayList()
    lateinit var topupShowProductAdapter: TopupShowProductAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topup, null)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        EventBus.getDefault().register(this)
        val mBundle = arguments
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewList.add(layoutInflater.inflate(R.layout.layout_finance_share, null, false))
        viewList.add(layoutInflater.inflate(R.layout.layout_finance_earn_rank, null, false))
        val viewAdapter = ViewAdapter()
        viewPager.adapter = viewAdapter
        val scaleCircleNavigator = ScaleCircleNavigator(activity)
        scaleCircleNavigator.setCircleCount(2)
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY)
        scaleCircleNavigator.setSelectedCircleColor(activity!!.resources.getColor(R.color.mainColor))
        scaleCircleNavigator.setCircleClickListener { index -> viewPager.currentItem = index }
        indicator.navigator = scaleCircleNavigator
        ViewPagerHelper.bind(indicator, viewPager)


        viewModel?.currentUserAccount?.observe(this, Observer {
            if (it != null) {
                tvIniviteCode.text = ConstantValue.currentUser.inviteCode
                llReferralCode.visibility = View.VISIBLE
                tvInivteNow.setOnClickListener {
                    startActivity(Intent(activity, InviteNowActivity::class.java))
                }
                llCopy.setOnClickListener {
                    //获取剪贴板管理器：
                    var cm = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", tvIniviteCode.text.toString().trim { it <= ' ' })
                    // 将ClipData内容放到系统剪贴板里。
                    cm!!.primaryClip = mClipData
                    ToastUtil.displayShortToast(getString(R.string.copy_success))
                }
                getOneFriendReward()
            } else {
                llReferralCode.visibility = View.GONE
            }
        })
        topupShowProductAdapter = TopupShowProductAdapter(arrayListOf())
        recyclerView.adapter = topupShowProductAdapter
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, activity!!)))
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false

        recyclerViewInvite.setHasFixedSize(true)
        recyclerViewInvite.isNestedScrollingEnabled = false
        getOneFriendReward()
        topupShowProductAdapter.setOnItemClickListener { adapter, view, position ->
//            if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size == 0) {
//                activity!!.alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately)) {
//                    negativeButton(getString(R.string.cancel)) { dismiss() }
//                    positiveButton(getString(R.string.create)) { startActivity(Intent(context, SelectWalletTypeActivity::class.java)) }
//                }.show()
//            } else {
//                if (topupShowProductAdapter.data[position].stock != 0) {
//                    startActivity(Intent(activity!!, QurryMobileActivity::class.java))
//                }
//            }
            if (topupShowProductAdapter.data[position].stock != 0) {
                startActivity(Intent(activity!!, QurryMobileActivity::class.java))
            }
        }
        etMobile.setOnClickListener {
            if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size == 0) {
                activity!!.alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately)) {
                    negativeButton(getString(R.string.cancel)) { dismiss() }
                    positiveButton(getString(R.string.create)) { startActivity(Intent(context, SelectWalletTypeActivity::class.java)) }
                }.show()
                return@setOnClickListener
            }
            startActivity(Intent(activity!!, QurryMobileActivity::class.java))
        }
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            getOneFriendReward()
        }

        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart.setBackgroundColor(resources.getColor(R.color.white))

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(false)

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setNoDataText("")

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300f
        val x = chart.xAxis
        x.isEnabled = false
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawAxisLine(false)
        x.setDrawGridLines(false)
        x.granularity = 1f
        x.setDrawLabels(true)
        x.position
        x.textColor = resources.getColor(R.color.color_29282a)
        x.valueFormatter = MyAxisValueFormatter()

        val y = chart.axisRight
        y.setLabelCount(6, false)
        y.textColor = resources.getColor(R.color.color_29282a)
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.setDrawAxisLine(false)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.isEnabled = false
        y.isEnabled = false
        y.axisLineColor = resources.getColor(R.color.mainColor)
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false


        // don't forget to refresh the drawing
        chart.invalidate()

        chart.postDelayed({
            getQlcKline("1h")
            setKLineSelectUi(0)
        }, 1000)
        tvHour.setOnClickListener {
            getQlcKline("1h")
            setKLineSelectUi(0)
        }
        tvDay.setOnClickListener {
            getQlcKline("1d")
            setKLineSelectUi(1)
        }
        tvWeek.setOnClickListener {
            getQlcKline("1w")
            setKLineSelectUi(2)
        }
        tvMonth.setOnClickListener {
            getQlcKline("1M")
            setKLineSelectUi(3)
        }
    }

    fun setKLineSelectUi(postion : Int) {
        when(postion) {
            0 -> {
                tvHour.setTextColor(resources.getColor(R.color.mainColor))
                tvDay.setTextColor(resources.getColor(R.color.color_29282a))
                tvWeek.setTextColor(resources.getColor(R.color.color_29282a))
                tvMonth.setTextColor(resources.getColor(R.color.color_29282a))
            }
            1 -> {
                tvHour.setTextColor(resources.getColor(R.color.color_29282a))
                tvDay.setTextColor(resources.getColor(R.color.mainColor))
                tvWeek.setTextColor(resources.getColor(R.color.color_29282a))
                tvMonth.setTextColor(resources.getColor(R.color.color_29282a))
            }
            2 -> {
                tvHour.setTextColor(resources.getColor(R.color.color_29282a))
                tvDay.setTextColor(resources.getColor(R.color.color_29282a))
                tvWeek.setTextColor(resources.getColor(R.color.mainColor))
                tvMonth.setTextColor(resources.getColor(R.color.color_29282a))
            }
            3 -> {
                tvHour.setTextColor(resources.getColor(R.color.color_29282a))
                tvDay.setTextColor(resources.getColor(R.color.color_29282a))
                tvWeek.setTextColor(resources.getColor(R.color.color_29282a))
                tvMonth.setTextColor(resources.getColor(R.color.mainColor))
            }
        }
    }

    fun getQlcKline(type : String) {
        val infoMap1 = HashMap<String, Any>()
        infoMap1.put("symbol", "QLC")
        infoMap1.put("interval", type)
        infoMap1.put("size", 100)
        mPresenter.getTokenKline(infoMap1)
    }

    override fun setChartData(data: KLine) {
        if (data.data == null || data.data.size == 0) {
            chart.post { chart.visibility = View.GONE }
            return
        }
        chart.visibility = View.VISIBLE
        val values = ArrayList<Entry>()
        for (i in 0 until data.data.size) {
            val now = TimeUnit.MILLISECONDS.toMinutes(java.lang.Long.parseLong(data.data[i][0]))
            values.add(Entry((now - 25000000).toFloat(), java.lang.Float.parseFloat(data.data[i][1])))
            //            KLog.i(i);
            //            KLog.i(now);
            //            KLog.i(Long.parseLong(data.getData().get(i).get(0)));
        }
        val set1: LineDataSet
        // create a dataset and give it a type
        set1 = LineDataSet(values, "")

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.0f
        set1.color = resources.getColor(R.color.main_color)
        set1.setDrawHorizontalHighlightIndicator(true)
        val drawable = ContextCompat.getDrawable(activity!!, R.drawable.chart_fade)
        set1.fillDrawable = drawable
        chart.animateY(500)
        set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
        // create a data object with the data sets
        val data1 = LineData(set1)
        data1.setValueTextSize(9f)
        data1.setDrawValues(false)
        chart.data = data1


        val sets = chart.data.dataSets
        for (iSet in sets) {

            val set = iSet as LineDataSet
            set.setDrawFilled(true)
            set.mode = LineDataSet.Mode.LINEAR
        }
        chart.invalidate()
        getTokenPrice()
    }

    private fun getTokenPrice() {
        val infoMap = HashMap<String, Any>()
        val tokens = arrayOfNulls<String>(1)
        tokens[0] = "QLC"
        infoMap["symbols"] = tokens
        infoMap["coin"] = ConstantValue.currencyBean.name
        mPresenter.getToeknPrice(infoMap)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    internal inner class ViewAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return viewList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(viewList[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(viewList[position])
            viewList[position].setOnClickListener(View.OnClickListener {
                if (position == 0) {
                    startActivity(Intent(activity, MyStakeActivity::class.java))
                }
                if (position == 1) {
                    startActivity(Intent(activity, MiningInviteActivity::class.java))
                }
            })
            return viewList[position]
        }
    }


    override fun setupFragmentComponent() {
        DaggerTopUpComponent
                .builder()
                .appComponent((activity!!.application as AppConfig).applicationComponent)
                .topUpModule(TopUpModule(this))
                .build()
                .inject(this)
    }

    fun getInviteRank() {
        if (ConstantValue.currentUser != null) {
            val infoMap = HashMap<String, String>()
            infoMap["account"] = ConstantValue.currentUser.account
            infoMap["token"] = AccountUtil.getUserToken()
            mPresenter.getInivteRank(infoMap)
        }
    }

    /**
     * 获取邀请到一个好友的奖励数
     */
    private fun getOneFriendReward() {
        val infoMap = HashMap<String, String>()
        infoMap["dictType"] = "winq_invite_reward_amount"
        mPresenter.getOneFriendReward(infoMap)
    }

    override fun setPresenter(presenter: TopUpContract.TopUpContractPresenter) {
        mPresenter = presenter as TopUpPresenter
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
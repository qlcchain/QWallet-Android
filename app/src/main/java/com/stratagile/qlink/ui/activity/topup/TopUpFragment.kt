package com.stratagile.qlink.ui.activity.topup

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.runDelayedOnUiThread
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.BuildConfig
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.*
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DexEntity
import com.stratagile.qlink.entity.eventbus.Logout
import com.stratagile.qlink.entity.eventbus.ShowMiningAct
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.CountryList
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupGroupKindList
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.defi.DappBrowserActivity
import com.stratagile.qlink.ui.activity.defi.SearchDefiActivity
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity
import com.stratagile.qlink.ui.activity.main.MainViewModel
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.my.EpidemicInviteNowActivity
import com.stratagile.qlink.ui.activity.my.EpidemicWebViewActivity
import com.stratagile.qlink.ui.activity.place.PlaceListActivity
import com.stratagile.qlink.ui.activity.place.PlaceVisitActivity
import com.stratagile.qlink.ui.activity.recommend.TopupProductDetailActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopUpComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.module.TopUpModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopUpPresenter
import com.stratagile.qlink.ui.adapter.defi.DexListAdapter
import com.stratagile.qlink.ui.adapter.finance.InvitedAdapter
import com.stratagile.qlink.ui.adapter.topup.CountryListAdapter
import com.stratagile.qlink.ui.adapter.topup.ImagesPagerAdapter
import com.stratagile.qlink.ui.adapter.topup.TopupShowProductAdapter
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.view.ScaleCircleNavigator
import kotlinx.android.synthetic.main.fragment_defi_list.*
import kotlinx.android.synthetic.main.fragment_topup.*
import kotlinx.android.synthetic.main.fragment_topup.recyclerView
import kotlinx.android.synthetic.main.fragment_topup.refreshLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */

class TopUpFragment : BaseFragment(), TopUpContract.View {
    override fun setQlcPrice(tokenPrice: TokenPrice) {
    }
    override fun initDataFromNet() {

    }
    override fun setOneFriendReward(dict: Dict) {
        queryProxyActivity()
        oneFirendClaimQgas = dict.data.value.toFloat()
    }

    lateinit var mIndexInterface: IndexInterface
    override fun setIndexInterface(indexInterface: IndexInterface) {
        var indexInterfaceStr = GsonUtils.objToJson(indexInterface)
        KLog.i(indexInterfaceStr)
        getDex()
        FileUtil.savaData("/Qwallet/indexInterfaceStr.txt", indexInterfaceStr)
        this.mIndexInterface = indexInterface
        viewModel!!.indexInterfaceMutableLiveData.value = indexInterface
        this.oneFirendClaimQgas = mIndexInterface.dictList.winq_invite_reward_amount.toFloat()
        //处理轮播图
        handlerBanner()


        var mustDiscount = 1.toDouble()
        mIndexInterface.groupKindList.forEach {
            if (mustDiscount > it.discount) {
                mustDiscount = it.discount
            }
        }
        mIndexInterface.groupKindList.forEach {
            if (mustDiscount == it.discount) {
                topupShowProductAdapter.mustGroupKind = it
            }
        }
        topupShowProductAdapter.payToken = mIndexInterface.payTokenList[0]

        if (mIndexInterface.dictList.show19.equals("1")) {
            rlXingcheng.visibility = View.VISIBLE
            tv_title.visibility = View.INVISIBLE
        } else {
            rlXingcheng.visibility = View.GONE
            tv_title.visibility = View.VISIBLE
        }
    }

    fun handlerBanner() {
        isStop = true
        viewList.clear()
        //分享
        viewList.add(R.layout.layout_finance_share)

        //赚取qgas
        if (mIndexInterface.tradeMiningList.size > 0) {
            ConstantValue.miningQLC = mIndexInterface.tradeMiningList[0].totalRewardAmount.toBigDecimal().stripTrailingZeros().toPlainString() + " QLC!"
            viewList.add(R.layout.layout_finance_earn_rank)
        }

        //回购
        if (TimeUtil.timeStamp(mIndexInterface.dictList.burnQgasVoteStartDate) < mIndexInterface.currentTimeMillis && (TimeUtil.timeStamp(mIndexInterface.dictList.burnQgasVoteEndDate) > mIndexInterface.currentTimeMillis)) {
            ConstantValue.qgasToQlcPrice = mIndexInterface.burnQgasList[0].unitPrice.toFloat()
            viewList.add(R.layout.layout_banner_buyback)
        }

        //代理
        if (TimeUtil.timeStamp(mIndexInterface.dictList.topupGroupStartDate) < mIndexInterface.currentTimeMillis && (TimeUtil.timeStamp(mIndexInterface.dictList.topopGroupEndDate) > mIndexInterface.currentTimeMillis)) {
            viewList.add(R.layout.layout_banner_proxy_youxiang)
        }

        val viewAdapter = ImagesPagerAdapter(viewList, viewPager, activity!!)
        viewPager.adapter = viewAdapter
        val scaleCircleNavigator = ScaleCircleNavigator(activity)
        scaleCircleNavigator.setCircleCount(viewList.size)
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY)
        scaleCircleNavigator.setSelectedCircleColor(activity!!.resources.getColor(R.color.mainColor))
        scaleCircleNavigator.setCircleClickListener { index -> viewPager.currentItem = index }
        indicator.navigator = scaleCircleNavigator
        ViewPagerHelper.bind(indicator, viewPager, viewList.size)

        if (viewList.size > 1) {
            autoPlayView()
        }
    }


    lateinit var proxyAcitivtyDict: Dict
    override fun setProxyActivityBanner(dict: Dict) {
    }

    private var isStop: Boolean = true
    var autoPlayThread: Thread? = null

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private fun autoPlayView() {
        isStop = false
        //自动播放图片
        if (autoPlayThread == null) {
            autoPlayThread = Thread(Runnable {
                while (true) {
                    if (!isStop && viewList.size > 1) {
                        runOnUiThread { viewPager.currentItem = viewPager.currentItem + 1 }
                        SystemClock.sleep(5000)
                    }
                    SystemClock.sleep(10)
                }
            })
            autoPlayThread!!.start()
        }
    }

    lateinit var showMiningAct: ShowMiningAct
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setMiningAct(showMiningAct: ShowMiningAct) {
    }

    lateinit var burnQgasAct1: BurnQgasAct
    override fun setBurnQgasAct(burnQgasAct: BurnQgasAct) {
    }

    @Inject
    lateinit internal var mPresenter: TopUpPresenter
    lateinit var countryAdapter: CountryListAdapter
    private var oneFirendClaimQgas = 0f
    private var viewModel: MainViewModel? = null
    internal var viewList: MutableList<Int> = ArrayList()
    lateinit var topupShowProductAdapter: TopupShowProductAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topup, null)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        EventBus.getDefault().register(this)
        val mBundle = arguments
        return view
    }

    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private fun setFirstLocation() {
        val m = Integer.MAX_VALUE / 2 % viewList.size
        val currentPosition = Integer.MAX_VALUE / 2 - m
        viewPager.currentItem = currentPosition
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewList.add(R.layout.layout_finance_share)
        var viewAdapter = ImagesPagerAdapter(viewList, viewPager, activity!!)

        val llp = LinearLayout.LayoutParams(UIUtils.getDisplayWidth(activity!!), UIUtils.getStatusBarHeight(activity!!))
        status_bar.setLayoutParams(llp)

        viewPager.adapter = viewAdapter
        val scaleCircleNavigator = ScaleCircleNavigator(activity)
        scaleCircleNavigator.setCircleCount(viewList.size)
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY)
        scaleCircleNavigator.setSelectedCircleColor(activity!!.resources.getColor(R.color.mainColor))
        scaleCircleNavigator.setCircleClickListener { index -> viewPager.currentItem = index }
        indicator.navigator = scaleCircleNavigator
        ViewPagerHelper.bind(indicator, viewPager!!, viewList.size)

        tvPlaceQuery.setOnClickListener {
            mPresenter.getLocation(hashMapOf<String, String>())
        }

        getLocalData()

        mPresenter.indexInterface()
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            mPresenter.indexInterface()
        }
        getDex()
        tvSearch.setOnClickListener {
            var dappIntent = Intent(activity, SearchDefiActivity::class.java)
            startActivity(dappIntent)
        }
    }

    fun getDex() {
        getDefis()
//        var dexStr = FileUtil.getJson(activity, "dex.json")
//        KLog.i(dexStr)
//        if (!"".equals(dexStr)) {
//            var list = Gson().fromJson<ArrayList<DexEntity>>(dexStr, object : TypeToken<ArrayList<DexEntity>>() {}.type)
//            list.forEach {
//                KLog.i(it.toString())
//            }
//            var dexListAdapter = DexListAdapter(list)
//            recyclerView.adapter = dexListAdapter
//            dexListAdapter.setOnItemClickListener { adapter, view, position ->
//                var dappIntent = Intent(activity, DappBrowserActivity::class.java)
//                dappIntent.putExtra("url", dexListAdapter.data[position].url)
//                startActivity(dappIntent)
//            }
//        }
    }

    fun getDefis() {
        KLog.i("获取defi列表")
        var infoMap = hashMapOf<String, String>()
        infoMap["category"] = "DEXes"
        infoMap["page"] = "1"
        infoMap["size"] = "10"
        mPresenter.getDefis(infoMap)
    }

    override fun setLocation(location: Location) {
        FireBaseUtils.logEvent(activity!!, FireBaseUtils.Campaign_Covid19_more_details)
        if ("domestic".equals(location.location)) {
            val intent = Intent(activity, EpidemicWebViewActivity::class.java)
            intent.putExtra("url", ConstantValue.guoneiEpidemic)
            intent.putExtra("title", "COVID-19 Live Updates")
            startActivity(intent)
        } else {
            val intent = Intent(activity, EpidemicWebViewActivity::class.java)
            intent.putExtra("url", ConstantValue.haiwaiEpidemic)
            intent.putExtra("title", "COVID-19 Live Updates")
            startActivity(intent)
        }
    }

    override fun setDefiList(defiList: DefiList) {
        var dexListAdapter = DexListAdapter(defiList.projectList)
        recyclerView.adapter = dexListAdapter
        dexListAdapter.setOnItemClickListener { adapter, view, position ->
            var dappIntent = Intent(activity, DappBrowserActivity::class.java)
            var website = dexListAdapter.data[position].swapUrl
            website = website.replace("https://", "")
            website = website.replace("http://", "")
            website = "https://" + website
            dappIntent.putExtra("url", website)
            startActivity(dappIntent)
        }
    }

    fun getLocalData() {
        runDelayedOnUiThread(5) {
            KLog.i("获取本地数据")
            try {
                var interfaceListStr = FileUtil.readData("/Qwallet/indexInterfaceStr.txt")
                KLog.i(interfaceListStr)
                if (!"".equals(interfaceListStr)) {
                    var indexInterface = GsonUtils.jsonToObj(interfaceListStr, IndexInterface::class.java)
                    setIndexInterface(indexInterface)
                }
                var productListStr = FileUtil.readData("/Qwallet/productListStr.txt")
                KLog.i(productListStr)
                if (!"".equals(productListStr)) {
                    var topuproduct = GsonUtils.jsonToObj(productListStr, TopupProduct::class.java)
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            var returnPayToken = data!!.getParcelableExtra<PayToken.PayTokenListBean>("selectPayToken")
            selectPayToken = returnPayToken
            topupShowProductAdapter.payToken = selectPayToken
            topupShowProductAdapter.notifyDataSetChanged()
        }
    }


    fun queryProxyActivity() {
        val infoMap: MutableMap<String, String> = HashMap()
        infoMap["dictType"] = "app_dict"
        mPresenter.queryDict(infoMap)
    }

    lateinit var selectPayToken: PayToken.PayTokenListBean
    override fun setPayToken(payToken: PayToken) {
//        selectPayToken = payToken.payTokenList[0]
//        topupShowProductAdapter.payToken = selectPayToken
//        if ("".equals(payToken.payTokenList[0].logo_png)) {
//            Glide.with(activity!!)
//                    .load(resources.getIdentifier(payToken.payTokenList[0].symbol.toLowerCase(), "mipmap", activity!!.packageName))
//                    .apply(AppConfig.instance.optionsTopup)
//                    .into(ivDeduction)
//        } else {
//            Glide.with(activity!!)
//                    .load(payToken.payTokenList[0].logo_png)
//                    .apply(AppConfig.instance.optionsTopup)
//                    .into(ivDeduction)
//        }
//        getOneFriendReward()
    }

    override fun setChartData(data: KLine) {

    }


    override fun onDestroy() {
        isStop = true
        EventBus.getDefault().unregister(this)
        super.onDestroy()
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
        } else {
//            mPresenter.getCountryList(hashMapOf())
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
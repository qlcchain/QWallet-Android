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
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.BuildConfig
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseFragment
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.BurnQgasAct
import com.stratagile.qlink.entity.InviteList
import com.stratagile.qlink.entity.KLine
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.entity.eventbus.Logout
import com.stratagile.qlink.entity.eventbus.ShowMiningAct
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.CountryList
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupGroupKindList
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity
import com.stratagile.qlink.ui.activity.main.MainViewModel
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.place.PlaceListActivity
import com.stratagile.qlink.ui.activity.place.PlaceVisitActivity
import com.stratagile.qlink.ui.activity.recommend.TopupProductDetailActivity
import com.stratagile.qlink.ui.activity.topup.component.DaggerTopUpComponent
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.module.TopUpModule
import com.stratagile.qlink.ui.activity.topup.presenter.TopUpPresenter
import com.stratagile.qlink.ui.adapter.finance.InvitedAdapter
import com.stratagile.qlink.ui.adapter.topup.CountryListAdapter
import com.stratagile.qlink.ui.adapter.topup.ImagesPagerAdapter
import com.stratagile.qlink.ui.adapter.topup.TopupShowProductAdapter
import com.stratagile.qlink.utils.*
import com.stratagile.qlink.view.ScaleCircleNavigator
import kotlinx.android.synthetic.main.fragment_topup.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
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

    override fun setOneFriendReward(dict: Dict) {
        queryProxyActivity()
        oneFirendClaimQgas = dict.data.value.toFloat()
    }

    lateinit var proxyAcitivtyDict : Dict
    override fun setProxyActivityBanner(dict: Dict) {
        mPresenter.getTopupGroupKindList(mutableMapOf())

        proxyAcitivtyDict = dict
        topupShowProductAdapter.proxyAcitivtyDict = dict
        if (!isStop) {
            return
        }
        isStop = false
        viewList.clear()
        if (this::showMiningAct.isInitialized && showMiningAct.isShow) {
            viewList.add(R.layout.layout_finance_share)
            ConstantValue.miningQLC = showMiningAct.count.toBigDecimal().stripTrailingZeros().toPlainString() + " QLC!"
            viewList.add(R.layout.layout_finance_earn_rank)
            if (TimeUtil.timeStamp(dict.data.topupGroupStartDate) < dict.currentTimeMillis && (TimeUtil.timeStamp(dict.data.topopGroupEndDate) > dict.currentTimeMillis)) {
                viewList.add(R.layout.layout_banner_proxy_youxiang)
            }

        } else {
            viewList.add(R.layout.layout_finance_share)
            if (TimeUtil.timeStamp(dict.data.topupGroupStartDate) < dict.currentTimeMillis && (TimeUtil.timeStamp(dict.data.topopGroupEndDate) > dict.currentTimeMillis)) {
                viewList.add(R.layout.layout_banner_proxy_youxiang)
            }
        }
        if (this::burnQgasAct1.isInitialized && burnQgasAct1.list.size > 0) {
            viewList.add(R.layout.layout_banner_buyback)
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
//            setFirstLocation()
            autoPlayView()
        }
    }

    override fun setInviteRank(inviteList: InviteList) {
        mPresenter.getCountryList(hashMapOf())
        val invitedAdapter = InvitedAdapter(inviteList.top5, oneFirendClaimQgas)
        recyclerViewInvite.adapter = invitedAdapter
    }

    override fun setProductList(topupProduct: TopupProduct, next : Boolean) {
        if (!this::selectPayToken.isInitialized) {
            mPresenter.getPayToken()
        } else {
            topupShowProductAdapter.setNewData(topupProduct.productList)
            if (next) {
                getInviteRank()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun logOut(logout: Logout) {
        llReferralCode.visibility = View.GONE
    }

    private var isStop: Boolean = true

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private fun autoPlayView() {
        //自动播放图片
        Thread(Runnable {
            while (!isStop) {
                runOnUiThread { viewPager.currentItem = viewPager.currentItem + 1 }
                SystemClock.sleep(5000)
            }
        }).start()
    }

    lateinit var showMiningAct : ShowMiningAct
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setMiningAct(showMiningAct: ShowMiningAct) {
        KLog.i("原先的活动轮播逻辑...")
        this.showMiningAct = showMiningAct
        if (!isStop) {
            return
        }
        isStop = false
        viewList.clear()
        if (showMiningAct.isShow) {
            viewList.add(R.layout.layout_finance_share)
            ConstantValue.miningQLC = showMiningAct.count.toBigDecimal().stripTrailingZeros().toPlainString() + " QLC!"
            viewList.add(R.layout.layout_finance_earn_rank)
            if (this::proxyAcitivtyDict.isInitialized) {
                if (TimeUtil.timeStamp(proxyAcitivtyDict.data.topupGroupStartDate) < proxyAcitivtyDict.currentTimeMillis && (TimeUtil.timeStamp(proxyAcitivtyDict.data.topopGroupEndDate) > proxyAcitivtyDict.currentTimeMillis)) {
                    viewList.add(R.layout.layout_banner_proxy_youxiang)
                }
            }
        } else {
            viewList.add(R.layout.layout_finance_share)
            if (this::proxyAcitivtyDict.isInitialized) {
                if (TimeUtil.timeStamp(proxyAcitivtyDict.data.topupGroupStartDate) < proxyAcitivtyDict.currentTimeMillis && (TimeUtil.timeStamp(proxyAcitivtyDict.data.topopGroupEndDate) > proxyAcitivtyDict.currentTimeMillis)) {
                    viewList.add(R.layout.layout_banner_proxy_youxiang)
                }
            }
        }
        if (this::burnQgasAct1.isInitialized && burnQgasAct1.list.size > 0) {
            viewList.add(R.layout.layout_banner_buyback)
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
//            setFirstLocation()
            autoPlayView()
        }
    }

    lateinit var burnQgasAct1: BurnQgasAct
    override fun setBurnQgasAct(burnQgasAct: BurnQgasAct) {
        KLog.i("setBurnQgasAct")
//        if (!isStop) {
//            return
//        }
//        isStop = false
        viewList.clear()

        if (showMiningAct.isShow) {
            viewList.add(R.layout.layout_finance_share)
            ConstantValue.miningQLC = showMiningAct.count.toBigDecimal().stripTrailingZeros().toPlainString() + " QLC!"
            viewList.add(R.layout.layout_finance_earn_rank)
            if (this::proxyAcitivtyDict.isInitialized) {
                if (TimeUtil.timeStamp(proxyAcitivtyDict.data.topupGroupStartDate) < proxyAcitivtyDict.currentTimeMillis && (TimeUtil.timeStamp(proxyAcitivtyDict.data.topopGroupEndDate) > proxyAcitivtyDict.currentTimeMillis)) {
                    viewList.add(R.layout.layout_banner_proxy_youxiang)
                }
            }
        } else {
            viewList.add(R.layout.layout_finance_share)
            if (this::proxyAcitivtyDict.isInitialized) {
                if (TimeUtil.timeStamp(proxyAcitivtyDict.data.topupGroupStartDate) < proxyAcitivtyDict.currentTimeMillis && (TimeUtil.timeStamp(proxyAcitivtyDict.data.topopGroupEndDate) > proxyAcitivtyDict.currentTimeMillis)) {
                    viewList.add(R.layout.layout_banner_proxy_youxiang)
                }
            }
        }
        val currentBurnQgasActId = SpUtil.getString(activity, ConstantValue.currentBurnQgasActId, "")
        if (burnQgasAct.list.size > 0) {
            viewList.add(R.layout.layout_banner_buyback)
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
//            setFirstLocation()
            autoPlayView()
        }
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
        rlWallet.setOnClickListener {
            val intent1 = Intent(activity!!, TopupSelectDeductionTokenActivity::class.java)
            startActivityForResult(intent1, 11)
        }

        rl1.setOnClickListener {
            val intent1 = Intent(activity!!, TopupOrderListActivity::class.java)
            startActivity(intent1)
        }
        tvPlaceQuery.setOnClickListener {
            val intent1 = Intent(activity!!, PlaceListActivity::class.java)
            startActivity(intent1)
        }

        var isCn = true
        isCn = SpUtil.getInt(activity!!, ConstantValue.Language, -1) == 1
        if (isCn) {
            ivxingcheng.setImageResource(R.mipmap.cx)
        } else {
            ivxingcheng.setImageResource(R.mipmap.xc_en)
        }

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
                    cm!!.setPrimaryClip(mClipData)
                    ToastUtil.displayShortToast(getString(R.string.copy_success))
                }
                if (!it.startApp) {
                    if (!this::selectPayToken.isInitialized) {
                        mPresenter.getPayToken()
                    } else {
                        getOneFriendReward()
                    }
                }
            } else {
                llReferralCode.visibility = View.GONE
            }
        })
        topupShowProductAdapter = TopupShowProductAdapter(arrayListOf())
        recyclerView.adapter = topupShowProductAdapter
        refreshLayout.setColorSchemeColors(resources.getColor(R.color.mainColor))
//        recyclerView.addItemDecoration(BottomMarginItemDecoration(UIUtils.dip2px(15f, activity!!)))
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false

        recyclerViewInvite.setHasFixedSize(true)
        recyclerViewInvite.isNestedScrollingEnabled = false
        mPresenter.getPayToken()
        topupShowProductAdapter.setOnItemClickListener { adapter, view, position ->
            if (ConstantValue.currentUser == null) {
                startActivity(Intent(activity!!, AccountActivity::class.java))
                return@setOnItemClickListener
            }
            if (topupShowProductAdapter.data[position].stock != 0) {
                if ("FIAT".equals(topupShowProductAdapter.data[position].payWay)) {
                    var qurryIntent = Intent(activity!!, QurryMobileActivity::class.java)
                    qurryIntent.putExtra("country", selectedCountry.nameEn)
                    qurryIntent.putExtra("area", selectedCountry.globalRoaming)
                    qurryIntent.putExtra("isp", topupShowProductAdapter.data[position].ispEn.trim())
                    startActivity(qurryIntent)
                } else {
                    getGroupKindList(position)
//                    if (this::proxyAcitivtyDict.isInitialized && TimeUtil.timeStamp(proxyAcitivtyDict.data.topupGroupStartDate) < proxyAcitivtyDict.currentTimeMillis && (TimeUtil.timeStamp(proxyAcitivtyDict.data.topopGroupEndDate) > proxyAcitivtyDict.currentTimeMillis)) {
//
//                    } else {
//                        var qurryIntent = Intent(activity!!, QurryMobileActivity::class.java)
//                        qurryIntent.putExtra("area", selectedCountry.nameEn)
//                        qurryIntent.putExtra("country", selectedCountry.globalRoaming)
//                        qurryIntent.putExtra("isp", topupShowProductAdapter.data[position].ispEn.trim())
//                        startActivity(qurryIntent)
//                    }
                }
            } else {
                toast(getString(R.string.the_product_is_sold_out))
            }
        }
        rlXingcheng.setOnClickListener {

            var xingchengIntent = Intent(activity!!, PlaceVisitActivity::class.java)

            startActivity(xingchengIntent)
        }
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            if (!this::selectPayToken.isInitialized) {
                mPresenter.getPayToken()
            } else {
                getOneFriendReward()
            }
        }

        var countryListBean = CountryList.CountryListBean()
        countryListBean.name = "全部"
        countryListBean.nameEn = "All"
        countryListBean.globalRoaming = ""
        countryListBean.imgPath = ""
        selectedCountry = countryListBean
        if (BuildConfig.isGooglePlay) {
            rlXingcheng.visibility = View.GONE
            rl1.background = resources.getDrawable(R.drawable.main_bg_shape)
            status_bar.background = resources.getDrawable(R.drawable.main_bg_shape)
            tv_title.visibility = View.VISIBLE
        } else {

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 11 && resultCode == Activity.RESULT_OK) {
            selectPayToken = data!!.getParcelableExtra("selectPayToken")
            topupShowProductAdapter.payToken = selectPayToken
            topupShowProductAdapter.notifyDataSetChanged()
            if ("".equals(selectPayToken.logo_png)) {
                Glide.with(activity!!)
                        .load(activity!!.resources.getIdentifier(selectPayToken.symbol.toLowerCase(), "mipmap", activity!!.packageName))
                        .apply(AppConfig.instance.optionsTopup)
                        .into(ivDeduction)
            } else {
                Glide.with(activity!!)
                        .load(selectPayToken.logo_png)
                        .apply(AppConfig.instance.optionsTopup)
                        .into(ivDeduction)
            }
        }
    }

    override fun setGroupDate(dict: Dict, position: Int) {
        this.proxyAcitivtyDict = dict
        topupShowProductAdapter.proxyAcitivtyDict = dict
        if (TimeUtil.timeStamp(dict.data.topupGroupStartDate) < dict.currentTimeMillis && (TimeUtil.timeStamp(dict.data.topopGroupEndDate) > dict.currentTimeMillis)) {
            var productIntent = Intent(activity!!, TopupProductDetailActivity::class.java)
            productIntent.putExtra("productBean", topupShowProductAdapter!!.data[position])
            productIntent.putExtra("globalRoaming", selectedCountry.globalRoaming)
            productIntent.putExtra("phoneNumber", "")
            productIntent.putExtra("selectedPayToken", selectPayToken)
            startActivity(productIntent)
        } else {
            var qurryIntent = Intent(activity!!, QurryMobileActivity::class.java)
            qurryIntent.putExtra("country", selectedCountry.nameEn)
            qurryIntent.putExtra("area", selectedCountry.globalRoaming)
            qurryIntent.putExtra("isp", topupShowProductAdapter.data[position].ispEn.trim())
//            var deductionTokenPrice = 0.toDouble()
//            if ("CNY".equals(topupShowProductAdapter!!.data[position].payFiat)) {
//                deductionTokenPrice = selectPayToken.price
//            } else if ("USD".equals(topupShowProductAdapter!!.data[position].payFiat)){
//                deductionTokenPrice = selectPayToken.usdPrice
//            }
//
//            var dikoubijine = topupShowProductAdapter!!.data[position].payFiatAmount.toBigDecimal().multiply(topupShowProductAdapter!!.data[position].qgasDiscount.toBigDecimal())
//            var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)
//            var zhifufabijine = topupShowProductAdapter!!.data[position].payFiatAmount.toBigDecimal().multiply(topupShowProductAdapter!!.data[position].discount.toBigDecimal())
//            var zhifudaibijine = zhifufabijine - dikoubijine
//            var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(topupShowProductAdapter!!.data[position].payFiat)){topupShowProductAdapter!!.data[position].payTokenCnyPrice.toBigDecimal()} else {topupAbleAdapter!!.data[position].payTokenUsdPrice.toBigDecimal()}, 3, BigDecimal.ROUND_HALF_UP)
//
//            activity!!.alert(getString(R.string.a_cahrge_of_will_cost_paytoken_and_deduction_token, topupShowProductAdapter!!.data[position].amountOfMoney.toString(), zhifubishuliang.stripTrailingZeros().toPlainString(), topupAbleAdapter!!.data[position].payTokenSymbol, dikoubishuliang.stripTrailingZeros().toPlainString(), selectedPayToken.symbol, topupAbleAdapter!!.data[position].localFiat)) {
//                negativeButton(getString(R.string.cancel)) { dismiss() }
//                positiveButton(getString(R.string.buy_topup)) {
//                    if (AppConfig.instance.daoSession.qlcAccountDao.loadAll().size != 0) {
//                        generateTopupOrder(topupShowProductAdapter!!.data[position])
//                    } else {
//                        activity!!.alert(getString(R.string.you_do_not_have_qlcwallet_create_immediately, "QLC Chain")) {
//                            negativeButton(getString(R.string.cancel)) { dismiss() }
//                            positiveButton(getString(R.string.create)) { startActivity(Intent(activity!!, SelectWalletTypeActivity::class.java)) }
//                        }.show()
//                    }
//                }
//            }.show()
        }
    }

    fun getGroupKindList(posiion : Int) {
        val infoMap: MutableMap<String, String> = HashMap()
        infoMap["dictType"] = "app_dict"
        mPresenter.qurryDict(infoMap, posiion)
    }

    fun queryProxyActivity() {
        val infoMap: MutableMap<String, String> = HashMap()
        infoMap["dictType"] = "app_dict"
        mPresenter.queryDict(infoMap)
    }

    lateinit var selectPayToken: PayToken.PayTokenListBean
    override fun setPayToken(payToken: PayToken) {
        selectPayToken = payToken.payTokenList[0]
        topupShowProductAdapter.payToken = selectPayToken
        if ("".equals(payToken.payTokenList[0].logo_png)) {
            Glide.with(activity!!)
                    .load(resources.getIdentifier(payToken.payTokenList[0].symbol.toLowerCase(), "mipmap", activity!!.packageName))
                    .apply(AppConfig.instance.optionsTopup)
                    .into(ivDeduction)
        } else {
            Glide.with(activity!!)
                    .load(payToken.payTokenList[0].logo_png)
                    .apply(AppConfig.instance.optionsTopup)
                    .into(ivDeduction)
        }
        getOneFriendReward()
    }

    override fun setChartData(data: KLine) {

    }

    private val mFragmentContainerHelper = FragmentContainerHelper()
    lateinit var selectedCountry : CountryList.CountryListBean
    override fun setCountryList(countryList: CountryList) {
//        countryAdapter.setNewData(countryList.countryList)
        var countryListBean = CountryList.CountryListBean()
        countryListBean.name = "全部"
        countryListBean.nameEn = "All"
        countryListBean.globalRoaming = ""
        countryListBean.imgPath = ""
        selectedCountry = countryListBean
        countryList.countryList.add(0, countryListBean)
        val commonNavigator = CommonNavigator(activity!!)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return countryList.countryList.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                var isCn = true
                isCn = SpUtil.getInt(activity!!, ConstantValue.Language, -1) == 1
                if (isCn) {
                    simplePagerTitleView.setText(countryList.countryList.get(i).name)
                } else {
                    simplePagerTitleView.setText(countryList.countryList.get(i).nameEn)
                }
                simplePagerTitleView.isSingleLine = false
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_505050)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.color_F50B6E)
                simplePagerTitleView.setOnClickListener {
                    mFragmentContainerHelper.handlePageSelected(i)
                    selectedCountry = countryList.countryList[i]
                    reChangeTaoCan(countryList.countryList[i])
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.lineHeight = resources.getDimension(R.dimen.x3)
                indicator.setColors(resources.getColor(R.color.transparent))
                return indicator
            }
        }
        indicatorPlan.navigator = commonNavigator
        commonNavigator.titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        mFragmentContainerHelper.attachMagicIndicator(indicatorPlan)
//        commonNavigator.titleContainer.dividerDrawable = object : ColorDrawable() {
//            override fun getIntrinsicWidth(): Int {
//                return UIUtils.dip2px(10f, this@InviteActivity)
//            }
//        }
        mPresenter.burnQgasList(hashMapOf())
    }

    fun reChangeTaoCan(bean: CountryList.CountryListBean) {
        var map = mutableMapOf<String, String>()
        map.put("page", "1")
        map.put("globalRoaming", bean.globalRoaming)
        map.put("deductionTokenId", if (this::selectPayToken.isInitialized) {selectPayToken.id} else {""})
        map.put("size", "20")
        mPresenter.getProductList(map, false)
    }

    lateinit var mustGroupKind : TopupGroupKindList.GroupKindListBean
    override fun setGroupKindList(topupGroupList: TopupGroupKindList) {
        var mustDiscount = 1.toDouble()
        topupGroupList.groupKindList.forEach {
            if (mustDiscount > it.discount) {
                mustDiscount = it.discount
            }
        }
        topupGroupList.groupKindList.forEach {
            if (mustDiscount == it.discount) {
                topupShowProductAdapter.mustGroupKind = it
            }
        }

        var map = mutableMapOf<String, String>()
        map.put("page", "1")
        map.put("size", "20")
        map.put("globalRoaming", if (this::selectedCountry.isInitialized) {selectedCountry.globalRoaming} else {""})
        map.put("deductionTokenId", if (this::selectPayToken.isInitialized) {selectPayToken.id} else {""})
        mPresenter.getProductList(map, true)
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
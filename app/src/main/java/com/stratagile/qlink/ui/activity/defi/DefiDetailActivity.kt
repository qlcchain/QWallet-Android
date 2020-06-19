package com.stratagile.qlink.ui.activity.defi

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v4n.view.ViewPager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.defi.DefiDetail
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.ui.activity.defi.component.DaggerDefiDetailComponent
import com.stratagile.qlink.ui.activity.defi.contract.DefiDetailContract
import com.stratagile.qlink.ui.activity.defi.module.DefiDetailModule
import com.stratagile.qlink.ui.activity.defi.presenter.DefiDetailPresenter
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.adapter.defi.DefiDetailCategoryAdapter
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.FireBaseUtils
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.activity_defi_detail.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.*
import javax.inject.Inject


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/05/29 09:13:19
 */

class DefiDetailActivity : BaseActivity(), DefiDetailContract.View {

    @Inject
    internal lateinit var mPresenter: DefiDetailPresenter
    lateinit var defiEntity : DefiList.ProjectListBean
    lateinit var defiDetailCategoryAdapter: DefiDetailCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        drawableBg = R.drawable.main_bg_shape
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_defi_detail)
        recyclerViewType.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        defiDetailCategoryAdapter = DefiDetailCategoryAdapter(arrayListOf())
        recyclerViewType.adapter = defiDetailCategoryAdapter

        val titles = ArrayList<String>()
        titles.add(getString(R.string.key_stats))
        titles.add(getString(R.string.active_data))
        titles.add(getString(R.string.historical_stats))
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                if (position == 0) {
                    return DefiKeyStateFragment()
                }
                if (position == 1) {
                    return DefiActiveDataFragment()
                }
                if (position == 2) {
                    return DefiHistoricalStateFragment()
                }
                return Fragment()
            }

            override fun getCount(): Int {
                return titles.size
            }
        }
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = titles[i]
                simplePagerTitleView.setSingleLine(false)
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_1e1e24)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.mainColor)
                simplePagerTitleView.setOnClickListener {
                    viewPager.currentItem = i
                    if (i == 0) {
                    }
                    if (i == 1) {
                    }
                    if (i == 2) {
                    }
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.lineHeight = resources.getDimension(R.dimen.x3)
                indicator.setColors(resources.getColor(R.color.mainColor))
                return indicator
            }
        }
        indicator.navigator = commonNavigator
        commonNavigator.titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        commonNavigator.titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtils.dip2px(10f, this@DefiDetailActivity)
            }
        }
        viewPager.offscreenPageLimit = 3
        ViewPagerHelper.bind(indicator, viewPager)
        tvDesc.setOnClickListener {
            if (tvDesc.maxLines == 3) {
                tvDesc.maxLines = 100
            } else {
                tvDesc.maxLines = 3
            }
        }
        viewPager.addOnPageChangeListener(object : OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                when(p0) {
                    0 -> {
                        FireBaseUtils.logEvent(this@DefiDetailActivity, FireBaseUtils.Defi_Detail_KeyStats)
                    }
                    1 -> {
                        FireBaseUtils.logEvent(this@DefiDetailActivity, FireBaseUtils.Defi_Detail_ActiveData)
                    }
                    2 -> {
                        FireBaseUtils.logEvent(this@DefiDetailActivity, FireBaseUtils.Defi_Detail_HistoricalStats)
                    }
                }
            }
        })
    }
    override fun initData() {
        title.text = getString(R.string.defi_details)
        viewModel = ViewModelProviders.of(this).get(DefiViewModel::class.java)
        defiEntity = intent.getParcelableExtra("defiEntity")
        getDefiDetail()
        defiDetailCategoryAdapter.setNewData(arrayListOf(defiEntity.chain, defiEntity.category))
        var fillColor = 0x000000
        if (DefiUtil.parseDefiRating(defiEntity.rating.toInt()).contains("A")) {
            fillColor = Color.parseColor("#7ED321")
        } else if (DefiUtil.parseDefiRating(defiEntity.rating.toInt()).contains("B")) {
            fillColor = Color.parseColor("#108EE9")
        } else if (DefiUtil.parseDefiRating(defiEntity.rating.toInt()).contains("C")) {
            fillColor = Color.parseColor("#F5A623")
        } else {
            fillColor = Color.parseColor("#FF3669")
        }
        var gd = GradientDrawable()
        gd.setColor(fillColor)
        gd.setCornerRadius(UIUtils.dip2px(4f, this).toFloat())
        tvRating.background = gd
        var preStr = getString(R.string.rating__)
        var text = "${preStr}${DefiUtil.parseDefiRating(defiEntity.rating.toInt())}"
        var sp = SpannableString(text)
        sp.setSpan(AbsoluteSizeSpan(9, true), 0, preStr.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sp.setSpan(AbsoluteSizeSpan(12, true), preStr.length + 1, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        tvRating.text = sp

        if ("".equals(defiEntity.shortName)) {
            tvDefi.text = defiEntity.name
            Glide.with(this)
                    .load(resources.getIdentifier(defiEntity.name.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", packageName))
                    .into(ivDefi)
        } else {
            tvDefi.text = defiEntity.shortName
            Glide.with(this)
                    .load(resources.getIdentifier(defiEntity.shortName.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", packageName))
                    .into(ivDefi)
        }

        tvOpreate1.setOnClickListener {
            FireBaseUtils.logEvent(this@DefiDetailActivity, FireBaseUtils.Defi_Detail_Rate_)
            if (ConstantValue.currentUser == null) {
                startActivity(Intent(this, AccountActivity::class.java))
                finish()
                return@setOnClickListener
            }
            var intent = Intent(this, DefiRateActivity::class.java)
            intent.putExtra("defiEntity", defiEntity)
            startActivity(intent)
        }

        tvOpreate3.setOnClickListener {
            FireBaseUtils.logEvent(this@DefiDetailActivity, FireBaseUtils.Defi_Detail_Explore)
            try {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                var website = "https://" + mDefiDetail.project.website.replace("https://", "").replace("http://", "")
                val content_url = Uri.parse(website)
                intent.data = content_url
                startActivity(intent)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

    }

    fun getDefiDetail() {
        var infoMap = hashMapOf<String, String>()
        infoMap["projectId"] = defiEntity.id
        mPresenter.getDefiDetail(infoMap)
    }

    fun getDefiStateList() {
        var infoMap = hashMapOf<String, String>()
        infoMap["projectId"] =mDefiDetail.project.id
        infoMap["page"] = "1"
        infoMap["size"] = "30"
        mPresenter.getDefiStateList(infoMap)
    }

    override fun setDefiStateList(defiStateList: DefiStateList) {
        viewModel.defiStateListLiveData.value = defiStateList
    }

    lateinit var viewModel: DefiViewModel
    lateinit var mDefiDetail: DefiDetail
    override fun setDefiDetail(defiDetail: DefiDetail) {
        mDefiDetail = defiDetail
        getDefiStateList()
        tvDesc.text = defiDetail.project.description
        viewModel.defiDetailLiveData.value = mDefiDetail
    }

    override fun setupActivityComponent() {
       DaggerDefiDetailComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .defiDetailModule(DefiDetailModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: DefiDetailContract.DefiDetailContractPresenter) {
            mPresenter = presenter as DefiDetailPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
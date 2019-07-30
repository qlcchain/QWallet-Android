package com.stratagile.qlink.ui.activity.otc

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.TypedValue
import android.widget.LinearLayout
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.otc.component.DaggerOtcOrderRecordComponent
import com.stratagile.qlink.ui.activity.otc.contract.OtcOrderRecordContract
import com.stratagile.qlink.ui.activity.otc.module.OtcOrderRecordModule
import com.stratagile.qlink.ui.activity.otc.presenter.OtcOrderRecordPresenter
import kotlinx.android.synthetic.main.activity_otc_order_record.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.ArrayList

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/16 17:43:20
 */

class OtcOrderRecordActivity : BaseActivity(), OtcOrderRecordContract.View {

    @Inject
    internal lateinit var mPresenter: OtcOrderRecordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_otc_order_record)
    }
    override fun initData() {
        title.text = "Record"
        val titles = ArrayList<String>()
        titles.add("Posted")
        titles.add("Processing")
        titles.add("Completed")
        titles.add("Closed")
        titles.add("Appealed")
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when(position) {
                    0 -> {
                        return PostedFragment()
                    }
                    1 -> {
                        return ProcessFragment()
                    }
                    2 -> {
                        return CompleteFragment()
                    }
                    3 -> {
                        return ClosedOrderFragment()
                    }
                    4 -> {
                        return AppealsFragment()
                    }
                }
                return Fragment()
            }

            override fun getCount(): Int {
                return titles.size
            }
        }
        viewPager.offscreenPageLimit = 5
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = titles[i]
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_b3b3b3)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.mainColor)
                simplePagerTitleView.setOnClickListener { viewPager.setCurrentItem(i) }
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
        indicator.setNavigator(commonNavigator)
        commonNavigator.titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        commonNavigator.titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return resources.getDimension(R.dimen.x8).toInt()
            }
        }
        ViewPagerHelper.bind(indicator, viewPager)
    }

    override fun setupActivityComponent() {
       DaggerOtcOrderRecordComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .otcOrderRecordModule(OtcOrderRecordModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: OtcOrderRecordContract.OtcOrderRecordContractPresenter) {
            mPresenter = presenter as OtcOrderRecordPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
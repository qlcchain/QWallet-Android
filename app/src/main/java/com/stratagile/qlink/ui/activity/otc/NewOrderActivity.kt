package com.stratagile.qlink.ui.activity.otc

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.TypedValue
import android.widget.LinearLayout
import com.jaeger.library.StatusBarUtil
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.statusbar.StatusBarCompat
import com.stratagile.qlink.ui.activity.otc.component.DaggerNewOrderComponent
import com.stratagile.qlink.ui.activity.otc.contract.NewOrderContract
import com.stratagile.qlink.ui.activity.otc.module.NewOrderModule
import com.stratagile.qlink.ui.activity.otc.presenter.NewOrderPresenter
import kotlinx.android.synthetic.main.activity_new_order.*
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
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/08 16:00:52
 */

class NewOrderActivity : BaseActivity(), NewOrderContract.View {

    @Inject
    internal lateinit var mPresenter: NewOrderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        isEditActivity = true
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_new_order)
    }

    override fun initData() {
        if (ConstantValue.mainAddressData == null) {
            mPresenter.getMainAddress()
        }
        title.text = getString(R.string.new_order)
        val titles = ArrayList<String>()
        titles.add(getString(R.string.buy).toUpperCase())
        titles.add(getString(R.string.sell).toUpperCase())
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return if (position == 0) {
                    OrderBuyFragment()
                } else {
                    OrderSellFragment()
                }
            }

            override fun getCount(): Int {
                return titles.size
            }
        }
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = titles[i]
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_b3b3b3)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
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
                return resources.getDimension(R.dimen.x140).toInt()
            }
        }
        ViewPagerHelper.bind(indicator, viewPager)
    }

    override fun setupActivityComponent() {
        DaggerNewOrderComponent
                .builder()
                .appComponent((application as AppConfig).applicationComponent)
                .newOrderModule(NewOrderModule(this))
                .build()
                .inject(this)
    }

    override fun setPresenter(presenter: NewOrderContract.NewOrderContractPresenter) {
        mPresenter = presenter as NewOrderPresenter
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
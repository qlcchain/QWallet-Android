package com.stratagile.qlink.ui.activity.defi

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.ui.activity.defi.component.DaggerEthTransformComponent
import com.stratagile.qlink.ui.activity.defi.contract.EthTransformContract
import com.stratagile.qlink.ui.activity.defi.module.EthTransformModule
import com.stratagile.qlink.ui.activity.defi.presenter.EthTransformPresenter
import com.stratagile.qlink.ui.activity.stake.StakeExplainActivity
import com.stratagile.qlink.utils.UIUtils
import kotlinx.android.synthetic.main.activity_transform_qlc.*
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
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: $description
 * @date 2020/08/15 16:13:43
 */

class EthTransformActivity : BaseActivity(), EthTransformContract.View {

    @Inject
    internal lateinit var mPresenter: EthTransformPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        isEditActivity = true
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_eth_transform)
    }
    override fun initData() {
        val titles = ArrayList<String>()
        var ethWallets = AppConfig.instance.daoSession.ethWalletDao.loadAll()
        var ethWallet = ethWallets.filter { it.isCurrent() }[0]
        title.text = getString(R.string.qlc_cross_chain_swap)
        titles.add("Swap")
//        titles.add(getString(R.string.confidant))
        titles.add("Record")
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when(position) {
                    0 -> {
                        return EthSwapFragment()
                    }
                    1 -> {
                        return EthSwapRecordFragment.EthSwapRecordFragmentInstance.getInstance(ethWallet.address)
                    }
                }
                return Fragment()
            }

            override fun getCount(): Int {
                return titles.size
            }
        }
        viewPager.offscreenPageLimit = 3
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = titles[i]
                simplePagerTitleView.normalColor = resources.getColor(R.color.color_b3b3b3)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.mainColor)
                simplePagerTitleView.setOnClickListener { viewPager.setCurrentItem(i) }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.lineHeight = UIUtils.dip2px(2f, this@EthTransformActivity).toFloat()
                indicator.setColors(resources.getColor(R.color.mainColor))
                return indicator
            }
        }
        indicator.setNavigator(commonNavigator)
        commonNavigator.titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        commonNavigator.titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtils.dip2px(60f, this@EthTransformActivity)
            }
        }
        ViewPagerHelper.bind(indicator, viewPager)
    }

    override fun setupActivityComponent() {
       DaggerEthTransformComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .ethTransformModule(EthTransformModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: EthTransformContract.EthTransformContractPresenter) {
            mPresenter = presenter as EthTransformPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.stake_explain, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.stakeExplain) {
//            startActivity(Intent(this, StakeExplainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}
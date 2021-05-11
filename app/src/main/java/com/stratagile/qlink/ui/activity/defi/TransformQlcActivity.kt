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
import com.stratagile.qlink.ui.activity.defi.component.DaggerTransformQlcComponent
import com.stratagile.qlink.ui.activity.defi.contract.TransformQlcContract
import com.stratagile.qlink.ui.activity.defi.module.TransformQlcModule
import com.stratagile.qlink.ui.activity.defi.presenter.TransformQlcPresenter
import com.stratagile.qlink.ui.activity.main.WebViewActivity
import com.stratagile.qlink.ui.activity.stake.StakeExplainActivity
import com.stratagile.qlink.ui.activity.stake.TokenMintageFragment
import com.stratagile.qlink.ui.activity.stake.VoteNodeFragment
import com.stratagile.qlink.utils.LanguageUtil
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
 * @date 2020/08/12 15:15:34
 */

class TransformQlcActivity : BaseActivity(), TransformQlcContract.View {

    @Inject
    internal lateinit var mPresenter: TransformQlcPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        isEditActivity = true
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_transform_qlc)
    }
    override fun initData() {
        var ethWallets = AppConfig.instance.daoSession.walletDao.loadAll()
        var ethWallet = ethWallets.filter { it.isCurrent }[0]
        val titles = ArrayList<String>()
        title.text = getString(R.string.qlc_cross_chain_swap)
        titles.add(getString(R.string.swap))
//        titles.add(getString(R.string.confidant))
        titles.add(getString(R.string.record_swap))
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when(position) {
                    0 -> {
                        return SwapFragment.SwapFragmentInstance.getInstance(ethWallet.address)
                    }
                    1 -> {
                        return SwapRecordFragment.SwapRecordFragmentInstance.getInstance(ethWallet.address)
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
                indicator.lineHeight = UIUtils.dip2px(2f, this@TransformQlcActivity).toFloat()
                indicator.setColors(resources.getColor(R.color.mainColor))
                return indicator
            }
        }
        indicator.setNavigator(commonNavigator)
        commonNavigator.titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        commonNavigator.titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtils.dip2px(60f, this@TransformQlcActivity)
            }
        }
        ViewPagerHelper.bind(indicator, viewPager)
    }

    override fun setupActivityComponent() {
       DaggerTransformQlcComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .transformQlcModule(TransformQlcModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: TransformQlcContract.TransformQlcContractPresenter) {
            mPresenter = presenter as TransformQlcPresenter
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
                var intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("title", getString(R.string.qlc_cross_chain_intro))
                if (LanguageUtil.isCN(this)) {
                    intent.putExtra("url", "http://dapp-t.qlink.mobi/f/swap/faq_cn.html")
                } else {
                    intent.putExtra("url", "http://dapp-t.qlink.mobi/f/swap/faq_en.html")
                }
                startActivity(intent)
            }
        return super.onOptionsItemSelected(item)
    }

}
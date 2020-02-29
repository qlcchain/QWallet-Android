package com.stratagile.qlink.ui.adapter.topup


import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.SwitchToOtc
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity
import com.stratagile.qlink.ui.activity.my.AccountActivity
import com.stratagile.qlink.ui.activity.my.BurnIntroduceActivity
import com.stratagile.qlink.ui.activity.recommend.AgencyExcellenceActivity
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import org.greenrobot.eventbus.EventBus

import java.util.zip.Inflater


class ImagesPagerAdapter(private val simpleDraweeViewList: MutableList<Int>, private val viewPager: ViewPager, private val context: Context) : PagerAdapter() {

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

    //删除指定位置的页面；适配器负责从view容器中删除view，然而它只保证在finishUpdate(ViewGroup)返回时才完成。
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // 把ImageView从ViewPager中移除掉
        viewPager.removeView(`object` as View)
        //super.destroyItem(container, position, object);
    }

    //是否获取缓存
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    //实例化Item
    //在指定的位置创建页面；适配器负责添加view到这个容器中，然而它只保证在finishUpdate(ViewGroup)返回时才完成。
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view1 = LayoutInflater.from(context).inflate(simpleDraweeViewList[position % simpleDraweeViewList.size], null, false)
        container.addView(view1)
        if (simpleDraweeViewList[position % simpleDraweeViewList.size] == R.layout.layout_finance_share) {
            var referFriend = view1.findViewById<View>(R.id.tvReferFriend)
            var stakeQlc = view1.findViewById<View>(R.id.tvStakeQlc)
            referFriend.setOnClickListener {
                if (ConstantValue.currentUser != null) {
                    context.startActivity(Intent(context, InviteNowActivity::class.java))
                } else {
                    context.startActivity(Intent(context, AccountActivity::class.java))
                }
            }
            stakeQlc.setOnClickListener {
                if (ConstantValue.currentUser != null) {
                    context.startActivity(Intent(context, MyStakeActivity::class.java))
                } else {
                    context.startActivity(Intent(context, AccountActivity::class.java))
                }
            }
        }
        if (simpleDraweeViewList[position % simpleDraweeViewList.size] == R.layout.layout_finance_earn_rank) {
            view1.findViewById<TextView>(R.id.tvQlc).text = ConstantValue.miningQLC
            view1.setOnClickListener {
                if (ConstantValue.currentUser != null) {
                    context.startActivity(Intent(context, MiningInviteActivity::class.java))
                } else {
                    context.startActivity(Intent(context, AccountActivity::class.java))
                }
            }
        }
        if (simpleDraweeViewList[position % simpleDraweeViewList.size] == R.layout.layout_banner_proxy_youxiang) {
            view1.setOnClickListener {
                if (ConstantValue.currentUser != null) {
                    context.startActivity(Intent(context, AgencyExcellenceActivity::class.java))
                } else {
                    context.startActivity(Intent(context, AccountActivity::class.java))
                }
            }
        }
        if (simpleDraweeViewList[position % simpleDraweeViewList.size] == R.layout.layout_banner_buyback) {
            var tvJoinNow = view1.findViewById<TextView>(R.id.tvJoinNow)
            tvJoinNow.setOnClickListener {
                EventBus.getDefault().post(SwitchToOtc(true))
            }
            view1.setOnClickListener {
                context.startActivity(Intent(context, BurnIntroduceActivity::class.java))
            }
        }
        return view1
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    //无论是创建view添加到容器中  还是 销毁view 都是在此方法结束之后执行的
    override fun finishUpdate(container: ViewGroup) {
        super.finishUpdate(container)

//        var position = viewPager.currentItem
//
//        if (position == 0) {
//            position = simpleDraweeViewList.size - 2
//            viewPager.setCurrentItem(position, false)
//        } else if (position == simpleDraweeViewList.size - 1) {
//            position = 1
//            viewPager.setCurrentItem(position, false)
//        }
    }

}

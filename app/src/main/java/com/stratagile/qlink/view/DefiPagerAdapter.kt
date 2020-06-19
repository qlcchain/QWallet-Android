package com.stratagile.qlink.view

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.stratagile.qlink.R
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.DefiUtil.parseLocalDefiRating
import java.util.*

class DefiPagerAdapter(private val mContext: Context, private val list: ArrayList<Int>) : PagerAdapter() {
    private val inflater: LayoutInflater
    private val textView: TextView? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.layout_rating_item, null, false) as TextView
        //        textView = view.findViewById(R.id.tvName);
//        textView = view.findViewById(R.id.tvName);
        view.text = parseLocalDefiRating(position)
        var fillColor = 0x000000
        if (view.text.toString().contains("A")) {
            fillColor = R.color.color_7ED321
        } else if (view.text.toString().contains("B")) {
            fillColor = R.color.color_108EE9
        } else if (view.text.toString().contains("C")) {
            fillColor = R.color.color_F5A623
        } else {
            fillColor = R.color.color_FF3669
        }
        view.setTextColor(mContext.resources.getColor(fillColor))
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    init {
        inflater = LayoutInflater.from(mContext)
    }
}
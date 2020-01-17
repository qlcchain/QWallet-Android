package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pawegio.kandroid.setWidth
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.CountryList
import com.stratagile.qlink.entity.topup.IspList
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.view.SmoothCheckBox

class IspListAdapter(array: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_isp_list, array) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tvIsp, item)
    }
}
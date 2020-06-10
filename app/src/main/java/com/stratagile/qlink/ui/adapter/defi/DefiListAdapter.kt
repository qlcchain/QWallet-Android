package com.stratagile.qlink.ui.adapter.defi

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.UIUtils

class DefiListAdapter(array: ArrayList<DefiList.ProjectListBean>) : BaseQuickAdapter<DefiList.ProjectListBean, BaseViewHolder>(R.layout.item_defi_list, array) {
    override fun convert(helper: BaseViewHolder, item: DefiList.ProjectListBean) {
        helper.setText(R.id.tvIndex, (helper.layoutPosition + 1).toString())
        val imageView = helper.getView<ImageView>(R.id.ivAvatar)
        if ("".equals(item.shortName)) {
            helper.setText(R.id.tvDefiProjectName, item.name)
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.name.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName))
                    .into(imageView)
        } else {
            helper.setText(R.id.tvDefiProjectName, item.shortName)
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.shortName.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName))
                    .into(imageView)
        }
        var ratingView = helper.getView<TextView>(R.id.tvRating)
        helper.setText(R.id.tvRating, DefiUtil.parseDefiRating(item.rating.toInt()))
        helper.setText(R.id.tvLockedCount, DefiUtil.parseUsd(item.tvlUsd))
        var fillColor = 0x000000
        if (DefiUtil.parseDefiRating(item.rating.toInt()).contains("A")) {
            fillColor = Color.parseColor("#7ED321")
        } else if (DefiUtil.parseDefiRating(item.rating.toInt()).contains("B")) {
            fillColor = Color.parseColor("#108EE9")
        } else if (DefiUtil.parseDefiRating(item.rating.toInt()).contains("C")) {
            fillColor = Color.parseColor("#F5A623")
        } else {
            fillColor = Color.parseColor("#FF3669")
        }
        var gd = GradientDrawable()
        gd.setColor(fillColor)
        gd.setCornerRadius(UIUtils.dip2px(4f, mContext).toFloat())
        ratingView.background = gd

        var chart = helper.getView<LineChart>(R.id.chart)
        chart.setNoDataText("")
        if (item.statsCache != null && item.statsCache.cache.size > 0) {
            chart.visibility = View.VISIBLE
            chart.setViewPortOffsets(20f, 0f, 20f, 20f)
            chart.xAxis.isEnabled = false
            chart.xAxis.setDrawGridLines(false)
            chart.axisLeft.isEnabled = false
            chart.axisRight.isEnabled = false
            chart.description.isEnabled = false
            chart.setDrawGridBackground(false)
            chart.setHighlightPerDragEnabled(false)
            chart.setHighlightPerTapEnabled(false)
            chart.setScaleEnabled(false)
            chart.setDrawBorders(false)
            chart.description.isEnabled = false
//            chart.axisLeft.mAxisMaximum = item.statsCache.cache.max()!!.toFloat()
//            chart.axisLeft.mAxisMinimum = item.statsCache.cache.min()!!.toFloat()
            // enable scaling and dragging

            // enable scaling and dragging
            chart.isDragEnabled = false

            // if disabled, scaling can be done on x- and y-axis separately

            // if disabled, scaling can be done on x- and y-axis separately
            chart.setPinchZoom(false)
            chart.legend.form = Legend.LegendForm.EMPTY
            val set1: LineDataSet
            val values = java.util.ArrayList<Entry>()
            item.statsCache.cache.forEachIndexed { index, d ->
                values.add(Entry(index.toFloat(), d.toFloat()))
            }
            set1 = LineDataSet(values, "")
            set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(false)
            set1.setDrawIcons(false)

            set1.setDrawCircleHole(false)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.0f
            if (item.statsCache.cache.first() < item.statsCache.cache.last()) {
                set1.color = mContext.resources.getColor(R.color.color_7ED321)
            } else {
                set1.color = mContext.resources.getColor(R.color.color_ff3669)
            }
            set1.fillColor = mContext.resources.getColor(R.color.transparent)
            set1.fillAlpha = 0
            set1.setDrawHorizontalHighlightIndicator(false)
            val data1 = LineData(set1)
//            chart.animateX(2000)
//            data1.setValueTextSize(9f)
            data1.setDrawValues(false)
            chart.data = data1

            val sets = chart.data.dataSets
            for (iSet in sets) {
                val set = iSet as LineDataSet
                set.setDrawFilled(true)
                set.mode = LineDataSet.Mode.LINEAR
            }
            chart.invalidate()
        } else {
            chart.visibility = View.INVISIBLE
        }

    }
}
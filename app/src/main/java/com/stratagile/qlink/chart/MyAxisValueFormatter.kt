package com.stratagile.qlink.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.stratagile.qlink.utils.TimeUtil

import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class MyAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        val millis = TimeUnit.MINUTES.toMillis(value.toLong() + 25000000)
        return TimeUtil.getTransactionHistoryTime(millis)
    }
}
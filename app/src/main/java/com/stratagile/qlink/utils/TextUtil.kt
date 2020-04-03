package com.stratagile.qlink.utils

import android.text.Html
import android.widget.TextView
import java.math.BigDecimal

object TextUtil {
    fun setGroupInfo(text1: String, text2 : String, textView: TextView) {
        val strCount = "<font color='#F32A40'>${text1}</font><font color='#2B2B2B'>${text2}</font>"
        textView.text = Html.fromHtml(strCount.replace("\n","<br>"))
    }
}
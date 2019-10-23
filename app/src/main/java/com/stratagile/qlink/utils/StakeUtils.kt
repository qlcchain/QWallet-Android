package com.stratagile.qlink.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.TextView
import com.stratagile.qlink.R
import kotlinx.android.synthetic.main.activity_appeal_detail.*

object StakeUtils {
    fun parseStakeType(type : String) {

    }

    fun copyTxetToClip(context: Context, textView: TextView) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("Label", textView.text.toString())
        cm.primaryClip = mClipData
        ToastUtil.displayShortToast(context.getString(R.string.copy_success))
    }
}
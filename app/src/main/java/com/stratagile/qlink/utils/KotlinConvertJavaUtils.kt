package com.stratagile.qlink.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.pawegio.kandroid.alert
import com.stratagile.qlink.R
import com.stratagile.qlink.ui.activity.my.VerificationActivity

object KotlinConvertJavaUtils {
    fun needVerify(context: Context) {
        context.alert(context.getString(R.string.need_verify)) {
            negativeButton (context.getString(R.string.cancel)) { dismiss() }
            positiveButton("To Verify") { context.startActivity(Intent(context, VerificationActivity::class.java)) }
        }.show()
    }

    fun showUploadedDialog(context: Activity) {
        context.alert("Verification is required to comply with KYC regulations and to protect your account from unauthorized access. It will be usually completed within 2 hours. Please contact qlc.chain@gmail.com if any questions. ") {
            positiveButton("ok") { context.finish() }
        }.show()
    }

    fun showNotApprovedDialog(context: Activity) {
        context.alert("Please resubmit the required information. Photos submitted before were not eligible.", "Not approved") {
            positiveButton("ok") { dismiss() }
        }.show()
    }
}
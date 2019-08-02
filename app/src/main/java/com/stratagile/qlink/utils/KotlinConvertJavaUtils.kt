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
            positiveButton(context.getString(R.string.to_verify)) { context.startActivity(Intent(context, VerificationActivity::class.java)) }
        }.show()
    }

    fun showUploadedDialog(context: Activity) {
        context.alert(context.getString(R.string.verification_is_required_to_comply_with_kyc_regulations_and_to_protect_your_account_from_unauthorized_access_it_will_be_usually_completed_within_2_hours_please_contact_qlc_chain_gmail_com_if_any_questions)) {
            positiveButton(context.getString(R.string.ok)) { context.finish() }
        }.show()
    }

    fun showNotApprovedDialog(context: Activity) {
        context.alert(context.getString(R.string.please_resubmit_the_required_information_photos_submitted_before_were_not_eligible), context.getString(R.string.not_approved)) {
            positiveButton(context.getString(R.string.ok)) { dismiss() }
        }.show()
    }
}
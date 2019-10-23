package com.stratagile.qlink.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.toast
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
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

    fun showNewVersionDialog(context: Activity, lastVersion : Int) {
        context.alert(context.getString(R.string.has_new_version)) {
            negativeButton (context.getString(R.string.cancel)) {
                SpUtil.putInt(context, ConstantValue.ignoreVersion, lastVersion)
                dismiss()
            }
            positiveButton(context.getString(R.string.install_now)) {
                rateNow(context)
            }
        }.show()
    }

    fun rateNow(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=" + context.getPackageName())
//            intent.setPackage(GoogleMarket.GOOGLE_PLAY)//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {//没有应用市场，通过浏览器跳转到Google Play
                val intent2 = Intent(Intent.ACTION_VIEW)
                intent2.data = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())
                if (intent2.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent2)
                } else {
                    //没有Google Play 也没有浏览器
                }
            }
        } catch (activityNotFoundException1: ActivityNotFoundException) {
            context.toast(context.getString(R.string.no_market_no_browser))
        }

    }

}
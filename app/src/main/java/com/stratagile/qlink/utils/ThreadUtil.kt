package com.stratagile.qlink.utils

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig

class ThreadUtil {
    companion object {
        class CreateEnglishQRCode(var userId : String, var view: ImageView, var bitmap: Bitmap?) : AsyncTask<Void, Void, Bitmap>() {
            override fun doInBackground(vararg p0: Void?): Bitmap {
                if (bitmap == null) {
                    return QRCodeEncoder.syncEncodeQRCode(userId, BGAQRCodeUtil.dp2px(AppConfig.instance, 150f), AppConfig.instance.resources.getColor(R.color.black))
                }
                return QRCodeEncoder.syncEncodeQRCode(userId, BGAQRCodeUtil.dp2px(AppConfig.instance, 150f), AppConfig.instance.resources.getColor(R.color.black), bitmap)
            }

            override fun onPostExecute(bitmap: Bitmap?) {
                if (bitmap != null) {
                    view.setImageBitmap(bitmap)
                } else {
//                    Toast.makeText(AppConfig.instance, "生成英文二维码失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
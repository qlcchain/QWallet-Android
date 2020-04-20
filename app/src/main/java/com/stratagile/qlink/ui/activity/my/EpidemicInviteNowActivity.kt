package com.stratagile.qlink.ui.activity.my

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.ui.activity.my.component.DaggerEpidemicInviteNowComponent
import com.stratagile.qlink.ui.activity.my.contract.EpidemicInviteNowContract
import com.stratagile.qlink.ui.activity.my.module.EpidemicInviteNowModule
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicInviteNowPresenter
import com.stratagile.qlink.utils.FireBaseUtils
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_epidemic_invite_now.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2020/04/17 10:25:30
 */

class EpidemicInviteNowActivity : BaseActivity(), EpidemicInviteNowContract.View {

    @Inject
    internal lateinit var mPresenter: EpidemicInviteNowPresenter
    internal var content = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_epidemic_invite_now)
    }
    override fun initData() {
        setTitle(getString(R.string.invite_now))

        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
            //英文
            llShare.setBackground(resources.getDrawable(R.mipmap.me_invitation_en))
        } else {
            llShare.setBackground(resources.getDrawable(R.mipmap.me_invitation_ch))
        }

        Glide.with(this)
                .load(R.mipmap.qwallet_qrcode)
                .apply(AppConfig.getInstance().optionsAppeal)
                .into(ivQRCode)
        inviteCode.setText(ConstantValue.currentUser.inviteCode + "")
        inviteCode.setOnClickListener(View.OnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", ConstantValue.currentUser.id!!.toString() + "")
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share) {
            FireBaseUtils.logEvent(this, FireBaseUtils.Topup_Home_MyReferralCode_Share)
            llShare.isDrawingCacheEnabled = true
            llShare.buildDrawingCache()
            val bgimg0 = Bitmap.createBitmap(llShare.drawingCache)
            var share_intent = Intent()
            share_intent.action = Intent.ACTION_SEND//设置分享行为
            share_intent.type = "image/*"  //设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bgimg0, content))
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "share")
            startActivity(share_intent)
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 将图片存到本地
     */
    private fun saveBitmap(bm: Bitmap, picName: String): Uri? {
        try {
            val dir = Environment.getExternalStorageDirectory().absolutePath + "/Qwallet/image/" + picName + ".jpg"
            val f = File(dir)
            if (!f.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            }
            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            val uri: Uri
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(this, "com.stratagile.qwallet.fileprovider", f)
            } else {
                uri = Uri.fromFile(f)
            }
            return uri
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    override fun setupActivityComponent() {
       DaggerEpidemicInviteNowComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .epidemicInviteNowModule(EpidemicInviteNowModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: EpidemicInviteNowContract.EpidemicInviteNowContractPresenter) {
            mPresenter = presenter as EpidemicInviteNowPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
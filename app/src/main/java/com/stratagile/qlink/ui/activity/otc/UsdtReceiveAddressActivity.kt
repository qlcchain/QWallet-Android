package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.entity.QrEntity
import com.stratagile.qlink.ui.activity.otc.component.DaggerUsdtReceiveAddressComponent
import com.stratagile.qlink.ui.activity.otc.contract.UsdtReceiveAddressContract
import com.stratagile.qlink.ui.activity.otc.module.UsdtReceiveAddressModule
import com.stratagile.qlink.ui.activity.otc.presenter.UsdtReceiveAddressPresenter
import com.stratagile.qlink.utils.ThreadUtil
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_usdt_receive_address.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/17 16:50:34
 */

class UsdtReceiveAddressActivity : BaseActivity(), UsdtReceiveAddressContract.View {

    @Inject
    internal lateinit var mPresenter: UsdtReceiveAddressPresenter

    lateinit var qrEntity: QrEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_usdt_receive_address)
        title.text = "USDT Receivable Address"
    }
    override fun initData() {
        qrEntity = intent.getParcelableExtra("qrentity")
        val logo = BitmapFactory.decodeResource(resources, resources.getIdentifier("usdt", "mipmap", packageName))
        val createEnglishQRCode = ThreadUtil.Companion.CreateEnglishQRCode(qrEntity.content, ivQRCode, logo)
        createEnglishQRCode.execute()
        tvWalletAddess.text = qrEntity.content
        understand.setOnClickListener {
            var intent1 = Intent(this, UsdtPayActivity::class.java)
            intent1.putExtra("usdt", intent.getStringExtra("usdt"))
            intent1.putExtra("receiveAddress", intent.getStringExtra("receiveAddress"))
            intent1.putExtra("tradeOrderId", intent.getStringExtra("tradeOrderId"))
            intent1.putExtra("orderNumber", intent.getStringExtra("orderNumber"))
            startActivityForResult(intent1, 0)
        }

        tvWalletAddess.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", tvWalletAddess.text.toString())
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
            ToastUtil.displayShortToast(getString(R.string.copy_success))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share) {
            parent1.setDrawingCacheEnabled(true)
            parent1.buildDrawingCache()
            val bgimg0 = Bitmap.createBitmap(parent1.getDrawingCache())
            var share_intent = Intent()
            share_intent.action = Intent.ACTION_SEND//设置分享行为
            share_intent.type = "image/*"  //设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bgimg0, qrEntity.content))
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "share")
            startActivity(share_intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            finish()
        }
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
                uri = FileProvider.getUriForFile(this, "com.stratagile.qlink.dapp.fileprovider", f)
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
       DaggerUsdtReceiveAddressComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .usdtReceiveAddressModule(UsdtReceiveAddressModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: UsdtReceiveAddressContract.UsdtReceiveAddressContractPresenter) {
            mPresenter = presenter as UsdtReceiveAddressPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
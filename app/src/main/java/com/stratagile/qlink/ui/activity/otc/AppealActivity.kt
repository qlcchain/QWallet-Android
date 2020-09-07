package com.stratagile.qlink.ui.activity.otc

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.pawegio.kandroid.toast
import com.socks.library.KLog
import com.stratagile.qlink.R

import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.base.BaseActivity
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.otc.TradeOrderDetail
import com.stratagile.qlink.ui.activity.otc.component.DaggerAppealComponent
import com.stratagile.qlink.ui.activity.otc.contract.AppealContract
import com.stratagile.qlink.ui.activity.otc.module.AppealModule
import com.stratagile.qlink.ui.activity.otc.presenter.AppealPresenter
import com.stratagile.qlink.ui.adapter.AppealUploadImgItemDecoration
import com.stratagile.qlink.ui.adapter.otc.AppealImgAdapter
import com.stratagile.qlink.ui.adapter.otc.ImageEntity
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.utils.LogUtil
import com.stratagile.qlink.utils.SystemUtil
import com.stratagile.qlink.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_appeal.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: $description
 * @date 2019/07/19 11:44:36
 */

class AppealActivity : BaseActivity(), AppealContract.View {
    override fun generateAppealSuccess() {
        toast(getString(R.string.success))
        closeProgressDialog()
        finish()
    }

    @Inject
    internal lateinit var mPresenter: AppealPresenter
    lateinit var mTradeOrderDetail: TradeOrderDetail.OrderBean
    lateinit var appealImgAdapter: AppealImgAdapter
    lateinit var galleryPackName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        mainColor = R.color.white
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_appeal)
    }
    override fun initData() {
        title.text = getString(R.string.order_appeal)
        mTradeOrderDetail = intent.getParcelableExtra("tradeOrder")
        galleryPackName = SystemUtil.getSystemPackagesName(this, "gallery")
        if ("" == galleryPackName) {
            galleryPackName = SystemUtil.getSystemPackagesName(this, "gallery3d")
        }
        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (p0.toString().length > 100) {
                    etContent.setText(p0.toString().substring(0, 100))
                    etContent.setSelection(100)
                    tvTypeCount.text = "100/100"
                } else {
                    tvTypeCount.text = p0.length.toString() + "/100"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
        var list = arrayListOf<ImageEntity>()
        list.add(ImageEntity(0))
        appealImgAdapter = AppealImgAdapter(list)
        appealImgAdapter.isSee = false
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.addItemDecoration(AppealUploadImgItemDecoration(resources.getDimension(R.dimen.x6).toInt()))
        recyclerView.adapter = appealImgAdapter
        appealImgAdapter.setOnItemClickListener { adapter, view, position ->
            if (!appealImgAdapter.data[position].isSet) {
                //
                var intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (galleryPackName != "") {
                    intent.setPackage(galleryPackName)
                }
                //这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
                try {
                    startActivityForResult(intent, position)
                } catch (e: ActivityNotFoundException) {
                    intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, position)
                } finally {

                }
            }
        }
        appealImgAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.ivDelete) {
                appealImgAdapter.data.removeAt(position)
                appealImgAdapter.notifyDataSetChanged()
            }
        }

        tvSubmit.setOnClickListener {
            if (etContent.text.toString().equals("")) {
                return@setOnClickListener
            }
            showProgressDialog()
            var accountRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), ConstantValue.currentUser.account)
            var tokenRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), AccountUtil.getUserToken())
            var tradeOrderIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), mTradeOrderDetail.id)
            var reasonRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), etContent.text.toString().trim())
            var partBodyList = arrayListOf<MultipartBody.Part?>()
            for (i in 1..4) {
                if (appealImgAdapter.data.size >= i && appealImgAdapter.data[i - 1].isSet) {
                    var file = File(Environment.getExternalStorageDirectory().path + "/QWallet/otc/" + appealImgAdapter.data[i - 1].name)
                    val passport1 = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
                    val multipartBody = MultipartBody.Part.createFormData("photo" + i, appealImgAdapter.data[i - 1].name, passport1)
                    partBodyList.add(multipartBody)
                } else {
                    partBodyList.add(null)
                }
            }
            mPresenter.generateAppeal(accountRequestBody, tokenRequestBody, tradeOrderIdRequestBody, reasonRequestBody, partBodyList[0], partBodyList[1], partBodyList[2], partBodyList[3])
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //我们需要判断requestCode是否是我们之前传给startActivityForResult()方法的RESULT_LOAD_IMAGE，并且返回的数据不能为空
        if (requestCode < 100 && resultCode == Activity.RESULT_OK && null != data) {
            startPhotoZoom(data.data, requestCode)
        } else if (requestCode >= 100){
            appealImgAdapter.setCount++
            var fileName = mTradeOrderDetail.number + (requestCode - 100) + ".png"
            appealImgAdapter.data[requestCode - 100].name = fileName
            appealImgAdapter.data[requestCode - 100].isSet = true
            appealImgAdapter.notifyItemChanged(requestCode - 100)
            if (appealImgAdapter.data.size < 4) {
                appealImgAdapter.addData(ImageEntity(appealImgAdapter.setCount))
            }
        }
    }

    fun startPhotoZoom(uri: Uri?, position: Int) {
        try {
            var intent = Intent("com.android.camera.action.CROP")
            if (galleryPackName != "") {
                intent.setPackage(galleryPackName)
            }
            KLog.i(Environment.getExternalStorageDirectory().path)
            var file = File(Environment.getExternalStorageDirectory().path +  "/QWallet/otc/" + mTradeOrderDetail.number + position + ".png")
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            var outputFile = Uri.fromFile(file)
            intent.setDataAndType(uri, "image/*")
            intent.putExtra("crop", true)
//            intent.putExtra("aspectX", 326)
//            intent.putExtra("aspectY", 246)
            intent.putExtra("return-data", false)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile)  //imageurl 文件输出的位置
            intent.putExtra("noFaceDetection", true)
            try {
                startActivityForResult(intent, position + 100)
            } catch (e: ActivityNotFoundException) {
                intent = Intent("com.android.camera.action.CROP")
                intent.setDataAndType(uri, "image/*")
                intent.putExtra("crop", true)
//                intent.putExtra("aspectX", 326)
//                intent.putExtra("aspectY", 246)
                intent.putExtra("return-data", false)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile)  //imageurl 文件输出的位置
                intent.putExtra("noFaceDetection", true)
                startActivityForResult(intent, position + 100)
            } finally {

            }
        } catch (e: Exception) {
            try {
                val stringWriter = StringWriter()
                e.printStackTrace(PrintWriter(stringWriter))
                LogUtil.addLog("图像错误：$stringWriter", javaClass.simpleName)
            } catch (el: Exception) {

            }

            ToastUtil.displayShortToast(getString(R.string.loadPicError))
        }

    }

    override fun setupActivityComponent() {
       DaggerAppealComponent
               .builder()
               .appComponent((application as AppConfig).applicationComponent)
               .appealModule(AppealModule(this))
               .build()
               .inject(this)
    }
    override fun setPresenter(presenter: AppealContract.AppealContractPresenter) {
            mPresenter = presenter as AppealPresenter
        }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun closeProgressDialog() {
        progressDialog.hide()
    }

}
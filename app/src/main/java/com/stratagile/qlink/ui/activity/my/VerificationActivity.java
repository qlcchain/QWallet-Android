package com.stratagile.qlink.ui.activity.my;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.entity.otc.Passport;
import com.stratagile.qlink.ui.activity.my.component.DaggerVerificationComponent;
import com.stratagile.qlink.ui.activity.my.contract.VerificationContract;
import com.stratagile.qlink.ui.activity.my.module.VerificationModule;
import com.stratagile.qlink.ui.activity.my.presenter.VerificationPresenter;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.utils.KotlinConvertJavaUtils;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.RxFileTool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/06/14 15:10:49
 */

public class VerificationActivity extends BaseActivity implements VerificationContract.View {

    @Inject
    VerificationPresenter mPresenter;

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    Uri inputUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    Uri outputFile = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    String galleryPackName;
    @BindView(R.id.passport1)
    ImageView passport1;
    @BindView(R.id.passport2)
    ImageView passport2;
    @BindView(R.id.submit)
    TextView submit;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Verification");
    }

    @Override
    protected void initData() {
        if (ConstantValue.currentUser.getVstatus().equals("KYC_FAIL")) {
            KotlinConvertJavaUtils.INSTANCE.showNotApprovedDialog(this);
        }
        File lastFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/passport1.jpg", "");
        if (lastFile.exists()) {
            lastFile.delete();
        }
        File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/passport2.jpg", "");
        if (dataFile.exists()) {
            dataFile.delete();
        }
        galleryPackName = SystemUtil.getSystemPackagesName(this, "gallery");
        if ("".equals(galleryPackName)) {
            galleryPackName = SystemUtil.getSystemPackagesName(this, "gallery3d");
        }
        String strCamera = "";
        List<PackageInfo> packages = this.getPackageManager()
                .getInstalledPackages(0);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/temp.jpg");
            inputUri = RxFileTool.getUriForFile(this, tempFile);
            outputFile = Uri.fromFile(tempFile);
        }
        //验证状态[NOT_UPLOAD/未上传,UPLOADED/已上传,KYC_SUCCESS/KYC成功,KYC_FAIL/KYC失败]
        if (!ConstantValue.currentUser.getVstatus().equals("NOT_UPLOAD")) {
            submit.setVisibility(View.GONE);
            Glide.with(this)
                    .load(API.BASE_URL + ConstantValue.currentUser.getFacePhoto())
                    .apply(AppConfig.getInstance().optionsAppeal)
                    .into((ImageView) passport1);
            Glide.with(this)
                    .load(API.BASE_URL + ConstantValue.currentUser.getHoldingPhoto())
                    .apply(AppConfig.getInstance().optionsAppeal)
                    .into((ImageView) passport2);
            passport1.setEnabled(false);
            passport2.setEnabled(false);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerVerificationComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .verificationModule(new VerificationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(VerificationContract.VerificationContractPresenter presenter) {
        mPresenter = (VerificationPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void uploadImgSuccess(Passport upLoadAvatar) {
        ConstantValue.currentUser.setVstatus("UPLOADED");
        ConstantValue.currentUser.setFacePhoto(upLoadAvatar.getFacePhoto());
        ConstantValue.currentUser.setHoldingPhoto(upLoadAvatar.getHoldingPhoto());
        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(ConstantValue.currentUser);
        ToastUtil.displayShortToast("upload success");
        closeProgressDialog();
        KotlinConvertJavaUtils.INSTANCE.showUploadedDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//我们需要判断requestCode是否是我们之前传给startActivityForResult()方法的RESULT_LOAD_IMAGE，并且返回的数据不能为空
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData(), 1);
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            bitmap = decodeUriAsBitmap(inputUri);
            if (bitmap != null) {
                saveBitmap(bitmap, 1);
                passport1.setImageBitmap(bitmap);
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData(), 3);
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            bitmap = decodeUriAsBitmap(inputUri);
            if (bitmap != null) {
                saveBitmap(bitmap, 2);
                passport2.setImageBitmap(bitmap);
            }
        }
    }

    //以bitmap返回格式解析uri
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public void startPhotoZoom(Uri uri, int requestCode) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (!galleryPackName.equals("")) {
                intent.setPackage(galleryPackName);
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 484);
            intent.putExtra("aspectY", 300);
            intent.putExtra("return-data", false);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);  //imageurl 文件输出的位置
            intent.putExtra("noFaceDetection", true);
            try {
                startActivityForResult(intent, requestCode);
            } catch (ActivityNotFoundException e) {
                intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("crop", true);
                intent.putExtra("aspectX", 484);
                intent.putExtra("aspectY", 300);
                intent.putExtra("return-data", false);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);  //imageurl 文件输出的位置
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, requestCode);
            } finally {

            }
        } catch (Exception e) {
            try {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                LogUtil.addLog("图像错误：" + stringWriter.toString(), getClass().getSimpleName());
            } catch (Exception el) {

            }
            ToastUtil.displayShortToast(getString(R.string.loadPicError));
        }
    }


    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @return
     */
    public void saveBitmap(Bitmap mBitmap, int requestCode) {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File lastFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/passport" + requestCode + ".jpg", "");
                    if (lastFile.exists()) {
                        lastFile.delete();
                    }
                    File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image", "");
                    if (!dataFile.exists()) {
                        dataFile.mkdir();
                    }
                    File filePic;
                    filePic = new File(dataFile.getPath() + "/" + "passport" + requestCode + ".jpg");
                    if (!filePic.exists()) {
                        filePic.getParentFile().mkdirs();
                        filePic.createNewFile();
                    }
                    // 最大图片大小 100KB
                    int maxSize = 150;
                    FileOutputStream fos = new FileOutputStream(filePic);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int options = 100;
                    // 获取尺寸压缩倍数
                    int ratio = getRatioSize(mBitmap.getWidth(), mBitmap.getHeight());
                    KLog.i("获取尺寸压缩倍数" + ratio);
                    // 压缩Bitmap到对应尺寸
                    Bitmap result = Bitmap.createBitmap(mBitmap.getWidth() / ratio, mBitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Rect rect = new Rect(0, 0, mBitmap.getWidth() / ratio, mBitmap.getHeight() / ratio);
                    canvas.drawBitmap(mBitmap, null, rect, null);
                    result.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    while (baos.toByteArray().length / 1024 > maxSize) {
                        if (options < 20) {
                            break;
                        }
                        // 重置baos即清空baos
                        baos.reset();
                        // 每次都减少10
                        options -= 10;
                        // 这里压缩options%，把压缩后的数据存放到baos中
                        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    }
                    baos.writeTo(fos);
                    fos.flush();
                    fos.close();
                    KLog.i(filePic.getName());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });
//                    mPresenter.uploadImg(new HashMap<String, String>());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight = 540;
        int imageWidth = 540;
        // 缩放比
        int ratio = 1;
        KLog.i(bitWidth);
        KLog.i(bitHeight);
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth <= bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    @OnClick({R.id.passport1, R.id.passport2, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.passport1:
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (!galleryPackName.equals("")) {
                    intent.setPackage(galleryPackName);
                }
                //这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
                try {
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                } finally {

                }
                break;
            case R.id.passport2:
                Intent intent1 = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (!galleryPackName.equals("")) {
                    intent1.setPackage(galleryPackName);
                }
                //这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
                try {
                    startActivityForResult(intent1, 2);
                } catch (ActivityNotFoundException e) {
                    intent1 = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1, 2);
                } finally {

                }
                break;
            case R.id.submit:
                File lastFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/passport1.jpg", "");
                if (!lastFile.exists()) {
                    ToastUtil.displayShortToast("please select facephoto");
                    return;
                }
                File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/passport2.jpg", "");
                if (!dataFile.exists()) {
                    ToastUtil.displayShortToast("please select holdphoto");
                    return;
                }
                showProgressDialog();
                mPresenter.uploadImg(new HashMap<String, String>());
                break;
            default:
                break;
        }
    }
}
package com.stratagile.qlink.ui.activity.wallet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.eventbus.UpdateAvatar;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerProfilePictureComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ProfilePictureContract;
import com.stratagile.qlink.ui.activity.wallet.module.ProfilePictureModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ProfilePicturePresenter;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.RxFileTool;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/30 15:17:54
 */

public class ProfilePictureActivity extends BaseActivity implements ProfilePictureContract.View {

    @Inject
    ProfilePicturePresenter mPresenter;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.bt_skip)
    Button btSkip;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Bitmap bitmap;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    Uri inputUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    Uri outputFile = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    String galleryPackName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_profile_picture);
        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
//        File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/useImage.jpg", "");
//        if (dataFile.exists()) {
        galleryPackName = SystemUtil.getSystemPackagesName(this,"gallery");
        if("".equals(galleryPackName))
        {
            galleryPackName = SystemUtil.getSystemPackagesName(this,"gallery3d");
        }
        String strCamera = "";
        List<PackageInfo> packages = this.getPackageManager()
                .getInstalledPackages(0);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory()+"/Qwallet/image/temp.jpg");
            inputUri = RxFileTool.getUriForFile(this, tempFile);
            outputFile = Uri.fromFile(tempFile);
        }

        if (!"".equals(ConstantValue.currentUser.getAvatar())) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                        .into(ivPicture);
            } else {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + ConstantValue.currentUser.getAvatar())
                        .into(ivPicture);
            }
        } }

    @Override
    protected void setupActivityComponent() {
        DaggerProfilePictureComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .profilePictureModule(new ProfilePictureModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ProfilePictureContract.ProfilePictureContractPresenter presenter) {
        mPresenter = (ProfilePicturePresenter) presenter;
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
    public void updateImgSuccess(UpLoadAvatar upLoadAvatar) {
        ConstantValue.currentUser.setAvatar(upLoadAvatar.getHead());
        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(ConstantValue.currentUser);
        EventBus.getDefault().post(new UpdateAvatar());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @OnClick({R.id.iv_picture, R.id.bt_skip, R.id.bt_save, R.id.iv_flag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_picture:

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(!galleryPackName.equals("")){
                    intent.setPackage(galleryPackName);
                }
                //这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
                try
                {
                    startActivityForResult(intent, 0);
                }catch (ActivityNotFoundException e)
                {
                    intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                }finally {

                }

                break;
            case R.id.bt_skip:
                finish();
                break;
            case R.id.bt_save:
                if (bitmap != null) {
                    saveBitmap(bitmap);
                }
                break;
            case R.id.iv_flag:
                mPresenter.upLoadImg();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//我们需要判断requestCode是否是我们之前传给startActivityForResult()方法的RESULT_LOAD_IMAGE，并且返回的数据不能为空
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
            startPhotoZoom(data.getData());
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            bitmap = decodeUriAsBitmap(inputUri);
//            bitmap = rotateBitmapByDegree(bitmap, getBitmapDegree(imageFile.getPath()));
            Glide.with(this)
                    .load(bitmap)
                    .into(ivPicture);
            if (bitmap != null) {
                saveBitmap(bitmap);
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

    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @return
     */
    public void saveBitmap(Bitmap mBitmap) {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File lastFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image/" + SpUtil.getString(ProfilePictureActivity.this, ConstantValue.myAvaterUpdateTime, "") + ".jpg", "");
                    if (lastFile.exists()) {
                        lastFile.delete();
                    }
                    File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/image", "");
                    if (!dataFile.exists()) {
                        dataFile.mkdir();
                    }
                    File filePic;
                    filePic = new File(dataFile.getPath() + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                    if (!filePic.exists()) {
                        filePic.getParentFile().mkdirs();
                        filePic.createNewFile();
                    }
                    // 最大图片大小 100KB
                    int maxSize = 50;
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
                    SpUtil.putString(ProfilePictureActivity.this, ConstantValue.myAvaterUpdateTime, filePic.getName().substring(0, filePic.getName().lastIndexOf(".")));
                    mPresenter.upLoadImg();
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

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public void startPhotoZoom(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if(!galleryPackName.equals("")){
                intent.setPackage(galleryPackName);
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", false);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);  //imageurl 文件输出的位置
            intent.putExtra("noFaceDetection", true);
            try
            {
                startActivityForResult(intent, 1);
            }catch (ActivityNotFoundException e)
            {
                intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("crop", true);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", false);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);  //imageurl 文件输出的位置
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, 1);
            }finally {

            }
        }catch (Exception e) {
            try {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                LogUtil.addLog("图像错误：" + stringWriter.toString(), getClass().getSimpleName());
            } catch (Exception el)
            {

            }
            ToastUtil.displayShortToast(getString(R.string.loadPicError));
        }

    }
}
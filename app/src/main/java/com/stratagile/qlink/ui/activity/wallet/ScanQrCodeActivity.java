package com.stratagile.qlink.ui.activity.wallet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerScanQrCodeComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ScanQrCodeContract;
import com.stratagile.qlink.ui.activity.wallet.module.ScanQrCodeModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ScanQrCodePresenter;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.vondear.rxtools.RxAnimationTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxQrBarTool;
import com.vondear.rxtools.interfaces.OnRxScanerListener;
import com.vondear.rxtools.module.scaner.CameraManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.RationaleListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/03/05 17:42:26
 */

public class ScanQrCodeActivity extends BaseActivity implements ScanQrCodeContract.View, QRCodeView.Delegate {

    @Inject
    ScanQrCodePresenter mPresenter;
    private static OnRxScanerListener mScanerListener;//扫描结果监听
    @BindView(R.id.mZXingView)
    ZXingView mZXingView;
    @BindView(R.id.top_mask)
    ImageView topMask;
    @BindView(R.id.top_openpicture)
    ImageView topOpenpicture;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.capture_containter)
    RelativeLayout captureContainter;
    private boolean vibrate = true;//扫描成功后是否震动
    private boolean mFlashing = false;//闪光灯开启状态

    public static void setScanerListener(OnRxScanerListener scanerListener) {
        mScanerListener = scanerListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.scan));
        setContentView(R.layout.activity_scan_qr_code);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mZXingView.setDelegate(this);
        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setupActivityComponent() {
        DaggerScanQrCodeComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .scanQrCodeModule(new ScanQrCodeModule(this))
                .build()
                .inject(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPermission();
    }

    @Override
    public void setPresenter(ScanQrCodeContract.ScanQrCodeContractPresenter presenter) {
        mPresenter = (ScanQrCodePresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }


    public void btn(View view) {
        int viewId = view.getId();
        if (viewId == com.vondear.rxtools.R.id.top_mask) {
            light();
        } else if (viewId == com.vondear.rxtools.R.id.top_back) {
            finish();
        } else if (viewId == com.vondear.rxtools.R.id.top_openpicture) {
            RxPhotoTool.openLocalImage(this);
        }
    }

    private void light() {
        if (!mFlashing) {
            mFlashing = false;
            // 开闪光灯
            mZXingView.openFlashlight();
        } else {
            mFlashing = true;
            // 关闪光灯
            mZXingView.closeFlashlight(); // 打开闪光灯
        }

    }

    //========================================打开本地图片识别二维码 end=================================

    //--------------------------------------打开本地图片识别二维码 start---------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                mZXingView.decodeQRCode(photo);
                // 开始对图像资源解码
//                Result rawResult = RxQrBarTool.decodeFromPhoto(photo);
//                if (rawResult != null) {
//                    if (mScanerListener == null) {
//                        initDialogResult(rawResult);
//                    } else {
//                        mScanerListener.onSuccess("From to Picture", rawResult);
//                    }
//                } else {
//                    if (mScanerListener == null) {
////                        RxToast.error("图片识别失败.");
//                    } else {
//                        mScanerListener.onFail("From to Picture", "图片识别失败");
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //==============================================================================================解析结果 及 后续处理 end

    private void initDialogResult(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(R.string.result);
        tvContent.setText(result);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setText(getString(R.string.cancel).toLowerCase());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mZXingView.startSpot(); // 延迟0.5秒后开始识别
                // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("result", result);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });
        dialog.show();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        KLog.i( "result:$result");
        initDialogResult(result);
        mZXingView.stopSpot();
        vibrate();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{50, 50}, -1);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    public void getCameraPermissionSuccess() {
        mZXingView.startSpotAndShowRect();
    }

    public void getPermission() {
        AndPermission.with(this)
                .requestCode(101)
                .permission(
                        Manifest.permission.CAMERA
                )
                .rationale(rationaleListener)
                .callback(permission)
                .start();
    }

    private PermissionListener permission = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            LocalAssetsUtils.updateGreanDaoFromLocal();
            // 权限申请成功回调。
            if (requestCode == 101) {
                getCameraPermissionSuccess();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 101) {
                KLog.i("权限申请失败");

            }
        }
    };

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = (requestCode, rationale) -> {
        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(this)
                .setTitle(AppConfig.getInstance().getResources().getString(R.string.Permission_Requeset))
                .setMessage(AppConfig.getInstance().getResources().getString(R.string.We_Need_Some_Permission_to_continue))
                .setPositiveButton(AppConfig.getInstance().getResources().getString(R.string.Agree), (dialog, which) -> {
                    rationale.resume();
                })
                .setNegativeButton(AppConfig.getInstance().getResources().getString(R.string.Reject), (dialog, which) -> {
                    rationale.cancel();
                }).show();
    };
}
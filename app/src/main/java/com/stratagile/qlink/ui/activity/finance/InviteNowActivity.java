package com.stratagile.qlink.ui.activity.finance;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.ui.activity.finance.component.DaggerInviteNowComponent;
import com.stratagile.qlink.ui.activity.finance.contract.InviteNowContract;
import com.stratagile.qlink.ui.activity.finance.module.InviteNowModule;
import com.stratagile.qlink.ui.activity.finance.presenter.InviteNowPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ThreadUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.view.RxQRCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/28 16:32:00
 */

public class InviteNowActivity extends BaseActivity implements InviteNowContract.View {

    @Inject
    InviteNowPresenter mPresenter;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.inviteCode)
    TextView inviteCode;
    @BindView(R.id.llShare)
    LinearLayout llShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_invite_now);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    String content = "";
    @Override
    protected void initData() {
        setTitle(getString(R.string.invite_now));
        if (SpUtil.getInt(this, ConstantValue.Language, -1) == 0) {
            //英文
            llShare.setBackground(getResources().getDrawable(R.mipmap.invitation_en));
            content = "https://fir.im/qlc1";
        } else {
            llShare.setBackground(getResources().getDrawable(R.mipmap.invitation_ch));
            content = "https://fir.im/qlc1";
        }
        Bitmap logo = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("ic_launcher", "mipmap", getPackageName()));
        ThreadUtil.Companion.CreateEnglishQRCode createEnglishQRCode = new ThreadUtil.Companion.CreateEnglishQRCode(content, ivQRCode, logo);
        createEnglishQRCode.execute();
//        Bitmap bitmap = RxQRCode.builder(content).
//                backColor(getResources().getColor(com.vondear.rxtools.R.color.white)).
//                codeColor(getResources().getColor(com.vondear.rxtools.R.color.black)).
//                codeSide(800).
//                into(ivQRCode);


//        Glide.with(this)
//                .load(R.mipmap.qwallet_qrcode)
//                .apply(AppConfig.getInstance().optionsAppeal)
//                .into(ivQRCode);
        inviteCode.setText(ConstantValue.currentUser.getInviteCode() + "");
        inviteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", ConstantValue.currentUser.getId() + "");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.displayShortToast(getString(R.string.copy_success));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            llShare.setDrawingCacheEnabled(true);
            llShare.buildDrawingCache();
            Bitmap bgimg0 = Bitmap.createBitmap(llShare.getDrawingCache());
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("image/*");  //设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bgimg0, content));
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "share");
            startActivity(share_intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 将图片存到本地
     */
    private Uri saveBitmap(Bitmap bm, String picName) {
        try {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qwallet/image/" + picName + ".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(this, "com.stratagile.qwallet.fileprovider", f);
            } else {
                uri = Uri.fromFile(f);
            }
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void setupActivityComponent() {
        DaggerInviteNowComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .inviteNowModule(new InviteNowModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(InviteNowContract.InviteNowContractPresenter presenter) {
        mPresenter = (InviteNowPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}
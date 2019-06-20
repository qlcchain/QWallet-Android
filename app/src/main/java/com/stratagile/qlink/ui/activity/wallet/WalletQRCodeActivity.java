package com.stratagile.qlink.ui.activity.wallet;

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
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.QrEntity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerWalletQRCodeComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletQRCodeContract;
import com.stratagile.qlink.ui.activity.wallet.module.WalletQRCodeModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletQRCodePresenter;
import com.stratagile.qlink.utils.ThreadUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/30 15:54:27
 */

public class WalletQRCodeActivity extends BaseActivity implements WalletQRCodeContract.View {

    @Inject
    WalletQRCodePresenter mPresenter;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvWalletAddess)
    TextView tvWalletAddess;
    @BindView(R.id.parent)
    ConstraintLayout parent;
    private QrEntity qrEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_wallet_qr_code);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        qrEntity = getIntent().getParcelableExtra("qrentity");
        if (qrEntity.getIcon() != null && !"".equals(qrEntity.getIcon())) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(qrEntity.getIcon(), "mipmap", getPackageName()));
            ThreadUtil.Companion.CreateEnglishQRCode createEnglishQRCode = new ThreadUtil.Companion.CreateEnglishQRCode(qrEntity.getContent(), ivQRCode, logo);
            createEnglishQRCode.execute();
        } else {
            ThreadUtil.Companion.CreateEnglishQRCode createEnglishQRCode = new ThreadUtil.Companion.CreateEnglishQRCode(qrEntity.getContent(), ivQRCode, null);
            createEnglishQRCode.execute();
        }
        setTitle(qrEntity.getTitle());
        tvWalletAddess.setText(qrEntity.getContent());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerWalletQRCodeComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .walletQRCodeModule(new WalletQRCodeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WalletQRCodeContract.WalletQRCodeContractPresenter presenter) {
        mPresenter = (WalletQRCodePresenter) presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            parent.setDrawingCacheEnabled(true);
            parent.buildDrawingCache();
            Bitmap bgimg0 = Bitmap.createBitmap(parent.getDrawingCache());
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("image/*");  //设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bgimg0, qrEntity.getContent()));
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
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qlink/image/" + picName + ".jpg";
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
                uri = FileProvider.getUriForFile(this, "com.stratagile.qlink.dapp.fileprovider", f);
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
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.tvWalletAddess)
    public void onViewClicked() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText("", tvWalletAddess.getText().toString()));
        ToastUtil.displayShortToast(getResources().getString(R.string.copy_success));
    }
}
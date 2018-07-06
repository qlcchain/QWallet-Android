package com.stratagile.qlink.ui.activity.wallet;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerQrCodeDetailComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.QrCodeDetailContract;
import com.stratagile.qlink.ui.activity.wallet.module.QrCodeDetailModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.QrCodeDetailPresenter;
import com.vondear.rxtools.view.RxQRCode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/02/28 15:16:22
 */

public class QrCodeDetailActivity extends BaseActivity implements QrCodeDetailContract.View {

    @Inject
    QrCodeDetailPresenter mPresenter;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qr_code_detail);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white));
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getIntent().getStringExtra("title").toUpperCase());
        if (getIntent().getStringExtra("title").equals("public address")) {
            tvTitle.setText(R.string.Public_Address);
        } else if (getIntent().getStringExtra("title").equals("private key")) {
            tvTitle.setText(R.string.Private_Key_HEX);
        } else if (getIntent().getStringExtra("title").equals("encrypted key")) {
            tvTitle.setText(R.string.Encrypted_Key_WIF);
        }
        String content = getIntent().getStringExtra("content");
        Bitmap bitmap = RxQRCode.builder(content).
                backColor(getResources().getColor(com.vondear.rxtools.R.color.white)).
                codeColor(getResources().getColor(com.vondear.rxtools.R.color.black)).
                codeSide(800).
                into(ivQrCode);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerQrCodeDetailComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .qrCodeDetailModule(new QrCodeDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(QrCodeDetailContract.QrCodeDetailContractPresenter presenter) {
        mPresenter = (QrCodeDetailPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
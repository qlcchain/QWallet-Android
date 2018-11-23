package com.stratagile.qlink.ui.activity.eth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthMnemonicShowComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthMnemonicShowContract;
import com.stratagile.qlink.ui.activity.eth.module.EthMnemonicShowModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthMnemonicShowPresenter;
import com.stratagile.qlink.view.SweetAlertDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/23 15:34:11
 */

public class EthMnemonicShowActivity extends BaseActivity implements EthMnemonicShowContract.View {

    @Inject
    EthMnemonicShowPresenter mPresenter;
    @BindView(R.id.tvMnemonic)
    TextView tvMnemonic;
    @BindView(R.id.btBackup)
    Button btBackup;
    private EthWallet ethWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_mnemonic_show);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ethWallet = getIntent().getParcelableExtra("wallet");
        if (ethWallet.getMnemonic() == null || ethWallet.isBackup()) {
            btBackup.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        tvMnemonic.setText(ethWallet.getMnemonic());
        setTitle("Backup Mnemonic");
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthMnemonicShowComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethMnemonicShowModule(new EthMnemonicShowModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthMnemonicShowContract.EthMnemonicShowContractPresenter presenter) {
        mPresenter = (EthMnemonicShowPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    private void showBackupDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        ImageView imageView = view.findViewById(R.id.ivTitle);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.careful));
        tvContent.setText("The exposure of the mnemonic code will cause the loss of the assets, please copy carefully, do not take a screenshot! ");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvOk = view.findViewById(R.id.tvOpreate);
        tvOk.setText("OK");
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EthMnemonicShowActivity.this, EthMnemonicbackupActivity.class).putExtra("wallet", ethWallet), 0);
                sweetAlertDialog.cancel();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
            }
        });
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
    }


    @OnClick(R.id.btBackup)
    public void onViewClicked() {
        showBackupDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
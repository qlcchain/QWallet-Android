package com.stratagile.qlink.ui.activity.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerExportEthKeyStoreComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ExportEthKeyStoreContract;
import com.stratagile.qlink.ui.activity.wallet.module.ExportEthKeyStoreModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ExportEthKeyStorePresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/11/06 17:23:06
 */

public class ExportEthKeyStoreActivity extends BaseActivity implements ExportEthKeyStoreContract.View {

    @Inject
    ExportEthKeyStorePresenter mPresenter;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvCopy)
    TextView tvCopy;

    private EthWallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_export_eth_key_store);
        ButterKnife.bind(this);
        setTitle("Export Keystore");
    }

    @Override
    protected void initData() {
        wallet = getIntent().getParcelableExtra("wallet");
        tvContent.setText(ETHWalletUtils.deriveKeystore(wallet.getId()));
    }

    @Override
    protected void setupActivityComponent() {
        DaggerExportEthKeyStoreComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .exportEthKeyStoreModule(new ExportEthKeyStoreModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ExportEthKeyStoreContract.ExportEthKeyStoreContractPresenter presenter) {
        mPresenter = (ExportEthKeyStorePresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.tvCopy)
    public void onViewClicked() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText("", tvContent.getText().toString()));
        ToastUtil.displayShortToast("copy success");
    }
}
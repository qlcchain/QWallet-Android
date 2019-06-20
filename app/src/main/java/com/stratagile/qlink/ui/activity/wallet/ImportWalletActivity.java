package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.ui.activity.neo.WalletCreatedActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerImportWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ImportWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.ImportWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ImportWalletPresenter;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/23 14:24:49
 */

public class ImportWalletActivity extends BaseActivity implements ImportWalletContract.View {

    @Inject
    ImportWalletPresenter mPresenter;
    @BindView(R.id.etPrivateKey)
    EditText etPrivateKey;
    @BindView(R.id.tvWhatsPrivateKey)
    TextView tvWhatsPrivateKey;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.btImport)
    Button btImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Import Neo Wallet");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
        DaggerImportWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .importWalletModule(new ImportWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ImportWalletContract.ImportWalletContractPresenter presenter) {
        mPresenter = (ImportWalletPresenter) presenter;
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
    public void onImportWalletSuccess(CreateWallet createWallet) {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        for (Wallet wallet : walletList) {
            if (wallet.getPrivateKey().equals(createWallet.getData().getPrivateKey())) {
                ToastUtil.displayShortToast(getString(R.string.this_wallet_is_exist));
                closeProgressDialog();
                finish();
                return;
            }
        }
        closeProgressDialog();
        Intent intent = new Intent(this, WalletCreatedActivity.class);
        intent.putExtra("wallet", createWallet.getData());
        intent.putExtra("title", "wallet imported");
        startActivity(intent);
        AppConfig.getInstance().getDaoSession().getWalletDao().insert(createWallet.getData());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void reportCreatedWalletSuccess() {
        closeProgressDialog();
//        EventBus.getDefault().post(new ChangeWallet());
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrcode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.qrcode) {
            startActivityForResult(new Intent(this, ScanQrCodeActivity.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            etPrivateKey.setText(data.getStringExtra("result"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btImport, R.id.tvWhatsPrivateKey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvWhatsPrivateKey:

                break;
            case R.id.btImport:
                String privateKey = etPrivateKey.getText().toString().replace(" ", "");
                if (privateKey.equals("")) {
                    ToastUtil.displayShortToast(getResources().getString(R.string.privatekeyisnone));
                    return;
                }
                if (privateKey.length() >= 42) {
                    for (Wallet wallet : AppConfig.getInstance().getDaoSession().getWalletDao().loadAll()) {
//                        KLog.i(wallet.toString());
                        if (privateKey.toLowerCase().equals(wallet.getPrivateKey().toLowerCase()) || privateKey.equals(wallet.getWif())) {
                            ToastUtil.displayShortToast(getString(R.string.this_wallet_is_exist));
                            return;
                        }
                    }

                    showProgressDialog();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean result = Account.INSTANCE.fromWIF(privateKey);
                            if (!result) {
                                result = Account.INSTANCE.fromHex(privateKey);
                            }
                            if (result) {
                                List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                                for (Wallet wallet : wallets) {
                                    if (wallet.getIsCurrent()) {
                                        wallet.setIsCurrent(false);
                                        AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                                        break;
                                    }
                                }
                                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                                for (EthWallet wallet : ethWallets) {
                                    if (wallet.getIsCurrent()) {
                                        wallet.setIsCurrent(false);
                                        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallet);
                                        break;
                                    }
                                }
                                List<EosAccount> wallets2 = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
                                if (wallets2 != null && wallets2.size() != 0) {
                                    for (int i = 0; i < wallets2.size(); i++) {
                                        if (wallets2.get(i).getIsCurrent()) {
                                            wallets2.get(i).setIsCurrent(false);
                                            AppConfig.getInstance().getDaoSession().getEosAccountDao().update(wallets2.get(i));
                                            break;
                                        }
                                    }
                                }
                                neoutils.Wallet wallet = Account.INSTANCE.getWallet();
                                Wallet walletWinq = new Wallet();
                                walletWinq.setAddress(wallet.getAddress());
                                walletWinq.setWif(wallet.getWIF());
                                if (wallets.size() < 9) {
                                    walletWinq.setName("NEO-Wallet 0" + (wallets.size() + 1));
                                } else {
                                    walletWinq.setName("NEO-Wallet " + (wallets.size() + 1));
                                }
                                walletWinq.setPrivateKey(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()).toLowerCase());
                                walletWinq.setPublicKey(Account.INSTANCE.byteArray2String(wallet.getPublicKey()));
                                walletWinq.setScriptHash(Account.INSTANCE.byteArray2String(wallet.getHashedSignature()));
                                walletWinq.setIsCurrent(true);
                                AppConfig.getInstance().getDaoSession().getWalletDao().insert(walletWinq);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPresenter.reportWalletCreated(walletWinq.getAddress(), "NEO", walletWinq.getPublicKey());
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.displayShortToast("import wallet error");
                                        closeProgressDialog();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    ToastUtil.displayShortToast(getResources().getString(R.string.privatekeyerror));
                }
                break;
            default:
                break;
        }
    }
}
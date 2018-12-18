package com.stratagile.qlink.ui.activity.eos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.cypto.ec.EosPrivateKey;
import com.stratagile.qlink.blockchain.util.PublicAndPrivateKeyUtils;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosImportComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosImportContract;
import com.stratagile.qlink.ui.activity.eos.module.EosImportModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosImportPresenter;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.SweetAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: $description
 * @date 2018/11/26 17:06:38
 */

public class EosImportActivity extends BaseActivity implements EosImportContract.View {

    @Inject
    EosImportPresenter mPresenter;
    @BindView(R.id.accountOwnerKey)
    EditText accountOwnerKey;
    @BindView(R.id.accountActiveKey)
    EditText accountActiveKey;
    @BindView(R.id.btImport)
    Button btImport;

    private EosPrivateKey mOwnerKey;
    private EosPrivateKey mActiveKey;

    EosAccount eosAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mainColor = R.color.white;
        setContentView(R.layout.activity_eos_import);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("Import Eos Account");
        eosAccount = new EosAccount();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEosImportComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosImportModule(new EosImportModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosImportContract.EosImportContractPresenter presenter) {
        mPresenter = (EosImportPresenter) presenter;
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
    public void accountInfoBack(EosAccountInfo eosAccountInfo) {
        boolean noError = true;
        for (EosAccountInfo.DataBeanX.DataBean.PermissionsBean permissionsBean : eosAccountInfo.getData().getData().getPermissions()) {
            if ("owner".equals(permissionsBean.getPerm_name()) && eosAccount.getOwnerPublicKey() != null) {
                if (permissionsBean.getRequired_auth().getKeys().get(0).getKey().equals(mOwnerKey.getPublicKey().toString())) {

                } else {
                    noError = false;
                }
            }
            if ("active".equals(permissionsBean.getPerm_name()) && eosAccount.getActivePublicKey() != null) {
                if (permissionsBean.getRequired_auth().getKeys().get(0).getKey().equals(mActiveKey.getPublicKey().toString())) {

                } else {
                    noError = false;
                }
            }
        }
        List<Wallet> wallets1 = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (wallets1 != null && wallets1.size() != 0 ) {
            for (int i = 0; i < wallets1.size(); i++) {
                if (wallets1.get(i).getIsCurrent()) {
                    wallets1.get(i).setIsCurrent(false);
                    AppConfig.getInstance().getDaoSession().getWalletDao().update(wallets1.get(i));
                    break;
                }
            }

        }
        List<EthWallet> wallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        if (wallets != null && wallets.size() != 0) {
            for (int i = 0; i < wallets.size(); i++) {
                if (wallets.get(i).isCurrent()) {
                    wallets.get(i).setCurrent(false);
                    AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallets.get(i));
                    break;
                }
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

        if (noError) {
            eosAccount.setIsCurrent(true);
            eosAccount.setWalletName(getEosWalletName());
            eosAccount.setAccountPassword(SpUtil.getString(this, ConstantValue.walletPassWord, ""));
            AppConfig.getInstance().getDaoSession().getEosAccountDao().insert(eosAccount);
            if (eosAccount.getOwnerPrivateKey() != null) {
                mPresenter.reportWalletCreated(eosAccount.getAccountName(), eosAccount.getOwnerPublicKey(), eosAccount.getOwnerPrivateKey());
            } else {
                mPresenter.reportWalletCreated(eosAccount.getAccountName(), eosAccount.getActivePublicKey(), eosAccount.getActivePrivateKey());
            }
        } else {
            ToastUtil.displayShortToast("ownerKey or activeKey error");
        }
    }

    @NonNull
    public static String getEosWalletName() {
        List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        int size = eosAccounts.size();
        if (size < 9) {
            return "EOS-Wallet 0" + (eosAccounts.size() + 1);
        } else {
            return "EOS-Wallet " + (eosAccounts.size() + 1);
        }
    }

    @OnClick(R.id.btImport)
    public void onViewClicked() {
        if ("".equals(accountOwnerKey.getText().toString().trim()) && "".equals(accountActiveKey.getText().toString().trim())) {

            return;
        }
        try {
            if (!"".equals(accountOwnerKey.getText().toString().trim())) {
                mOwnerKey = new EosPrivateKey(accountOwnerKey.getText().toString());
                eosAccount.setOwnerPrivateKey(mOwnerKey.toString());
                eosAccount.setOwnerPublicKey(mOwnerKey.getPublicKey().toString());
            }
            if (!"".equals(accountActiveKey.getText().toString().trim())) {
                mActiveKey = new EosPrivateKey(accountActiveKey.getText().toString());
                eosAccount.setActivePrivateKey(mActiveKey.toString());
                eosAccount.setActivePublicKey(mActiveKey.getPublicKey().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.displayShortToast("私钥格式错误");
            return;
        }
        List<EosAccount> wallets2 = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        if (wallets2 != null && wallets2.size() != 0) {
            for (int i = 0; i < wallets2.size(); i++) {
                if (isSameKey(wallets2.get(i).getOwnerPrivateKey(), eosAccount.getOwnerPrivateKey()) || isSameKey(wallets2.get(i).getActivePrivateKey(), eosAccount.getActivePrivateKey())) {
                    ToastUtil.displayShortToast("wallet exist");
                    return;
                }
            }
        }
        showProgressDialog();
        Map<String, String> infoMap = new HashMap<>();
        if (eosAccount.getOwnerPublicKey() != null) {
            infoMap.put("public_key", eosAccount.getOwnerPublicKey());
        } else {
            infoMap.put("public_key", eosAccount.getActivePublicKey());
        }
        mPresenter.getEosKeyAccount(infoMap);
    }

    @Override
    public void getEosKeyAccountBack(ArrayList<EosKeyAccount> eosKeyAccounts) {
        closeProgressDialog();
        if (eosKeyAccounts.size() != 0) {
            showAccountNName(eosKeyAccounts.get(0).getAccount());
        }
    }

    @Override
    public void reportCreatedWalletSuccess() {
        closeProgressDialog();
        ToastUtil.displayShortToast("import eos account success");
        setResult(RESULT_OK);
        finish();
    }

    private void showAccountNName(String accountName) {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_choose_with_title, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        TextView tvConform = view.findViewById(R.id.tvConform);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView imageView = view.findViewById(R.id.tvTitle);
        imageView.setText("Confirmation account");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
            }
        });
        tvConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                sweetAlertDialog.cancel();
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("account", accountName);
                eosAccount.setAccountName(accountName);
                mPresenter.getEosAccountInfo(infoMap);
            }
        });

        tvContent.setText(accountName);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
    }

    private boolean isSameKey(String localKey, String neKey) {
        if (localKey == null || neKey == null) {
            return false;
        }
        if (localKey.equals(neKey)) {
            return true;
        }
        return false;
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
            if (accountActiveKey.isFocused()) {
                accountActiveKey.setText(data.getStringExtra("result"));
            } else if (accountOwnerKey.isFocused()) {
                accountOwnerKey.setText(data.getStringExtra("result"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
package com.stratagile.qlink.ui.activity.eos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.cypto.ec.EosPrivateKey;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosImportComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosImportContract;
import com.stratagile.qlink.ui.activity.eos.module.EosImportModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosImportPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

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
    @BindView(R.id.accountName)
    EditText accountName;
    @BindView(R.id.accountOwnerKey)
    EditText accountOwnerKey;
    @BindView(R.id.accountActiveKey)
    EditText accountActiveKey;
    @BindView(R.id.accountPassword)
    EditText accountPassword;
    @BindView(R.id.btImport)
    Button btImport;

    private EosPrivateKey mOwnerKey;
    private EosPrivateKey mActiveKey;

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
            if ("owner".equals(permissionsBean.getPerm_name())) {
                if (permissionsBean.getRequired_auth().getKeys().get(0).getKey().equals(mOwnerKey.getPublicKey().toString())) {

                } else {
                    noError = false;
                }
            }
            if ("active".equals(permissionsBean.getPerm_name())) {
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
            EosAccount eosAccount = new EosAccount();
            eosAccount.setAccountName(accountName.getText().toString());
            eosAccount.setActivePrivateKey(mActiveKey.toString());
            eosAccount.setActivePublicKey(mActiveKey.getPublicKey().toString());
            eosAccount.setOwnerPrivateKey(mOwnerKey.toString());
            eosAccount.setOwnerPublicKey(mOwnerKey.getPublicKey().toString());
            eosAccount.setIsCurrent(true);
            eosAccount.setAccountPassword(SpUtil.getString(this, ConstantValue.walletPassWord, ""));
            AppConfig.getInstance().getDaoSession().getEosAccountDao().insert(eosAccount);
            ToastUtil.displayShortToast("import eos account success");
            setResult(RESULT_OK);
            finish();
        } else {
            ToastUtil.displayShortToast("ownerKey or activeKey error");
        }
    }

    @OnClick(R.id.btImport)
    public void onViewClicked() {
        List<EosAccount> wallets2 = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        if (wallets2 != null && wallets2.size() != 0) {
            for (int i = 0; i < wallets2.size(); i++) {
                if (wallets2.get(i).getAccountName().equals(accountName.getText().toString())) {
                    ToastUtil.displayShortToast("wallet exist");
                    return;
                }
            }
        }
        try {
            mActiveKey = new EosPrivateKey(accountActiveKey.getText().toString());
            mOwnerKey = new EosPrivateKey(accountOwnerKey.getText().toString());
            KLog.i(mOwnerKey.getPublicKey().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.displayShortToast("私钥格式错误");
            return;
        }
        showProgressDialog();
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("account", accountName.getText().toString());
        mPresenter.getEosAccountInfo(infoMap);
    }
}
package com.stratagile.qlink.ui.activity.eos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.PushDatamanger;
import com.stratagile.qlink.blockchain.bean.BuyRamBytesBean;
import com.stratagile.qlink.blockchain.bean.CreateAccountBean;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EosAccountDao;
import com.stratagile.qlink.entity.eos.EosQrBean;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosActivationComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosActivationContract;
import com.stratagile.qlink.ui.activity.eos.module.EosActivationModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosActivationPresenter;
import com.stratagile.qlink.ui.activity.wallet.ChooseWalletActivity;
import com.stratagile.qlink.utils.DialogUtils;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: $description
 * @date 2018/12/12 11:17:52
 */

public class EosActivationActivity extends BaseActivity implements EosActivationContract.View {

    @Inject
    EosActivationPresenter mPresenter;

    EosQrBean eosQrBean;
    @BindView(R.id.etEosAccountName)
    TextView etEosAccountName;
    @BindView(R.id.etOwnerKey)
    TextView etOwnerKey;
    @BindView(R.id.etActiveKey)
    TextView etActiveKey;
    @BindView(R.id.etPaymentAccount)
    TextView etPaymentAccount;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    EosAccount eosAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eos_activation);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("Activate Account");
        eosQrBean = getIntent().getParcelableExtra("eosQrBean");
        if (getIntent().hasExtra("eoswallet")) {
            eosAccount = getIntent().getParcelableExtra("eoswallet");
            etPaymentAccount.setText(eosAccount.getWalletName() + " (" + eosAccount.getAccountName() + ")");
        } else {
            getEosAccount();
        }
        etEosAccountName.setText(eosQrBean.getAccountName());
        etOwnerKey.setText(eosQrBean.getOwnerPublicKey());
        etActiveKey.setText(eosQrBean.getActivePublicKey());
    }

    private void getEosAccount() {
        List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().queryBuilder().where(EosAccountDao.Properties.IsCreating.eq("false")).list();
        if (eosAccounts == null || eosAccounts.size() == 0) {
            DialogUtils.showDialog(this, "eos account not found", new DialogUtils.OnClick() {
                @Override
                public void onClick() {
                    finish();
                }
            });
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEosActivationComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosActivationModule(new EosActivationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosActivationContract.EosActivationContractPresenter presenter) {
        mPresenter = (EosActivationPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.tvRegister, R.id.etPaymentAccount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRegister:
                activateAccount();
                break;
            case R.id.etPaymentAccount:
                startActivityForResult(new Intent(this, ChooseWalletActivity.class), 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eosAccount = AppConfig.getInstance().getDaoSession().getEosAccountDao().queryBuilder().where(EosAccountDao.Properties.IsCurrent.eq("true")).unique();
        if (eosAccount == null) {
            DialogUtils.showDialog(this, "Please swith to EOS wallet", null);
        } else {
            etPaymentAccount.setText(eosAccount.getWalletName() + " (" + eosAccount.getAccountName() + ")");
        }
    }

    private void activateAccount() {
        showProgressDialog();
        CreateAccountBean createAccountBean = new CreateAccountBean();
        createAccountBean.setCreator(eosAccount.getAccountName());
        createAccountBean.setName(eosQrBean.getAccountName());
        CreateAccountBean.OwnerBean ownerBean = new CreateAccountBean.OwnerBean();
        ownerBean.setThreshold(1);
        CreateAccountBean.KeysBean keysBean = new CreateAccountBean.KeysBean();
        keysBean.setKey(eosQrBean.getOwnerPublicKey());
        keysBean.setWeight(1);
        ownerBean.setKeys(new ArrayList<>());
        ownerBean.getKeys().add(keysBean);
        ownerBean.setAccounts(new ArrayList<>());
        ownerBean.setWaits(new ArrayList<>());
        createAccountBean.setOwner(ownerBean);

        CreateAccountBean.ActiveBean activeBean = new CreateAccountBean.ActiveBean();
        activeBean.setThreshold(1);
        CreateAccountBean.KeysBean activeKey = new CreateAccountBean.KeysBean();
        activeKey.setWeight(1);
        activeKey.setKey(eosQrBean.getActivePublicKey());
        activeBean.setKeys(new ArrayList<>());
        activeBean.getKeys().add(activeKey);
        activeBean.setAccounts(new ArrayList<>());
        activeBean.setWaits(new ArrayList<>());
        createAccountBean.setActive(activeBean);
        String message = new Gson().toJson(createAccountBean);
        KLog.i(message);

        BuyRamBytesBean buyRamBean = new BuyRamBytesBean(eosAccount.getAccountName(), createAccountBean.getName(), 3042L);
        String buyRamBeanStr = new Gson().toJson(buyRamBean);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String privateKey = "";
                    String permission = "";
                    if (eosAccount.getOwnerPrivateKey() != null && !"".equals(eosAccount.getOwnerPrivateKey())) {
                        privateKey = eosAccount.getOwnerPrivateKey();
                        permission = "owner";
                    } else {
                        privateKey = eosAccount.getActivePrivateKey();
                        permission = "active";
                    }
                    new PushDatamanger(EosActivationActivity.this, privateKey, permission).createAccount(message, buyRamBeanStr, eosAccount.getAccountName(), result -> {
                        KLog.i(result);
                        tvRegister.post(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                if (result == null || "".equals(result)) {

                                } else {
                                    showTestDialog();
                                }
                            }
                        });
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showTestDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_tip, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        ImageView imageView = view.findViewById(R.id.ivTitle);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.op_success));
        tvContent.setText("Activate Success");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        tvRegister.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetAlertDialog.cancel();
                tvRegister.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                }, 200);
            }
        }, 2000);
    }


}
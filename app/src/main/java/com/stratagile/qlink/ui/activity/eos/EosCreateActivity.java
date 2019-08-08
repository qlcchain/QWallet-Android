package com.stratagile.qlink.ui.activity.eos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.cypto.ec.EosPrivateKey;
import com.stratagile.qlink.blockchain.util.PublicAndPrivateKeyUtils;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EosAccountDao;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.EthWalletDao;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.eos.EosNeedInfo;
import com.stratagile.qlink.entity.eos.EosQrBean;
import com.stratagile.qlink.entity.eos.TransferEthToCreateEos;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosCreateComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosCreateContract;
import com.stratagile.qlink.ui.activity.eos.module.EosCreateModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosCreatePresenter;
import com.stratagile.qlink.utils.EosUtil;
import com.stratagile.qlink.utils.PopWindowUtil;
import com.stratagile.qlink.utils.ThreadUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.CustomPopWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * @date 2018/12/07 11:29:04
 */

public class EosCreateActivity extends BaseActivity implements EosCreateContract.View {

    @Inject
    EosCreatePresenter mPresenter;
    @BindView(R.id.etEosAccountName)
    EditText etEosAccountName;
    @BindView(R.id.etOwnerKey)
    TextView etOwnerKey;
    @BindView(R.id.etActiveKey)
    TextView etActiveKey;
    @BindView(R.id.ll_controllerQrCode)
    LinearLayout llControllerQrCode;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvControllerQrcode)
    TextView tvControllerQrcode;

    EosPrivateKey mOwnerKey;

    List<EthWallet> ethWallets;
    private int currentSelectEthWallet = -1;

    private TokenInfo mTokenInfo;

    EosAccount eosAccount;

    private EosNeedInfo eosNeedInfo;

    private String transferHash = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mainColor = R.color.white;
        setContentView(R.layout.activity_eos_create);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.register_eos_account));
        ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().queryBuilder().where(EthWalletDao.Properties.IsLook.eq(false)).list();
        List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().queryBuilder().where(EosAccountDao.Properties.IsCreating.eq(true)).list();
        if (eosAccounts.size() != 0) {
            eosAccount = eosAccounts.get(0);
        }


        if (eosAccount == null) {
            mOwnerKey = PublicAndPrivateKeyUtils.getPrivateKey(2)[0];
            eosAccount = new EosAccount();
            eosAccount.setOwnerPrivateKey(mOwnerKey.toString());
            eosAccount.setOwnerPublicKey(mOwnerKey.getPublicKey().toString());
            eosAccount.setAccountPassword(ETHWalletUtils.getPassword());
            eosAccount.setActivePrivateKey(mOwnerKey.toString());
            eosAccount.setIsCreating(true);
            eosAccount.setActivePublicKey(mOwnerKey.getPublicKey().toString());
            eosAccount.setWalletName(getEosWalletName());
            AppConfig.getInstance().getDaoSession().getEosAccountDao().insert(eosAccount);
        } else {
            showProgressDialog();
            mOwnerKey = new EosPrivateKey(eosAccount.getOwnerPrivateKey());
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("public_key", eosAccount.getOwnerPublicKey());
            mPresenter.getEosKeyAccount(infoMap);
        }

        etOwnerKey.setText(mOwnerKey.getPublicKey().toString());
        etActiveKey.setText(mOwnerKey.getPublicKey().toString());
        etEosAccountName.setText(eosAccount.getAccountName());
        Map<String, String> infoMap = new HashMap<>();
        mPresenter.getEosNeedInfo(infoMap);


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

    @Override
    protected void setupActivityComponent() {
        DaggerEosCreateComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosCreateModule(new EosCreateModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosCreateContract.EosCreateContractPresenter presenter) {
        mPresenter = (EosCreatePresenter) presenter;
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
    public void setEth(TokenInfo tokenInfo) {
        if (tokenInfo.getTokenValue() > 0.015 + 0.0004) {
            mTokenInfo = tokenInfo;
            closeProgressDialog();
            showTransferEthDialog();
        } else {
            switchEthWallet();
        }
    }

    @Override
    public void transferEthSuccess(String hash) {
        closeProgressDialog();
        if ("".equals(hash)) {
            ToastUtil.displayShortToast(getString(R.string.transfer_error));
        } else {
            transferHash = hash;
            regsiterEos();
        }
    }

    @Override
    public void createEosAccountSuccess(String s) {
        closeProgressDialog();
        ToastUtil.displayShortToast(s);
        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(eosAccount);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void accountInfoBack(EosAccountInfo eosAccountInfo, int flag) {
        closeProgressDialog();
        if (eosAccountInfo.getData().getErrno() != 0 || "".equals(eosAccountInfo.getData().getData().getCreate_timestamp())) {
            if (flag == 0) {
                currentSelectEthWallet = -1;
                switchEthWallet();
            } else if (flag == 1) {
                ivQRCode.setVisibility(View.VISIBLE);
                tvControllerQrcode.setText(getString(R.string.hide_qr_code));
                generateQrCode();
            }
        } else {
            eosAccount.setAccountName("");
            etEosAccountName.setText("");
            ToastUtil.displayShortToast(getString(R.string.eos_account_exist));
        }
    }

    @Override
    public void setEosNeedInfo(EosNeedInfo eosNeedInfo) {
        this.eosNeedInfo = eosNeedInfo;
    }

    @Override
    public void getEosKeyAccountBack(EosKeyAccount eosKeyAccounts) {
        closeProgressDialog();
        if (eosKeyAccounts.getAccount_names().size() != 0) {
            eosAccount.setAccountName(eosKeyAccounts.getAccount_names().get(0));
            eosAccount.setIsCreating(false);
            AppConfig.getInstance().getDaoSession().getEosAccountDao().update(eosAccount);
            mPresenter.reportWalletCreated(eosAccount.getAccountName(), eosAccount.getOwnerPublicKey(), eosAccount.getOwnerPrivateKey());
            List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().queryBuilder().where(EosAccountDao.Properties.IsCreating.eq(true)).list();
            if (eosAccounts.size() != 0) {
                eosAccount = eosAccounts.get(0);
                showProgressDialog();
                mOwnerKey = new EosPrivateKey(eosAccount.getOwnerPrivateKey());
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("public_key", eosAccount.getOwnerPublicKey());
                mPresenter.getEosKeyAccount(infoMap);
            } else {
                mOwnerKey = PublicAndPrivateKeyUtils.getPrivateKey(2)[0];
                eosAccount = new EosAccount();
                eosAccount.setOwnerPrivateKey(mOwnerKey.toString());
                eosAccount.setOwnerPublicKey(mOwnerKey.getPublicKey().toString());
                eosAccount.setAccountPassword(ETHWalletUtils.getPassword());
                eosAccount.setActivePrivateKey(mOwnerKey.toString());
                eosAccount.setIsCreating(true);
                eosAccount.setActivePublicKey(mOwnerKey.getPublicKey().toString());
                eosAccount.setWalletName(getEosWalletName());
                AppConfig.getInstance().getDaoSession().getEosAccountDao().insert(eosAccount);
            }

            etOwnerKey.setText(mOwnerKey.getPublicKey().toString());
            etActiveKey.setText(mOwnerKey.getPublicKey().toString());
            etEosAccountName.setText(eosAccount.getAccountName());
        }
    }

    private void regsiterEos() {
        showProgressDialog();
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("txid", transferHash);
        infoMap.put("name", eosAccount.getAccountName());
        infoMap.put("owner", eosAccount.getOwnerPublicKey());
        infoMap.put("active", eosAccount.getActivePublicKey());
        mPresenter.createEosAccount(infoMap);
    }

    @OnClick({R.id.ll_controllerQrCode, R.id.tvRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_controllerQrCode:
                if (!EosUtil.isEosName(etEosAccountName.getText().toString().trim())) {
                    ToastUtil.displayShortToast(getString(R.string.innavalid_account_name));
                    return;
                }
                if (ivQRCode.getVisibility() == View.GONE) {
                    eosAccount.setAccountName(etEosAccountName.getText().toString().trim());
                    showProgressDialog();
                    getEosAccountInfo(1);
                } else {
                    ivQRCode.setVisibility(View.GONE);
                    tvControllerQrcode.setText(getString(R.string.open_qr_code));
                }
                break;
            case R.id.tvRegister:
                if (EosUtil.isEosName(etEosAccountName.getText().toString().trim())) {
                    eosAccount.setAccountName(etEosAccountName.getText().toString().trim());
                    if (eosNeedInfo == null) {
                        ToastUtil.displayShortToast(getString(R.string.please_wait));
                        Map<String, String> infoMap = new HashMap<>();
                        mPresenter.getEosNeedInfo(infoMap);
                        return;
                    }
                    getEosAccountInfo(0);
                    showProgressDialog();
                } else {
                    ToastUtil.displayShortToast(getString(R.string.innavalid_account_name));
                }
                break;
            default:
                break;
        }
    }

    String qrInfo;

    private void generateQrCode() {
        EosQrBean eosQrBean = new EosQrBean(etEosAccountName.getText().toString().trim(), etOwnerKey.getText().toString(), etActiveKey.getText().toString());
        String content = new Gson().toJson(eosQrBean);
        qrInfo = content;
        Bitmap logo = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("ic_launcher", "mipmap", getPackageName()));
        ThreadUtil.Companion.CreateEnglishQRCode createEnglishQRCode = new ThreadUtil.Companion.CreateEnglishQRCode(content, ivQRCode, logo);
        createEnglishQRCode.execute();
    }

    private void getEosAccountInfo(int flag) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("account", eosAccount.getAccountName());
        mPresenter.getEosAccountInfo(infoMap, flag);
    }

    EthWallet currentEthwallet;

    private void switchEthWallet() {
        showProgressDialog();
        if (currentSelectEthWallet >= ethWallets.size() - 1) {
            closeProgressDialog();
            ToastUtil.displayShortToast(getString(R.string.there_not_enough_eth_without_a_wallet));
            return;
        }
        currentSelectEthWallet++;
        currentEthwallet = ethWallets.get(currentSelectEthWallet);
        getEthWalletToken();
    }

    private void getEthWalletToken() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", currentEthwallet.getAddress());
        mPresenter.getETHWalletDetail(currentEthwallet.getAddress(), infoMap);
    }

    @Override
    public void onBackPressed() {
        if (CustomPopWindow.onBackPressed()) {
            return;
        }
        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(eosAccount);
        super.onBackPressed();
    }

    private void showTransferEthDialog() {
        TransferEthToCreateEos transferEthToCreateEos = new TransferEthToCreateEos();
        transferEthToCreateEos.setFrom(currentEthwallet.getAddress());
        transferEthToCreateEos.setTo(eosNeedInfo.getData().getEthAddress());
        transferEthToCreateEos.setEthValue(eosNeedInfo.getData().getEthAmount());
        transferEthToCreateEos.setCost("0.0004");
        transferEthToCreateEos.setCostDetail("= Gas(60000) * Gas Price(6 gwei)");
        PopWindowUtil.INSTANCE.showTransferEthPopWindow(transferEthToCreateEos, this, tvRegister, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvNext:
                        CustomPopWindow.onBackPressed();
                        showProgressDialog();
                        mPresenter.transactionEth(mTokenInfo, eosNeedInfo.getData().getEthAddress(), eosNeedInfo.getData().getEthAmount(), 60000, 6);
                        break;
                    default:
                        break;
                }
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
        if (item.getItemId() == R.id.share && ivQRCode.getVisibility() == View.VISIBLE) {
            ivQRCode.setDrawingCacheEnabled(true);
            ivQRCode.buildDrawingCache();
            Bitmap bgimg0 = Bitmap.createBitmap(ivQRCode.getDrawingCache());
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("image/*");  //设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bgimg0, qrInfo));
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

}
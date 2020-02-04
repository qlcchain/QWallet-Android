package com.stratagile.qlink.ui.activity.main;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.encoding.Crypto;
import com.socks.library.KLog;
import com.stratagile.qlink.DSBridge.JsApi;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.cypto.util.HexUtils;
import com.stratagile.qlink.db.BnbWallet;
import com.stratagile.qlink.db.BtcWallet;
import com.stratagile.qlink.ui.activity.main.component.DaggerTestComponent;
import com.stratagile.qlink.ui.activity.main.contract.TestContract;
import com.stratagile.qlink.ui.activity.main.module.TestModule;
import com.stratagile.qlink.ui.activity.main.presenter.TestPresenter;
import com.stratagile.qlink.utils.BnbUtil;
import com.stratagile.qlink.utils.ToastUtil;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.eblock.eos4j.utils.Hex;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

import static com.vondear.rxtools.view.RxToast.showToast;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/12/18 11:09:36
 */

public class TestActivity extends BaseActivity implements TestContract.View {

    @Inject
    TestPresenter mPresenter;
    @BindView(R.id.test)
    TextView test;
    @BindView(R.id.getBnbAccount)
    TextView getBnbAccount;
    @BindView(R.id.getBnbTokens)
    TextView getBnbTokens;

    @BindView(R.id.importBtc)
    TextView importBtc;

    @BindView(R.id.webview)
    DWebView webview;
    @BindView(R.id.addValue)
    TextView addValue;
    BnbWallet bnbWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DWebView.setWebContentsDebuggingEnabled(true);
    }

    @Override
    protected void initData() {
        if (AppConfig.getInstance().getDaoSession().getBnbWalletDao().loadAll().size() > 0) {
            bnbWallet = AppConfig.getInstance().getDaoSession().getBnbWalletDao().loadAll().get(0);
            KLog.i(bnbWallet.toString());
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerTestComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .testModule(new TestModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(TestContract.TestContractPresenter presenter) {
        mPresenter = (TestPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.getBnbTokens, R.id.getBnbAccount, R.id.test, R.id.importBtc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getBnbTokens:

                break;
            case R.id.getBnbAccount:

                break;
            case R.id.importBtc:

                break;
            case R.id.test:
                createBnbWallet();
                break;
            default:
                break;
        }
    }


    private static NetworkParameters getParams() {
        return TestNet3Params.get();
    }

    public void transferBtc(String privateKey, String recipientAddress, String amount) {

    }


    private void createBnbWallet() {
        AppConfig.getInstance().getDaoSession().getBnbWalletDao().deleteAll();
        try {
            List<String> mnemonicCodeWords = Crypto.generateMnemonicCode();
            com.binance.dex.api.client.Wallet wallet = com.binance.dex.api.client.Wallet.createWalletFromMnemonicCode(mnemonicCodeWords, BinanceDexEnvironment.PROD);
            KLog.i(wallet.toString());
            BnbWallet bnbWallet = new BnbWallet();
            bnbWallet.setIsBackup(false);
            bnbWallet.setName(BnbUtil.generateNewBnbWalletName());
            bnbWallet.setMnemonic(BnbUtil.convertMnemonicList(mnemonicCodeWords));
            bnbWallet.setAddress(wallet.getAddress());
            bnbWallet.setPrivateKey(wallet.getPrivateKey());
            bnbWallet.setPublicKey(Hex.bytesToHexString(wallet.getPubKeyForSign()));
            KLog.i(bnbWallet.toString());
            AppConfig.getInstance().getDaoSession().getBnbWalletDao().insert(bnbWallet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
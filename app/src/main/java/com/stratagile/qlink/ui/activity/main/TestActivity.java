package com.stratagile.qlink.ui.activity.main;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.domain.Account;
import com.binance.dex.api.client.domain.TransactionMetadata;
import com.binance.dex.api.client.domain.broadcast.TransactionOption;
import com.binance.dex.api.client.domain.broadcast.Transfer;
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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @BindView(R.id.transferBnb)
    TextView transferBnb;

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

    @OnClick({R.id.getBnbTokens, R.id.getBnbAccount, R.id.test, R.id.transferBnb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getBnbTokens:
                break;
            case R.id.getBnbAccount:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BinanceDexApiNodeClient binanceDexNodeApi = null;
                        binanceDexNodeApi = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(BinanceDexEnvironment.PROD.getNodeUrl(),BinanceDexEnvironment.PROD.getHrp());
                        String address = bnbWallet.getAddress();
                        Account account = binanceDexNodeApi.getAccount(address);
                        KLog.i(account);
                    }
                }).start();
                break;
            case R.id.transferBnb:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BinanceDexApiNodeClient binanceDexNodeApi = null;
                        binanceDexNodeApi = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(BinanceDexEnvironment.PROD.getNodeUrl(),BinanceDexEnvironment.PROD.getHrp());
                        com.binance.dex.api.client.Wallet walletSender = new com.binance.dex.api.client.Wallet(bnbWallet.getPrivateKey(), BinanceDexEnvironment.PROD);
                        walletSender.initAccount(binanceDexNodeApi);
                        Transfer transfer = new Transfer();
                        transfer.setAmount("10");
                        transfer.setCoin("MATIC-84A");
                        transfer.setFromAddress(walletSender.getAddress());
                        transfer.setToAddress("bnb136ns6lfw4zs5hg4n85vdthaad7hq5m4gtkgf23");
                        System.out.println(transfer.toString());
                        TransactionOption options = new TransactionOption("101252674", 0, null);
                        List<TransactionMetadata> resp = null;
                        try {
                            resp = binanceDexNodeApi.transfer(transfer, walletSender, options, true);
                            System.out.println(resp.get(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
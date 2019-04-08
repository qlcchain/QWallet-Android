package com.stratagile.qlink.ui.activity.main;

import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.btc.BitUtil;
import com.stratagile.qlink.blockchain.cypto.util.HexUtils;
import com.stratagile.qlink.db.BtcWallet;
import com.stratagile.qlink.ui.activity.main.component.DaggerTestComponent;
import com.stratagile.qlink.ui.activity.main.contract.TestContract;
import com.stratagile.qlink.ui.activity.main.module.TestModule;
import com.stratagile.qlink.ui.activity.main.presenter.TestPresenter;
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
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletTransaction;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.getBtc)
    TextView getBtc;
    @BindView(R.id.transferBtc)
    TextView transferBtc;

    BtcWallet btcWallet;
    @BindView(R.id.importBtc)
    TextView importBtc;

    WalletAppKit kit;

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
    }

    @Override
    protected void initData() {
        List<BtcWallet> btcWallets = AppConfig.getInstance().getDaoSession().getBtcWalletDao().loadAll();
        kit = AppConfig.getInstance().walletAppKit;
        if (btcWallets == null || btcWallets.size() == 0) {
            return;
        }
        btcWallet = btcWallets.get(0);
        KLog.i(btcWallet.toString());
//        String path = Environment.getExternalStorageDirectory() + "/Qlink/btc";
//        File file = new File(path);
//        kit = new WalletAppKit(getParams(), file, "") {
//            @Override
//            protected void onSetupCompleted() {
//                // This is called in a background thread after startAndWait is called, as setting up various objects
//                // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
//                // on the main thread.
//                KLog.i("onSetupCompleted");
//                if (wallet().getKeyChainGroupSize() < 1)
//                    wallet().importKey(ECKey.fromPrivate(HexUtils.toBytes("0069cdca0d1397964d8666cdd0561c55f95ab58df300279371a883213b801679")));
//            }
//        };
//        kit.startAsync();
//        kit.awaitRunning();
//        kit.wallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
//            @Override
//            public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
//                // Runs in the dedicated "user thread".
//                //
//                // The transaction "tx" can either be pending, or included into a block (we didn't see the broadcast).
//                Coin value = tx.getValueSentToMe(w);
//                System.out.println("Received tx for " + value.toFriendlyString() + ": " + tx);
//                System.out.println("Transaction will be forwarded after it confirms.");
//                // Wait until it's made it into the block chain (may run immediately if it's already there).
//                //
//                // For this dummy app of course, we could just forward the unconfirmed transaction. If it were
//                // to be double spent, no harm done. Wallet.allowSpendingUnconfirmedTransactions() would have to
//                // be called in onSetupCompleted() above. But we don't do that here to demonstrate the more common
//                // case of waiting for a block.
//                Futures.addCallback(tx.getConfidence().getDepthFuture(1), new FutureCallback<TransactionConfidence>() {
//                    @Override
//                    public void onSuccess(TransactionConfidence result) {
//                        // "result" here is the same as "tx" above, but we use it anyway for clarity.
////                        forwardCoins(result);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {}
//                });
//            }
//        });
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

    @OnClick({R.id.getBtc, R.id.transferBtc, R.id.test, R.id.importBtc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getBtc:
//                Wallet wallet1 = BitUtil.getWalletFromFile(Environment.getExternalStorageDirectory() + "/Qlink/btc/btcWalletHuang.wallet");
                KLog.i(kit.wallet().toString(true, true, true, null));
                KLog.i(kit.wallet().getKeyCrypter());
                KLog.i(kit.wallet().getImportedKeys().get(0).getPrivateKeyAsWiF(getParams()));
                KLog.i(kit.wallet().getImportedKeys().get(0).getPrivateKeyAsHex());
                KLog.i(kit.wallet().currentChangeAddress().toBase58());
                KLog.i(kit.wallet().currentReceiveAddress().toBase58());
                KLog.i(kit.wallet().getBalance().toFriendlyString());
                KLog.i(kit.wallet().getImportedKeys().get(0).toAddress(TestNet3Params.get()).toBase58());
//                signingtrasaction("cPU5oRL83t1oKZhGf3WX8M58TVsawcDtfxdM1GTefP1kprrPseVX", "mhoPNXK2u4kXBGmhTWFH9JC5AnFfgJyBC5");
//                List<BtcWallet> btcWallets = AppConfig.getInstance().getDaoSession().getBtcWalletDao().loadAll();
//                if (btcWallets == null || btcWallets.size() == 0) {
//                    return;
//                }
//                btcWallet = btcWallets.get(0);
//                KLog.i(btcWallet.toString());
                break;
            case R.id.transferBtc:
//                transferBtc();
                transferBtc("cRGc4hg5nR4waBwzFdDgJ7bY52PbQEk9pDDxa3awVbbtxZEzH5jB", "moPQHXaXEbDyrURN4Wm5SWhZNF7akX1PPe", "0.097273");
                break;
            case R.id.importBtc:
                NetworkParameters params1 = TestNet3Params.get();

                //hex导入
                ECKey ownerKey = ECKey.fromPrivate(HexUtils.toBytes("0069cdca0d1397964d8666cdd0561c55f95ab58df300279371a883213b801679"));
                KLog.i("hex导入");
                KLog.i(ownerKey.toAddress(params1).toBase58());

                //wif导入
                DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params1, "cMbWFBwySvJRZo2PfNXznW5heg96YjQiqj8mpLSLao8S8D9a6kkQ");
                KLog.i("wif导入");
                KLog.i(dumpedPrivateKey.getKey().toAddress(params1).toBase58());

                //biginteger导入
                ECKey ecKey = ECKey.fromPrivate(new BigInteger("186939247089168714061279076603219630370475745364531029459246252915479811705"));
                KLog.i("biginteger导入");
                KLog.i(ecKey.toAddress(params1).toBase58());


                DumpedPrivateKey importPrivateKey = DumpedPrivateKey.fromBase58(params1, "cMbWFBwySvJRZo2PfNXznW5heg96YjQiqj8mpLSLao8S8D9a6kkQ");
                KLog.i("wif导入");
                ECKey importEcKey = importPrivateKey.getKey();
                KLog.i(importEcKey.toAddress(params1).toBase58());
//                AppConfig.getInstance().getDaoSession().getBtcWalletDao().deleteAll();
                List<BtcWallet> btcWallets1 = AppConfig.getInstance().getDaoSession().getBtcWalletDao().loadAll();
                if (btcWallets1 != null && btcWallets1.size() != 0) {
                    for (BtcWallet wallet : btcWallets1) {
                        if (wallet.getWifPrivateKey().equals("cMbWFBwySvJRZo2PfNXznW5heg96YjQiqj8mpLSLao8S8D9a6kkQ")) {
                            ToastUtil.displayShortToast("wallet exist");
                            return;
                        }
                    }
                }

                BtcWallet btcWallet = new BtcWallet();
                btcWallet.setAddress(importEcKey.toAddress(params1).toBase58());
                btcWallet.setBigIntegerPrivateKey(importEcKey.getPrivKey().toString());
                btcWallet.setHexPrivateKey(importEcKey.getPrivateKeyAsHex());
                btcWallet.setWifPrivateKey(importEcKey.getPrivateKeyAsWiF(params1));
                btcWallet.setPublicKey(HexUtils.toHex(importEcKey.getPubKey()));
                KLog.i(btcWallet.toString());
                AppConfig.getInstance().getDaoSession().getBtcWalletDao().insert(btcWallet);
                break;
            case R.id.test:
                mPresenter.reportWalletCreated("moPQHXaXEbDyrURN4Wm5SWhZNF7akX1PPe", "BTC", "0351fcd59af8caf86ee273b2da6390a1d9d476b4495ce84104bef89a4c2e32c209", "cMbWFBwySvJRZo2PfNXznW5heg96YjQiqj8mpLSLao8S8D9a6kkQ");
//                ECKey ceKey = new ECKey();
//                NetworkParameters params = TestNet3Params.get();
//                ceKey.getPrivKey(); // 私钥， BigInteger
//                KLog.i(ceKey.getPrivateKeyAsHex()); // 私钥， Hex
//                String privateKey = ceKey.getPrivateKeyAsWiF(params); // 私钥， WIF(Wallet Import Format)
//                byte[] privateKeyByte = ceKey.getPrivKeyBytes(); // 私钥 byte[]
//
//                byte[] publicKeyByte = ceKey.getPubKey();
//                String address = ceKey.toAddress(params).toBase58();
//                KLog.i(privateKey);
//                KLog.i(HexUtils.toHex(privateKeyByte));
//                KLog.i(HexUtils.toHex(publicKeyByte));
//                KLog.i(address);
//
//                BtcWallet btcWallet1 = new BtcWallet();
//                btcWallet1.setAddress(address);
//                btcWallet1.setBigIntegerPrivateKey(ceKey.getPrivKey().toString());
//                btcWallet1.setHexPrivateKey(ceKey.getPrivateKeyAsHex());
//                btcWallet1.setWifPrivateKey(privateKey);
//                btcWallet1.setPublicKey(HexUtils.toHex(publicKeyByte));
//                AppConfig.getInstance().getDaoSession().getBtcWalletDao().insert(btcWallet1);
                break;
            default:
                break;
        }
    }

    private void transferBtc() {
        String ownerPrivInBigIntegerStr = "25451740256537772857211460956897374238827415036565128365929611689447833266250";
        BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
        NetworkParameters params = TestNet3Params.get();
// 构造初始拥有者key
        ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);
        Address ownerAddress = ownerKey.toAddress(params);
//        Transaction tx = new Transaction(params);
//        Coin initCoin = Coin.valueOf(1000L);
//        tx.addOutput(initCoin, ownerAddress);

        Coin valueNeeded = Coin.parseCoin("0.00000005"); // 转账金额
// 转账地址、转账金额
        SendRequest request = SendRequest.to(Address.fromBase58(params, "moPQHXaXEbDyrURN4Wm5SWhZNF7akX1PPe"), valueNeeded);
        request.changeAddress = ownerAddress;
        List<ECKey> ownerKeys = new ArrayList<ECKey>();
// 设置当前用户的key
        ownerKeys.add(ownerKey);
        Wallet wallet = Wallet.fromKeys(params, ownerKeys);
        List<TransactionOutput> unspent = wallet.getUnspents();
        KLog.i(unspent.get(0).toString());
//        WalletTransaction wtx = new WalletTransaction(WalletTransaction.Pool.UNSPENT, unspend);
// 将当前用户拥有的utxo所在的交易加入列表
//        wallet.addWalletTransaction(wtx);

// 生成交易
        try {
            Transaction tx = wallet.sendCoinsOffline(request);
            KLog.i(tx.getHash());
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        }


    }

    private static NetworkParameters getParams() {
        return TestNet3Params.get();
    }

    public void transferBtc(String privateKey,String recipientAddress,String amount){
        Context context = new Context(getParams());
        Context.propagate(context);
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(getParams(), privateKey);
        ECKey ownerKey = dumpedPrivateKey.getKey();
        Address ownerAddress = ownerKey.toAddress(getParams());
        SendRequest request = SendRequest.to(Address.fromBase58(getParams(), recipientAddress), Coin.parseCoin(amount));

        //设置找零钱包地址为当前的转出地址。
        request.changeAddress = ownerAddress;
        KLog.i(request.tx.getHashAsString());
//        List<ECKey> ownerKeys = new ArrayList<ECKey>();
//        ownerKeys.add(ownerKey);
//        Wallet wallet = Wallet.fromKeys(getParams(), ownerKeys);
//        Transaction transaction = new Transaction(getParams());
//        Coin initCoin = Coin.valueOf(1000L);
//        transaction.addOutput(initCoin, Address.fromBase58(getParams(), ownerKey.toAddress(getParams()).toBase58()));
//        WalletTransaction wtx = new WalletTransaction(WalletTransaction.Pool.UNSPENT, transaction);
//        // 将当前用户拥有的utxo所在的交易加入列表
//        wallet.addWalletTransaction(wtx);

        // 生成交易
//        try {
//            Transaction transaction1 = wallet.sendCoinsOffline(request);
//            KLog.i(transaction1.getHashAsString());
//        } catch (InsufficientMoneyException e) {
//            e.printStackTrace();
//        }
        try {
            kit.wallet().sendCoins(kit.peerGroup(), request);
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        }
//        signingtrasaction(privateKey,request.tx.getHashAsString());
    }

    public static  void signingtrasaction(String wif, String msg) {
        try {
            // creating a key object from WiF
            DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(getParams(), wif);
            ECKey key = dpk.getKey();
            // checking our key object
            // NetworkParameters main = MainNetParams.get();
            String check = key.getPrivateKeyAsWiF(getParams());
            System.out.println(wif.equals(check));  // true
            Log.e("wif check", String.valueOf(wif.equals(check)));
            // creating Sha object from string
            Sha256Hash hash = Sha256Hash.wrap(msg);
            // creating signature
            ECKey.ECDSASignature sig = key.sign(hash);
            // encoding
            byte[] res = sig.encodeToDER();
            // converting to hex
            //String hex = DatatypeConverter.printHexBinary(res);
            // String hex = new String(res);
            String hex = new String(Base64.encode(res, Base64.DEFAULT));
            Log.e("sigendTransaction", hex);
            Log.e("decrypttx",""+ HexUtils.toHex(sig.encodeToDER()));
        } catch (Exception e) {   //signingkey = ecdsa.from_string(privateKey.decode('hex'), curve=ecdsa.SECP256k1)
            e.printStackTrace();
        }
    }


}
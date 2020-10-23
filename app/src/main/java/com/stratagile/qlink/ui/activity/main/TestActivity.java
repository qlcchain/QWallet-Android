package com.stratagile.qlink.ui.activity.main;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.Wallet;
import com.binance.dex.api.client.domain.Account;
import com.binance.dex.api.client.encoding.Crypto;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.cypto.digest.Sha256;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.BnbWallet;
import com.stratagile.qlink.ui.activity.main.component.DaggerTestComponent;
import com.stratagile.qlink.ui.activity.main.contract.TestContract;
import com.stratagile.qlink.ui.activity.main.module.TestModule;
import com.stratagile.qlink.ui.activity.main.presenter.TestPresenter;
import com.stratagile.qlink.utils.BnbUtil;
import com.stratagile.qlink.view.BottomSheet;
import com.stratagile.qlink.walletconnect.WCClient;
import com.stratagile.qlink.walletconnect.WCSession;
import com.stratagile.qlink.walletconnect.entity.WCPeerMeta;

import org.json.JSONObject;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.eblock.eos4j.utils.Hex;
import io.neow3j.constants.OpCode;
import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptBuilder;
import io.neow3j.contract.ScriptHash;
import io.neow3j.contract.Test1Contract;
import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.crypto.transaction.RawTransactionAttribute;
import io.neow3j.model.types.TransactionAttributeUsageType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

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
    @BindView(R.id.testCoinmarketcap)
    TextView testCoinmarketcap;
    @BindView(R.id.refundUser)
    TextView refundUser;

    private Neow3j neow;

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
        // http://seed1.o3node.org:10332
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        builder.callTimeout(600, TimeUnit.SECONDS);
        builder.readTimeout(600, TimeUnit.SECONDS);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        neow = Neow3j.build(new HttpService(ConstantValue.neoNode, builder.build()));
        webview.loadUrl("file:///android_asset/eth.html");
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

    @OnClick({R.id.getBnbTokens, R.id.getBnbAccount, R.id.test, R.id.transferBnb, R.id.test2, R.id.nep5Transfer, R.id.testEthSmartContract, R.id.testEthTransaction, R.id.erc20Transaction, R.id.refundUser, R.id.deploy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getBnbTokens:
                BottomSheet.Builder builder = new BottomSheet.Builder(TestActivity.this);
                builder.setApplyTopPadding(false);

                LinearLayout linearLayout = new LinearLayout(TestActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                builder.setCustomView(linearLayout);
                builder.create().show();
                break;
            case R.id.getBnbAccount:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        KLog.i("开始");
                        Web3j web3j = Web3j.build(new org.web3j.protocol.http.HttpService(ConstantValue.ethNodeUrl));
                        try {
                            EthTransaction transaction = web3j.ethGetTransactionByHash("0x3256b5f4ce48f22ea9e43d764f4f6c9d846ef4bfee6f9682e99942f564c270b1").send();
                            KLog.i("返回");
                            if (transaction.hasError()) {
                                KLog.i(transaction.getError().getMessage());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        BinanceDexApiNodeClient binanceDexNodeApi = null;
//                        binanceDexNodeApi = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(BinanceDexEnvironment.PROD.getNodeUrl(), BinanceDexEnvironment.PROD.getHrp());
//                        String address = bnbWallet.getAddress();
//                        Account account = binanceDexNodeApi.getAccount(address);
//                        KLog.i(account);
                    }
                }).start();
                break;
            case R.id.transferBnb:
                getErc20TokenBalance();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        BinanceDexApiNodeClient binanceDexNodeApi = null;
//                        binanceDexNodeApi = BinanceDexApiClientFactory.newInstance().newNodeRpcClient(BinanceDexEnvironment.PROD.getNodeUrl(), BinanceDexEnvironment.PROD.getHrp());
//                        Wallet walletSender = new Wallet(bnbWallet.getPrivateKey(), BinanceDexEnvironment.PROD);
//                        walletSender.initAccount(binanceDexNodeApi);
//                        Transfer transfer = new Transfer();
//                        transfer.setAmount("10");
//                        transfer.setCoin("MATIC-84A");
//                        transfer.setFromAddress(walletSender.getAddress());
//                        transfer.setToAddress("bnb136ns6lfw4zs5hg4n85vdthaad7hq5m4gtkgf23");
//                        System.out.println(transfer.toString());
//                        TransactionOption options = new TransactionOption("101252674", 0, null);
//                        List<TransactionMetadata> resp = null;
//                        try {
//                            resp = binanceDexNodeApi.transfer(transfer, walletSender, options, true);
//                            System.out.println(resp.get(0));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
                break;
            case R.id.test:
//                callNeoSmartContract();
                querySwapInfo();
                break;
            case R.id.nep5Transfer:
                nep5Transfer();
                break;
            case R.id.testEthSmartContract:
                callEthSmartContract();
                break;
            case R.id.testEthTransaction:
                ethTransaction();
                break;
            case R.id.erc20Transaction:
                erc20Transaction();
                break;
            case R.id.deploy:
                deploy();
                break;
            case R.id.refundUser:
//                refundUser();
                testWalletConnect();
//                sha2561();
                break;
            case R.id.test2:
//                Test1Contract.byteArrayToString("516c696e6b20546f6b656e");
//                Test1Contract.byteArrayToString("514c43");
//                Test1Contract.byteArrayToString2("00a367b0ae29d500");
//                Test1Contract.byteArrayToString2("9ac2dde000");
                callNeoContract2();
                break;
            default:
                break;
        }
    }

    private void testWalletConnect() {
        String seseeionStr = "";
        WCSession session = WCSession.Companion.from(seseeionStr);
        WCClient wcClient = new WCClient(new GsonBuilder(), new OkHttpClient());
        WCPeerMeta wcPeerMeta = new WCPeerMeta("", "", "", null);
    }

    private void deploy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Test1Contract.deploy(neow);
            }
        }).start();
    }

    private void refundUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Test1Contract.refundUser(neow, "37e4bc3796c240e0ae161e7d2478ecc9", neoUserAddress, "KzviPYuqHtQvw4T6vkbxJGnyoRGo1yYULAAn6WbTLwpQboHEkXcW", neoContractAddress);
                KLog.i(result);
            }
        }).start();
    }
    private void sha2561() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Test1Contract.sha2561(neow, "65f1ec02cacb4616bf8fb60b97b67dd7", neoContractAddress);
                KLog.i(result);
                Object[] arrays = new Object[1];
                arrays[0] = result;
                webview.callHandler("staking.deSerializ", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((JSONObject) var1);
                    }

                    public final void onValue(JSONObject retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());


                    }
                }));
            }
        }).start();
    }
    private void sha2562() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Test1Contract.sha2562(neow, "65f1ec02cacb4616bf8fb60b97b67dd7", "KzviPYuqHtQvw4T6vkbxJGnyoRGo1yYULAAn6WbTLwpQboHEkXcW", neoContractAddress);
                KLog.i(result);
                Object[] arrays = new Object[1];
                arrays[0] = result;
                webview.callHandler("staking.deSerializ", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((JSONObject) var1);
                    }

                    public final void onValue(JSONObject retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());


                    }
                }));
            }
        }).start();
    }

    private void erc20Transaction() {
        Object[] arrays = new Object[5];
        //0x255的
        arrays[0] = "1c70c79c8f0c0ba5c700663256360230327f6d3688859c688b4106c890676440";
        //0x0a8的
//        arrays[0] = "67652fa52357b65255ac38d0ef8997b5608527a7c1d911ecefb8bc184d74e92e";
        arrays[1] = userAddress;
        arrays[2] = "0xE0632e90d6eB6649CfD82f6d625769cCf9E7762f";
        arrays[3] = contractAddress;
        arrays[4] = "100";
        new Thread(new Runnable() {
            @Override
            public void run() {
                webview.callHandler("ethSmartContract.erc20Transaction", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((String) var1);
                    }

                    public final void onValue(String retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    }
                }));
            }
        }).start();
    }

    private void test111() {
        char[] chars = "0123456789abcdef".toCharArray();
        StringBuilder builder = new StringBuilder("");
        byte[] bs = "ddbda109309f9fafa6dd6a9cb9f1df40".getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            builder.append("0x");
            builder.append(chars[bit]);
            bit = bs[i] & 0x0f;
            builder.append(chars[bit]);
            builder.append(", ");
        }
        System.out.println(builder.toString().trim());
    }

    private String contractAddress = "0x16e502c867C2d4CAC0F4B4dBd39AB722F5cEc050";
    private String onwerAddress = "0x0A8EFAacbeC7763855b9A39845DDbd03b03775C1";
    private String userAddress = "0x255eEcd17E11C5d2FFD5818da31d04B5c1721D7C";
    private String neoContractAddress = "c59bd98299324d6156c67cd9e2e9783054eaf383";
    private String neoUserAddress = "AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK";
//    private String neoContractAddress = "58960df3e9238f2f40c578aef727d26c1aa51b60";

    private void ethTransaction() {
        KLog.i("xxxxxx");
        Object[] arrays = new Object[4];
        arrays[0] = "1c70c79c8f0c0ba5c700663256360230327f6d3688859c688b4106c890676440";
        arrays[1] = userAddress;
//        arrays[2] = userAddress;
        arrays[2] = "0x99F74908466351992397Aa60962829Ca22cf4877";
        arrays[3] = contractAddress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                webview.callHandler("ethSmartContract.ethTransaction", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((JSONObject) var1);
                    }

                    public final void onValue(JSONObject retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    }
                }));
            }
        }).start();
    }

    private void callEthSmartContract() {
        Object[] arrays = new Object[3];
        arrays[0] = userAddress;
        arrays[1] = "xxxxxx";
        arrays[2] = contractAddress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                webview.callHandler("ethSmartContract.lockedBalanceOf", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((String) var1);
                    }

                    public final void onValue(String retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    }
                }));
            }
        }).start();
    }

    private void getErc20TokenBalance() {
        Object[] arrays = new Object[3];
        arrays[0] = userAddress;
        arrays[1] = userAddress;
        arrays[2] = contractAddress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                webview.callHandler("ethSmartContract.getErc20TokenBalance", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((String) var1);
                    }

                    public final void onValue(String retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    }
                }));
            }
        }).start();
    }

    private void querySwapInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Test1Contract.querySwapInfo(neow, "0x4b9893ca574762cde2b4ac16b53de121e09a5af30a4c94c2593bdeaee4e54ec7", neoContractAddress);
            }
        }).start();
    }

    private void nep5Transfer() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String orginHash = UUID.randomUUID().toString().replace("-", "");
//                KLog.i(orginHash);
//                String hashSha256 = Sha256.from(orginHash.getBytes()).toString();
//                KLog.i(hashSha256);
//                //neow3j: Neow3j, hash : String, fromAddress : String, privateKey : String, amount : Int, wrapperNeoAddress : String, overTimeBlocks : Int, contractAddress : String
//                Test1Contract.userLock(neow, hashSha256, "AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK", "KzviPYuqHtQvw4T6vkbxJGnyoRGo1yYULAAn6WbTLwpQboHEkXcW", new BigInteger("10"), "ARJZeUehdrFD3Koy3iAymfLDWi3HtCVKYV", 20, neoContractAddress);
//            }
//        }).start();

        String orginHash = UUID.randomUUID().toString().replace("-", "");
        KLog.i(orginHash);
        String hashSha256 = Sha256.from(orginHash.getBytes()).toString();
        KLog.i(hashSha256);
        Object[] arrays = new Object[5];
        arrays[0] = hashSha256;
        arrays[1] = "AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK";
        arrays[2] = "1100000000";
        arrays[3] = "81";
        arrays[4] = "KzviPYuqHtQvw4T6vkbxJGnyoRGo1yYULAAn6WbTLwpQboHEkXcW";
        new Thread(new Runnable() {
            @Override
            public void run() {
                webview.callHandler("staking.userLock", arrays, (new OnReturnValue() {
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((JSONObject)var1);
                    }

                    public final void onValue(JSONObject retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    }
                }));
            }
        }).start();
    }

    private void callNeoContract2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object[] arrays = new Object[1];
                arrays[0] = "AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        webview.callHandler("staking.nep5Transfer", arrays, (new OnReturnValue() {
                            @Override
                            public void onValue(Object var1) {
                                this.onValue((JSONObject) var1);
                            }

                            public final void onValue(JSONObject retValue) {
                                StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                                KLog.i(result.append(retValue).toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });

                            }
                        }));
                    }
                }).start();
            }
        }).start();
    }

    private void callNeoSmartContract() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Test1Contract.testContract(neow);
            }
        }).start();
//        Object[] arrays = new Object[1];
//        arrays[0] = com.stratagile.qlink.Account.INSTANCE.byteArray2String(com.stratagile.qlink.Account.INSTANCE.getWallet().getPrivateKey());
//        KLog.i(com.stratagile.qlink.Account.INSTANCE.byteArray2String(com.stratagile.qlink.Account.INSTANCE.getWallet().getPrivateKey()));
//        KLog.i(com.stratagile.qlink.Account.INSTANCE.byteArray2String(com.stratagile.qlink.Account.INSTANCE.getWallet().getHashedSignature()));
//       new Thread(new Runnable() {
//           @Override
//           public void run() {
//               webview.callHandler("staking.testContract1", arrays, (new OnReturnValue() {
//                   // $FF: synthetic method
//                   // $FF: bridge method
//                   @Override
//                   public void onValue(Object var1) {
//                       this.onValue((JSONObject)var1);
//                   }
//
//                   public final void onValue(JSONObject retValue) {
//                       StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
//                       KLog.i(result.append(retValue).toString());
//                       runOnUiThread(new Runnable() {
//                           @Override
//                           public void run() {
//                           }
//                       });
//
//                   }
//               }));
//           }
//       }).start();
    }

    private AlertDialog visibleDialog;
    private AlertDialog localeDialog;
    private AlertDialog proxyErrorDialog;

    public AlertDialog showAlertDialog(AlertDialog.Builder builder) {
        try {
            if (visibleDialog != null) {
                visibleDialog.dismiss();
                visibleDialog = null;
            }
        } catch (Exception e) {
//            FileLog.e(e);
        }
        try {
            visibleDialog = builder.show();
            visibleDialog.setCanceledOnTouchOutside(true);
            visibleDialog.setOnDismissListener(dialog -> {
                if (visibleDialog != null) {
                    if (visibleDialog == localeDialog) {
//                        try {
//                            String shorname = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
//                            Toast.makeText(LaunchActivity.this, getStringForLanguageAlert(shorname.equals("en") ? englishLocaleStrings : systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater), Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            FileLog.e(e);
//                        }
                        localeDialog = null;
                    } else if (visibleDialog == proxyErrorDialog) {
//                        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
//                        SharedPreferences.Editor editor = MessagesController.getGlobalMainSettings().edit();
//                        editor.putBoolean("proxy_enabled", false);
//                        editor.putBoolean("proxy_enabled_calls", false);
//                        editor.commit();
//                        ConnectionsManager.setProxySettings(false, "", 1080, "", "", "");
//                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged);
                        proxyErrorDialog = null;
                    }
                }
                visibleDialog = null;
            });
            return visibleDialog;
        } catch (Exception e) {
//            FileLog.e(e);
        }
        return null;
    }


    public void transferBtc(String privateKey, String recipientAddress, String amount) {

    }


    private void createBnbWallet() {
        AppConfig.getInstance().getDaoSession().getBnbWalletDao().deleteAll();
        try {
            List<String> mnemonicCodeWords = Crypto.generateMnemonicCode();
            Wallet wallet = Wallet.createWalletFromMnemonicCode(mnemonicCodeWords, BinanceDexEnvironment.PROD);
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
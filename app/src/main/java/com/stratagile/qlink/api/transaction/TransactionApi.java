package com.stratagile.qlink.api.transaction;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.Assets;
import com.stratagile.qlink.data.NeoNodeRPC;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.entity.TransactionResult;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.txutils.model.core.Transaction;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;


import org.greenrobot.eventbus.EventBus;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import neoutils.Wallet;


/**
 * Created by huzhipeng on 2018/5/3.
 * 交易的api，完成获取unspent，发送sendRaw功能， 转账功能。对外提供结果的回调
 * 该工具类是winq钱包的第二版的大改动，所有钱包的信息只保存在app本地，生成钱包的操作也是在app本地。
 * 钱包的敏感信息wif和私钥不会进行网络的传输
 */

public class TransactionApi {
    private static TransactionApi instance;
    private CompositeDisposable mCompositeDisposable;
    private Assets assets;

    /**
     * 获取单例
     *
     * @return 返回交易工具类的实体
     */
    public static synchronized TransactionApi getInstance() {
        if (instance == null) {
            instance = new TransactionApi();
        }
        return instance;
    }

    private TransactionApi() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
    }

    /**
     * neo兑换qlc的主方法
     *
     * @param toAddress    对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param neo          需要兑换的neo的数量
     * @param fromAddress  自己的钱包地址
     * @param sendCallBack 兑换结果的回调
     */
    public void transactionNEO(String toAddress, String neo, String fromAddress, SendCallBack sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getUtxo(toAddress, fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                sendNativeAsset(Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.NEO, fromAddress, toAddress, Integer.parseInt(neo), new SendCallBack() {
                    @Override
                    public void onSuccess() {
                        sendCallBack.onSuccess();
                    }

                    @Override
                    public void onFailure() {
                        sendCallBack.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

    /**
     * 实际neo兑换qlc的方法
     *
     * @param wallet            钱包实体，为第三方引用的那个库
     * @param tokenContractHash 第三方的资产的编号，这里只做兑换qlc，所以这个资产编号为qlc的编号。
     * @param fromAddress       自己的钱包地址
     * @param toAddress         对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param amount            需要兑换的neo的数量
     * @param sendCallBack      兑换结果的回调
     */
    private void sendNativeAsset(Wallet wallet, NeoNodeRPC.Asset tokenContractHash, String fromAddress, String toAddress, int amount, SendCallBack sendCallBack) {
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNativeAssetTransaction(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Transaction transaction = getTxid(isSuccess);
            KLog.i(transaction);
            Map<String, String> infoMap = new HashMap<>();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            uuid = uuid.substring(0, 32);
            infoMap.put("exchangeId", uuid);
            infoMap.put("address", fromAddress);
            infoMap.put("neo", amount + "");
            infoMap.put("tx", isSuccess);
            Disposable disposable = AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().buyQlc(infoMap)
                    .subscribe(sendRow -> {
                        KLog.i("onSuccesse");
                        sendCallBack.onSuccess();
                        EventBus.getDefault().post(new NeoRefrash());
                        TransactionRecord transactionRecord = new TransactionRecord();
                        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
                            switch (entry.getKey()) {
                                case "neo":
                                    transactionRecord.setNeoCount(Integer.parseInt(entry.getValue()));
                                    break;
                                case "exchangeId":
                                    transactionRecord.setExChangeId(entry.getValue());
                                    transactionRecord.setTxid(entry.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                        transactionRecord.setTimestamp(Calendar.getInstance().getTimeInMillis());
                        transactionRecord.setTransactiomType(TransactionRecord.transactionType.transactionGetQlc.ordinal());
                        AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(transactionRecord);
                    }, throwable -> {
                        KLog.i("onError");
                        sendCallBack.onFailure();
                        throwable.printStackTrace();
                    }, () -> {
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                    });
            mCompositeDisposable.add(disposable);
        });
    }

    /**
     * 转账的主方法
     *
     * @param fromAddress  自己的钱包地址
     * @param address      对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param qlc          转账的qlc的数量
     * @param sendCallBack 转账结果的回调
     */
    public void trasaction(String fromAddress, String address, String qlc, SendCallBack sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getUtxo(fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                sendNEP5Token(Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), new SendCallBack() {
                    @Override
                    public void onSuccess() {
                        sendCallBack.onSuccess();
                    }

                    @Override
                    public void onFailure() {
                        sendCallBack.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

    /**
     * 实际进行转账的方法
     *
     * @param wallet            钱包实体，为第三方引用的那个库
     * @param tokenContractHash 第三方的资产的编号，这里只做兑换qlc，所以这个资产编号为qlc的编号。
     * @param fromAddress       自己的钱包地址
     * @param toAddress         对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param amount            转账的qlc的数量
     * @param sendCallBack      转账结果的回调
     */
    private void sendNEP5Token(Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, SendCallBack sendCallBack) {
        if (assets == null) {
            sendCallBack.onFailure();
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.send_failure));
            return;
        }
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNEP5Token(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, (String isSuccess) -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Transaction tx = getTxid(isSuccess);
            KLog.i(tx.getHash());
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("tx", isSuccess);
            Disposable disposable = AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().sendRawTransaction(infoMap)
                    .subscribe(sendRow -> {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        sendCallBack.onSuccess();
                    }, throwable -> {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        sendCallBack.onFailure();
                    }, () -> {
                        //onComplete
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                    });
            mCompositeDisposable.add(disposable);
        });
    }

    /**
     * 连接vpn或打赏的主方法, 还有打赏
     *
     * @param map          封装的请求网络的参数的map集合。这个集合里的参数根据需求的不同而不同
     * @param fromAddress  自己的钱包地址
     * @param address      对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param qlc          连接vpn或打赏的qlc的数量
     * @param sendCallBack 连接vpn或打赏的结果的回调
     */
    public void v2Transaction(Map map, String fromAddress, String address, String qlc, SendBackWithTxId sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getUtxo(fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                payTransaction(map, Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        sendCallBack.onSuccess(txid);
                    }

                    @Override
                    public void onFailure() {
                        sendCallBack.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

    /**
     * 主网连接vpn或打赏的主方法, 还有打赏
     *
     * @param map          封装的请求网络的参数的map集合。这个集合里的参数根据需求的不同而不同
     * @param fromAddress  自己的钱包地址
     * @param address      对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param qlc          连接vpn或打赏的qlc的数量
     * @param sendCallBack 连接vpn或打赏的结果的回调
     */
    public void v2TransactionInMain(Map map, String fromAddress, String address, String qlc, SendBackWithTxId sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getUtxoInMain(fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                payTransaction(map, Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        sendCallBack.onSuccess(txid);
                    }

                    @Override
                    public void onFailure() {
                        sendCallBack.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

    /**
     * 进行连接vpn和打赏的扣费的实际方法
     *
     * @param map               封装的请求网络的参数的map集合。这个集合里的参数根据需求的不同而不同
     * @param wallet            钱包实体
     * @param tokenContractHash 第三方的资产的编号，这里只做兑换qlc，所以这个资产编号为qlc的编号。
     * @param fromAddress       自己的钱包地址
     * @param toAddress         对方的钱包地址， 也就是兑换neo的主钱包地址
     * @param amount            连接vpn或打赏的qlc的数量
     * @param sendCallBack      连接vpn或打赏的结果的回调
     */
    private void payTransaction(Map map, Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, SendBackWithTxId sendCallBack) {
        if (assets == null) {
            sendCallBack.onFailure();
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.send_failure));
            return;
        }
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNEP5Token(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Transaction tx = getTxid(isSuccess);
            KLog.i(tx.getHash());
            map.put("tx", isSuccess);
            AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().v2Transaction(map)
                    .subscribe(new Observer<TransactionResult>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(TransactionResult baseBack) {
                            KLog.i("onSuccesse");
                            if (baseBack.getData().isOperationResult()) {
                                sendCallBack.onSuccess(baseBack.getData().getRecordId());
                            } else {
                                sendCallBack.onFailure();
                            }
                            disposable.dispose();
                        }

                        @Override
                        public void onError(Throwable e) {
                            KLog.i("onError");
                            e.printStackTrace();
                            sendCallBack.onFailure();
                            disposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            KLog.i("onComplete");
                            sendCallBack.onFailure();
//                            disposable.dispose();
                        }
                    });
        });
    }

    public void registerWiFi(Map map, String fromAddress, String address, String qlc, SendBackWithTxId sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getUtxo(address, fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                regsiterWiFiReal(map, Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        sendCallBack.onSuccess(txid);
                    }

                    @Override
                    public void onFailure() {
                        sendCallBack.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

    private void regsiterWiFiReal(Map map, Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, SendBackWithTxId sendCallBack) {
        if (assets == null) {
            sendCallBack.onFailure();
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.send_failure));
            return;
        }
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNEP5Token(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Transaction tx = getTxid(isSuccess);
            KLog.i(tx.getHash());
            map.put("tx", isSuccess);
            Disposable disposable = AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().registeWWifiV3(map)
                    .subscribe(registerWiFi -> {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        TransactionRecord recordSave = new TransactionRecord();
                        recordSave.setTxid(registerWiFi.getRecordId());
                        recordSave.setExChangeId(registerWiFi.getRecordId());
                        recordSave.setAssetName(map.get("ssId") + "");
                        recordSave.setTransactiomType(4);
                        recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                        AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                        sendCallBack.onSuccess(registerWiFi.getRecordId());
                    }, throwable -> {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        sendCallBack.onFailure();
                    }, () -> {
                        //onComplete
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                    });
            mCompositeDisposable.add(disposable);
        });
    }

    public void registerVPN(Map map, String fromAddress, String address, String qlc, SendBackWithTxId sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getUtxo(address, fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                regsiterVPNReal(map, Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        sendCallBack.onSuccess(txid);
                    }

                    @Override
                    public void onFailure() {
                        sendCallBack.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

    private void regsiterVPNReal(Map map, Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, SendBackWithTxId sendCallBack) {
        if (assets == null) {
            sendCallBack.onFailure();
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.send_failure));
            return;
        }
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNEP5Token(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Transaction tx = getTxid(isSuccess);
            KLog.i(tx.getHash());
            map.put("tx", isSuccess);
            AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().vpnRegisterV2(map)
                    .subscribe(new Observer<RegisterVpn>() {
                        Disposable disposable;
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(RegisterVpn registerWiFi) {
                            KLog.i("onSuccesse");
                            TransactionRecord recordSave = new TransactionRecord();
                            recordSave.setTxid(registerWiFi.getRecordId());
                            recordSave.setExChangeId(registerWiFi.getRecordId());
                            recordSave.setAssetName(map.get("vpnName") + "");
                            recordSave.setTransactiomType(5);
                            recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                            AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                            VpnEntity vpnEntity = new VpnEntity();
                            vpnEntity.setVpnName(registerWiFi.getVpnName());
                            vpnEntity.setUsername(registerWiFi.getVpnName());
                            vpnEntity.setIsConnected(false);
                            vpnEntity.setP2pId(registerWiFi.getP2pId());
                            vpnEntity.setProfileLocalPath(registerWiFi.getProfileLocalPath());
                            vpnEntity.setAvatar(registerWiFi.getImgUrl());
                            vpnEntity.setCountry(registerWiFi.getCountry());
                            vpnEntity.setAddress(registerWiFi.getAddress());
                            vpnEntity.setRegisterQlc(registerWiFi.getRegisterQlc());
                            vpnEntity.setAssetTranfer(registerWiFi.getQlc());
                            vpnEntity.setIsInMainWallet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                            vpnEntity.setOnline(true);
                            AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(vpnEntity);
                            sendCallBack.onSuccess(registerWiFi.getRecordId());
                            disposable.dispose();
                        }

                        @Override
                        public void onError(Throwable e) {
                            KLog.i("onError");
                            e.printStackTrace();
                            sendCallBack.onFailure();
                            disposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            KLog.i("onComplete");
                            sendCallBack.onFailure();
                            disposable.dispose();
                        }
                    });
        });
    }

    /**
     * 获取unpsent的方法
     *
     * @param address      需要获取unspent的钱包的地址
     * @param sendCallBack 回调结果
     *                     这个方法验证了主网和测试网切换之后，主钱包是否切换成功了。
     *                     涉及到注册资产，兑换neo，兑换bnb。
     */
    private void getUtxo(String mainAddress, String address, SendCallBack sendCallBack) {

        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            if (ConstantValue.mainAddressData != null && ConstantValue.mainAddressData.getNet().equals("MainNet")) {

            } else {
                ToastUtil.displayShortToast(AppConfig.getInstance().getString(R.string.please_wait));
                EventBus.getDefault().post(new ChangeWalletNeedRefesh());
                sendCallBack.onFailure();
                return;
            }
        } else {
            if (ConstantValue.mainAddressData != null && ConstantValue.mainAddressData.getNet().equals("TestNet")) {

            } else {
                ToastUtil.displayShortToast(AppConfig.getInstance().getString(R.string.please_wait));
                EventBus.getDefault().post(new ChangeWalletNeedRefesh());
                sendCallBack.onFailure();
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().getUnspentAsset(map)
                .subscribe(new Observer<AssetsWarpper>() {
                    Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(AssetsWarpper assetsWarpper) {
                        KLog.i("获取unspent成功");
                        assets = assetsWarpper.getData();
                        sendCallBack.onSuccess();
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.i("onError");
                        e.printStackTrace();
                        sendCallBack.onFailure();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                        disposable.dispose();
                    }
                });
    }

    /**
     * 获取unpsent的方法
     *
     * @param address      需要获取unspent的钱包的地址
     * @param sendCallBack 回调结果
     */
    private void getUtxo(String address, SendCallBack sendCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().getUnspentAsset(map)
                .subscribe(new Observer<AssetsWarpper>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(AssetsWarpper assetsWarpper) {
                        KLog.i("获取unspent成功");
                        assets = assetsWarpper.getData();
                        sendCallBack.onSuccess();
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.i("onError");
                        e.printStackTrace();
                        sendCallBack.onFailure();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                        disposable.dispose();
                    }
                });
    }

    /**
     * 获取unpsent的方法
     *
     * @param address      需要获取unspent的钱包的地址
     * @param sendCallBack 回调结果
     */
    private void getMainUtxo(String address, SendCallBack sendCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        AppConfig.getInstance().getApplicationComponent().getMainHttpAPIWrapper().getUnspentAsset(map)
                .subscribe(new Observer<AssetsWarpper>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(AssetsWarpper assetsWarpper) {
                        KLog.i("获取unspent成功");
                        assets = assetsWarpper.getData();
                        sendCallBack.onSuccess();
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.i("onError");
                        e.printStackTrace();
                        sendCallBack.onFailure();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                        disposable.dispose();
                    }
                });
    }

    /**
     * 主网获取unpsent的方法
     *
     * @param address      需要获取unspent的钱包的地址
     * @param sendCallBack 回调结果
     */
    private void getUtxoInMain(String address, SendCallBack sendCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        AppConfig.getInstance().getApplicationComponent().getMainHttpAPIWrapper().getUnspentAsset(map)
                .subscribe(new Observer<AssetsWarpper>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(AssetsWarpper assetsWarpper) {
                        KLog.i("获取unspent成功");
                        assets = assetsWarpper.getData();
                        sendCallBack.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.i("onError");
                        e.printStackTrace();
                        sendCallBack.onFailure();
                    }

                    @Override
                    public void onComplete() {
                        KLog.i("onComplete");
                        sendCallBack.onFailure();
                    }
                });
    }

    /**
     * 获取交易id的方法
     *
     * @param hex sendrow的参数，一串很长的参数
     * @return 返回该笔交易的id
     */
    private Transaction getTxid(String hex) {
        final byte[] ba;
        ba = ModelUtil.decodeHex(hex);
        return new Transaction(ByteBuffer.wrap(ba));
    }

    public void getTransactionHex(String fromAddress, String address, String qlc, SendBackWithTxId sendCallBack) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        getMainUtxo(fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                if (assets == null) {
                    sendCallBack.onFailure();
                    ToastUtil.displayShortToast("send failure");
                    return;
                }
                NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
                neoNodeRPC.sendNEP5Token(assets, Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), isSuccess -> {
                    KLog.i("开始调用sendRow");
                    KLog.i(isSuccess);
                    Transaction tx = getTxid(isSuccess);
                    KLog.i(tx.getHash());
                    sendCallBack.onSuccess(isSuccess);
                });
            }

            @Override
            public void onFailure() {
                sendCallBack.onFailure();
            }
        });
    }

}

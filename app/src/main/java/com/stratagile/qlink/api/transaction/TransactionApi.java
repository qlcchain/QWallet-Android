package com.stratagile.qlink.api.transaction;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.Assets;
import com.stratagile.qlink.data.NeoCallBack;
import com.stratagile.qlink.data.NeoNodeRPC;
import com.stratagile.qlink.data.UTXOS;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.NeoTransfer;
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
    private UTXOS assets;

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


    public void sendNeo(UTXOS assets, Wallet wallet, NeoNodeRPC.Asset tokenContractHash, String fromAddress, String toAddress, double amount, SendBackWithTxId sendCallBack) {
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNativeAssetTransaction(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, isSuccess -> {
            sendCallBack.onSuccess(isSuccess);
        });
    }


    /**
     * 向另外的钱包地址发送代币
     * 新版本开发
     */
    public void sendNEP5Token(UTXOS assets, Map map, Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, String remark, SendBackWithTxId sendCallBack) {
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        KLog.i("neo钱包为：" + wallet.toString());
        neoNodeRPC.sendNEP5Token(assets, wallet, tokenContractHash, fromAddress, toAddress, amount, remark, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Transaction tx = getTxid(isSuccess);
            KLog.i(tx.getHash());
            map.put("tx", isSuccess);
            AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().neoTokenTransaction(map)
                    .subscribe(new Observer<NeoTransfer>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(NeoTransfer baseBack) {
                            KLog.i("onSuccesse");
                            KLog.i(baseBack);
                            if (baseBack.getData().isTransferResult()) {
                                sendCallBack.onSuccess(tx.getHash().toReverseHexString());
                            } else {
                                sendCallBack.onSuccess("error");
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

}

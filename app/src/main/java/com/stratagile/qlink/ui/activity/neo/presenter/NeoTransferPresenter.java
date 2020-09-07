package com.stratagile.qlink.ui.activity.neo.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.UTXO;
import com.stratagile.qlink.data.UTXOS;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.NeoTransfer;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.ui.activity.neo.contract.NeoTransferContract;
import com.stratagile.qlink.ui.activity.neo.NeoTransferActivity;
import com.stratagile.qlink.utils.ToastUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of NeoTransferActivity
 * @date 2018/11/06 18:16:07
 */
public class NeoTransferPresenter implements NeoTransferContract.NeoTransferContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final NeoTransferContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private NeoTransferActivity mActivity;

    private UTXOS assets;

    @Inject
    public NeoTransferPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull NeoTransferContract.View view, NeoTransferActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
             mCompositeDisposable.dispose();
        }
    }

    @Override
    public void getNeoWalletDetail(String address, Map map) {
        Disposable disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe(new Consumer<NeoWalletInfo>() {
                    @Override
                    public void accept(NeoWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        getNeoTokensInfo(baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getNeoTokensInfo(NeoWalletInfo neoWalletInfo) {
        ArrayList<TokenInfo> tokenInfos = new ArrayList<>();
        for (int i = 0; i < neoWalletInfo.getData().getBalance().size(); i++) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName(neoWalletInfo.getData().getBalance().get(i).getAsset());
            tokenInfo.setTokenSymol(neoWalletInfo.getData().getBalance().get(i).getAsset_symbol());
            tokenInfo.setTokenValue(neoWalletInfo.getData().getBalance().get(i).getAmount());
            tokenInfo.setTokenImgName(getNeoTokenImg(neoWalletInfo.getData().getBalance().get(i)));
            tokenInfo.setWalletAddress(neoWalletInfo.getData().getAddress());
            tokenInfo.setTokenAddress(neoWalletInfo.getData().getBalance().get(i).getAsset_hash());
            tokenInfo.setMainNetToken(true);
            tokenInfo.setWalletType(AllWallet.WalletType.NeoWallet);
            tokenInfos.add(tokenInfo);
        }
        getTokenPrice(tokenInfos);
    }

    private String getNeoTokenImg(NeoWalletInfo.DataBean.BalanceBean balanceBean) {
        return "neo_" + balanceBean.getAsset_symbol().toLowerCase();
    }

    private void getTokenPrice(ArrayList<TokenInfo> tokenInfos) {
        if (tokenInfos == null || tokenInfos.size() == 0) {
            mView.getTokenPriceBack(new ArrayList<>());
            return;
        }
        HashMap<String, Object> infoMap = new HashMap<>();
        String[] tokens = new String[tokenInfos.size()];
        for (int i = 0; i < tokenInfos.size(); i++) {
            tokens[i] = tokenInfos.get(i).getTokenSymol();
        }
        infoMap.put("symbols", tokens);
        infoMap.put("coin", ConstantValue.currencyBean.getName());

        getToeknPrice(tokenInfos, infoMap);
    }

    public void getToeknPrice(ArrayList<TokenInfo> arrayList, HashMap map) {
        Disposable disposable = httpAPIWrapper.getTokenPrice(map)
                .subscribe(new Consumer<TokenPrice>() {
                    @Override
                    public void accept(TokenPrice baseBack) throws Exception {
                        //isSuccesse
                        for (int i = 0; i < baseBack.getData().size(); i++) {
                            if (arrayList.get(i).isMainNetToken()) {
                                arrayList.get(i).setTokenPrice(baseBack.getData().get(i).getPrice());
                            }
                        }
                        mView.getTokenPriceBack(arrayList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getUtxo(String address, SendCallBack sendCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        Disposable disposable = httpAPIWrapper.getMainUnspentAsset(map)
                .subscribe(new Consumer<AssetsWarpper>() {
                    @Override
                    public void accept(AssetsWarpper unspent) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        UTXO[] utxos = new UTXO[unspent.getData().length];
                        assets = new UTXOS(unspent.getData());
//                        assets.copy(unspent.getData());
                        if (sendCallBack != null) {
                            sendCallBack.onSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void sendNEP5Token(TokenInfo tokenInfo, String amount, String toAddress, String remark) {
//        if (assets == null) {
//            ToastUtil.displayShortToast(AppConfig.instance.getString(R.string.pleasewait));
//            mView.showProgressDialog();
//            getUtxo(tokenInfo.getWalletAddress(), new SendCallBack() {
//                @Override
//                public void onSuccess() {
//                    mView.closeProgressDialog();
//                    mView.showProgressDialog();
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("addressFrom", tokenInfo.getWalletAddress());
//                    map.put("addressTo", toAddress);
//                    map.put("symbol", tokenInfo.getTokenSymol());
//                    map.put("amount", amount);
//                    TransactionApi.getInstance().sendNEP5Token(assets, map, com.stratagile.qlink.Account.INSTANCE.getWallet(), tokenInfo.getTokenAddress(), tokenInfo.getWalletAddress(), toAddress, Double.parseDouble(amount), remark, new SendBackWithTxId() {
//
//                        @Override
//                        public void onSuccess(String txid) {
//                            KLog.i(txid);
//                            mView.sendSuccess(AppConfig.getInstance().getResources().getString(R.string.success));
//                            mView.closeProgressDialog();
//                        }
//
//                        @Override
//                        public void onFailure() {
//                            mView.closeProgressDialog();
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure() {
//
//                }
//            });
//            return;
//        }
//        mView.showProgressDialog();
//        Map<String, Object> map = new HashMap<>();
//        map.put("addressFrom", tokenInfo.getWalletAddress());
//        map.put("addressTo", toAddress);
//        map.put("symbol", tokenInfo.getTokenSymol());
//        map.put("amount", amount);
//        TransactionApi.getInstance().sendNEP5Token(assets, map, com.stratagile.qlink.Account.INSTANCE.getWallet(), tokenInfo.getTokenAddress(), tokenInfo.getWalletAddress(), toAddress, Double.parseDouble(amount), remark, new SendBackWithTxId() {
//
//            @Override
//            public void onSuccess(String txid) {
//                KLog.i(txid);
//                mView.sendSuccess(AppConfig.getInstance().getResources().getString(R.string.success));
//                mView.closeProgressDialog();
//            }
//
//            @Override
//            public void onFailure() {
//                mView.closeProgressDialog();
//            }
//        });
    }

    @Override
    public void sendNeo(String amount, String toAddress, TokenInfo tokenInfo) {
//        mView.showProgressDialog();
//        if (assets == null) {
//            getUtxo(com.stratagile.qlink.Account.INSTANCE.getWallet().getAddress(), new SendCallBack() {
//                @Override
//                public void onSuccess() {
//                    mView.closeProgressDialog();
//                }
//
//                @Override
//                public void onFailure() {
//                    mView.closeProgressDialog();
//                }
//            });
//            ToastUtil.displayShortToast(AppConfig.instance.getString(R.string.pleasewait));
//            return;
//        }
//        if (tokenInfo.getTokenSymol().toLowerCase().equals("neo")) {
//            TransactionApi.getInstance().sendNeo(assets, com.stratagile.qlink.Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.NEO, tokenInfo.getWalletAddress(), toAddress, Double.parseDouble(amount), new SendBackWithTxId() {
//                @Override
//                public void onSuccess(String txid) {
//                    KLog.i(txid);
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("addressFrom", tokenInfo.getWalletAddress());
//                    map.put("addressTo", toAddress);
//                    map.put("symbol", tokenInfo.getTokenSymol());
//                    map.put("amount", amount);
//                    map.put("tx", txid);
//                    Disposable disposable = httpAPIWrapper.neoTokenTransaction(map)
//                            .subscribe(new Consumer<NeoTransfer>() {
//                                @Override
//                                public void accept(NeoTransfer unspent) throws Exception {
//                                    //isSuccesse
//                                    KLog.i("onSuccesse");
//                                    if (unspent.getData().isTransferResult()) {
//                                        mView.sendSuccess(AppConfig.getInstance().getResources().getString(R.string.success));
//                                    } else {
//                                        mView.sendSuccess(AppConfig.getInstance().getResources().getString(R.string.error2));
//                                    }
//                                    mView.closeProgressDialog();
//
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    //onError
//                                    KLog.i("onError");
//                                    throwable.printStackTrace();
//                                    mView.closeProgressDialog();
//                                }
//                            }, new Action() {
//                                @Override
//                                public void run() throws Exception {
//                                    //onComplete
//                                    KLog.i("onComplete");
//                                    mView.closeProgressDialog();
//                                }
//                            });
//                    mCompositeDisposable.add(disposable);
//                }
//
//                @Override
//                public void onFailure() {
//                    mView.closeProgressDialog();
//                }
//            });
//        } else {
//            TransactionApi.getInstance().sendNeo(assets, com.stratagile.qlink.Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.GAS, tokenInfo.getWalletAddress(), toAddress, Double.parseDouble(amount), new SendBackWithTxId() {
//                @Override
//                public void onSuccess(String txid) {
//                    KLog.i(txid);
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("addressFrom", tokenInfo.getWalletAddress());
//                    map.put("addressTo", toAddress);
//                    map.put("symbol", tokenInfo.getTokenSymol());
//                    map.put("amount", amount);
//                    map.put("tx", txid);
//                    Disposable disposable = httpAPIWrapper.neoTokenTransaction(map)
//                            .subscribe(new Consumer<BaseBack>() {
//                                @Override
//                                public void accept(BaseBack unspent) throws Exception {
//                                    //isSuccesse
//                                    KLog.i("onSuccesse");
//                                    mView.sendSuccess(AppConfig.getInstance().getResources().getString(R.string.success));
//                                    mView.closeProgressDialog();
//
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    //onError
//                                    KLog.i("onError");
//                                    throwable.printStackTrace();
//                                }
//                            }, new Action() {
//                                @Override
//                                public void run() throws Exception {
//                                    //onComplete
//                                    KLog.i("onComplete");
//                                }
//                            });
//                    mCompositeDisposable.add(disposable);
//                }
//
//                @Override
//                public void onFailure() {
//                    mView.closeProgressDialog();
//                }
//            });
//        }

    }

}
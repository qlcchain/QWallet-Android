package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.Assets;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.entity.newwinq.ProductDetail;
import com.stratagile.qlink.ui.activity.finance.contract.ProductDetailContract;
import com.stratagile.qlink.ui.activity.finance.ProductDetailActivity;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of ProductDetailActivity
 * @date 2019/04/11 11:16:32
 */
public class ProductDetailPresenter implements ProductDetailContract.ProductDetailContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ProductDetailContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ProductDetailActivity mActivity;

    private Assets assets;
    @Inject
    public ProductDetailPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ProductDetailContract.View view, ProductDetailActivity activity) {
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
    public void getProductDetail(Map map) {
        Disposable disposable = httpAPIWrapper.getProductDetail(map)
                .subscribe(new Consumer<ProductDetail>() {
                    @Override
                    public void accept(ProductDetail baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        mView.showProductDetail(baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getNeoWalletDetail(String address, Map map, String toAddress, String amount, SendBackWithTxId sendCallBack) {
        Disposable disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe(new Consumer<NeoWalletInfo>() {
                    @Override
                    public void accept(NeoWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        for (int i = 0; i < baseBack.getData().getBalance().size(); i++) {
                            if (baseBack.getData().getBalance().get(i).getAsset_symbol().toLowerCase().equals("qlc")) {
                                if (baseBack.getData().getBalance().get(i).getAmount() >= Double.parseDouble(amount)) {
                                    transferQLC(amount, address, toAddress, baseBack.getData().getBalance().get(i), new SendBackWithTxId() {
                                        @Override
                                        public void onSuccess(String txid) {
                                            sendCallBack.onSuccess(txid);
                                        }

                                        @Override
                                        public void onFailure() {

                                        }
                                    });
                                } else {
                                    mView.closeProgressDialog();
                                    ToastUtil.displayShortToast(AppConfig.getInstance().getString(R.string.no_enough_qlc));
                                }
                            }
                        }
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
                        assets = unspent.getData();
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
    public void buyQLCProduct(Map map) {
        Disposable disposable = httpAPIWrapper.buyQLCProduct(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack unspent) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                        if (unspent.getCode().equals("0")) {
                            mView.buyQLCProductBack();
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
    public void getQLCCount(Map map) {
        Disposable disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe(new Consumer<NeoWalletInfo>() {
                    @Override
                    public void accept(NeoWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        mView.getNeoTokensInfo(baseBack);
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

    private void transferQLC(String amount, String fromAddress, String toAddress, NeoWalletInfo.DataBean.BalanceBean balanceBean, SendBackWithTxId sendBackWithTxId) {
        Map<String, Object> map = new HashMap<>();
        map.put("addressFrom", fromAddress);
        map.put("addressTo", toAddress);
        map.put("symbol", balanceBean.getAsset_symbol());
        map.put("amount", amount);
        TransactionApi.getInstance().buyQLCProduct(assets, map, Account.INSTANCE.getWallet(), balanceBean.getAsset_hash(), fromAddress, toAddress, Double.parseDouble(amount), new SendBackWithTxId() {

            @Override
            public void onSuccess(String txid) {
                KLog.i(txid);
                sendBackWithTxId.onSuccess(txid);
            }

            @Override
            public void onFailure() {
                mView.closeProgressDialog();
            }
        });
    }

}
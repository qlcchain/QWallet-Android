package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.ui.activity.wallet.contract.NoWalletContract;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of NoWalletActivity
 * @date 2018/01/23 13:54:18
 */
public class NoWalletPresenter implements NoWalletContract.NoWalletContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final NoWalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private NoWalletActivity mActivity;

    @Inject
    public NoWalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull NoWalletContract.View view, NoWalletActivity activity) {
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
    public void createWallet(Map map) {
        mView.showProgressDialog();

    }

    private void getNeoAndGasFromServer(Map map) {
        Disposable disposable = httpAPIWrapper.createWallet(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        Wallet wallet = Account.INSTANCE.getWallet();
//                        com.stratagile.qlink.db.Wallet walletWinq = new com.stratagile.qlink.db.Wallet();
//                        walletWinq.setAddress(wallet.getAddress());
//                        walletWinq.setWif(wallet.getWIF());
//                        walletWinq.setPrivateKey(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()).toLowerCase());
//                        walletWinq.setPublicKey(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()));
//                        walletWinq.setScriptHash(Account.INSTANCE.byteArray2String(wallet.getHashedSignature()));
//                        walletWinq.setIsMain(false);
//                        KLog.i();walletWinq.toString();
//                        AppConfig.getInstance().getDaoSession().getWalletDao().insert(walletWinq);
//                        int size = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().size();
//                        SpUtil.putInt(AppConfig.getInstance(), ConstantValue.currentWallet, size - 1);
//                        CreateWallet createWallet = new CreateWallet();
//                        createWallet.setData(walletWinq);
//                        mView.onCreatWalletSuccess(createWallet, 0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.createWalletFaliure();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        mView.createWalletFaliure();
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void importWallet(Map map) {
        mView.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = Account.INSTANCE.fromWIF(map.get("key") + "");
                if (!result) {
                    result = Account.INSTANCE.fromHex(map.get("key") + "");
                }
                if (result) {
//                    Wallet wallet = Account.INSTANCE.getWallet();
//                    map.put("address", wallet.getAddress());
//                    getNeoAndGasFromServer(map);
                } else {
                    mView.createWalletFaliure();
                }
            }
        }).start();
    }

    @Override
    public void getSacnPermission() {
        AndPermission.with(((Activity) mView))
                .requestCode(101)
                .permission(
                        Manifest.permission.CAMERA
                )
                .rationale((requestCode, rationale) -> {
                            AndPermission
                                    .rationaleDialog((((Activity) mView)), rationale)
                                    .setTitle(AppConfig.getInstance().getResources().getString(R.string.Permission_Requeset))
                                    .setMessage(AppConfig.getInstance().getResources().getString(R.string.We_Need_Some_Permission_to_continue))
                                    .setNegativeButton(AppConfig.getInstance().getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.permission_denied));
                                        }
                                    })
                                    .show();
                        }
                )
                .callback(permission)
                .start();
    }

    private PermissionListener permission = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 101) {
                mView.getScanPermissionSuccess();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 101) {
                KLog.i("权限申请失败");
                ToastUtil.show(((Activity) mView), AppConfig.getInstance().getResources().getString(R.string.permission_denied));
            }
        }
    };
}
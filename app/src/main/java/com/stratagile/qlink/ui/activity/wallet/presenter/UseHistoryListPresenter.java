package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.Assets;
import com.stratagile.qlink.data.NeoNodeRPC;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.ui.activity.wallet.contract.UseHistoryListContract;
import com.stratagile.qlink.ui.activity.wallet.UseHistoryListFragment;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import neoutils.Wallet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of UseHistoryListFragment
 * @date 2018/01/19 11:44:00
 */
public class UseHistoryListPresenter implements UseHistoryListContract.UseHistoryListContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final UseHistoryListContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private UseHistoryListFragment mFragment;
    Assets assets;

    @Inject
    public UseHistoryListPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull UseHistoryListContract.View view, UseHistoryListFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
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
    public void getUseHistoryListFromServer(String useHistoryStatus, int requestPage, int onePageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", onePageSize + "");
        map.put("pageNum", requestPage + "");
    }

    @Override
    public void buyQlc(String exchangeId, String toAddress, String neo, String wif, String fromAddress, SendCallBack sendCallBack) {
        mView.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Account.INSTANCE.getWallet() == null) {
                    Account.INSTANCE.fromWIF(wif);
                }
            }
        }).start();
        String toSendAsset = NeoNodeRPC.Asset.NEO.assetID();
        getUtxo(fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                sendNativeAsset(Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.NEO, fromAddress, toAddress, Double.parseDouble(neo), new SendCallBack() {
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

    @Override
    public void trasaction(String fromAddress, String address, String wif, String qlc) {
        mView.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Account.INSTANCE.getWallet() == null) {
                    Account.INSTANCE.fromWIF(wif);
                }
            }
        }).start();
        getUtxo(fromAddress, new SendCallBack() {
            @Override
            public void onSuccess() {
                sendNEP5Token(Account.INSTANCE.getWallet(), NeoNodeRPC.Asset.QLC.assetID(), fromAddress, address, Double.parseDouble(qlc), new SendCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void getUtxo(String address, SendCallBack sendCallBack) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        Disposable disposable = httpAPIWrapper.getUnspentAsset(map)
                .subscribe(new Consumer<AssetsWarpper>() {
                    @Override
                    public void accept(AssetsWarpper unspent) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        assets = unspent.getData();
                        sendCallBack.onSuccess();
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
    public void getMainAddress(String neo) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.getMainAddress(new HashMap())
                .subscribe(new Consumer<MainAddress>() {
                    @Override
                    public void accept(MainAddress mainAddress) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        ConstantValue.mainAddress = mainAddress.getData().getNEO().getAddress();
                        ConstantValue.ethMainAddress = mainAddress.getData().getETH().getAddress();
                        ConstantValue.mainAddressData = mainAddress.getData();
                        mView.getMainAddressSuccess(mainAddress.getData().getNEO().getAddress(), neo);
                        mView.closeProgressDialog();
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
    public void sendNEP5Token(Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, SendCallBack sendCallBack) {
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNEP5Token(assets, wallet, tokenContractHash, wallet.getAddress(), toAddress, amount, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("tx", isSuccess);
            Disposable disposable = httpAPIWrapper.sendRawTransaction(infoMap)
                    .subscribe(new Consumer<BaseBack>() {
                        @Override
                        public void accept(BaseBack sendRow) throws Exception {
                            //isSuccesse
                            KLog.i("onSuccesse");
                            Gson gson = new Gson();
                            sendCallBack.onSuccess();
//                            mView.sendRowResult(sendRow);
                            mView.closeProgressDialog();
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
        });
    }

    public void sendNativeAsset(Wallet wallet, NeoNodeRPC.Asset tokenContractHash, String fromAddress, String toAddress, Double amount, SendCallBack sendCallBack) {
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        neoNodeRPC.sendNativeAssetTransaction(assets, wallet, tokenContractHash, wallet.getAddress(), toAddress, amount, isSuccess -> {
            KLog.i("开始调用sendRow");
            KLog.i(isSuccess);
            Map<String, String> infoMap = new HashMap<>();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            uuid = uuid.substring(0, 32);
            infoMap.put("exchangeId", uuid);
            infoMap.put("address", toAddress);
            infoMap.put("neo", amount + "");
            infoMap.put("tx", isSuccess);
            Disposable disposable = httpAPIWrapper.buyQlc(infoMap)
                    .subscribe(new Consumer<BaseBack>() {
                        @Override
                        public void accept(BaseBack sendRow) throws Exception {
                            //isSuccesse
                            KLog.i("onSuccesse");
                            sendCallBack.onSuccess();
//                            mView.sendRowResult(sendRow);
                            mView.closeProgressDialog();
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
        });
    }

    @Override
    public void getScanPermission() {
        AndPermission.with(((Fragment) mView))
                .requestCode(101)
                .permission(
                        Manifest.permission.CAMERA
                )
                .rationale((requestCode, rationale) -> {
                            AndPermission
                                    .rationaleDialog((((Fragment) mView).getContext()), rationale)
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
                ToastUtil.show(mFragment.getActivity(), AppConfig.getInstance().getResources().getString(R.string.permission_denied));
            }
        }
    };
    @Override
    public void getRecords(Map map) {
        Disposable disposable = httpAPIWrapper.recordQuerys(map)
                .subscribe(new Consumer<Record>() {
                    @Override
                    public void accept(Record record) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        //mView.closeProgressDialog();
                        //mView.getRecordSuccess(record);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        mView.closeProgressDialog();
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
package com.stratagile.qlink.ui.activity.main.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.GoogleResult;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.eventbus.VpnTitle;
import com.stratagile.qlink.ui.activity.main.contract.MainContract;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.WalletKtutil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import neoutils.Wallet;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: presenter of MainActivity
 * @date 2018/01/09 09:57:09
 */
public class MainPresenter implements MainContract.MainContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final MainContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public MainPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull MainContract.View view) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
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
    public void latlngParseCountry(Map map) {
        Disposable disposable = httpAPIWrapper.latlngParseCountry(map)
                .subscribe(new Consumer<GoogleResult>() {
                    @Override
                    public void accept(GoogleResult googleResult) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        for (GoogleResult.ResultsBean.AddressComponentsBean addressComponentsBean : googleResult.getResults().get(0).getAddress_components()) {
                            KLog.i("循环中.....");
                            if (addressComponentsBean.getTypes().contains("country")) {
                                EventBus.getDefault().post(new VpnTitle(addressComponentsBean.getLong_name()));
                                ConstantValue.longcountry = addressComponentsBean.getLong_name();
                                ConstantValue.shortcountry = addressComponentsBean.getShort_name();
                                KLog.i(addressComponentsBean.getLong_name());
                                KLog.i(addressComponentsBean.getShort_name());
                                return;
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
//                        EventBus.getDefault().post(new VpnTitle("China"));
//                        ConstantValue.longcountry = "China";
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
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
    public void getTox() {
        KLog.i("获取tox的json");
        getMainAddress();
        String jsonPath = Environment.getExternalStorageDirectory() + "/Qlink/Profile/jsonFile.json";
        File jsonFile = new File(jsonPath);
        if (!jsonFile.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "https://nodes.tox.chat/json";
                    String result = "";
                    BufferedReader in = null;
                    try {
                        String urlNameString = url;
                        URL realUrl = new URL(urlNameString);
                        // 打开和URL之间的连接
                        URLConnection connection = realUrl.openConnection();
                        connection.setReadTimeout(10000);
                        connection.setConnectTimeout(10000);
                        // 建立实际的连接
                        connection.connect();
                        // 获取所有响应头字段
                        Map<String, List<String>> map = connection.getHeaderFields();
                        // 定义 BufferedReader输入流来读取URL的响应
                        in = new BufferedReader(new InputStreamReader(
                                connection.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            result += line;
                        }
                        KLog.i(result);
                        FileWriter fw = null;
                        jsonFile.createNewFile();
                        fw = new FileWriter(jsonFile);
                        BufferedWriter out = new BufferedWriter(fw);
                        out.write(result, 0, result.length());
                        out.close();
                    } catch (Exception e) {
                        System.out.println("发送GET请求出现异常！" + e);
                        e.printStackTrace();
                    }
                    // 使用finally块来关闭输入流
                    finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void getLocation() {
        AndPermission.with(((Activity) mView))
                .requestCode(101)
                .permission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .rationale((requestCode, rationale) -> {
                            AndPermission
                                    .rationaleDialog((((Activity) mView)), rationale)
                                    .setTitle(AppConfig.getInstance().getResources().getString(R.string.Permission_Requeset))
                                    .setMessage(AppConfig.getInstance().getResources().getString(R.string.We_Need_Some_Permission_to_continue))
                                    .setNegativeButton(AppConfig.getInstance().getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToastUtil.displayShortToast(AppConfig.getInstance().getString(R.string.permission_denied));
//                                            mView.getPermissionSuccess();
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
                mView.getPermissionSuccess();
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

    @Override
    public void importWallet(Map map) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] privateKey = map.get("key").toString().split(",");
                List<String> privateKeyList = Arrays.asList(privateKey);
                ArrayList<com.stratagile.qlink.db.Wallet> walletArrayList = new ArrayList<>();
                for (int i = 0; i < privateKeyList.size(); i++) {
                    if (Account.INSTANCE.fromHex(privateKeyList.get(i))) {
                        Wallet wallet = Account.INSTANCE.getWallet();
                        com.stratagile.qlink.db.Wallet wallet1 = new com.stratagile.qlink.db.Wallet();
                        wallet1.setIsMain(false);
                        wallet1.setAddress(wallet.getAddress());
                        wallet1.setPrivateKey(WalletKtutil.byteArrayToHex(wallet.getPrivateKey()));
                        wallet1.setPublicKey(WalletKtutil.byteArrayToHex(wallet.getPublicKey()));
                        wallet1.setScriptHash(WalletKtutil.byteArrayToHex(wallet.getHashedSignature()));
                        wallet1.setWif(wallet.getWIF());
                        walletArrayList.add(wallet1);
                        AppConfig.getInstance().getDaoSession().getWalletDao().insert(wallet1);
                        ConstantValue.canClickWallet = true;
                    }
                }
                mView.onCreatWalletSuccess(walletArrayList, 1);
            }
        }).start();
//        Disposable disposable = httpAPIWrapper.batchImportWallet(map)
//                .subscribe(new Consumer<ImportWalletResult>() {
//                    @Override
//                    public void accept(ImportWalletResult wallet) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        if(wallet != null)
//                            mView.onCreatWalletSuccess(wallet, 1);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                        //mView.closeProgressDialog();
//                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    @Override
    public void heartBeat(Map map) {
        httpAPIWrapper.heartBeat(map).subscribe(new HttpObserver<BaseBack>() {
            @Override
            public void onNext(BaseBack baseBack) {
                //isSuccesse
                KLog.i("心跳：onSuccesse");
                onComplete();
            }
        });
    }

    @Override
    public void userAvatar(Map map) {
        if ("".equals(map.get("p2pId"))) {
            return;
        }
        httpAPIWrapper.userHeadView(map)
                .subscribe(new HttpObserver<UpLoadAvatar>() {
                    @Override
                    public void onNext(UpLoadAvatar upLoadAvatar) {
                        KLog.i("onSuccesse");
                        SpUtil.putString(AppConfig.getInstance(), ConstantValue.myAvatarPath, upLoadAvatar.getHead());
                        mView.getAvatarSuccess(upLoadAvatar);
                        Map<String, String> infoMap = new HashMap<>();
                        infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
                        zsFreeNum(infoMap);
                        onComplete();
                    }
                });
    }

    public void getMainAddress() {
        httpAPIWrapper.getMainAddress(new HashMap()).subscribe(new HttpObserver<MainAddress>() {
            @Override
            public void onNext(MainAddress mainAddress) {
                KLog.i("onSuccesse");
                ConstantValue.mainAddress = mainAddress.getData().getNEO().getAddress();
                ConstantValue.ethMainAddress = mainAddress.getData().getETH().getAddress();
                ConstantValue.mainAddressData = mainAddress.getData();
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
                userAvatar(infoMap);
                onComplete();
            }
        });
    }

    public void zsFreeNum(Map map) {
        httpAPIWrapper.zsFreeNum(map)
                .subscribe(new HttpObserver<FreeNum>() {
                    @Override
                    public void onNext(FreeNum baseBack) {
                        mView.onGetFreeNumBack(baseBack.getData().getFreeNum());
                    }
                });
    }
}
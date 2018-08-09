package com.stratagile.qlink.ui.activity.vpn.presenter;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteWindowVpnActivityContract;
import com.stratagile.qlink.ui.activity.vpn.RegisteWindowVpnActivityActivity;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of RegisteWindowVpnActivityActivity
 * @date 2018/08/03 11:56:07
 */
public class RegisteWindowVpnActivityPresenter implements RegisteWindowVpnActivityContract.RegisteWindowVpnActivityContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final RegisteWindowVpnActivityContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private RegisteWindowVpnActivityActivity mActivity;

    private VpnEntity addVpnEntity;

    @Inject
    public RegisteWindowVpnActivityPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull RegisteWindowVpnActivityContract.View view, RegisteWindowVpnActivityActivity activity) {
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
    public void getScanPermission() {
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
                ToastUtil.show(AppConfig.getInstance(), AppConfig.getInstance().getResources().getString(R.string.permission_denied));
            }
        }
    };
    @Override
    public void preAddVpn(VpnEntity vpnEntity) {
        addVpnEntity = vpnEntity;
        Map<String, String> map = new HashMap<>();
        map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0)).getAddress());
        getBalance(map);
    }
    private void getBalance(Map map) {
        httpAPIWrapper.getBalance(map)
                .subscribe(new HttpObserver<Balance>() {
                    @Override
                    public void onNext(Balance balance) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        if (balance.getData().getQLC() > 1) {
                            if (qlinkcom.GetFriendConnectionStatus(addVpnEntity.getFriendNum()) > 0) {
                                Qsdk.getInstance().sendVpnFileRequest(addVpnEntity.getFriendNum(), "", "");
                            }
                        } else {
                            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.Not_enough_QLC));
                        }
                    }
                });
    }
    @Override
    public void upLoadImg(String p2pIdPc) {
        File upLoadFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(mActivity, ConstantValue.myAvaterUpdateTime, "") + ".jpg");
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), upLoadFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("head", SpUtil.getString(mActivity, ConstantValue.myAvaterUpdateTime, "") + ".jpg", image);
        Disposable disposable = httpAPIWrapper.updateMyAvatar(photo, RequestBody.create(MediaType.parse("text/plain"), p2pIdPc))     //userId, nickName
                .subscribe(new Consumer<UpLoadAvatar>() {
                    @Override
                    public void accept(UpLoadAvatar upLoadAvatar) throws Exception {
                        KLog.i("onSucess");
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
    }
}
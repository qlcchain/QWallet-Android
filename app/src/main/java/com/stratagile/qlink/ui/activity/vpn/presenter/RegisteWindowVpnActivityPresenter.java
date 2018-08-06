package com.stratagile.qlink.ui.activity.vpn.presenter;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteWindowVpnActivityContract;
import com.stratagile.qlink.ui.activity.vpn.RegisteWindowVpnActivityActivity;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

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
    /**
     * 检查和分享者的连接情况
     */
    public void checkSharerConnect() {
        if (qlinkcom.GetP2PConnectionStatus() > 0) {
            if (addVpnEntity.getFriendNum() == null || "".equals(addVpnEntity.getFriendNum())) {
                byte[] p2pId = new byte[100];
                String friendNumStr = "";
                qlinkcom.GetFriendP2PPublicKey(addVpnEntity.getP2pId(), p2pId);
                friendNumStr = new String(p2pId).trim();
                KLog.i(friendNumStr);
                if (friendNumStr == null) {
                    friendNumStr = "";
                }
                addVpnEntity.setFriendNum(friendNumStr);
            }
            if (qlinkcom.GetFriendConnectionStatus(addVpnEntity.getFriendNum()) > 0) {
                mView.showProgressDialog();
                QlinkUtil.parseMap2StringAndSend(addVpnEntity.getFriendNum(), ConstantValue.checkConnectReq, new HashMap());
                //handler.sendEmptyMessage(1);
            } else {
                ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.The_friend_is_not_online));
            }
        } else {
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.you_offline));
        }
    }
}
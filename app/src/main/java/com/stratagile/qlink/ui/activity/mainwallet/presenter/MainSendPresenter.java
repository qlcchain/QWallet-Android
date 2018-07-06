package com.stratagile.qlink.ui.activity.mainwallet.presenter;
import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainSendContract;
import com.stratagile.qlink.ui.activity.mainwallet.MainSendFragment;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: presenter of MainSendFragment
 * @date 2018/06/14 09:24:50
 */
public class MainSendPresenter implements MainSendContract.MainSendContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final MainSendContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MainSendFragment mFragment;

    @Inject
    public MainSendPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull MainSendContract.View view, MainSendFragment fragment) {
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
}
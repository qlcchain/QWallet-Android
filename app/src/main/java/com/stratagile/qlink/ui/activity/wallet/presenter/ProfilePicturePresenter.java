package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.ui.activity.wallet.contract.ProfilePictureContract;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.utils.SpUtil;

import java.io.File;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of ProfilePictureActivity
 * @date 2018/01/30 15:17:54
 */
public class ProfilePicturePresenter implements ProfilePictureContract.ProfilePictureContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ProfilePictureContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ProfilePictureActivity mActivity;

    @Inject
    public ProfilePicturePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ProfilePictureContract.View view, ProfilePictureActivity activity) {
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
    public void upLoadImg() {
        mView.showProgressDialog();
        String p2pId = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "");
        File upLoadFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(mActivity, ConstantValue.myAvaterUpdateTime, "") + ".jpg");
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), upLoadFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("head", SpUtil.getString(mActivity, ConstantValue.myAvaterUpdateTime, "") + ".jpg", image);
        Disposable disposable = httpAPIWrapper.updateMyAvatar(photo, RequestBody.create(MediaType.parse("text/plain"), p2pId))     //userId, nickName
                .subscribe(new Consumer<UpLoadAvatar>() {
                    @Override
                    public void accept(UpLoadAvatar upLoadAvatar) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                        mView.updateImgSuccess(upLoadAvatar);
                        SpUtil.putString(AppConfig.getInstance(), ConstantValue.myAvatarPath, upLoadAvatar.getHead());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
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
    public void upLoadImgPc(String p2pIdPc) {
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
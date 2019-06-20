package com.stratagile.qlink.ui.activity.my.presenter;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.otc.Passport;
import com.stratagile.qlink.ui.activity.my.contract.VerificationContract;
import com.stratagile.qlink.ui.activity.my.VerificationActivity;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.SpUtil;

import java.io.File;
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
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of VerificationActivity
 * @date 2019/06/14 15:10:49
 */
public class VerificationPresenter implements VerificationContract.VerificationContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final VerificationContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private VerificationActivity mActivity;

    @Inject
    public VerificationPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull VerificationContract.View view, VerificationActivity activity) {
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
    public void uploadImg(Map map) {
        File upLoadFile1 = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/passport1.jpg");
        File upLoadFile2 = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/passport2.jpg");
        RequestBody passport1 = RequestBody.create(MediaType.parse("image/jpg"), upLoadFile1);
        MultipartBody.Part photo1 = MultipartBody.Part.createFormData("facePhoto", "passport1.jpg", passport1);

        RequestBody passport2 = RequestBody.create(MediaType.parse("image/jpg"), upLoadFile2);
        MultipartBody.Part photo2 = MultipartBody.Part.createFormData("holdingPhoto", "passport2.jpg", passport2);

        Disposable disposable = httpAPIWrapper.updateIdCard(photo1, photo2, RequestBody.create(MediaType.parse("text/plain"), AccountUtil.getUserToken()), RequestBody.create(MediaType.parse("text/plain"), ConstantValue.currentUser.getAccount()))     //userId, nickName
                .subscribe(new Consumer<Passport>() {
                    @Override
                    public void accept(Passport upLoadAvatar) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
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

//    @Override
//    public void getUser(HashMap map) {
//        //mView.showProgressDialog();
//        Disposable disposable = httpAPIWrapper.getUser(map)
//                .subscribe(new Consumer<User>() {
//                    @Override
//                    public void accept(User user) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        mView.setText(user);
//                      //mView.closeProgressDialog();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                      //mView.closeProgressDialog();
//                      //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
//    }
}
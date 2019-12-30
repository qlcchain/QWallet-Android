package com.stratagile.qlink.ui.activity.setting.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.ui.activity.setting.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.setting.SettingsActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: presenter of SettingsActivity
 * @date 2018/05/29 09:30:35
 */
public class SettingsPresenter implements SettingsContract.SettingsContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final SettingsContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private SettingsActivity mActivity;

    @Inject
    public SettingsPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull SettingsContract.View view, SettingsActivity activity) {
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

    public void logout(Map map) {
        mCompositeDisposable.add(httpAPIWrapper.userLogout(map).subscribe(new Consumer<BaseBack>() {
            @Override
            public void accept(BaseBack user) throws Exception {
                //isSuccesse
//                mView.logoutSuccess();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //onError
                throwable.printStackTrace();
                //mView.closeProgressDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //onComplete
            }
        }));
    }


}
package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.MyAssetsActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.MyAssetsContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.MyAssetsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of MyAssetsActivity, provide field for MyAssetsActivity
 * @date 2018/11/11 21:42:25
 */
@Module
public class MyAssetsModule {
    private final MyAssetsContract.View mView;


    public MyAssetsModule(MyAssetsContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MyAssetsPresenter provideMyAssetsPresenter(HttpAPIWrapper httpAPIWrapper, MyAssetsActivity mActivity) {
        return new MyAssetsPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public MyAssetsActivity provideMyAssetsActivity() {
        return (MyAssetsActivity) mView;
    }
}
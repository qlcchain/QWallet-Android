package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.SplashActivity;
import com.stratagile.qlink.ui.activity.main.contract.SplashContract;
import com.stratagile.qlink.ui.activity.main.presenter.SplashPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of SplashActivity, provide field for SplashActivity
 * @date 2018/01/09 11:24:32
 */
@Module
public class SplashModule {
    private final SplashContract.View mView;


    public SplashModule(SplashContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SplashPresenter provideSplashPresenter(HttpAPIWrapper httpAPIWrapper, SplashActivity mActivity) {
        return new SplashPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SplashActivity provideSplashActivity() {
        return (SplashActivity) mView;
    }
}
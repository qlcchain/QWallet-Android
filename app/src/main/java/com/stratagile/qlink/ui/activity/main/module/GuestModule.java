package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.GuestActivity;
import com.stratagile.qlink.ui.activity.main.contract.GuestContract;
import com.stratagile.qlink.ui.activity.main.presenter.GuestPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of GuestActivity, provide field for GuestActivity
 * @date 2018/06/21 15:39:34
 */
@Module
public class GuestModule {
    private final GuestContract.View mView;


    public GuestModule(GuestContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public GuestPresenter provideGuestPresenter(HttpAPIWrapper httpAPIWrapper, GuestActivity mActivity) {
        return new GuestPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public GuestActivity provideGuestActivity() {
        return (GuestActivity) mView;
    }
}
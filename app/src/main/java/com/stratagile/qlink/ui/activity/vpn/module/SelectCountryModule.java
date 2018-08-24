package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.SelectCountryActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.SelectCountryContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.SelectCountryPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of SelectCountryActivity, provide field for SelectCountryActivity
 * @date 2018/02/07 11:01:05
 */
@Module
public class SelectCountryModule {
    private final SelectCountryContract.View mView;


    public SelectCountryModule(SelectCountryContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SelectCountryPresenter provideSelectCountryPresenter(HttpAPIWrapper httpAPIWrapper, SelectCountryActivity mActivity) {
        return new SelectCountryPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SelectCountryActivity provideSelectCountryActivity() {
        return (SelectCountryActivity) mView;
    }
}
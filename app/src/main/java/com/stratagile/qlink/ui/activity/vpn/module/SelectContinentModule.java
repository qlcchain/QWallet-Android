package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.SelectContinentActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.SelectContinentContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.SelectContinentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of SelectContinentActivity, provide field for SelectContinentActivity
 * @date 2018/03/12 16:57:42
 */
@Module
public class SelectContinentModule {
    private final SelectContinentContract.View mView;


    public SelectContinentModule(SelectContinentContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SelectContinentPresenter provideSelectContinentPresenter(HttpAPIWrapper httpAPIWrapper, SelectContinentActivity mActivity) {
        return new SelectContinentPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SelectContinentActivity provideSelectContinentActivity() {
        return (SelectContinentActivity) mView;
    }
}
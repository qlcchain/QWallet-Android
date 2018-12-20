package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosResourceManagementActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosResourceManagementContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosResourceManagementPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosResourceManagementActivity, provide field for EosResourceManagementActivity
 * @date 2018/12/04 18:08:32
 */
@Module
public class EosResourceManagementModule {
    private final EosResourceManagementContract.View mView;


    public EosResourceManagementModule(EosResourceManagementContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosResourceManagementPresenter provideEosResourceManagementPresenter(HttpAPIWrapper httpAPIWrapper, EosResourceManagementActivity mActivity) {
        return new EosResourceManagementPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosResourceManagementActivity provideEosResourceManagementActivity() {
        return (EosResourceManagementActivity) mView;
    }
}
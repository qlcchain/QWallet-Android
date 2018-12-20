package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosActivationActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosActivationContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosActivationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosActivationActivity, provide field for EosActivationActivity
 * @date 2018/12/12 11:17:52
 */
@Module
public class EosActivationModule {
    private final EosActivationContract.View mView;


    public EosActivationModule(EosActivationContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosActivationPresenter provideEosActivationPresenter(HttpAPIWrapper httpAPIWrapper, EosActivationActivity mActivity) {
        return new EosActivationPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosActivationActivity provideEosActivationActivity() {
        return (EosActivationActivity) mView;
    }
}
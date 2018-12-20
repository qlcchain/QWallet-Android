package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosCreateActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosCreateContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosCreatePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosCreateActivity, provide field for EosCreateActivity
 * @date 2018/12/07 11:29:04
 */
@Module
public class EosCreateModule {
    private final EosCreateContract.View mView;


    public EosCreateModule(EosCreateContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosCreatePresenter provideEosCreatePresenter(HttpAPIWrapper httpAPIWrapper, EosCreateActivity mActivity) {
        return new EosCreatePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosCreateActivity provideEosCreateActivity() {
        return (EosCreateActivity) mView;
    }
}
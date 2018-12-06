package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosBuyCpuAndNetActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyCpuAndNetContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosBuyCpuAndNetPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosBuyCpuAndNetActivity, provide field for EosBuyCpuAndNetActivity
 * @date 2018/12/05 18:03:46
 */
@Module
public class EosBuyCpuAndNetModule {
    private final EosBuyCpuAndNetContract.View mView;


    public EosBuyCpuAndNetModule(EosBuyCpuAndNetContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosBuyCpuAndNetPresenter provideEosBuyCpuAndNetPresenter(HttpAPIWrapper httpAPIWrapper, EosBuyCpuAndNetActivity mActivity) {
        return new EosBuyCpuAndNetPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosBuyCpuAndNetActivity provideEosBuyCpuAndNetActivity() {
        return (EosBuyCpuAndNetActivity) mView;
    }
}
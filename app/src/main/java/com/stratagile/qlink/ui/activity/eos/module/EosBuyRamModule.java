package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosBuyRamActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyRamContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosBuyRamPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosBuyRamActivity, provide field for EosBuyRamActivity
 * @date 2018/12/06 14:39:06
 */
@Module
public class EosBuyRamModule {
    private final EosBuyRamContract.View mView;


    public EosBuyRamModule(EosBuyRamContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosBuyRamPresenter provideEosBuyRamPresenter(HttpAPIWrapper httpAPIWrapper, EosBuyRamActivity mActivity) {
        return new EosBuyRamPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosBuyRamActivity provideEosBuyRamActivity() {
        return (EosBuyRamActivity) mView;
    }
}
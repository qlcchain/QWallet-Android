package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosImportActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosImportContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosImportPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosImportActivity, provide field for EosImportActivity
 * @date 2018/11/26 17:06:38
 */
@Module
public class EosImportModule {
    private final EosImportContract.View mView;


    public EosImportModule(EosImportContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosImportPresenter provideEosImportPresenter(HttpAPIWrapper httpAPIWrapper, EosImportActivity mActivity) {
        return new EosImportPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosImportActivity provideEosImportActivity() {
        return (EosImportActivity) mView;
    }
}
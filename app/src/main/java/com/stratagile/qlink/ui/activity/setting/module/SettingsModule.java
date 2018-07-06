package com.stratagile.qlink.ui.activity.setting.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.setting.SettingsActivity;
import com.stratagile.qlink.ui.activity.setting.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.setting.presenter.SettingsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: The moduele of SettingsActivity, provide field for SettingsActivity
 * @date 2018/05/29 09:30:35
 */
@Module
public class SettingsModule {
    private final SettingsContract.View mView;


    public SettingsModule(SettingsContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SettingsPresenter provideSettingsPresenter(HttpAPIWrapper httpAPIWrapper, SettingsActivity mActivity) {
        return new SettingsPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SettingsActivity provideSettingsActivity() {
        return (SettingsActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.SettingsFragment;
import com.stratagile.qlink.ui.activity.main.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.main.presenter.SettingsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of SettingsFragment, provide field for SettingsFragment
 * @date 2018/10/29 10:38:15
 */
@Module
public class SettingsModule {
    private final SettingsContract.View mView;


    public SettingsModule(SettingsContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SettingsPresenter provideSettingsPresenter(HttpAPIWrapper httpAPIWrapper, SettingsFragment mFragment) {
        return new SettingsPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public SettingsFragment provideSettingsFragment() {
        return (SettingsFragment) mView;
    }
}
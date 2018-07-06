package com.stratagile.qlink.ui.activity.setting.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.setting.SelectLanguageActivityActivity;
import com.stratagile.qlink.ui.activity.setting.contract.SelectLanguageActivityContract;
import com.stratagile.qlink.ui.activity.setting.presenter.SelectLanguageActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: The moduele of SelectLanguageActivityActivity, provide field for SelectLanguageActivityActivity
 * @date 2018/06/26 17:11:28
 */
@Module
public class SelectLanguageActivityModule {
    private final SelectLanguageActivityContract.View mView;


    public SelectLanguageActivityModule(SelectLanguageActivityContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SelectLanguageActivityPresenter provideSelectLanguageActivityPresenter(HttpAPIWrapper httpAPIWrapper, SelectLanguageActivityActivity mActivity) {
        return new SelectLanguageActivityPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SelectLanguageActivityActivity provideSelectLanguageActivityActivity() {
        return (SelectLanguageActivityActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.ui.activity.main.contract.MainContract;
import com.stratagile.qlink.ui.activity.main.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of MainActivity, provide field for MainActivity
 * @date 2018/01/09 09:57:09
 */
@Module
public class MainModule {
    private final MainContract.View mView;


    public MainModule(MainContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MainPresenter provideMainPresenter(HttpAPIWrapper httpAPIWrapper) {
        return new MainPresenter(httpAPIWrapper, mView);
    }

}
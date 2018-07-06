package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.LogActivity;
import com.stratagile.qlink.ui.activity.main.contract.LogContract;
import com.stratagile.qlink.ui.activity.main.presenter.LogPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of LogActivity, provide field for LogActivity
 * @date 2018/02/12 14:51:34
 */
@Module
public class LogModule {
    private final LogContract.View mView;


    public LogModule(LogContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public LogPresenter provideLogPresenter(HttpAPIWrapper httpAPIWrapper, LogActivity mActivity) {
        return new LogPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public LogActivity provideLogActivity() {
        return (LogActivity) mView;
    }
}
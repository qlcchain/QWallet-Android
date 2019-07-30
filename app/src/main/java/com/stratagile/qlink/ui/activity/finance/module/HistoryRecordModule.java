package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.HistoryRecordActivity;
import com.stratagile.qlink.ui.activity.finance.contract.HistoryRecordContract;
import com.stratagile.qlink.ui.activity.finance.presenter.HistoryRecordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of HistoryRecordActivity, provide field for HistoryRecordActivity
 * @date 2019/04/24 13:48:39
 */
@Module
public class HistoryRecordModule {
    private final HistoryRecordContract.View mView;


    public HistoryRecordModule(HistoryRecordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public HistoryRecordPresenter provideHistoryRecordPresenter(HttpAPIWrapper httpAPIWrapper, HistoryRecordActivity mActivity) {
        return new HistoryRecordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public HistoryRecordActivity provideHistoryRecordActivity() {
        return (HistoryRecordActivity) mView;
    }
}
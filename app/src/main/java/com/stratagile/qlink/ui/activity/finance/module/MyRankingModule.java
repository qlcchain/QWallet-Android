package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.MyRankingActivity;
import com.stratagile.qlink.ui.activity.finance.contract.MyRankingContract;
import com.stratagile.qlink.ui.activity.finance.presenter.MyRankingPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of MyRankingActivity, provide field for MyRankingActivity
 * @date 2019/04/24 11:14:23
 */
@Module
public class MyRankingModule {
    private final MyRankingContract.View mView;


    public MyRankingModule(MyRankingContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MyRankingPresenter provideMyRankingPresenter(HttpAPIWrapper httpAPIWrapper, MyRankingActivity mActivity) {
        return new MyRankingPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public MyRankingActivity provideMyRankingActivity() {
        return (MyRankingActivity) mView;
    }
}
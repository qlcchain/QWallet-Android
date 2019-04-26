package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.JoinCommunityActivity;
import com.stratagile.qlink.ui.activity.finance.contract.JoinCommunityContract;
import com.stratagile.qlink.ui.activity.finance.presenter.JoinCommunityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of JoinCommunityActivity, provide field for JoinCommunityActivity
 * @date 2019/04/24 17:15:42
 */
@Module
public class JoinCommunityModule {
    private final JoinCommunityContract.View mView;


    public JoinCommunityModule(JoinCommunityContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public JoinCommunityPresenter provideJoinCommunityPresenter(HttpAPIWrapper httpAPIWrapper, JoinCommunityActivity mActivity) {
        return new JoinCommunityPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public JoinCommunityActivity provideJoinCommunityActivity() {
        return (JoinCommunityActivity) mView;
    }
}
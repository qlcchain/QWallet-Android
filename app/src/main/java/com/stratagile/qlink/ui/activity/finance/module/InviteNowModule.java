package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity;
import com.stratagile.qlink.ui.activity.finance.contract.InviteNowContract;
import com.stratagile.qlink.ui.activity.finance.presenter.InviteNowPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of InviteNowActivity, provide field for InviteNowActivity
 * @date 2019/04/28 16:32:00
 */
@Module
public class InviteNowModule {
    private final InviteNowContract.View mView;


    public InviteNowModule(InviteNowContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public InviteNowPresenter provideInviteNowPresenter(HttpAPIWrapper httpAPIWrapper, InviteNowActivity mActivity) {
        return new InviteNowPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public InviteNowActivity provideInviteNowActivity() {
        return (InviteNowActivity) mView;
    }
}
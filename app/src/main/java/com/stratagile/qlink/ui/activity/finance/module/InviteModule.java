package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.InviteActivity;
import com.stratagile.qlink.ui.activity.finance.contract.InviteContract;
import com.stratagile.qlink.ui.activity.finance.presenter.InvitePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of InviteActivity, provide field for InviteActivity
 * @date 2019/04/23 15:34:34
 */
@Module
public class InviteModule {
    private final InviteContract.View mView;


    public InviteModule(InviteContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public InvitePresenter provideInvitePresenter(HttpAPIWrapper httpAPIWrapper, InviteActivity mActivity) {
        return new InvitePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public InviteActivity provideInviteActivity() {
        return (InviteActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ChangePasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ChangePasswordContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChangePasswordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ChangePasswordActivity, provide field for ChangePasswordActivity
 * @date 2018/11/12 13:44:10
 */
@Module
public class ChangePasswordModule {
    private final ChangePasswordContract.View mView;


    public ChangePasswordModule(ChangePasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ChangePasswordPresenter provideChangePasswordPresenter(HttpAPIWrapper httpAPIWrapper, ChangePasswordActivity mActivity) {
        return new ChangePasswordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ChangePasswordActivity provideChangePasswordActivity() {
        return (ChangePasswordActivity) mView;
    }
}
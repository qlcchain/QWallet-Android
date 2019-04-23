package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.LoginActivity;
import com.stratagile.qlink.ui.activity.my.contract.LoginContract;
import com.stratagile.qlink.ui.activity.my.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of LoginActivity, provide field for LoginActivity
 * @date 2019/04/23 10:05:31
 */
@Module
public class LoginModule {
    private final LoginContract.View mView;


    public LoginModule(LoginContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public LoginPresenter provideLoginPresenter(HttpAPIWrapper httpAPIWrapper, LoginActivity mActivity) {
        return new LoginPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public LoginActivity provideLoginActivity() {
        return (LoginActivity) mView;
    }
}
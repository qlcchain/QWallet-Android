package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.LoginFragment;
import com.stratagile.qlink.ui.activity.my.contract.LoginContract;
import com.stratagile.qlink.ui.activity.my.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of LoginFragment, provide field for LoginFragment
 * @date 2019/04/09 11:45:22
 */
@Module
public class LoginModule {
    private final LoginContract.View mView;


    public LoginModule(LoginContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public LoginPresenter provideLoginPresenter(HttpAPIWrapper httpAPIWrapper, LoginFragment mFragment) {
        return new LoginPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public LoginFragment provideLoginFragment() {
        return (LoginFragment) mView;
    }
}
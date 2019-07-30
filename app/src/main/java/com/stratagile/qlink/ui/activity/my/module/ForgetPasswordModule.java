package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.ForgetPasswordActivity;
import com.stratagile.qlink.ui.activity.my.contract.ForgetPasswordContract;
import com.stratagile.qlink.ui.activity.my.presenter.ForgetPasswordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of ForgetPasswordActivity, provide field for ForgetPasswordActivity
 * @date 2019/04/25 10:28:27
 */
@Module
public class ForgetPasswordModule {
    private final ForgetPasswordContract.View mView;


    public ForgetPasswordModule(ForgetPasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ForgetPasswordPresenter provideForgetPasswordPresenter(HttpAPIWrapper httpAPIWrapper, ForgetPasswordActivity mActivity) {
        return new ForgetPasswordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ForgetPasswordActivity provideForgetPasswordActivity() {
        return (ForgetPasswordActivity) mView;
    }
}
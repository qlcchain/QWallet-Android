package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.VerificationActivity;
import com.stratagile.qlink.ui.activity.my.contract.VerificationContract;
import com.stratagile.qlink.ui.activity.my.presenter.VerificationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of VerificationActivity, provide field for VerificationActivity
 * @date 2019/06/14 15:10:49
 */
@Module
public class VerificationModule {
    private final VerificationContract.View mView;


    public VerificationModule(VerificationContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public VerificationPresenter provideVerificationPresenter(HttpAPIWrapper httpAPIWrapper, VerificationActivity mActivity) {
        return new VerificationPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public VerificationActivity provideVerificationActivity() {
        return (VerificationActivity) mView;
    }
}
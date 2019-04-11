package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.RetrievePasswordActivity;
import com.stratagile.qlink.ui.activity.my.contract.RetrievePasswordContract;
import com.stratagile.qlink.ui.activity.my.presenter.RetrievePasswordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of RetrievePasswordActivity, provide field for RetrievePasswordActivity
 * @date 2019/04/09 14:21:19
 */
@Module
public class RetrievePasswordModule {
    private final RetrievePasswordContract.View mView;


    public RetrievePasswordModule(RetrievePasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RetrievePasswordPresenter provideRetrievePasswordPresenter(HttpAPIWrapper httpAPIWrapper, RetrievePasswordActivity mActivity) {
        return new RetrievePasswordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RetrievePasswordActivity provideRetrievePasswordActivity() {
        return (RetrievePasswordActivity) mView;
    }
}
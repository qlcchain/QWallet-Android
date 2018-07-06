package com.stratagile.qlink.ui.activity.sms.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.sms.SmsFragment;
import com.stratagile.qlink.ui.activity.sms.contract.SmsContract;
import com.stratagile.qlink.ui.activity.sms.presenter.SmsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.sms
 * @Description: The moduele of SmsFragment, provide field for SmsFragment
 * @date 2018/01/10 14:59:05
 */
@Module
public class SmsModule {
    private final SmsContract.View mView;


    public SmsModule(SmsContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SmsPresenter provideSmsPresenter(HttpAPIWrapper httpAPIWrapper, SmsFragment mFragment) {
        return new SmsPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public SmsFragment provideSmsFragment() {
        return (SmsFragment) mView;
    }
}
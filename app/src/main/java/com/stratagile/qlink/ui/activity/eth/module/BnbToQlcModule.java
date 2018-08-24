package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.BnbToQlcActivity;
import com.stratagile.qlink.ui.activity.eth.contract.BnbToQlcContract;
import com.stratagile.qlink.ui.activity.eth.presenter.BnbToQlcPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of BnbToQlcActivity, provide field for BnbToQlcActivity
 * @date 2018/05/24 10:46:37
 */
@Module
public class BnbToQlcModule {
    private final BnbToQlcContract.View mView;


    public BnbToQlcModule(BnbToQlcContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public BnbToQlcPresenter provideBnbToQlcPresenter(HttpAPIWrapper httpAPIWrapper, BnbToQlcActivity mActivity) {
        return new BnbToQlcPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public BnbToQlcActivity provideBnbToQlcActivity() {
        return (BnbToQlcActivity) mView;
    }
}
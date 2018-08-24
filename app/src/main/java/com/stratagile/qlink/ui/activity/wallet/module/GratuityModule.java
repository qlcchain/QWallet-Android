package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.GratuityActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.GratuityContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.GratuityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of GratuityActivity, provide field for GratuityActivity
 * @date 2018/02/02 16:19:02
 */
@Module
public class GratuityModule {
    private final GratuityContract.View mView;


    public GratuityModule(GratuityContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public GratuityPresenter provideGratuityPresenter(HttpAPIWrapper httpAPIWrapper, GratuityActivity mActivity) {
        return new GratuityPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public GratuityActivity provideGratuityActivity() {
        return (GratuityActivity) mView;
    }
}
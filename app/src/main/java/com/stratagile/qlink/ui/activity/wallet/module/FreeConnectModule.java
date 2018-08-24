package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.FreeConnectActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.FreeConnectContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.FreeConnectPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of FreeConnectActivity, provide field for FreeConnectActivity
 * @date 2018/07/18 11:53:01
 */
@Module
public class FreeConnectModule {
    private final FreeConnectContract.View mView;


    public FreeConnectModule(FreeConnectContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public FreeConnectPresenter provideFreeConnectPresenter(HttpAPIWrapper httpAPIWrapper, FreeConnectActivity mActivity) {
        return new FreeConnectPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public FreeConnectActivity provideFreeConnectActivity() {
        return (FreeConnectActivity) mView;
    }
}
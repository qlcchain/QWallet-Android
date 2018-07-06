package com.stratagile.qlink.ui.activity.mainwallet.module;

import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainWalletActivity;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainWalletContract;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The moduele of MainWalletActivity, provide field for MainWalletActivity
 * @date 2018/06/13 14:09:33
 */
@Module
public class MainWalletModule {
    private final MainWalletContract.View mView;


    public MainWalletModule(MainWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MainWalletPresenter provideMainWalletPresenter(MainHttpAPIWrapper httpAPIWrapper, MainWalletActivity mActivity) {
        return new MainWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public MainWalletActivity provideMainWalletActivity() {
        return (MainWalletActivity) mView;
    }
}
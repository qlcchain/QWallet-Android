package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletCreatedActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletCreatedContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletCreatedPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of WalletCreatedActivity, provide field for WalletCreatedActivity
 * @date 2018/01/24 16:27:17
 */
@Module
public class WalletCreatedModule {
    private final WalletCreatedContract.View mView;


    public WalletCreatedModule(WalletCreatedContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WalletCreatedPresenter provideWalletCreatedPresenter(HttpAPIWrapper httpAPIWrapper, WalletCreatedActivity mActivity) {
        return new WalletCreatedPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WalletCreatedActivity provideWalletCreatedActivity() {
        return (WalletCreatedActivity) mView;
    }
}
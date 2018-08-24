package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.NoWalletContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.NoWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of NoWalletActivity, provide field for NoWalletActivity
 * @date 2018/01/23 13:54:18
 */
@Module
public class NoWalletModule {
    private final NoWalletContract.View mView;


    public NoWalletModule(NoWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public NoWalletPresenter provideNoWalletPresenter(HttpAPIWrapper httpAPIWrapper, NoWalletActivity mActivity) {
        return new NoWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public NoWalletActivity provideNoWalletActivity() {
        return (NoWalletActivity) mView;
    }
}
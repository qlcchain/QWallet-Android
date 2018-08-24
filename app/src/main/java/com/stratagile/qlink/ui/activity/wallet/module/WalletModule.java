package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletFragment;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of WalletFragment, provide field for WalletFragment
 * @date 2018/01/18 19:08:00
 */
@Module
public class WalletModule {
    private final WalletContract.View mView;


    public WalletModule(WalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WalletPresenter provideWalletPresenter(HttpAPIWrapper httpAPIWrapper) {
        return new WalletPresenter(httpAPIWrapper, mView);
    }

}
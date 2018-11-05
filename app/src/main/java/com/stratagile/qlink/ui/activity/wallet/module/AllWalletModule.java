package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.AllWalletFragment;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of AllWalletFragment, provide field for AllWalletFragment
 * @date 2018/10/24 10:17:57
 */
@Module
public class AllWalletModule {
    private final AllWalletContract.View mView;


    public AllWalletModule(AllWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public AllWalletPresenter provideAllWalletPresenter(HttpAPIWrapper httpAPIWrapper, AllWalletFragment mFragment) {
        return new AllWalletPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public AllWalletFragment provideAllWalletFragment() {
        return (AllWalletFragment) mView;
    }
}
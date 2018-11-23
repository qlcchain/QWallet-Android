package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.AllWalletTokenActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletTokenContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletTokenPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of AllWalletTokenActivity, provide field for AllWalletTokenActivity
 * @date 2018/10/24 15:49:29
 */
@Module
public class AllWalletTokenModule {
    private final AllWalletTokenContract.View mView;


    public AllWalletTokenModule(AllWalletTokenContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public AllWalletTokenPresenter provideAllWalletTokenPresenter(HttpAPIWrapper httpAPIWrapper, AllWalletTokenActivity mActivity) {
        return new AllWalletTokenPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public AllWalletTokenActivity provideAllWalletTokenActivity() {
        return (AllWalletTokenActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletDetailActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletDetailContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of WalletDetailActivity, provide field for WalletDetailActivity
 * @date 2018/01/19 10:21:00
 */
@Module
public class WalletDetailModule {
    private final WalletDetailContract.View mView;


    public WalletDetailModule(WalletDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WalletDetailPresenter provideWalletDetailPresenter(HttpAPIWrapper httpAPIWrapper, WalletDetailActivity mActivity) {
        return new WalletDetailPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WalletDetailActivity provideWalletDetailActivity() {
        return (WalletDetailActivity) mView;
    }
}
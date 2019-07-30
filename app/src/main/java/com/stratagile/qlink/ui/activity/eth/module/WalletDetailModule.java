package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.WalletDetailActivity;
import com.stratagile.qlink.ui.activity.eth.contract.WalletDetailContract;
import com.stratagile.qlink.ui.activity.eth.presenter.WalletDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthWalletDetailActivity, provide field for EthWalletDetailActivity
 * @date 2018/10/25 15:02:11
 */
@Module
public class WalletDetailModule {
    private final WalletDetailContract.View mView;


    public WalletDetailModule(WalletDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WalletDetailPresenter provideEthWalletDetailPresenter(HttpAPIWrapper httpAPIWrapper, WalletDetailActivity mActivity) {
        return new WalletDetailPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WalletDetailActivity provideEthWalletDetailActivity() {
        return (WalletDetailActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWalletActivity;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthWalletActivity, provide field for EthWalletActivity
 * @date 2018/05/24 10:30:26
 */
@Module
public class EthWalletModule {
    private final EthWalletContract.View mView;


    public EthWalletModule(EthWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthWalletPresenter provideEthWalletPresenter(HttpAPIWrapper httpAPIWrapper, EthWalletActivity mActivity) {
        return new EthWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthWalletActivity provideEthWalletActivity() {
        return (EthWalletActivity) mView;
    }
}
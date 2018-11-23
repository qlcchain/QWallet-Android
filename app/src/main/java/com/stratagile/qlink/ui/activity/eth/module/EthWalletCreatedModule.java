package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletCreatedContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWalletCreatedPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthWalletCreatedActivity, provide field for EthWalletCreatedActivity
 * @date 2018/10/23 15:15:28
 */
@Module
public class EthWalletCreatedModule {
    private final EthWalletCreatedContract.View mView;


    public EthWalletCreatedModule(EthWalletCreatedContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthWalletCreatedPresenter provideEthWalletCreatedPresenter(HttpAPIWrapper httpAPIWrapper, EthWalletCreatedActivity mActivity) {
        return new EthWalletCreatedPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthWalletCreatedActivity provideEthWalletCreatedActivity() {
        return (EthWalletCreatedActivity) mView;
    }
}
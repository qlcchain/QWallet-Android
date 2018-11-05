package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWalletDetailActivity;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletDetailContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWalletDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthWalletDetailActivity, provide field for EthWalletDetailActivity
 * @date 2018/10/25 15:02:11
 */
@Module
public class EthWalletDetailModule {
    private final EthWalletDetailContract.View mView;


    public EthWalletDetailModule(EthWalletDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthWalletDetailPresenter provideEthWalletDetailPresenter(HttpAPIWrapper httpAPIWrapper, EthWalletDetailActivity mActivity) {
        return new EthWalletDetailPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthWalletDetailActivity provideEthWalletDetailActivity() {
        return (EthWalletDetailActivity) mView;
    }
}
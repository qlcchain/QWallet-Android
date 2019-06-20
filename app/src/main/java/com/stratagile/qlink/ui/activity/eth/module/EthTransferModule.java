package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthTransferActivity;
import com.stratagile.qlink.ui.activity.eth.contract.EthTransferContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthTransferPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of EthTransferActivity, provide field for EthTransferActivity
 * @date 2018/10/31 10:17:17
 */
@Module
public class EthTransferModule {
    private final EthTransferContract.View mView;


    public EthTransferModule(EthTransferContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthTransferPresenter provideEthTransferPresenter(HttpAPIWrapper httpAPIWrapper, EthTransferActivity mActivity) {
        return new EthTransferPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthTransferActivity provideEthTransferActivity() {
        return (EthTransferActivity) mView;
    }
}
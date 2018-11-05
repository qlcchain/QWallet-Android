package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.EthTransactionRecordActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.EthTransactionRecordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of EthTransactionRecordActivity, provide field for EthTransactionRecordActivity
 * @date 2018/10/29 16:12:21
 */
@Module
public class EthTransactionRecordModule {
    private final EthTransactionRecordContract.View mView;


    public EthTransactionRecordModule(EthTransactionRecordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthTransactionRecordPresenter provideEthTransactionRecordPresenter(HttpAPIWrapper httpAPIWrapper, EthTransactionRecordActivity mActivity) {
        return new EthTransactionRecordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthTransactionRecordActivity provideEthTransactionRecordActivity() {
        return (EthTransactionRecordActivity) mView;
    }
}
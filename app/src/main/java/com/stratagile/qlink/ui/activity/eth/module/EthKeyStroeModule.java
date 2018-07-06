package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthKeyStroeFragment;
import com.stratagile.qlink.ui.activity.eth.contract.EthKeyStroeContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthKeyStroePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthKeyStroeFragment, provide field for EthKeyStroeFragment
 * @date 2018/05/24 17:48:34
 */
@Module
public class EthKeyStroeModule {
    private final EthKeyStroeContract.View mView;


    public EthKeyStroeModule(EthKeyStroeContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthKeyStroePresenter provideEthKeyStroePresenter(HttpAPIWrapper httpAPIWrapper, EthKeyStroeFragment mFragment) {
        return new EthKeyStroePresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public EthKeyStroeFragment provideEthKeyStroeFragment() {
        return (EthKeyStroeFragment) mView;
    }
}
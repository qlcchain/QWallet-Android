package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWatchFragment;
import com.stratagile.qlink.ui.activity.eth.contract.EthWatchContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWatchPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthWatchFragment, provide field for EthWatchFragment
 * @date 2018/10/22 14:16:26
 */
@Module
public class EthWatchModule {
    private final EthWatchContract.View mView;


    public EthWatchModule(EthWatchContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthWatchPresenter provideEthWatchPresenter(HttpAPIWrapper httpAPIWrapper, EthWatchFragment mFragment) {
        return new EthWatchPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public EthWatchFragment provideEthWatchFragment() {
        return (EthWatchFragment) mView;
    }
}
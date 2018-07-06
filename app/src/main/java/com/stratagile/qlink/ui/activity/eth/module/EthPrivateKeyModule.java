package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthPrivateKeyFragment;
import com.stratagile.qlink.ui.activity.eth.contract.EthPrivateKeyContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthPrivateKeyPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthPrivateKeyFragment, provide field for EthPrivateKeyFragment
 * @date 2018/05/24 17:49:02
 */
@Module
public class EthPrivateKeyModule {
    private final EthPrivateKeyContract.View mView;


    public EthPrivateKeyModule(EthPrivateKeyContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthPrivateKeyPresenter provideEthPrivateKeyPresenter(HttpAPIWrapper httpAPIWrapper, EthPrivateKeyFragment mFragment) {
        return new EthPrivateKeyPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public EthPrivateKeyFragment provideEthPrivateKeyFragment() {
        return (EthPrivateKeyFragment) mView;
    }
}
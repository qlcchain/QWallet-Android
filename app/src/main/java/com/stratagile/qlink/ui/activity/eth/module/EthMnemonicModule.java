package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicFragment;
import com.stratagile.qlink.ui.activity.eth.contract.EthMnemonicContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthMnemonicPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthMnemonicFragment, provide field for EthMnemonicFragment
 * @date 2018/10/22 14:12:37
 */
@Module
public class EthMnemonicModule {
    private final EthMnemonicContract.View mView;


    public EthMnemonicModule(EthMnemonicContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthMnemonicPresenter provideEthMnemonicPresenter(HttpAPIWrapper httpAPIWrapper, EthMnemonicFragment mFragment) {
        return new EthMnemonicPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public EthMnemonicFragment provideEthMnemonicFragment() {
        return (EthMnemonicFragment) mView;
    }
}
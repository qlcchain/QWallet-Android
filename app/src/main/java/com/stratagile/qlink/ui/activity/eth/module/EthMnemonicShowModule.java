package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicShowActivity;
import com.stratagile.qlink.ui.activity.eth.contract.EthMnemonicShowContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthMnemonicShowPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthMnemonicShowActivity, provide field for EthMnemonicShowActivity
 * @date 2018/10/23 15:34:11
 */
@Module
public class EthMnemonicShowModule {
    private final EthMnemonicShowContract.View mView;


    public EthMnemonicShowModule(EthMnemonicShowContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthMnemonicShowPresenter provideEthMnemonicShowPresenter(HttpAPIWrapper httpAPIWrapper, EthMnemonicShowActivity mActivity) {
        return new EthMnemonicShowPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthMnemonicShowActivity provideEthMnemonicShowActivity() {
        return (EthMnemonicShowActivity) mView;
    }
}
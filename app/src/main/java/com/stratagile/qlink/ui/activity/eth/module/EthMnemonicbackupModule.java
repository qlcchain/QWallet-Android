package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicbackupActivity;
import com.stratagile.qlink.ui.activity.eth.contract.EthMnemonicbackupContract;
import com.stratagile.qlink.ui.activity.eth.presenter.EthMnemonicbackupPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of EthMnemonicbackupActivity, provide field for EthMnemonicbackupActivity
 * @date 2018/10/23 15:13:44
 */
@Module
public class EthMnemonicbackupModule {
    private final EthMnemonicbackupContract.View mView;


    public EthMnemonicbackupModule(EthMnemonicbackupContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EthMnemonicbackupPresenter provideEthMnemonicbackupPresenter(HttpAPIWrapper httpAPIWrapper, EthMnemonicbackupActivity mActivity) {
        return new EthMnemonicbackupPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EthMnemonicbackupActivity provideEthMnemonicbackupActivity() {
        return (EthMnemonicbackupActivity) mView;
    }
}
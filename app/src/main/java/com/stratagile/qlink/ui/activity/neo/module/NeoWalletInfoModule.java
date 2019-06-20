package com.stratagile.qlink.ui.activity.neo.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.neo.NeoWalletInfoActivity;
import com.stratagile.qlink.ui.activity.neo.contract.NeoWalletInfoContract;
import com.stratagile.qlink.ui.activity.neo.presenter.NeoWalletInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of NeoWalletInfoActivity, provide field for NeoWalletInfoActivity
 * @date 2018/11/05 17:19:51
 */
@Module
public class NeoWalletInfoModule {
    private final NeoWalletInfoContract.View mView;


    public NeoWalletInfoModule(NeoWalletInfoContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public NeoWalletInfoPresenter provideNeoWalletInfoPresenter(HttpAPIWrapper httpAPIWrapper, NeoWalletInfoActivity mActivity) {
        return new NeoWalletInfoPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public NeoWalletInfoActivity provideNeoWalletInfoActivity() {
        return (NeoWalletInfoActivity) mView;
    }
}
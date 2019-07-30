package com.stratagile.qlink.ui.activity.neo.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.neo.NeoTransferActivity;
import com.stratagile.qlink.ui.activity.neo.contract.NeoTransferContract;
import com.stratagile.qlink.ui.activity.neo.presenter.NeoTransferPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of NeoTransferActivity, provide field for NeoTransferActivity
 * @date 2018/11/06 18:16:07
 */
@Module
public class NeoTransferModule {
    private final NeoTransferContract.View mView;


    public NeoTransferModule(NeoTransferContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public NeoTransferPresenter provideNeoTransferPresenter(HttpAPIWrapper httpAPIWrapper, NeoTransferActivity mActivity) {
        return new NeoTransferPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public NeoTransferActivity provideNeoTransferActivity() {
        return (NeoTransferActivity) mView;
    }
}
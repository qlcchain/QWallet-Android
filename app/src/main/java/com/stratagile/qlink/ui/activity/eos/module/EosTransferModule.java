package com.stratagile.qlink.ui.activity.eos.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosTransferActivity;
import com.stratagile.qlink.ui.activity.eos.contract.EosTransferContract;
import com.stratagile.qlink.ui.activity.eos.presenter.EosTransferPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The moduele of EosTransferActivity, provide field for EosTransferActivity
 * @date 2018/11/27 14:27:47
 */
@Module
public class EosTransferModule {
    private final EosTransferContract.View mView;


    public EosTransferModule(EosTransferContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EosTransferPresenter provideEosTransferPresenter(HttpAPIWrapper httpAPIWrapper, EosTransferActivity mActivity) {
        return new EosTransferPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EosTransferActivity provideEosTransferActivity() {
        return (EosTransferActivity) mView;
    }
}
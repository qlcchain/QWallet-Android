package com.stratagile.qlink.ui.activity.mainwallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainSendFragment;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainSendContract;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainSendPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The moduele of MainSendFragment, provide field for MainSendFragment
 * @date 2018/06/14 09:24:50
 */
@Module
public class MainSendModule {
    private final MainSendContract.View mView;


    public MainSendModule(MainSendContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MainSendPresenter provideMainSendPresenter(HttpAPIWrapper httpAPIWrapper, MainSendFragment mFragment) {
        return new MainSendPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public MainSendFragment provideMainSendFragment() {
        return (MainSendFragment) mView;
    }
}
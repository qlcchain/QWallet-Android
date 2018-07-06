package com.stratagile.qlink.ui.activity.mainwallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainReceiveFragment;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainReceiveContract;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainReceivePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The moduele of MainReceiveFragment, provide field for MainReceiveFragment
 * @date 2018/06/14 09:25:13
 */
@Module
public class MainReceiveModule {
    private final MainReceiveContract.View mView;


    public MainReceiveModule(MainReceiveContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MainReceivePresenter provideMainReceivePresenter(HttpAPIWrapper httpAPIWrapper, MainReceiveFragment mFragment) {
        return new MainReceivePresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public MainReceiveFragment provideMainReceiveFragment() {
        return (MainReceiveFragment) mView;
    }
}
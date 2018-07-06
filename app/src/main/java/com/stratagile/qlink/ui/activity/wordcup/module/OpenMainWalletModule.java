package com.stratagile.qlink.ui.activity.wordcup.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.OpenMainWalletFragment;
import com.stratagile.qlink.ui.activity.wordcup.contract.OpenMainWalletContract;
import com.stratagile.qlink.ui.activity.wordcup.presenter.OpenMainWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The moduele of OpenMainWalletFragment, provide field for OpenMainWalletFragment
 * @date 2018/06/13 17:37:05
 */
@Module
public class OpenMainWalletModule {
    private final OpenMainWalletContract.View mView;


    public OpenMainWalletModule(OpenMainWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public OpenMainWalletPresenter provideOpenMainWalletPresenter(HttpAPIWrapper httpAPIWrapper, OpenMainWalletFragment mFragment) {
        return new OpenMainWalletPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public OpenMainWalletFragment provideOpenMainWalletFragment() {
        return (OpenMainWalletFragment) mView;
    }
}
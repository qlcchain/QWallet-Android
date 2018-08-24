package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletListFragment;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletListContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletListPresenter;
import com.stratagile.qlink.ui.adapter.wallet.WalletListAdapter;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of WalletListFragment, provide field for WalletListFragment
 * @date 2018/01/09 15:08:22
 */
@Module
public class WalletListModule {
    private final WalletListContract.View mView;


    public WalletListModule(WalletListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WalletListPresenter provideWalletListPresenter(HttpAPIWrapper httpAPIWrapper, WalletListFragment fragment) {
        return new WalletListPresenter(httpAPIWrapper, mView, fragment);
    }

    @Provides
    @ActivityScope
    public WalletListFragment provideWalletListFragment() {
        return (WalletListFragment) mView;
    }

    @Provides
    @ActivityScope
    public WalletListAdapter provideWalletListAdapter() {
        return new WalletListAdapter(new ArrayList<>());
    }
}
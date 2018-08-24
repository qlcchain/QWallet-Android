package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ChangeWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ChangeWalletContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChangeWalletPresenter;
import com.stratagile.qlink.ui.adapter.wallet.WalletListAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ChangeWalletActivity, provide field for ChangeWalletActivity
 * @date 2018/03/05 11:36:39
 */
@Module
public class ChangeWalletModule {
    private final ChangeWalletContract.View mView;


    public ChangeWalletModule(ChangeWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ChangeWalletPresenter provideChangeWalletPresenter(HttpAPIWrapper httpAPIWrapper, ChangeWalletActivity mActivity) {
        return new ChangeWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ChangeWalletActivity provideChangeWalletActivity() {
        return (ChangeWalletActivity) mView;
    }

    @Provides
    @ActivityScope
    public WalletListAdapter provideWalletListAdapter() {
        return new WalletListAdapter(new ArrayList<Wallet>());
    }
}
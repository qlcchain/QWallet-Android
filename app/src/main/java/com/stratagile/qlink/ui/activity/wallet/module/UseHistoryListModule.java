package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.UseHistoryListFragment;
import com.stratagile.qlink.ui.activity.wallet.contract.UseHistoryListContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.UseHistoryListPresenter;
import com.stratagile.qlink.ui.adapter.wallet.UseHistoryListAdapter;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of UseHistoryListFragment, provide field for UseHistoryListFragment
 * @date 2018/01/19 11:44:00
 */
@Module
public class UseHistoryListModule {
    private final UseHistoryListContract.View mView;


    public UseHistoryListModule(UseHistoryListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public UseHistoryListPresenter provideUseHistoryListPresenter(HttpAPIWrapper httpAPIWrapper, UseHistoryListFragment fragment) {
        return new UseHistoryListPresenter(httpAPIWrapper, mView, fragment);
    }

    @Provides
    @ActivityScope
    public UseHistoryListFragment provideUseHistoryListFragment() {
        return (UseHistoryListFragment) mView;
    }

    @Provides
    @ActivityScope
    public UseHistoryListAdapter provideUseHistoryListAdapter() {
        return new UseHistoryListAdapter(new ArrayList<>());
    }
}
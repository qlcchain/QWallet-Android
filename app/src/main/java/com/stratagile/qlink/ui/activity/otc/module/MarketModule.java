package com.stratagile.qlink.ui.activity.otc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.otc.MarketFragment;
import com.stratagile.qlink.ui.activity.otc.contract.MarketContract;
import com.stratagile.qlink.ui.activity.otc.presenter.MarketPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: The moduele of MarketFragment, provide field for MarketFragment
 * @date 2019/06/14 16:23:19
 */
@Module
public class MarketModule {
    private final MarketContract.View mView;


    public MarketModule(MarketContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MarketPresenter provideMarketPresenter(HttpAPIWrapper httpAPIWrapper, MarketFragment mFragment) {
        return new MarketPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public MarketFragment provideMarketFragment() {
        return (MarketFragment) mView;
    }
}
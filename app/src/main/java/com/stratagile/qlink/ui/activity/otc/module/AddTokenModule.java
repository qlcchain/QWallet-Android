package com.stratagile.qlink.ui.activity.otc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.otc.AddTokenActivity;
import com.stratagile.qlink.ui.activity.otc.contract.AddTokenContract;
import com.stratagile.qlink.ui.activity.otc.presenter.AddTokenPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: The moduele of AddTokenActivity, provide field for AddTokenActivity
 * @date 2018/11/28 11:03:57
 */
@Module
public class AddTokenModule {
    private final AddTokenContract.View mView;


    public AddTokenModule(AddTokenContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public AddTokenPresenter provideAddTokenPresenter(HttpAPIWrapper httpAPIWrapper, AddTokenActivity mActivity) {
        return new AddTokenPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public AddTokenActivity provideAddTokenActivity() {
        return (AddTokenActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.RegiesterFragment;
import com.stratagile.qlink.ui.activity.my.contract.RegiesterContract;
import com.stratagile.qlink.ui.activity.my.presenter.RegiesterPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of RegiesterFragment, provide field for RegiesterFragment
 * @date 2019/04/09 11:45:07
 */
@Module
public class RegiesterModule {
    private final RegiesterContract.View mView;


    public RegiesterModule(RegiesterContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegiesterPresenter provideRegiesterPresenter(HttpAPIWrapper httpAPIWrapper, RegiesterFragment mFragment) {
        return new RegiesterPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public RegiesterFragment provideRegiesterFragment() {
        return (RegiesterFragment) mView;
    }
}
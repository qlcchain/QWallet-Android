package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.Login1Fragment;
import com.stratagile.qlink.ui.activity.my.contract.Login1Contract;
import com.stratagile.qlink.ui.activity.my.presenter.Login1Presenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of Login1Fragment, provide field for Login1Fragment
 * @date 2019/04/24 18:02:10
 */
@Module
public class Login1Module {
    private final Login1Contract.View mView;


    public Login1Module(Login1Contract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public Login1Presenter provideLogin1Presenter(HttpAPIWrapper httpAPIWrapper, Login1Fragment mFragment) {
        return new Login1Presenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public Login1Fragment provideLogin1Fragment() {
        return (Login1Fragment) mView;
    }
}
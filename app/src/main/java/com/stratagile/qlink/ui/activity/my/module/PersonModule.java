package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.PersonActivity;
import com.stratagile.qlink.ui.activity.my.contract.PersonContract;
import com.stratagile.qlink.ui.activity.my.presenter.PersonPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of PersonActivity, provide field for PersonActivity
 * @date 2019/04/22 14:28:46
 */
@Module
public class PersonModule {
    private final PersonContract.View mView;


    public PersonModule(PersonContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public PersonPresenter providePersonPresenter(HttpAPIWrapper httpAPIWrapper, PersonActivity mActivity) {
        return new PersonPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public PersonActivity providePersonActivity() {
        return (PersonActivity) mView;
    }
}
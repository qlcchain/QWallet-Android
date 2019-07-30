package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.EditInputActivity;
import com.stratagile.qlink.ui.activity.main.contract.EditInputContract;
import com.stratagile.qlink.ui.activity.main.presenter.EditInputPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of EditInputActivity, provide field for EditInputActivity
 * @date 2019/04/25 14:13:26
 */
@Module
public class EditInputModule {
    private final EditInputContract.View mView;


    public EditInputModule(EditInputContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EditInputPresenter provideEditInputPresenter(HttpAPIWrapper httpAPIWrapper, EditInputActivity mActivity) {
        return new EditInputPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EditInputActivity provideEditInputActivity() {
        return (EditInputActivity) mView;
    }
}
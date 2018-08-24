package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ProfilePictureContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ProfilePicturePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ProfilePictureActivity, provide field for ProfilePictureActivity
 * @date 2018/01/30 15:17:54
 */
@Module
public class ProfilePictureModule {
    private final ProfilePictureContract.View mView;


    public ProfilePictureModule(ProfilePictureContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ProfilePicturePresenter provideProfilePicturePresenter(HttpAPIWrapper httpAPIWrapper, ProfilePictureActivity mActivity) {
        return new ProfilePicturePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ProfilePictureActivity provideProfilePictureActivity() {
        return (ProfilePictureActivity) mView;
    }
}
package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ProfilePictureModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ProfilePictureActivity
 * @date 2018/01/30 15:17:54
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ProfilePictureModule.class)
public interface ProfilePictureComponent {
    ProfilePictureActivity inject(ProfilePictureActivity Activity);
}
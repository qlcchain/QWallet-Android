package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.GratuityActivity;
import com.stratagile.qlink.ui.activity.wallet.module.GratuityModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for GratuityActivity
 * @date 2018/02/02 16:19:02
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = GratuityModule.class)
public interface GratuityComponent {
    GratuityActivity inject(GratuityActivity Activity);
}
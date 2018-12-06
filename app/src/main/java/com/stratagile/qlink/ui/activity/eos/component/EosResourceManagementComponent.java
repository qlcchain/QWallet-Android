package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosResourceManagementActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosResourceManagementModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosResourceManagementActivity
 * @date 2018/12/04 18:08:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosResourceManagementModule.class)
public interface EosResourceManagementComponent {
    EosResourceManagementActivity inject(EosResourceManagementActivity Activity);
}
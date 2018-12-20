package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosCreateActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosCreateModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosCreateActivity
 * @date 2018/12/07 11:29:04
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosCreateModule.class)
public interface EosCreateComponent {
    EosCreateActivity inject(EosCreateActivity Activity);
}
package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosActivationActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosActivationModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosActivationActivity
 * @date 2018/12/12 11:17:52
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosActivationModule.class)
public interface EosActivationComponent {
    EosActivationActivity inject(EosActivationActivity Activity);
}
package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosBuyCpuAndNetActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosBuyCpuAndNetModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosBuyCpuAndNetActivity
 * @date 2018/12/05 18:03:46
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosBuyCpuAndNetModule.class)
public interface EosBuyCpuAndNetComponent {
    EosBuyCpuAndNetActivity inject(EosBuyCpuAndNetActivity Activity);
}
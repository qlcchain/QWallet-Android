package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosBuyRamActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosBuyRamModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosBuyRamActivity
 * @date 2018/12/06 14:39:06
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosBuyRamModule.class)
public interface EosBuyRamComponent {
    EosBuyRamActivity inject(EosBuyRamActivity Activity);
}
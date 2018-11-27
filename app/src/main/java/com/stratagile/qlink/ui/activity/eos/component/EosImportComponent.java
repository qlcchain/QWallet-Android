package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosImportActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosImportModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosImportActivity
 * @date 2018/11/26 17:06:38
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosImportModule.class)
public interface EosImportComponent {
    EosImportActivity inject(EosImportActivity Activity);
}
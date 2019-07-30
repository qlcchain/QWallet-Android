package com.stratagile.qlink.ui.activity.qlc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcSeedFragment;
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcSeedModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for ImportQlcSeedFragment
 * @date 2019/05/21 09:46:27
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ImportQlcSeedModule.class)
public interface ImportQlcSeedComponent {
    ImportQlcSeedFragment inject(ImportQlcSeedFragment Fragment);
}
package com.stratagile.qlink.ui.activity.qlc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcPrivateKeyFragment;
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcPrivateKeyModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for ImportQlcPrivateKeyFragment
 * @date 2019/05/21 09:46:08
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ImportQlcPrivateKeyModule.class)
public interface ImportQlcPrivateKeyComponent {
    ImportQlcPrivateKeyFragment inject(ImportQlcPrivateKeyFragment Fragment);
}
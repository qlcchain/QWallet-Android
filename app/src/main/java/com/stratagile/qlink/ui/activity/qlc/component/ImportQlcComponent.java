package com.stratagile.qlink.ui.activity.qlc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcActivity;
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for ImportQlcActivity
 * @date 2019/05/21 09:40:38
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ImportQlcModule.class)
public interface ImportQlcComponent {
    ImportQlcActivity inject(ImportQlcActivity Activity);
}
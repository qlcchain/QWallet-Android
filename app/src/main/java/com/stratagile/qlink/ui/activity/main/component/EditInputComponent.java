package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.EditInputActivity;
import com.stratagile.qlink.ui.activity.main.module.EditInputModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for EditInputActivity
 * @date 2019/04/25 14:13:26
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EditInputModule.class)
public interface EditInputComponent {
    EditInputActivity inject(EditInputActivity Activity);
}
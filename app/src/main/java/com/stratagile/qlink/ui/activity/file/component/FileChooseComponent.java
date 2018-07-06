package com.stratagile.qlink.ui.activity.file.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.file.FileChooseActivity;
import com.stratagile.qlink.ui.activity.file.module.FileChooseModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: The component for FileChooseActivity
 * @date 2018/05/18 14:15:35
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = FileChooseModule.class)
public interface FileChooseComponent {
    FileChooseActivity inject(FileChooseActivity Activity);
}
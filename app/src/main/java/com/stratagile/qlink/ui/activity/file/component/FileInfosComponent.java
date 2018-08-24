package com.stratagile.qlink.ui.activity.file.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.file.FileInfosFragment;
import com.stratagile.qlink.ui.activity.file.module.FileInfosModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: The component for FileInfosFragment
 * @date 2018/05/18 16:46:15
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = FileInfosModule.class)
public interface FileInfosComponent {
    FileInfosFragment inject(FileInfosFragment Fragment);
}
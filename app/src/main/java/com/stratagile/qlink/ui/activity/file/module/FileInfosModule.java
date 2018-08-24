package com.stratagile.qlink.ui.activity.file.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.file.FileInfosFragment;
import com.stratagile.qlink.ui.activity.file.contract.FileInfosContract;
import com.stratagile.qlink.ui.activity.file.presenter.FileInfosPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: The moduele of FileInfosFragment, provide field for FileInfosFragment
 * @date 2018/05/18 16:46:15
 */
@Module
public class FileInfosModule {
    private final FileInfosContract.View mView;


    public FileInfosModule(FileInfosContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public FileInfosPresenter provideFileInfosPresenter(HttpAPIWrapper httpAPIWrapper, FileInfosFragment mFragment) {
        return new FileInfosPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public FileInfosFragment provideFileInfosFragment() {
        return (FileInfosFragment) mView;
    }
}
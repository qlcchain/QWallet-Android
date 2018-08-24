package com.stratagile.qlink.ui.activity.file.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.file.FileChooseActivity;
import com.stratagile.qlink.ui.activity.file.contract.FileChooseContract;
import com.stratagile.qlink.ui.activity.file.presenter.FileChoosePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.file
 * @Description: The moduele of FileChooseActivity, provide field for FileChooseActivity
 * @date 2018/05/18 14:15:35
 */
@Module
public class FileChooseModule {
    private final FileChooseContract.View mView;


    public FileChooseModule(FileChooseContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public FileChoosePresenter provideFileInfosPresenter(HttpAPIWrapper httpAPIWrapper, FileChooseActivity mActivity) {
        return new FileChoosePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public FileChooseActivity provideFileInfosActivity() {
        return (FileChooseActivity) mView;
    }
}
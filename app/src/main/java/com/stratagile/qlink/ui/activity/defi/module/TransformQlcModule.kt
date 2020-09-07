package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.TransformQlcActivity
import com.stratagile.qlink.ui.activity.defi.contract.TransformQlcContract
import com.stratagile.qlink.ui.activity.defi.presenter.TransformQlcPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of TransformQlcActivity, provide field for TransformQlcActivity
 * @date 2020/08/12 15:15:34
 */
@Module
class TransformQlcModule (private val mView: TransformQlcContract.View) {

    @Provides
    @ActivityScope
    fun provideTransformQlcPresenter(httpAPIWrapper: HttpAPIWrapper) :TransformQlcPresenter {
        return TransformQlcPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTransformQlcActivity() : TransformQlcActivity {
        return mView as TransformQlcActivity
    }
}
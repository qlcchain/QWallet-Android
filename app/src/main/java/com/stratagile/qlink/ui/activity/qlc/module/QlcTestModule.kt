package com.stratagile.qlink.ui.activity.qlc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.QlcTestActivity
import com.stratagile.qlink.ui.activity.qlc.contract.QlcTestContract
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcTestPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of QlcTestActivity, provide field for QlcTestActivity
 * @date 2019/05/05 16:24:30
 */
@Module
class QlcTestModule (private val mView: QlcTestContract.View) {

    @Provides
    @ActivityScope
    fun provideQlcTestPresenter(httpAPIWrapper: HttpAPIWrapper) :QlcTestPresenter {
        return QlcTestPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideQlcTestActivity() : QlcTestActivity {
        return mView as QlcTestActivity
    }
}
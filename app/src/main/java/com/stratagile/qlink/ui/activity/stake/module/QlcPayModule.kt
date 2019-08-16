package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.QlcPayActivity
import com.stratagile.qlink.ui.activity.stake.contract.QlcPayContract
import com.stratagile.qlink.ui.activity.stake.presenter.QlcPayPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of QlcPayActivity, provide field for QlcPayActivity
 * @date 2019/08/16 09:59:21
 */
@Module
class QlcPayModule (private val mView: QlcPayContract.View) {

    @Provides
    @ActivityScope
    fun provideQlcPayPresenter(httpAPIWrapper: HttpAPIWrapper) :QlcPayPresenter {
        return QlcPayPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideQlcPayActivity() : QlcPayActivity {
        return mView as QlcPayActivity
    }
}
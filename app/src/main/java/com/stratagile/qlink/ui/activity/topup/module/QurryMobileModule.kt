package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.QurryMobileActivity
import com.stratagile.qlink.ui.activity.topup.contract.QurryMobileContract
import com.stratagile.qlink.ui.activity.topup.presenter.QurryMobilePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of QurryMobileActivity, provide field for QurryMobileActivity
 * @date 2019/09/24 14:50:33
 */
@Module
class QurryMobileModule (private val mView: QurryMobileContract.View) {

    @Provides
    @ActivityScope
    fun provideQurryMobilePresenter(httpAPIWrapper: HttpAPIWrapper) :QurryMobilePresenter {
        return QurryMobilePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideQurryMobileActivity() : QurryMobileActivity {
        return mView as QurryMobileActivity
    }
}
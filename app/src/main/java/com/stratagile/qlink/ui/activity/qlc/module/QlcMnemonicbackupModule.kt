package com.stratagile.qlink.ui.activity.qlc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicbackupActivity
import com.stratagile.qlink.ui.activity.qlc.contract.QlcMnemonicbackupContract
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcMnemonicbackupPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of QlcMnemonicbackupActivity, provide field for QlcMnemonicbackupActivity
 * @date 2019/06/05 18:37:03
 */
@Module
class QlcMnemonicbackupModule (private val mView: QlcMnemonicbackupContract.View) {

    @Provides
    @ActivityScope
    fun provideQlcMnemonicbackupPresenter(httpAPIWrapper: HttpAPIWrapper) :QlcMnemonicbackupPresenter {
        return QlcMnemonicbackupPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideQlcMnemonicbackupActivity() : QlcMnemonicbackupActivity {
        return mView as QlcMnemonicbackupActivity
    }
}
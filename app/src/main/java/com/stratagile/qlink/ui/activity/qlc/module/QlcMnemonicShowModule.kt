package com.stratagile.qlink.ui.activity.qlc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicShowActivity
import com.stratagile.qlink.ui.activity.qlc.contract.QlcMnemonicShowContract
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcMnemonicShowPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of QlcMnemonicShowActivity, provide field for QlcMnemonicShowActivity
 * @date 2019/06/05 18:36:43
 */
@Module
class QlcMnemonicShowModule (private val mView: QlcMnemonicShowContract.View) {

    @Provides
    @ActivityScope
    fun provideQlcMnemonicShowPresenter(httpAPIWrapper: HttpAPIWrapper) :QlcMnemonicShowPresenter {
        return QlcMnemonicShowPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideQlcMnemonicShowActivity() : QlcMnemonicShowActivity {
        return mView as QlcMnemonicShowActivity
    }
}
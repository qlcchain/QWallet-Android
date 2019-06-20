package com.stratagile.qlink.ui.activity.qlc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.ImportQlcMnemonicFragment
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcMnemonicContract
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcMnemonicPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of ImportQlcMnemonicFragment, provide field for ImportQlcMnemonicFragment
 * @date 2019/06/06 10:37:31
 */
@Module
class ImportQlcMnemonicModule (private val mView: ImportQlcMnemonicContract.View) {

    @Provides
    @ActivityScope
    fun provideImportQlcMnemonicPresenter(httpAPIWrapper: HttpAPIWrapper) :ImportQlcMnemonicPresenter {
        return ImportQlcMnemonicPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideImportQlcMnemonicFragment() : ImportQlcMnemonicFragment {
        return mView as ImportQlcMnemonicFragment
    }
}
package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.TokenMintageFragment
import com.stratagile.qlink.ui.activity.stake.contract.TokenMintageContract
import com.stratagile.qlink.ui.activity.stake.presenter.TokenMintagePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of TokenMintageFragment, provide field for TokenMintageFragment
 * @date 2019/08/08 16:38:20
 */
@Module
class TokenMintageModule (private val mView: TokenMintageContract.View) {

    @Provides
    @ActivityScope
    fun provideTokenMintagePresenter(httpAPIWrapper: HttpAPIWrapper) :TokenMintagePresenter {
        return TokenMintagePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTokenMintageFragment() : TokenMintageFragment {
        return mView as TokenMintageFragment
    }
}
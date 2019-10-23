package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.TokenMintageFragment
import com.stratagile.qlink.ui.activity.stake.module.TokenMintageModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for TokenMintageFragment
 * @date 2019/08/08 16:38:20
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TokenMintageModule::class))
interface TokenMintageComponent {
    fun inject(TokenMintageFragment: TokenMintageFragment): TokenMintageFragment
}
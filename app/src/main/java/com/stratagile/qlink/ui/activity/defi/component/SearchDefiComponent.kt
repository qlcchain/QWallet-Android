package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SearchDefiActivity
import com.stratagile.qlink.ui.activity.defi.module.SearchDefiModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for SearchDefiActivity
 * @date 2020/10/22 15:09:25
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SearchDefiModule::class))
interface SearchDefiComponent {
    fun inject(SearchDefiActivity: SearchDefiActivity): SearchDefiActivity
}
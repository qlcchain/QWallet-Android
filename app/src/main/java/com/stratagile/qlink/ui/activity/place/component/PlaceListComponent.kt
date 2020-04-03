package com.stratagile.qlink.ui.activity.place.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.place.PlaceListActivity
import com.stratagile.qlink.ui.activity.place.module.PlaceListModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: The component for PlaceListActivity
 * @date 2020/02/21 21:39:44
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PlaceListModule::class))
interface PlaceListComponent {
    fun inject(PlaceListActivity: PlaceListActivity): PlaceListActivity
}
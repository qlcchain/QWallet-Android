package com.stratagile.qlink.ui.activity.place.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.place.PlaceVisitActivity
import com.stratagile.qlink.ui.activity.place.module.PlaceVisitModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: The component for PlaceVisitActivity
 * @date 2020/02/20 10:07:00
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PlaceVisitModule::class))
interface PlaceVisitComponent {
    fun inject(PlaceVisitActivity: PlaceVisitActivity): PlaceVisitActivity
}
package com.stratagile.qlink.ui.activity.recommend.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.AgencyExcellenceActivity
import com.stratagile.qlink.ui.activity.recommend.module.AgencyExcellenceModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The component for AgencyExcellenceActivity
 * @date 2020/01/09 13:58:26
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(AgencyExcellenceModule::class))
interface AgencyExcellenceComponent {
    fun inject(AgencyExcellenceActivity: AgencyExcellenceActivity): AgencyExcellenceActivity
}
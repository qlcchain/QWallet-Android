package com.stratagile.qlink.ui.activity.recommend.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.TopupProductDetailActivity
import com.stratagile.qlink.ui.activity.recommend.module.TopupProductDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The component for TopupProductDetailActivity
 * @date 2020/01/13 15:36:22
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupProductDetailModule::class))
interface TopupProductDetailComponent {
    fun inject(TopupProductDetailActivity: TopupProductDetailActivity): TopupProductDetailActivity
}
package com.stratagile.qlink.ui.activity.place.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.place.PlaceVisitActivity
import com.stratagile.qlink.ui.activity.place.contract.PlaceVisitContract
import com.stratagile.qlink.ui.activity.place.presenter.PlaceVisitPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: The moduele of PlaceVisitActivity, provide field for PlaceVisitActivity
 * @date 2020/02/20 10:07:00
 */
@Module
class PlaceVisitModule (private val mView: PlaceVisitContract.View) {

    @Provides
    @ActivityScope
    fun providePlaceVisitPresenter(httpAPIWrapper: HttpAPIWrapper) :PlaceVisitPresenter {
        return PlaceVisitPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun providePlaceVisitActivity() : PlaceVisitActivity {
        return mView as PlaceVisitActivity
    }
}
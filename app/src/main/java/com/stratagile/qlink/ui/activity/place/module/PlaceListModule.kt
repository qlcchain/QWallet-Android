package com.stratagile.qlink.ui.activity.place.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.place.PlaceListActivity
import com.stratagile.qlink.ui.activity.place.contract.PlaceListContract
import com.stratagile.qlink.ui.activity.place.presenter.PlaceListPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: The moduele of PlaceListActivity, provide field for PlaceListActivity
 * @date 2020/02/21 21:39:44
 */
@Module
class PlaceListModule (private val mView: PlaceListContract.View) {

    @Provides
    @ActivityScope
    fun providePlaceListPresenter(httpAPIWrapper: HttpAPIWrapper) :PlaceListPresenter {
        return PlaceListPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun providePlaceListActivity() : PlaceListActivity {
        return mView as PlaceListActivity
    }
}
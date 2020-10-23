package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SearchDefiActivity
import com.stratagile.qlink.ui.activity.defi.contract.SearchDefiContract
import com.stratagile.qlink.ui.activity.defi.presenter.SearchDefiPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of SearchDefiActivity, provide field for SearchDefiActivity
 * @date 2020/10/22 15:09:25
 */
@Module
class SearchDefiModule (private val mView: SearchDefiContract.View) {

    @Provides
    @ActivityScope
    fun provideSearchDefiPresenter(httpAPIWrapper: HttpAPIWrapper) :SearchDefiPresenter {
        return SearchDefiPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSearchDefiActivity() : SearchDefiActivity {
        return mView as SearchDefiActivity
    }
}
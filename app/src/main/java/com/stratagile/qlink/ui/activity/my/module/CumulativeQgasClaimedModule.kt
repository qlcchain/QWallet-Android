package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.CumulativeQgasClaimedActivity
import com.stratagile.qlink.ui.activity.my.contract.CumulativeQgasClaimedContract
import com.stratagile.qlink.ui.activity.my.presenter.CumulativeQgasClaimedPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of CumulativeQgasClaimedActivity, provide field for CumulativeQgasClaimedActivity
 * @date 2020/04/15 09:37:37
 */
@Module
class CumulativeQgasClaimedModule (private val mView: CumulativeQgasClaimedContract.View) {

    @Provides
    @ActivityScope
    fun provideCumulativeQgasClaimedPresenter(httpAPIWrapper: HttpAPIWrapper) :CumulativeQgasClaimedPresenter {
        return CumulativeQgasClaimedPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideCumulativeQgasClaimedActivity() : CumulativeQgasClaimedActivity {
        return mView as CumulativeQgasClaimedActivity
    }
}
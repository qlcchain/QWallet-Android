package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.BurnIntroduceActivity
import com.stratagile.qlink.ui.activity.my.contract.BurnIntroduceContract
import com.stratagile.qlink.ui.activity.my.presenter.BurnIntroducePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of BurnIntroduceActivity, provide field for BurnIntroduceActivity
 * @date 2020/02/29 17:33:46
 */
@Module
class BurnIntroduceModule (private val mView: BurnIntroduceContract.View) {

    @Provides
    @ActivityScope
    fun provideBurnIntroducePresenter(httpAPIWrapper: HttpAPIWrapper) :BurnIntroducePresenter {
        return BurnIntroducePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideBurnIntroduceActivity() : BurnIntroduceActivity {
        return mView as BurnIntroduceActivity
    }
}
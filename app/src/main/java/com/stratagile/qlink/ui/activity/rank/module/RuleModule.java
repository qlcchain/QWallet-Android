package com.stratagile.qlink.ui.activity.rank.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.rank.RuleActivity;
import com.stratagile.qlink.ui.activity.rank.contract.RuleContract;
import com.stratagile.qlink.ui.activity.rank.presenter.RulePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: The moduele of RuleActivity, provide field for RuleActivity
 * @date 2018/08/03 09:44:05
 */
@Module
public class RuleModule {
    private final RuleContract.View mView;


    public RuleModule(RuleContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RulePresenter provideRulePresenter(HttpAPIWrapper httpAPIWrapper, RuleActivity mActivity) {
        return new RulePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RuleActivity provideRuleActivity() {
        return (RuleActivity) mView;
    }
}
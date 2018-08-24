package com.stratagile.qlink.ui.activity.im.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.im.ConversationActivity;
import com.stratagile.qlink.ui.activity.im.contract.ConversationContract;
import com.stratagile.qlink.ui.activity.im.presenter.ConversationPresenter;
import com.stratagile.qlink.ui.adapter.im.ConversationListAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.im
 * @Description: The moduele of ConversationActivity, provide field for ConversationActivity
 * @date 2018/03/19 15:49:59
 */
@Module
public class ConversationModule {
    private final ConversationContract.View mView;


    public ConversationModule(ConversationContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ConversationPresenter provideConversationPresenter(HttpAPIWrapper httpAPIWrapper, ConversationActivity mActivity) {
        return new ConversationPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ConversationActivity provideConversationActivity() {
        return (ConversationActivity) mView;
    }
    @Provides
    @ActivityScope
    public ConversationListAdapter provideConversationListAdapter() {
        return new ConversationListAdapter(new ArrayList<>());
    }
}
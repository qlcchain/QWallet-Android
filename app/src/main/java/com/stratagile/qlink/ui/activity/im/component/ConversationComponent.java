package com.stratagile.qlink.ui.activity.im.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.im.ConversationActivity;
import com.stratagile.qlink.ui.activity.im.module.ConversationModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.im
 * @Description: The component for ConversationActivity
 * @date 2018/03/19 15:49:59
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ConversationModule.class)
public interface ConversationComponent {
    ConversationActivity inject(ConversationActivity Activity);
}
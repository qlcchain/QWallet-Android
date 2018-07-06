package com.stratagile.qlink.ui.activity.sms.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.sms.SmsFragment;
import com.stratagile.qlink.ui.activity.sms.module.SmsModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.sms
 * @Description: The component for SmsFragment
 * @date 2018/01/10 14:59:05
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SmsModule.class)
public interface SmsComponent {
    SmsFragment inject(SmsFragment Fragment);
}
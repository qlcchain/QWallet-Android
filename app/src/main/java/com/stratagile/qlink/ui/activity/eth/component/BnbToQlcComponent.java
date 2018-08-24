package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.BnbToQlcActivity;
import com.stratagile.qlink.ui.activity.eth.module.BnbToQlcModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for BnbToQlcActivity
 * @date 2018/05/24 10:46:37
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = BnbToQlcModule.class)
public interface BnbToQlcComponent {
    BnbToQlcActivity inject(BnbToQlcActivity Activity);
}
package com.stratagile.qlink.ui.activity.eos.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eos.EosTransferActivity;
import com.stratagile.qlink.ui.activity.eos.module.EosTransferModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: The component for EosTransferActivity
 * @date 2018/11/27 14:27:47
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EosTransferModule.class)
public interface EosTransferComponent {
    EosTransferActivity inject(EosTransferActivity Activity);
}
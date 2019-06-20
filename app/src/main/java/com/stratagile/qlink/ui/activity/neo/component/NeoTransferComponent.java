package com.stratagile.qlink.ui.activity.neo.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.neo.NeoTransferActivity;
import com.stratagile.qlink.ui.activity.neo.module.NeoTransferModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for NeoTransferActivity
 * @date 2018/11/06 18:16:07
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = NeoTransferModule.class)
public interface NeoTransferComponent {
    NeoTransferActivity inject(NeoTransferActivity Activity);
}
package com.stratagile.qlink.ui.activity.qlc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.QlcTransferActivity;
import com.stratagile.qlink.ui.activity.qlc.module.QlcTransferModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for QlcTransferActivity
 * @date 2019/05/20 18:05:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = QlcTransferModule.class)
public interface QlcTransferComponent {
    QlcTransferActivity inject(QlcTransferActivity Activity);
}
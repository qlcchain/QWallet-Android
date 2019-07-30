package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.HistoryRecordActivity;
import com.stratagile.qlink.ui.activity.finance.module.HistoryRecordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for HistoryRecordActivity
 * @date 2019/04/24 13:48:39
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = HistoryRecordModule.class)
public interface HistoryRecordComponent {
    HistoryRecordActivity inject(HistoryRecordActivity Activity);
}
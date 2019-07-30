package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.MyProductActivity;
import com.stratagile.qlink.ui.activity.finance.module.MyProductModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for MyProductActivity
 * @date 2019/04/11 16:18:23
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MyProductModule.class)
public interface MyProductComponent {
    MyProductActivity inject(MyProductActivity Activity);
}
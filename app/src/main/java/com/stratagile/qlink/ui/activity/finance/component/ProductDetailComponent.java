package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.ProductDetailActivity;
import com.stratagile.qlink.ui.activity.finance.module.ProductDetailModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for ProductDetailActivity
 * @date 2019/04/11 11:16:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ProductDetailModule.class)
public interface ProductDetailComponent {
    ProductDetailActivity inject(ProductDetailActivity Activity);
}
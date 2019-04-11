package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.ProductDetailActivity;
import com.stratagile.qlink.ui.activity.finance.contract.ProductDetailContract;
import com.stratagile.qlink.ui.activity.finance.presenter.ProductDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of ProductDetailActivity, provide field for ProductDetailActivity
 * @date 2019/04/11 11:16:32
 */
@Module
public class ProductDetailModule {
    private final ProductDetailContract.View mView;


    public ProductDetailModule(ProductDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ProductDetailPresenter provideProductDetailPresenter(HttpAPIWrapper httpAPIWrapper, ProductDetailActivity mActivity) {
        return new ProductDetailPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ProductDetailActivity provideProductDetailActivity() {
        return (ProductDetailActivity) mView;
    }
}
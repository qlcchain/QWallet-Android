package com.stratagile.qlink.ui.activity.finance;

import android.os.Bundle;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.finance.component.DaggerMyProductComponent;
import com.stratagile.qlink.ui.activity.finance.contract.MyProductContract;
import com.stratagile.qlink.ui.activity.finance.module.MyProductModule;
import com.stratagile.qlink.ui.activity.finance.presenter.MyProductPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/11 16:18:23
 */

public class MyProductActivity extends BaseActivity implements MyProductContract.View {

    @Inject
    MyProductPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_product);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
       DaggerMyProductComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .myProductModule(new MyProductModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(MyProductContract.MyProductContractPresenter presenter) {
        mPresenter = (MyProductPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}
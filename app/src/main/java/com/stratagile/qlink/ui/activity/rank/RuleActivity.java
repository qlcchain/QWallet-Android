package com.stratagile.qlink.ui.activity.rank;

import android.os.Bundle;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.rank.component.DaggerRuleComponent;
import com.stratagile.qlink.ui.activity.rank.contract.RuleContract;
import com.stratagile.qlink.ui.activity.rank.module.RuleModule;
import com.stratagile.qlink.ui.activity.rank.presenter.RulePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: $description
 * @date 2018/08/03 09:44:05
 */

public class RuleActivity extends BaseActivity implements RuleContract.View {

    @Inject
    RulePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_rule);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
       DaggerRuleComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .ruleModule(new RuleModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(RuleContract.RuleContractPresenter presenter) {
        mPresenter = (RulePresenter) presenter;
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
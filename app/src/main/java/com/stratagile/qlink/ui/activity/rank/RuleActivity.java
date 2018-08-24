package com.stratagile.qlink.ui.activity.rank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.ui.activity.rank.component.DaggerRuleComponent;
import com.stratagile.qlink.ui.activity.rank.contract.RuleContract;
import com.stratagile.qlink.ui.activity.rank.module.RuleModule;
import com.stratagile.qlink.ui.activity.rank.presenter.RulePresenter;
import com.stratagile.qlink.utils.SpUtil;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: $description
 * @date 2018/08/03 09:44:05
 */

public class RuleActivity extends BaseActivity implements RuleContract.View {

    @Inject
    RulePresenter mPresenter;
    @BindView(R.id.iv_link)
    ImageView ivLink;
    @BindView(R.id.iv_1)
    ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_rule);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String defaultLanguage = Locale.getDefault().getLanguage();
        String selectLanguage = SpUtil.getString(AppConfig.getInstance(), ConstantValue.selectLanguage, "");
        if (!"".equals(selectLanguage)) {
            switch (selectLanguage) {
                case "English":
                    Glide.with(this).load(R.mipmap.img_activities).into(iv1);
                    Glide.with(this).load(R.mipmap.img_link).into(ivLink);
                    break;
                case "Turkish"://土耳其语
                    Glide.with(this).load(R.mipmap.img_activities_tr).into(iv1);
                    Glide.with(this).load(R.mipmap.img_link_tr).into(ivLink);
                    break;
                default:
                    Glide.with(this).load(R.mipmap.img_activities).into(iv1);
                    Glide.with(this).load(R.mipmap.img_link).into(ivLink);
                    break;
            }
        } else {
            switch (defaultLanguage) {
                case "tr"://土耳其语
                    Glide.with(this).load(R.mipmap.img_activities_tr).into(iv1);
                    Glide.with(this).load(R.mipmap.img_link_tr).into(ivLink);
                    break;
                default:
                    Glide.with(this).load(R.mipmap.img_activities).into(iv1);
                    Glide.with(this).load(R.mipmap.img_link).into(ivLink);
                    break;
            }
        }
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.rules));
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

    @OnClick(R.id.iv_link)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("https://t.me/winqdapp");
        intent.setData(content_url);
        startActivity(intent);
    }
}
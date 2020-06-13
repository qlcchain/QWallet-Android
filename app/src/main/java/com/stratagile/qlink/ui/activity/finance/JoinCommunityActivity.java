package com.stratagile.qlink.ui.activity.finance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.finance.component.DaggerJoinCommunityComponent;
import com.stratagile.qlink.ui.activity.finance.contract.JoinCommunityContract;
import com.stratagile.qlink.ui.activity.finance.module.JoinCommunityModule;
import com.stratagile.qlink.ui.activity.finance.presenter.JoinCommunityPresenter;
import com.stratagile.qlink.utils.FireBaseUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/24 17:15:42
 */

public class JoinCommunityActivity extends BaseActivity implements JoinCommunityContract.View {

    @Inject
    JoinCommunityPresenter mPresenter;
    @BindView(R.id.twitter)
    LinearLayout twitter;
    @BindView(R.id.telegram)
    LinearLayout telegram;
    @BindView(R.id.facebook)
    LinearLayout facebook;
    @BindView(R.id.winq)
    LinearLayout winq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_join_community);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.join_the_community));
    }

    @Override
    protected void setupActivityComponent() {
        DaggerJoinCommunityComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .joinCommunityModule(new JoinCommunityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(JoinCommunityContract.JoinCommunityContractPresenter presenter) {
        mPresenter = (JoinCommunityPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.twitter, R.id.telegram, R.id.facebook, R.id.winq})
    public void onViewClicked(View view) {
        Intent intent1 = new Intent();
        intent1.setAction("android.intent.action.VIEW");
        switch (view.getId()) {
            case R.id.twitter:
                intent1.setData(Uri.parse("https://t.me/myqwallet"));
                startActivity(intent1);
                FireBaseUtils.logEvent(this, FireBaseUtils.Community_Twitter);
                break;
            case R.id.telegram:
                FireBaseUtils.logEvent(this, FireBaseUtils.Community_Telegram);
                intent1.setData(Uri.parse("https://t.me/qlinkmobile"));
                startActivity(intent1);
                break;
            case R.id.facebook:
                FireBaseUtils.logEvent(this, FireBaseUtils.Community_Facebook);
                intent1.setData(Uri.parse("https://www.facebook.com/QLCchain"));
                startActivity(intent1);
                break;
            case R.id.winq:
                FireBaseUtils.logEvent(this, FireBaseUtils.Community_QLC_Chain);
                intent1.setData(Uri.parse("https://qlcchain.org"));
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
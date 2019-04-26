package com.stratagile.qlink.ui.activity.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.finance.EarnRank;
import com.stratagile.qlink.ui.activity.finance.component.DaggerEarnRankComponent;
import com.stratagile.qlink.ui.activity.finance.contract.EarnRankContract;
import com.stratagile.qlink.ui.activity.finance.module.EarnRankModule;
import com.stratagile.qlink.ui.activity.finance.presenter.EarnRankPresenter;
import com.stratagile.qlink.ui.adapter.finance.EarnRankAdapter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/24 11:27:01
 * 富豪榜页面
 */

public class EarnRankActivity extends BaseActivity implements EarnRankContract.View {

    @Inject
    EarnRankPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_earn_ranking);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EarnRankActivity.this, MyProductActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.earnings_ranking));
        Map map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "20");
        mPresenter.getEarnRank(map);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEarnRankComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .earnRankModule(new EarnRankModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EarnRankContract.EarnRankContractPresenter presenter) {
        mPresenter = (EarnRankPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void setData(EarnRank earnRank) {
        recyclerView.setAdapter(new EarnRankAdapter(earnRank.getData()));
    }

}
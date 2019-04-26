package com.stratagile.qlink.ui.activity.finance;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.ui.activity.finance.component.DaggerMyRankingComponent;
import com.stratagile.qlink.ui.activity.finance.contract.MyRankingContract;
import com.stratagile.qlink.ui.activity.finance.module.MyRankingModule;
import com.stratagile.qlink.ui.activity.finance.presenter.MyRankingPresenter;
import com.stratagile.qlink.ui.adapter.finance.MyRankAdapter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/24 11:14:23
 */

public class MyRankingActivity extends BaseActivity implements MyRankingContract.View {

    @Inject
    MyRankingPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_ranking);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.my_ranking));
        Map map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "20");
        mPresenter.getRanking(map);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerMyRankingComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .myRankingModule(new MyRankingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MyRankingContract.MyRankingContractPresenter presenter) {
        mPresenter = (MyRankingPresenter) presenter;
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
    public void setData(MyRanking myRanking) {
        recyclerView.setAdapter(new MyRankAdapter(myRanking.getData()));
    }

}
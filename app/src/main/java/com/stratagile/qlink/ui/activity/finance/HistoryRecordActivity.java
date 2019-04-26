package com.stratagile.qlink.ui.activity.finance;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.finance.HistoryRecord;
import com.stratagile.qlink.ui.activity.finance.component.DaggerHistoryRecordComponent;
import com.stratagile.qlink.ui.activity.finance.contract.HistoryRecordContract;
import com.stratagile.qlink.ui.activity.finance.module.HistoryRecordModule;
import com.stratagile.qlink.ui.activity.finance.presenter.HistoryRecordPresenter;
import com.stratagile.qlink.ui.adapter.finance.HistoryRecordAdapter;
import com.stratagile.qlink.utils.AccountUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/24 13:48:39
 */

public class HistoryRecordActivity extends BaseActivity implements HistoryRecordContract.View {

    @Inject
    HistoryRecordPresenter mPresenter;
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
        setTitle(getString(R.string.history_record));
        Map map = new HashMap<String, String>();
        map.put("account", ConstantValue.currentUser.getAccount());
        map.put("token", AccountUtil.getUserToken());
        map.put("page", "1");
        map.put("size", "20");
        mPresenter.getHistory(map);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerHistoryRecordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .historyRecordModule(new HistoryRecordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(HistoryRecordContract.HistoryRecordContractPresenter presenter) {
        mPresenter = (HistoryRecordPresenter) presenter;
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
    public void setData(HistoryRecord historyRecord) {
        recyclerView.setAdapter(new HistoryRecordAdapter(historyRecord.getTransactionList()));
    }

}
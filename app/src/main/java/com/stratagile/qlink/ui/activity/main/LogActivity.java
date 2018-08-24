package com.stratagile.qlink.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.main.component.DaggerLogComponent;
import com.stratagile.qlink.ui.activity.main.contract.LogContract;
import com.stratagile.qlink.ui.activity.main.module.LogModule;
import com.stratagile.qlink.ui.activity.main.presenter.LogPresenter;
import com.stratagile.qlink.utils.LogUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/02/12 14:51:34
 */

public class LogActivity extends BaseActivity implements LogContract.View {

    @Inject
    LogPresenter mPresenter;
    @BindView(R.id.tv_log)
    TextView tvLog;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_log);
        TextView title = toolbar.findViewById(R.id.title);
        title.setText(R.string.Log);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        tvLog.setText(LogUtil.mLogInfo);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvLog.setText(LogUtil.mLogInfo);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clear_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                LogUtil.mLogInfo = "";
                tvLog.setText(LogUtil.mLogInfo);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerLogComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .logModule(new LogModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(LogContract.LogContractPresenter presenter) {
        mPresenter = (LogPresenter) presenter;
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
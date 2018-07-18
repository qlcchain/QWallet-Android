package com.stratagile.qlink.ui.activity.wallet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerFreeConnectComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.FreeConnectContract;
import com.stratagile.qlink.ui.activity.wallet.module.FreeConnectModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.FreeConnectPresenter;
import com.stratagile.qlink.ui.adapter.wallet.FreeRecordAdapter;
import com.stratagile.qlink.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/07/18 11:53:01
 */

public class FreeConnectActivity extends BaseActivity implements FreeConnectContract.View {

    @Inject
    FreeConnectPresenter mPresenter;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    FreeRecordAdapter freeRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_free_connect);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("free connection details");
        Map<String, Object> map = new HashMap<>();
        map.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
        map.put("type", 0);
        mPresenter.queryFreeRecords(map);
//        Map<String, String> map = new HashMap<>();
//        map.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
//        mPresenter.zsFreeNum(map);
        tvCount.setText(ConstantValue.freeNum + "");
        freeRecordAdapter = new FreeRecordAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(freeRecordAdapter);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFreeConnectComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .freeConnectModule(new FreeConnectModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(FreeConnectContract.FreeConnectContractPresenter presenter) {
        mPresenter = (FreeConnectPresenter) presenter;
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
    public void onGetFreeNumBack(int num) {
        tvCount.setText(num + "");
        Map<String, Object> map = new HashMap<>();
        map.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
        map.put("type", 0);
        mPresenter.queryFreeRecords(map);
    }

    @Override
    public void onGetFreeRecordBack(FreeRecord freeRecord) {
        freeRecordAdapter.setNewData(freeRecord.getData());
    }

}
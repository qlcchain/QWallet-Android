package com.stratagile.qlink.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.newwinq.Area;
import com.stratagile.qlink.ui.activity.my.component.DaggerSelectCountryComponent;
import com.stratagile.qlink.ui.activity.my.contract.SelectCountryContract;
import com.stratagile.qlink.ui.activity.my.module.SelectCountryModule;
import com.stratagile.qlink.ui.activity.my.presenter.SelectCountryPresenter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.view.sidebar.SideAdapter;
import com.stratagile.qlink.view.sidebar.SideBar;
import com.stratagile.qlink.view.sidebar.SideBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/09 14:38:53
 */

public class SelectCountryActivity extends BaseActivity implements SelectCountryContract.View {

    @Inject
    SelectCountryPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    private SideAdapter sideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_country);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    List<SideBean> sideBeans;
    @Override
    protected void initData() {
        setTitle("国家地区");
        String areaJson = "";
        areaJson = FileUtil.getJson(AppConfig.getInstance(), "area.json");
        Area area = new Gson().fromJson(areaJson, Area.class);
        sideBeans = area.getCountry();
        for (SideBean sideBean : sideBeans) {
            sideBean.setIndexTag(sideBean.getName().substring(0, 1));
        }
        sideBeans = Account.INSTANCE.sort(sideBeans);
        sideAdapter = new SideAdapter(sideBeans);
        recyclerView.setAdapter(sideAdapter);
        String indexStr = "";
        for (SideBean sideBean : sideBeans) {
            if (!indexStr.contains(sideBean.getIndexTag())) {
                indexStr  = indexStr + sideBean.getIndexTag();
            }
        }
        sideBar.setIndexStr(indexStr);
        sideBar.setIndexChangeListener(new SideBar.indexChangeListener() {
            @Override
            public void indexChanged(String tag) {
                if (TextUtils.isEmpty(tag) || sideBeans.size() <= 0) return;
                for (int i = 0; i < sideBeans.size(); i++) {
                    if (tag.equals(sideBeans.get(i).getIndexTag())) {
                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
//                        layoutManager.scrollToPosition(i);
                        return;
                    }
                }
            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSelectCountryComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .selectCountryModule(new SelectCountryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SelectCountryContract.SelectCountryContractPresenter presenter) {
        mPresenter = (SelectCountryPresenter) presenter;
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
package com.stratagile.qlink.ui.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.CurrencyBean;
import com.stratagile.qlink.entity.eventbus.ChangeCurrency;
import com.stratagile.qlink.ui.activity.setting.component.DaggerCurrencyUnitComponent;
import com.stratagile.qlink.ui.activity.setting.contract.CurrencyUnitContract;
import com.stratagile.qlink.ui.activity.setting.module.CurrencyUnitModule;
import com.stratagile.qlink.ui.activity.setting.presenter.CurrencyUnitPresenter;
import com.stratagile.qlink.ui.adapter.settings.CurrencyUnitAdapter;
import com.stratagile.qlink.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: $description
 * @date 2018/10/29 14:00:24
 */

public class CurrencyUnitActivity extends BaseActivity implements CurrencyUnitContract.View {

    @Inject
    CurrencyUnitPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CurrencyUnitAdapter currencyUnitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_currency_unit);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.currency_unit));
        currencyUnitAdapter = new CurrencyUnitAdapter(getCurrencyBeans());
        recyclerView.setAdapter(currencyUnitAdapter);
        currencyUnitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i =  0; i < currencyUnitAdapter.getData().size(); i++) {
                    currencyUnitAdapter.getData().get(i).setCheck(false);
                }
                currencyUnitAdapter.getData().get(position).setCheck(true);
                currencyUnitAdapter.notifyDataSetChanged();
            }
        });
    }

    private ArrayList<CurrencyBean> getCurrencyBeans() {
        ArrayList<CurrencyBean> currencyBeans = new ArrayList<>();
        currencyBeans.add(new CurrencyBean("USD", true, "$"));
        currencyBeans.add(new CurrencyBean("CNY", false, "¥"));
        currencyBeans.add(new CurrencyBean("TWD", false, "NT$"));
        //港币
        currencyBeans.add(new CurrencyBean("HKD", false, "HK$"));
        //澳门币
        currencyBeans.add(new CurrencyBean("MOP", false, "MOP$"));
        //欧元
        currencyBeans.add(new CurrencyBean("EUR", false, "€"));
        //卢布，俄罗斯
        currencyBeans.add(new CurrencyBean("RUB", false, "Br"));
        //韩元
        currencyBeans.add(new CurrencyBean("KRW", false, "₩"));
        //菲律宾比索
        currencyBeans.add(new CurrencyBean("PHP", false, "₱"));
        //日本币
        currencyBeans.add(new CurrencyBean("JPY", false, "￥"));
        //泰铢
        currencyBeans.add(new CurrencyBean("THB", false, "฿"));
        //土耳其，里拉
        currencyBeans.add(new CurrencyBean("TRY", false, "₺"));
        //越南
        currencyBeans.add(new CurrencyBean("VND", false, "₫"));
        String currency = SpUtil.getString(this, ConstantValue.currencyUnit, "USD");
        for (int i =  0; i < currencyBeans.size(); i++) {
            if (currencyBeans.get(i).getName().equals(currency)) {
                currencyBeans.get(i).setCheck(true);
            } else {
                currencyBeans.get(i).setCheck(false);
            }
        }
        return currencyBeans;
    }

    @Override
    public void onBackPressed() {
        for (int i =  0; i < currencyUnitAdapter.getData().size(); i++) {
            if (currencyUnitAdapter.getData().get(i).isCheck()) {
                SpUtil.putString(this, ConstantValue.currencyUnit, currencyUnitAdapter.getData().get(i).getName());
                ConstantValue.currencyBean = currencyUnitAdapter.getData().get(i);
            }
        }
        EventBus.getDefault().post(new ChangeCurrency());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerCurrencyUnitComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .currencyUnitModule(new CurrencyUnitModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(CurrencyUnitContract.CurrencyUnitContractPresenter presenter) {
        mPresenter = (CurrencyUnitPresenter) presenter;
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
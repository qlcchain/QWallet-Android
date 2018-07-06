package com.stratagile.qlink.ui.activity.vpn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerSelectCountryComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.SelectCountryContract;
import com.stratagile.qlink.ui.activity.vpn.module.SelectCountryModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.SelectCountryPresenter;
import com.stratagile.qlink.ui.adapter.vpn.ContactCityAdapter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/02/07 11:01:05
 */

public class SelectCountryActivity extends BaseActivity implements SelectCountryContract.View {

    @Inject
    SelectCountryPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tabLayout)
    TextView tabLayout;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.et_search)
    EditText et_search;

    private ContactCityAdapter mAdapterContactCity;
    private ContinentAndCountry.ContinentBean continentChose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_country);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.VPN);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
//        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
//            @Override
//            public boolean create(RecyclerView parent, int adapterPosition) {
//                return true;
//            }
//        });
//        recyclerView.addItemDecoration(decoration);

        Gson gson = new Gson();
        ContinentAndCountry continentAndCountry = gson.fromJson(FileUtil.getJson(SelectCountryActivity.this, "ContinentAndCountryBean.json"), ContinentAndCountry.class);
        for (int i = 0; i < continentAndCountry.getContinent().size(); i++) {
            if (continentAndCountry.getContinent().get(i).getContinent().toUpperCase(Locale.ENGLISH).equals(getIntent().getStringExtra("continent"))) {
                KLog.i("设置适配器。。。");
                Collections.sort(continentAndCountry.getContinent().get(i).getCountry());
                continentChose = continentAndCountry.getContinent().get(i);
                mAdapterContactCity = new ContactCityAdapter(continentAndCountry.getContinent().get(i).getCountry());
                recyclerView.setAdapter(mAdapterContactCity);
                mAdapterContactCity.setOnItemChangeListener(new ContactCityAdapter.OnItemChangeListener() {
                    @Override
                    public void onItemChange(int position) {

                    }
                });
            }
        }
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(continentChose == null)
                {
                    return;
                }
                KLog.i(charSequence.toString());
                String inputStr = charSequence.toString();
                List<ContinentAndCountry.ContinentBean.CountryBean> countTryList = continentChose.getCountry();
                KLog.i("old_"+countTryList.size());
                List<ContinentAndCountry.ContinentBean.CountryBean> newCountTryList = new ArrayList<>();
                for(int j = 0 ; j < countTryList.size() ; j++) {
                    ContinentAndCountry.ContinentBean.CountryBean temp = countTryList.get(j);
                    if(StringUitl.isSimilar(inputStr,temp.getName()))
                    {
                        newCountTryList.add(temp);
                    }
                }
                mAdapterContactCity = new ContactCityAdapter(newCountTryList);
                recyclerView.setAdapter(mAdapterContactCity);
                KLog.i("new_"+newCountTryList.size());
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    @OnClick({R.id.bt_cancel, R.id.bt_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_continue:
                if(mAdapterContactCity == null  || mAdapterContactCity.getItem(mAdapterContactCity.getSelectItem()) == null)
                {
                    ToastUtil.displayShortToast(getResources().getString(R.string.please_choose_a_region));
                    return;
                }
                Intent result = new Intent();
                result.putExtra("country", mAdapterContactCity.getItem(mAdapterContactCity.getSelectItem()).getName());
                result.putExtra("continent", getIntent().getStringExtra("continent"));
                setResult(RESULT_OK, result);
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
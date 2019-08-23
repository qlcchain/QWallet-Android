package com.stratagile.qlink.ui.activity.setting;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.LanguageCountry;
import com.stratagile.qlink.ui.activity.main.SplashActivity;
import com.stratagile.qlink.ui.activity.setting.component.DaggerSelectLanguageActivityComponent;
import com.stratagile.qlink.ui.activity.setting.contract.SelectLanguageActivityContract;
import com.stratagile.qlink.ui.activity.setting.module.SelectLanguageActivityModule;
import com.stratagile.qlink.ui.activity.setting.presenter.SelectLanguageActivityPresenter;
import com.stratagile.qlink.ui.adapter.settings.LanguageCityAdapter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.SpUtil;
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
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: $description
 * @date 2018/06/26 17:11:28
 */

public class SelectLanguageActivityActivity extends BaseActivity implements SelectLanguageActivityContract.View {

    @Inject
    SelectLanguageActivityPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.et_search)
    EditText et_search;

    private int defaultLanguage = 0;

    private LanguageCityAdapter mAdapterContactCity;
    private LanguageCountry.ContinentBean continentChose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.setting_language);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Gson gson = new Gson();
        LanguageCountry LanguageCountry = gson.fromJson(FileUtil.getJson(SelectLanguageActivityActivity.this, "Language.json"), LanguageCountry.class);
        for (int i = 0; i < LanguageCountry.getContinent().size(); i++) {
            KLog.i("设置适配器。。。");
//            Collections.sort(LanguageCountry.getContinent().get(i).getCountry());
            continentChose = LanguageCountry.getContinent().get(i);
            mAdapterContactCity = new LanguageCityAdapter(LanguageCountry.getContinent().get(i).getCountry());
            recyclerView.setAdapter(mAdapterContactCity);
            mAdapterContactCity.setOnItemChangeListener(new LanguageCityAdapter.OnItemChangeListener() {
                @Override
                public void onItemChange(int position) {
                    SpUtil.putInt(SelectLanguageActivityActivity.this, ConstantValue.Language, position);
                }
            });
        }
        defaultLanguage = SpUtil.getInt(SelectLanguageActivityActivity.this, ConstantValue.Language, -1);
        if (defaultLanguage == -1) {
            Resources resources = getResources();
            // 获取应用内语言
            final Configuration configuration = resources.getConfiguration();
            if (configuration.locale == Locale.ENGLISH) {
                defaultLanguage = 0;
            } else {
                defaultLanguage = 1;
            }
        }
//        String selectLanguage = SpUtil.getString(AppConfig.getInstance(), ConstantValue.selectLanguage, "");
//        String defaultLanguage = Locale.getDefault().getLanguage();
//        KLog.i(selectLanguage);
//        KLog.i(defaultLanguage);
        if (mAdapterContactCity != null) {
            mAdapterContactCity.setSelectItem(defaultLanguage);
        }

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (continentChose == null) {
                    return;
                }
                KLog.i(charSequence.toString());
                String inputStr = charSequence.toString();
                List<LanguageCountry.ContinentBean.CountryBean> countTryList = continentChose.getCountry();
                KLog.i("old_" + countTryList.size());
                List<LanguageCountry.ContinentBean.CountryBean> newCountTryList = new ArrayList<>();
                for (int j = 0; j < countTryList.size(); j++) {
                    LanguageCountry.ContinentBean.CountryBean temp = countTryList.get(j);
                    if (StringUitl.isSimilar(inputStr, temp.getName())) {
                        newCountTryList.add(temp);
                    }
                }
                mAdapterContactCity = new LanguageCityAdapter(newCountTryList);
                recyclerView.setAdapter(mAdapterContactCity);
                KLog.i("new_" + newCountTryList.size());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSelectLanguageActivityComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .selectLanguageActivityModule(new SelectLanguageActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SelectLanguageActivityContract.SelectLanguageActivityContractPresenter presenter) {
        mPresenter = (SelectLanguageActivityPresenter) presenter;
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
                if (mAdapterContactCity == null || mAdapterContactCity.getItem(mAdapterContactCity.getSelectItem()) == null) {
                    ToastUtil.displayShortToast(getResources().getString(R.string.please_choose_a_region));
                    return;
                }
                if (mAdapterContactCity.getSelectItem() == defaultLanguage) {
                    finish();
                } else {
                    SpUtil.putString(AppConfig.getInstance(), ConstantValue.selectLanguage, mAdapterContactCity.getItem(mAdapterContactCity.getSelectItem()).getName());
                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
                break;
            default:
                break;
        }
    }
}
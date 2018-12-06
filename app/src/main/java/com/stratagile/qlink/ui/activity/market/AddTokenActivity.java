package com.stratagile.qlink.ui.activity.market;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.LocalTokenBean;
import com.stratagile.qlink.entity.TokenSelect;
import com.stratagile.qlink.ui.activity.market.component.DaggerAddTokenComponent;
import com.stratagile.qlink.ui.activity.market.contract.AddTokenContract;
import com.stratagile.qlink.ui.activity.market.module.AddTokenModule;
import com.stratagile.qlink.ui.activity.market.presenter.AddTokenPresenter;
import com.stratagile.qlink.ui.adapter.market.MarketTokenAdapter;
import com.stratagile.qlink.utils.LocalWalletUtil;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: $description
 * @date 2018/11/28 11:03:57
 */

public class AddTokenActivity extends BaseActivity implements AddTokenContract.View {

    @Inject
    AddTokenPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MarketTokenAdapter marketTokenAdapter;
    @BindView(R.id.search)
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_token);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Markets");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterToken(s.toString().toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterToken(String string) {
        if (mLocalTokenBean == null) {
            return;
        }
        for (int i = 0; i < marketTokenAdapter.getData().size(); i++) {
            if (marketTokenAdapter.getData().get(i).getTokenName().contains(string)) {
                smoothMoveToPosition(recyclerView, i);
                break;
            }
        }
//        ArrayList<String> contansStr = new ArrayList<>();
//        for (String s : mLocalTokenBean.getData()) {
//           if (s.contains(string)) {
//               contansStr.add(s);
//           }
//
//        }
//
//        ArrayList<String> selectedTokens = LocalWalletUtil.getLocalTokens();
//        ArrayList<TokenSelect> tokenSelects = new ArrayList<>();
//        for (String s : contansStr) {
//            boolean selected = false;
//            TokenSelect tokenSelect = new TokenSelect();
//            tokenSelect.setTokenName(s);
//            for (String s1 : selectedTokens) {
//                if (s.equals(s1)) {
//                    selected = true;
//                    break;
//                }
//            }
//            tokenSelect.setSelect(selected);
//            tokenSelects.add(tokenSelect);
//        }
//        marketTokenAdapter.setNewData(tokenSelects);
    }

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            linearManager.setSmoothScrollbarEnabled(true);
            linearManager.scrollToPositionWithOffset(position, 0);
        }
    }
    @Override
    protected void initData() {
        marketTokenAdapter = new MarketTokenAdapter(new ArrayList<>());
        recyclerView.setAdapter(marketTokenAdapter);
        marketTokenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                marketTokenAdapter.getData().get(position).setSelect(!marketTokenAdapter.getData().get(position).isSelect());
                marketTokenAdapter.notifyItemChanged(position);
            }
        });
        mPresenter.getBinaTokens(new HashMap<String, Object>());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerAddTokenComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .addTokenModule(new AddTokenModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(AddTokenContract.AddTokenContractPresenter presenter) {
        mPresenter = (AddTokenPresenter) presenter;
    }

    @Override
    public void onBackPressed() {
        ArrayList<String> selectedTokens = new ArrayList<>();
        for (TokenSelect tokenSelect : marketTokenAdapter.getData()) {
            KLog.i(tokenSelect.getTokenName());
            if (tokenSelect.isSelect()) {
                selectedTokens.add(tokenSelect.getTokenName());
            }
        }
        LocalWalletUtil.updateLocalTokens(selectedTokens);
        KLog.i(selectedTokens);
        super.onBackPressed();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    LocalTokenBean mLocalTokenBean;
    @Override
    public void getBinaTokensSuccess(LocalTokenBean localTokenBean) {
        mLocalTokenBean = localTokenBean;
        ArrayList<String> selectedTokens = LocalWalletUtil.getLocalTokens();
        ArrayList<TokenSelect> tokenSelects = new ArrayList<>();
        for (String s : localTokenBean.getData()) {
            boolean selected = false;
            TokenSelect tokenSelect = new TokenSelect();
            tokenSelect.setTokenName(s);
            for (String s1 : selectedTokens) {
                if (s.equals(s1)) {
                    selected = true;
                    break;
                }
            }
            tokenSelect.setSelect(selected);
            tokenSelects.add(tokenSelect);
        }
        marketTokenAdapter.setNewData(tokenSelects);
    }

}
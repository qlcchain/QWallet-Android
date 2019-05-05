package com.stratagile.qlink.ui.activity.finance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.qualifier.Local;
import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.ui.activity.finance.component.DaggerFinanceComponent;
import com.stratagile.qlink.ui.activity.finance.contract.FinanceContract;
import com.stratagile.qlink.ui.activity.finance.module.FinanceModule;
import com.stratagile.qlink.ui.activity.finance.presenter.FinancePresenter;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.adapter.finance.ProduceListAdapter;
import com.stratagile.qlink.utils.LanguageUtil;
import com.stratagile.qlink.view.ScaleCircleNavigator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/08 17:36:49
 */

public class FinanceFragment extends BaseFragment implements FinanceContract.View {

    @Inject
    FinancePresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    ProduceListAdapter produceListAdapter;
    @BindView(R.id.qlcDaily)
    TextView qlcDaily;
    @BindView(R.id.rlInvite)
    RelativeLayout rlInvite;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.tvInComeRate)
    TextView tvInComeRate;
    @BindView(R.id.tvLeastAmount)
    TextView tvLeastAmount;
    @BindView(R.id.joinNow)
    TextView joinNow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getProduct();
            }
        });
        produceListAdapter = new ProduceListAdapter(new ArrayList<>());
        recyclerView.setAdapter(produceListAdapter);
        produceListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.i("点击。。。。。");
                if (!produceListAdapter.getData().get(position).getStatus().equals("END")) {
                    startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("productId", produceListAdapter.getData().get(position).getId()));
                }
            }
        });
        initDataFromLocal();
        viewList.add(getLayoutInflater().inflate(R.layout.layout_finance_share, null, false));
        viewList.add(getLayoutInflater().inflate(R.layout.layout_finance_earn_rank, null, false));
        ViewAdapter viewAdapter = new ViewAdapter();
        viewPager.setAdapter(viewAdapter);
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(getActivity());
        scaleCircleNavigator.setCircleCount(2);
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(getActivity().getResources().getColor(R.color.mainColor));
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        indicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(indicator, viewPager);
        return view;
    }

    List<View> viewList = new ArrayList<>();

    @OnClick(R.id.joinNow)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("productId", ririYing.getId()));
    }

    class ViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(viewList.get(position));
            viewList.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ConstantValue.currentUser == null) {
                        startActivity(new Intent(getActivity(), AccountActivity.class));
                        return;
                    }
                    if (position == 0) {
                        startActivity(new Intent(getActivity(), InviteActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), EarnRankActivity.class));
                    }
                }
            });
            return viewList.get(position);
        }
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerFinanceComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .financeModule(new FinanceModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(FinanceContract.FinanceContractPresenter presenter) {
        mPresenter = (FinancePresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {
        getProduct();
    }

    private void getProduct() {
        Map map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "50");
        mPresenter.getProductList(map);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    Product.DataBean ririYing;

    @Override
    public void getProductBack(Product product) {
        produceListAdapter.setNewData(product.getData());
        ririYing = product.getData().get(0);
        tvInComeRate.setText(BigDecimal.valueOf(ririYing.getAnnualIncomeRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        if (LanguageUtil.isCN(getActivity())) {
            qlcDaily.setText(ririYing.getName());
        } else {
            qlcDaily.setText(ririYing.getNameEn());
        }
        tvLeastAmount.setText(getString(R.string.from_)  + " " + ririYing.getLeastAmount() + " QLC");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
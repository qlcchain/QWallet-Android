package com.stratagile.qlink.ui.activity.otc;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.EntrustOrderList;
import com.stratagile.qlink.entity.eventbus.GetPairs;
import com.stratagile.qlink.entity.eventbus.StartFilter;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.my.Login1Fragment;
import com.stratagile.qlink.ui.activity.my.RegiesterFragment;
import com.stratagile.qlink.ui.activity.otc.component.DaggerMarketComponent;
import com.stratagile.qlink.ui.activity.otc.contract.MarketContract;
import com.stratagile.qlink.ui.activity.otc.module.MarketModule;
import com.stratagile.qlink.ui.activity.otc.presenter.MarketPresenter;
import com.stratagile.qlink.ui.adapter.otc.EntrustOrderListAdapter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.KotlinConvertJavaUtils;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: $description
 * @date 2019/06/14 16:23:19
 */

public class MarketFragment extends BaseFragment implements MarketContract.View {

    @Inject
    MarketPresenter mPresenter;
    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.refreshLayout)
//    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.tvFilter)
    TextView tvFilter;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market1, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        EventBus.getDefault().register(this);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.pairsLiveData.observe(this, new Observer<ArrayList<TradePair.PairsListBean>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TradePair.PairsListBean> pairsListBeans) {
                ArrayList<String> titles = new ArrayList<>();
                ArrayList<Fragment> fragments = new ArrayList<>();
                for (int i = 0; i < pairsListBeans.size(); i++) {
                    if (!titles.contains(pairsListBeans.get(i).getTradeToken()) && pairsListBeans.get(i).isSelect()) {
                        titles.add(pairsListBeans.get(i).getTradeToken());
                        fragments.add(TradeListFragment.Companion.getInstance(pairsListBeans.get(i).getTradeToken()));
                    }
                }
                viewPager.removeAllViewsInLayout();
                MarketListAdapter adapter = new MarketListAdapter(getActivity(), getChildFragmentManager(), fragments);
                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(5);
                CommonNavigator commonNavigator = new CommonNavigator(getActivity());
                commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                    @Override
                    public int getCount() {
                        return titles.size();
                    }

                    @Override
                    public IPagerTitleView getTitleView(Context context, int i) {
                        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                        simplePagerTitleView.setText(titles.get(i));
                        simplePagerTitleView.setNormalColor(getResources().getColor(R.color.white));
                        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.white));
                        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewPager.setCurrentItem(i);
                            }
                        });
                        return simplePagerTitleView;
                    }

                    @Override
                    public IPagerIndicator getIndicator(Context context) {
                        LinePagerIndicator indicator = new LinePagerIndicator(context);
                        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                        indicator.setLineHeight(getResources().getDimension(R.dimen.x3));
                        indicator.setColors(getResources().getColor(R.color.white));
                        return indicator;
                    }
                });
                indicator.setNavigator(commonNavigator);
                commonNavigator.getTitleContainer().setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                commonNavigator.getTitleContainer().setDividerDrawable(new ColorDrawable() {
                    @Override
                    public int getIntrinsicWidth() {
                        return (int) getResources().getDimension(R.dimen.x10);
                    }
                });
                ViewPagerHelper.bind(indicator, viewPager);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPairs(GetPairs getPairs) {
        monitorPairs();
    }

    private void monitorPairs() {
        Observable.interval(0, 5, TimeUnit.SECONDS).take(5)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return 5 - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//发射用的是observeOn
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        KLog.i("1");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        KLog.i("2");
                    }

                    @Override
                    public void onNext(@NonNull Long remainTime) {
                        KLog.i("剩余时间" + remainTime);
                        getPairs();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        KLog.i("4");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> titles = new ArrayList<>();
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StartFilter());
            }
        });
        monitorPairs();
    }

    public void getPairs() {
        if (viewModel.pairsLiveData.getValue() != null) {
            return;
        }
        mPresenter.getPairs();
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerMarketComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .marketModule(new MarketModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MarketContract.MarketContractPresenter presenter) {
        mPresenter = (MarketPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

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
    public void setRemoteTradePairs(ArrayList<TradePair.PairsListBean> pairsListBeans) {
        String local = FileUtil.getStrDataFromFile(new File(Environment.getExternalStorageDirectory() + "/Qwallet/tradePair.json"));
        Gson gson = new Gson();
        if (!"".equals(local)) {
            ArrayList<TradePair.PairsListBean> localPair = gson.fromJson(local, new TypeToken<ArrayList<TradePair.PairsListBean >>() {
            }.getType());
            for (TradePair.PairsListBean pairsListBean : pairsListBeans) {
                for (TradePair.PairsListBean pairsListBean1 : localPair) {
                    if (pairsListBean.getTradeToken().equals(pairsListBean1.getTradeToken()) && pairsListBean.getPayToken().equals(pairsListBean1.getPayToken())) {
                        pairsListBean.setSelect(true);
                    }
                }
            }
        } else {
            for (int i = 0; i < pairsListBeans.size(); i++) {
                if (i < 3) {
                    pairsListBeans.get(i).setSelect(true);
                }
            }
        }
        viewModel.pairsLiveData.postValue(pairsListBeans);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
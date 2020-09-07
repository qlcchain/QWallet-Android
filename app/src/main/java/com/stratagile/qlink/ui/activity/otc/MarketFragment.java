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
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.eventbus.GetPairs;
import com.stratagile.qlink.entity.eventbus.ShowMiningAct;
import com.stratagile.qlink.entity.eventbus.StartFilter;
import com.stratagile.qlink.entity.newwinq.MiningAct;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.otc.component.DaggerMarketComponent;
import com.stratagile.qlink.ui.activity.otc.contract.MarketContract;
import com.stratagile.qlink.ui.activity.otc.module.MarketModule;
import com.stratagile.qlink.ui.activity.otc.presenter.MarketPresenter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.FireBaseUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.SegmentedGroup;
import com.stratagile.qlink.view.SweetAlertDialog;

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
import java.math.BigDecimal;
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
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.rlWallet)
    RelativeLayout rlWallet;

    @Override
    protected void initDataFromNet() {

    }

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
    @BindView(R.id.status_bar)
    TextView statusBar;
    @BindView(R.id.button21)
    RadioButton button21;
    @BindView(R.id.button22)
    RadioButton button22;
    @BindView(R.id.segmentControlView)
    SegmentedGroup segmentControlView;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market1, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        EventBus.getDefault().register(this);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(UIUtils.getDisplayWidth(getActivity()), UIUtils.getStatusBarHeight(getActivity()));
        statusBar.setLayoutParams(llp);

        String local = FileUtil.getStrDataFromFile(new File(Environment.getExternalStorageDirectory() + "/Qwallet/tradePair.json"));
        Gson gson = new Gson();
        if (!"".equals(local)) {
            ArrayList<TradePair.PairsListBean> localPair = gson.fromJson(local, new TypeToken<ArrayList<TradePair.PairsListBean>>() {
            }.getType());
            for (TradePair.PairsListBean pairsListBean1 : localPair) {
                pairsListBean1.setSelect(true);
            }
            viewModel.pairsLiveData.postValue(localPair);
        }

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
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getMiningList();
            }
        }, 1000);

        button21.toggle();
        segmentControlView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (viewModel.pairsLiveData.getValue() == null || viewModel.pairsLiveData.getValue().size() == 0) {
                    EventBus.getDefault().post(new GetPairs());
                } else {
                    if (i == R.id.button21) {
                        FireBaseUtils.logEvent(getActivity(), FireBaseUtils.OTC_Home_BUY);
                        viewModel.currentEntrustOrderType.postValue(ConstantValue.orderTypeSell);
                    } else {
                        FireBaseUtils.logEvent(getActivity(), FireBaseUtils.OTC_Home_SELL);
                        viewModel.currentEntrustOrderType.postValue(ConstantValue.orderTypeBuy);
                    }
                }
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

    private boolean isGetMiningList = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getMiningList();
        }
    }

    private void getMiningList() {
        if (!isGetMiningList) {
            mPresenter.getMiningList();
            isGetMiningList = true;
        }
    }

    private SweetAlertDialog sweetAlertDialog;

    private void showMiningAct(MiningAct miningAct) {
        View view = getLayoutInflater().inflate(R.layout.alert_mining, null, false);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView qlc = view.findViewById(R.id.qlc);
        qlc.setText(BigDecimal.valueOf(miningAct.getList().get(0).getTotalRewardAmount()).stripTrailingZeros().toPlainString());
        TextView tvOpreate = view.findViewById(R.id.tvOpreate);
        tvOpreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.dismissWithAnimation();
                if (ConstantValue.currentUser == null) {
                    viewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getActivity(), AccountActivity.class));
                        }
                    }, 200);
                } else {
                    viewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getActivity(), MiningInviteActivity.class));
                        }
                    }, 200);
                }

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sweetAlertDialog = new SweetAlertDialog(getActivity());
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        SpUtil.putBoolean(AppConfig.getInstance(), ConstantValue.isShowMiningAct, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> titles = new ArrayList<>();
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FireBaseUtils.logEvent(getActivity(), FireBaseUtils.OTC_Home_Filter);
                EventBus.getDefault().post(new StartFilter());
            }
        });
        mPresenter.getPairs();
        ivAvater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConstantValue.currentUser == null) {
                    startActivity(new Intent(getActivity(), AccountActivity.class));
                    return;
                }
                FireBaseUtils.logEvent(getActivity(), FireBaseUtils.OTC_Home_Record);
                Intent intent1 = new Intent(getActivity(), OtcOrderRecordActivity.class);
                startActivity(intent1);
            }
        });
        rlWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConstantValue.currentUser == null) {
                    startActivity(new Intent(getActivity(), AccountActivity.class));
                    return;
                }
                FireBaseUtils.logEvent(getActivity(), FireBaseUtils.OTC_Home_NewOrder);
                startActivityForResult(new Intent(getActivity(), NewOrderActivity.class).putExtra("pair", viewModel.pairsLiveData.getValue().get(0)), NEW_ORDER);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ORDER) {
            viewModel.timeStampLiveData.postValue(System.currentTimeMillis());
        }
    }

    public static final int NEW_ORDER = 8;

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
            ArrayList<TradePair.PairsListBean> localPair = gson.fromJson(local, new TypeToken<ArrayList<TradePair.PairsListBean>>() {
            }.getType());
            for (TradePair.PairsListBean pairsListBean : pairsListBeans) {
                boolean hasLocalPair = false;
                for (TradePair.PairsListBean pairsListBean1 : localPair) {
                    if (pairsListBean.getTradeToken().equals(pairsListBean1.getTradeToken()) && pairsListBean.getPayToken().equals(pairsListBean1.getPayToken())) {
                        hasLocalPair = true;
                        if (pairsListBean1.isSelect()) {
                            pairsListBean.setSelect(true);
                        }
                    }
                }
                if (!hasLocalPair) {
                    pairsListBean.setSelect(true);
                }
            }
        } else {
            for (int i = 0; i < pairsListBeans.size(); i++) {
                if (i < 3) {
                    pairsListBeans.get(i).setSelect(true);
                }
            }
        }
        String saveData = new Gson().toJson(pairsListBeans);
        FileUtil.savaData("/Qwallet/tradePair.json", saveData);
        viewModel.pairsLiveData.postValue(pairsListBeans);
    }

    @Override
    public void setMiningAct(MiningAct miningAct) {
        KLog.i("显示。。。");
        if (miningAct.getList().size() > 0) {
            String currentMiningActId = SpUtil.getString(getActivity(), ConstantValue.currentMiningActId, "");
            if (!miningAct.getList().get(0).getId().equals(currentMiningActId) && isVisibleToUser) {
                SpUtil.putString(getActivity(), ConstantValue.currentMiningActId, miningAct.getList().get(0).getId());
                showMiningAct(miningAct);
            }
            EventBus.getDefault().post(new ShowMiningAct(true, miningAct.getList().get(0).getTotalRewardAmount() + ""));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
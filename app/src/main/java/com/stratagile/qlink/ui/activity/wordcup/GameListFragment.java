package com.stratagile.qlink.ui.activity.wordcup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.adapter.wordcup.GameListAdapter;
import com.stratagile.qlink.ui.activity.wordcup.component.DaggerGameListComponent;
import com.stratagile.qlink.ui.activity.wordcup.contract.GameListContract;
import com.stratagile.qlink.ui.activity.wordcup.module.GameListModule;
import com.stratagile.qlink.ui.activity.wordcup.presenter.GameListPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: $description
 * @date 2018/06/11 16:31:05
 */

public class GameListFragment extends MyBaseFragment implements GameListContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Inject
    GameListPresenter mPresenter;
    @Inject
    GameListAdapter gameListAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String mType;
    private int mNextPage;
    private static final int ONE_PAGE_SIZE = 100;
    private int total;
    private static final String ARG_TYPE = "arg_type";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
        recyclerView.setAdapter(gameListAdapter);
//        gameListAdapter.setOnLoadMoreListener(this, recyclerView);
        gameListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), WordCupBetActivity.class);
                RaceTimes.DataBean dataBean = (RaceTimes.DataBean)gameListAdapter.getItem(i);
                intent.putExtra("dataBean", dataBean);
                startActivity(intent);
//                RaceTimes.DataBean dataBean = (RaceTimes.DataBean)gameListAdapter.getItem(i);
//                mPresenter.isCanBetThisRace(dataBean.getRaceTime(), i);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public void fetchData() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataFromServer(true);
            }
        });
    }

    public void loadDataFromServer(boolean showRefresh) {
        mPresenter.getCurrentServerTime(new HashMap());
        Map<String, Object> infoMap = new HashMap<>();
        List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        infoMap.put("address", wallets.get(SpUtil.getInt(getActivity(), ConstantValue.currentMainWallet, 0)).getAddress());
        infoMap.put("raceStatus", mType.equals("0") ? 0 : 1);
        mPresenter.getRaceTimes(infoMap);
        refreshLayout.setRefreshing(showRefresh);
//        if (showRefresh) {
//            mNextPage = 1;
//        }
//        mPresenter.getGameListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }

    public static GameListFragment newInstance(String param) {
        GameListFragment fragment = new GameListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mNextPage = 1;
        loadDataFromServer(true);
    }

    @Override
    public void onLoadMoreRequested() {
        if (gameListAdapter.getData().size() < ONE_PAGE_SIZE || gameListAdapter.getData().size() >= total) {
            gameListAdapter.loadMoreEnd(false);
            return;
        }
        mNextPage++;
        mPresenter.getGameListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerGameListComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .gameListModule(new GameListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(GameListContract.GameListContractPresenter presenter) {
        mPresenter = (GameListPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    public void showRaceTimes(List<RaceTimes.DataBean> arrayList) {
        List<MultiItemEntity> showList = new ArrayList<>();
        List<String> beginTime = new ArrayList<>();
        for (RaceTimes.DataBean dataBean : arrayList) {
            if (!beginTime.contains(dataBean.getRaceTime().substring(0, 5))) {
                beginTime.add(dataBean.getRaceTime().substring(0, 5));
                RaceTimes.RaceBeginTime raceBeginTime = new RaceTimes.RaceBeginTime(dataBean.getRaceTime().substring(0, 5));
                showList.add(raceBeginTime);
            }
            showList.add(dataBean);

        }
        gameListAdapter.setNewData(showList);
        refreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(RaceTimes.DataBean dataBean) {
        KLog.i("收到eventbus。。。。。");
        for (int i = 0; i < gameListAdapter.getData().size(); i++) {
            if (gameListAdapter.getData().get(i).getItemType() == GameListAdapter.TYPE_CHILDREN) {
                RaceTimes.DataBean dataBean1 = (RaceTimes.DataBean) gameListAdapter.getData().get(i);
                if (dataBean1.getRacekey().equals(dataBean.getRacekey())) {
                    gameListAdapter.getData().set(i, dataBean);
                    gameListAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    @Override
    public void setCurrentServerTime(String time) {
        gameListAdapter.setCurrentServerTime(time);
        gameListAdapter.notifyDataSetChanged();
    }

    @Override
    public void startBet(int position) {
        ToastUtil.displayShortToast("开始下注" + position);
        Intent intent = new Intent(getActivity(), WordCupBetActivity.class);
        RaceTimes.DataBean dataBean = (RaceTimes.DataBean)gameListAdapter.getItem(position);
        intent.putExtra("dataBean", dataBean);
        startActivity(intent);
    }

}
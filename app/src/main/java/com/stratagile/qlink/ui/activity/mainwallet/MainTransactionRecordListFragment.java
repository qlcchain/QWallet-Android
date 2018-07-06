package com.stratagile.qlink.ui.activity.mainwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.activity.wordcup.WordCupBetActivity;
import com.stratagile.qlink.ui.adapter.mainwallet.MainTransactionRecordListAdapter;
import com.stratagile.qlink.ui.activity.mainwallet.component.DaggerMainTransactionRecordListComponent;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainTransactionRecordListContract;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainTransactionRecordListModule;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainTransactionRecordListPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.eth.WalletStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: $description
 * @date 2018/06/13 20:52:12
 */

public class MainTransactionRecordListFragment extends MyBaseFragment implements MainTransactionRecordListContract.View ,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {

    @Inject
    MainTransactionRecordListPresenter mPresenter;
    @Inject
    MainTransactionRecordListAdapter mainTransactionRecordListAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String mType;
    private int mNextPage;
    private static final int ONE_PAGE_SIZE = 5;
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
        recyclerView.setAdapter(mainTransactionRecordListAdapter);
//        mainTransactionRecordListAdapter.setOnLoadMoreListener(this, recyclerView);
        mainTransactionRecordListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), WordCupBetActivity.class);
                Record.DataBean dataBean = (Record.DataBean)mainTransactionRecordListAdapter.getItem(i);
                intent.putExtra("dataBean", dataBean);
                startActivity(intent);
//                RaceTimes.DataBean dataBean = (RaceTimes.DataBean)mainTransactionRecordListAdapter.getItem(i);
//                mPresenter.isCanBetThisRace(dataBean.getRaceTime(), i);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        KLog.i("开始拉数据");
        List<TransactionRecord> transactionRecordList = AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll();
        checkData();
        Collections.sort(transactionRecordList);
        Map<String, Object> map = new HashMap<>();
        int count = 0;
        for (int i = 0; i < transactionRecordList.size(); i++) {
            if (transactionRecordList.get(i).getTxid() == null || transactionRecordList.get(i).getTxid().equals("") || transactionRecordList.get(i).getTxid().equals(null)) {
            } else {
                count++;
            }
        }
        String[] ssids = new String[count];
        for (int i = 0; i < transactionRecordList.size(); i++) {
            if (transactionRecordList.get(i).getTxid() == null || transactionRecordList.get(i).getTxid().equals("") || transactionRecordList.get(i).getTxid().equals(null)) {
                KLog.i(transactionRecordList.get(i).getTxid() + "异常");
            } else {
                KLog.i(transactionRecordList.get(i).getTxid() + "正常");
                ssids[i] = transactionRecordList.get(i).getTxid();
            }
        }
        map.put("recordIds", ssids);
        mPresenter.getRecords(map);
    }
    private void checkData() {
        List<TransactionRecord> transactionRecordList = AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll();
        for (int i = 0; i < transactionRecordList.size(); i++) {
            KLog.i(transactionRecordList.get(i).toString());
            if (transactionRecordList.get(i).getTxid() == null || transactionRecordList.get(i).getTxid().equals("") || transactionRecordList.get(i).getTxid().equals(null)) {
                AppConfig.getInstance().getDaoSession().getTransactionRecordDao().delete(transactionRecordList.get(i));
                KLog.i("删除");
                checkData();
                break;
            }
        }
    }
    public static MainTransactionRecordListFragment newInstance(String param) {
        MainTransactionRecordListFragment fragment = new MainTransactionRecordListFragment();
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
        if (mainTransactionRecordListAdapter.getData().size() < ONE_PAGE_SIZE || mainTransactionRecordListAdapter.getData().size() >= total) {
            mainTransactionRecordListAdapter.loadMoreEnd(false);
            return;
        }
        mNextPage ++;
        mPresenter.getMainTransactionRecordListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }


    @Override
    protected void setupFragmentComponent() {
       DaggerMainTransactionRecordListComponent
               .builder()
               .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
               .mainTransactionRecordListModule(new MainTransactionRecordListModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(MainTransactionRecordListContract.MainTransactionRecordListContractPresenter presenter) {
        mPresenter = (MainTransactionRecordListPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }
    @Override
    public void getRecordSuccess(Record record) {
        refreshLayout.setRefreshing(false);
        //Collections.reverse(record.getData());
        for (TransactionRecord transactionRecord : AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll()) {
            if (transactionRecord.getTransactiomType() == 0) {
                for (Record.DataBean dataBean : record.getData()) {
                    if (dataBean.getRecordId().equals(transactionRecord.getTxid())) {
                        dataBean.setWifiName(transactionRecord.getAssetName());
                        dataBean.setConnectType(transactionRecord.getConnectType());
                    }
                }
            } else if (transactionRecord.getTransactiomType() == 3) {
                for (Record.DataBean dataBean : record.getData()) {
                    if (dataBean.getRecordId().equals(transactionRecord.getTxid())) {
                        dataBean.setConnectType(transactionRecord.getConnectType());
                        if (!transactionRecord.getIsReported()) {
                            //为什么vpn给提供端的记录在这里上报？因为在刚连接上vpn的时候，网络在切换，不稳定，所以统一在这里判断并且上报
                            Qsdk.getInstance().sendRecordSaveReq(transactionRecord.getFriendNum(), transactionRecord);
                        }
                    }
                }
            }
        }
        List<Record.DataBean> dataBeanList = new ArrayList<>();
        String ethWalletAddress ="";
        if(WalletStorage.getInstance(getActivity()) != null && WalletStorage.getInstance(getActivity()).get().size() > 0)
        {
            ethWalletAddress = WalletStorage.getInstance(getActivity()).get().get(SpUtil.getInt(getActivity(), ConstantValue.currentEthWallet, 0)).getPubKey();
        }
        KLog.i(ethWalletAddress);
        String walletAddress = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress();
        for (int i = 0; i < record.getData().size(); i++) {
            if (record.getData().get(i).getAddressFrom().equals(walletAddress) || record.getData().get(i).getAddressTo().equals(walletAddress) || record.getData().get(i).getAddressFrom().equals(ethWalletAddress) || record.getData().get(i).getAddressTo().equals(ethWalletAddress)) {
                dataBeanList.add(record.getData().get(i));
            }
        }
        mainTransactionRecordListAdapter.setNewData(dataBeanList);
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
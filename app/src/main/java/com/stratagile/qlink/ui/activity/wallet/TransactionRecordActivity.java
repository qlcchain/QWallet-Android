package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerTransactionRecordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.TransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.module.TransactionRecordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.TransactionRecordPresenter;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.wallet.TransactionRecordAdapter;
import com.stratagile.qlink.utils.eth.WalletStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/26 17:16:57
 */

public class TransactionRecordActivity extends BaseActivity implements TransactionRecordContract.View {

    @Inject
    TransactionRecordPresenter mPresenter;
    @Inject
    TransactionRecordAdapter transactionRecordAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rl_asset_tip)
    RelativeLayout rlAssetTip;

    private static final int REWARD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_transaction_record);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionRecordAdapter.setCurrentWalletAddress(AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress());
        recyclerView.setAdapter(transactionRecordAdapter);
        setTitle(getString(R.string.history_records).toUpperCase());
        recyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10, this)));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        refreshLayout.setRefreshing(true);
        transactionRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (transactionRecordAdapter.getItem(position).getType() == 0 && transactionRecordAdapter.getItem(position).getIsGratuity().equals("NohaveAreward") && transactionRecordAdapter.getItem(position).getConnectType() == 0) {
                    Intent intent = new Intent(TransactionRecordActivity.this, GratuityActivity.class);
                    intent.putExtra("dataBean", transactionRecordAdapter.getItem(position));
                    startActivityForResult(intent, REWARD);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REWARD && resultCode == RESULT_OK) {
            refreshLayout.setRefreshing(true);
            initData();
        }
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

    @Override
    protected void initData() {
        KLog.i("开始拉数据");
        ArrayList<TransactionRecord> transactionRecordList = (ArrayList<TransactionRecord>) AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll();
        Iterator<TransactionRecord> iterator = transactionRecordList.iterator();
        ArrayList<TransactionRecord> transactionRecords = new ArrayList<>();
        while (iterator.hasNext()) {
            TransactionRecord transactionRecord = iterator.next();
            if ((transactionRecord.getIsMainNet() && SpUtil.getBoolean(this, ConstantValue.isMainNet, true)) || (!transactionRecord.getIsMainNet() && !SpUtil.getBoolean(this, ConstantValue.isMainNet, false))) {
                //相同的网
                transactionRecords.add(transactionRecord);
            } else {

            }
        }
        transactionRecordList = transactionRecords;
        checkData();
        Collections.sort(transactionRecordList);
        if (transactionRecordList == null || transactionRecordList.size() == 0) {
            rlAssetTip.setVisibility(View.GONE);
        } else {
            rlAssetTip.setVisibility(View.VISIBLE);
        }
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

    @Override
    protected void setupActivityComponent() {
        DaggerTransactionRecordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .transactionRecordModule(new TransactionRecordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(TransactionRecordContract.TransactionRecordContractPresenter presenter) {
        mPresenter = (TransactionRecordPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
        refreshLayout.setRefreshing(false);
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
        if(WalletStorage.getInstance(this) != null && WalletStorage.getInstance(this).get().size() > 0)
        {
            ethWalletAddress = WalletStorage.getInstance(this).get().get(SpUtil.getInt(this, ConstantValue.currentEthWallet, 0)).getPubKey();
        }
        KLog.i(ethWalletAddress);
        String walletAddress = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress();
        for (int i = 0; i < record.getData().size(); i++) {
            if (record.getData().get(i).getAddressFrom().equals(walletAddress) || record.getData().get(i).getAddressTo().equals(walletAddress) || record.getData().get(i).getAddressFrom().equals(ethWalletAddress) || record.getData().get(i).getAddressTo().equals(ethWalletAddress)) {
                if (record.getData().get(i).getType() != -1) {
                    dataBeanList.add(record.getData().get(i));
                }
            }
        }
        transactionRecordAdapter.setNewData(dataBeanList);
    }

}
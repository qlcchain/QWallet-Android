package com.stratagile.qlink.ui.activity.eth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthMnemonicbackupComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthMnemonicbackupContract;
import com.stratagile.qlink.ui.activity.eth.module.EthMnemonicbackupModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthMnemonicbackupPresenter;
import com.stratagile.qlink.ui.adapter.eth.EthSelectedMnemonicAdapter;
import com.stratagile.qlink.ui.adapter.eth.EthUnSelectedMnemonicAdapter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/23 15:13:44
 * 以太坊钱包助记词备份页面
 */

public class EthMnemonicbackupActivity extends BaseActivity implements EthMnemonicbackupContract.View {

    @Inject
    EthMnemonicbackupPresenter mPresenter;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @BindView(R.id.btBackup)
    Button btBackup;

    private EthWallet ethWallet;
    private ArrayList<String> mnemonicList = new ArrayList<>();
    private ArrayList<String> selectedMnemonicList = new ArrayList<>();
    private ArrayList<String> unSelectedMnemonicList = new ArrayList<>();
    private EthSelectedMnemonicAdapter ethSelectedMnemonicAdapter;
    private EthUnSelectedMnemonicAdapter ethUnSelectedMnemonicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_mnemonicbackup);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ethWallet = getIntent().getParcelableExtra("wallet");
        String[] words = ethWallet.getMnemonic().split(" ");
        for (int i = 0; i < words.length; i++) {
            mnemonicList.add(words[i]);
        }
        unSelectedMnemonicList.addAll(mnemonicList);
        Collections.shuffle(unSelectedMnemonicList);
        ethSelectedMnemonicAdapter = new EthSelectedMnemonicAdapter(selectedMnemonicList);
        ethUnSelectedMnemonicAdapter = new EthUnSelectedMnemonicAdapter(unSelectedMnemonicList);
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        FlexboxLayoutManager flexboxLayoutManager2 = new FlexboxLayoutManager(this);
        recyclerView1.setLayoutManager(flexboxLayoutManager1);
        recyclerView2.setLayoutManager(flexboxLayoutManager2);
        recyclerView1.setAdapter(ethSelectedMnemonicAdapter);
        recyclerView2.setAdapter(ethUnSelectedMnemonicAdapter);
        ethUnSelectedMnemonicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ethSelectedMnemonicAdapter.getData().add(ethUnSelectedMnemonicAdapter.getData().get(position));
                ethUnSelectedMnemonicAdapter.getData().remove(position);

                ethUnSelectedMnemonicAdapter.notifyDataSetChanged();
                ethSelectedMnemonicAdapter.notifyDataSetChanged();
            }
        });
        ethSelectedMnemonicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ethUnSelectedMnemonicAdapter.getData().add(ethSelectedMnemonicAdapter.getData().get(position));
                ethSelectedMnemonicAdapter.getData().remove(position);

                ethUnSelectedMnemonicAdapter.notifyDataSetChanged();
                ethSelectedMnemonicAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        setTitle("Backup Mnemonic");
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthMnemonicbackupComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethMnemonicbackupModule(new EthMnemonicbackupModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthMnemonicbackupContract.EthMnemonicbackupContractPresenter presenter) {
        mPresenter = (EthMnemonicbackupPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.btBackup)
    public void onViewClicked() {
        if (selectedMnemonicList.size() != mnemonicList.size()) {
            ToastUtil.displayShortToast("please select all mnemonic");
            return;
        }
        for (int i = 0; i < mnemonicList.size(); i++) {
            if (!mnemonicList.get(i).equals(selectedMnemonicList.get(i))) {
                ToastUtil.displayShortToast("Incorrect sequence");
                return;
            }
        }
        ethWallet.setIsBackup(true);
        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(ethWallet);
        showTestDialog();

    }

    private void showTestDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_tip, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        ImageView imageView = view.findViewById(R.id.ivTitle);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.op_success));
        tvContent.setText("backup success");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        btBackup.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetAlertDialog.cancel();
                btBackup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, 200);
            }
        }, 2000);
    }

}
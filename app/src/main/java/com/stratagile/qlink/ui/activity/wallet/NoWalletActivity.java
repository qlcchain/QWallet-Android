package com.stratagile.qlink.ui.activity.wallet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.guideview.Component;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.NewWalletComponent;
import com.stratagile.qlink.ui.activity.neo.WalletCreatedActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerNoWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.NoWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.NoWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.NoWalletPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/23 13:54:18
 */

public class NoWalletActivity extends BaseActivity implements NoWalletContract.View {

    @Inject
    NoWalletPresenter mPresenter;
    @BindView(R.id.bt_create)
    Button btCreate;
    @BindView(R.id.bt_import)
    Button btImport;
    @BindView(R.id.bt_later)
    Button btLater;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.t_tip)
    TextView tTip;
    @BindView(R.id.bt_scan)
    Button btScan;
    @BindView(R.id.tv_tip_two)
    TextView tvTipTwo;
    @BindView(R.id.noWalletParent)
    LinearLayout noWalletParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_no_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        if (getIntent().getStringExtra("flag").equals("nowallet")) {
            tvTitle.setText(getString(R.string.no_wallet).toUpperCase());
        } else {
            tvTitle.setText(getString(R.string.new_wallet).toUpperCase());
            tTip.setText(R.string.Hello_again_Do_you_want_to_add_another_wallet);
            tvTipTwo.setVisibility(View.GONE);
        }
        noWalletParent.setBackgroundResource(R.drawable.navigation_shape);
        if (!SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
            showTestNetDialog();
        }
//        if (fromType != null) {
//            switch (fromType) {
//                case "worldCup"://世界杯背景
//                    noWalletParent.setBackgroundResource(R.mipmap.bg_world_cup_two);
//                    break;
//                default:
//                    showTestNetDialog();
//                    noWalletParent.setBackgroundResource(R.drawable.navigation_shape);
//                    break;
//            }
//        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerNoWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .noWalletModule(new NoWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(NoWalletContract.NoWalletContractPresenter presenter) {
        mPresenter = (NoWalletPresenter) presenter;
    }

    @Override
    public void onBackPressed() {

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
    public void onCreatWalletSuccess(CreateWallet createWallet, int flag) {
        closeProgressDialog();
//        neoutils.Wallet wallet = Account.INSTANCE.getWallet();
//        KLog.i(wallet.toString());
//        String privateKey = WalletKtutil.byteArrayToHex(wallet.getPrivateKey());
//        AppConfig.getInstance().getDaoSession().getWalletDao().insert(createWallet.getData());
        Intent intent = new Intent(this, WalletCreatedActivity.class);
        intent.putExtra("wallet", createWallet.getData());
        if (flag == 0) {
            intent.putExtra("title", "Wallet Created");
        } else {
            intent.putExtra("title", "Wallet Imported");
        }
        startActivityForResult(intent, 2);
    }

    @Override
    public void getScanPermissionSuccess() {
        Intent intent1 = new Intent(this, ScanQrCodeActivity.class);
        startActivityForResult(intent1, 1);
    }

    @Override
    public void createWalletFaliure() {
        closeProgressDialog();
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList != null && walletList.size() != 0) {
            Wallet wallet = walletList.get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Account.INSTANCE.fromWIF(wallet.getWif());
                }
            }).start();
        }
    }

    @OnClick({R.id.bt_create, R.id.bt_import, R.id.bt_later, R.id.bt_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_create:
                mPresenter.createWallet(new HashMap());

                break;
            case R.id.bt_import:
                Intent intent = new Intent(this, ImportWalletActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.bt_later:
                EventBus.getDefault().post(new ChangeViewpager(1));
                finish();
                break;
            case R.id.bt_scan:
                mPresenter.getSacnPermission();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            KLog.i(result);
            for (Wallet wallet : AppConfig.getInstance().getDaoSession().getWalletDao().loadAll()) {
                KLog.i(wallet.toString());
                if (result.toLowerCase().equals(wallet.getPrivateKey().toLowerCase()) || result.equals(wallet.getWif())) {
                    ToastUtil.displayShortToast(getString(R.string.this_wallet_is_exist));
                    return;
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("key", result);
            mPresenter.importWallet(map);
        }
        if (requestCode == 2) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 显示为测试网络的弹窗
     */
    private void showTestNetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.testnet_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(false);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        tvContent.setText(Html.fromHtml(getResources().getString(R.string.tesetnet_text)));
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showGuideViewNewWallet();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showGuideViewNewWallet();
            }
        });
        dialog.show();
    }

    private void showGuideViewNewWallet() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowNewWalletGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowNewWalletGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(btCreate)
                    .setAlpha(150)
                    .setHighTargetCorner(10)
                    .setHighTargetPadding(0)
                    .setHighTargetGraphStyle(Component.ROUNDRECT)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(true);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new NewWalletComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }

}
package com.stratagile.qlink.ui.activity.finance;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.ui.activity.finance.component.DaggerMyProductComponent;
import com.stratagile.qlink.ui.activity.finance.contract.MyProductContract;
import com.stratagile.qlink.ui.activity.finance.module.MyProductModule;
import com.stratagile.qlink.ui.activity.finance.presenter.MyProductPresenter;
import com.stratagile.qlink.ui.adapter.finance.OrderListAdapter;
import com.stratagile.qlink.utils.RSAEncrypt;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/11 16:18:23
 */

public class MyProductActivity extends BaseActivity implements MyProductContract.View {

    @Inject
    MyProductPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    OrderListAdapter orderListAdapter;
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.totolQlc)
    TextView totolQlc;
    @BindView(R.id.tvYesterdayEarn)
    TextView tvYesterdayEarn;
    @BindView(R.id.totolEarn)
    TextView totolEarn;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivHsitory)
    ImageView ivHsitory;

    private Order.OrderListBean currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_product);
        ButterKnife.bind(this);
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
        statusBar.setLayoutParams(llp);
    }

    @Override
    protected void initData() {
        getOrderList();
        orderListAdapter = new OrderListAdapter(new ArrayList<>());
        recyclerView.setAdapter(orderListAdapter);
        orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentOrder = orderListAdapter.getItem(position);
                showRecleem();
            }
        });
    }

    private void getOrderList() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("address", Account.INSTANCE.getWallet().getAddress());
        infoMap.put("page", "1");
        infoMap.put("size", "40");
        String orgin = Calendar.getInstance().getTimeInMillis() + "," + ConstantValue.currentUser.getPassword();
        KLog.i("加密前的token为：" + orgin);
        String token = RSAEncrypt.encrypt(orgin, ConstantValue.currentUser.getPubKey());
        infoMap.put("token", token);
        mPresenter.getOrderList(infoMap);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerMyProductComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .myProductModule(new MyProductModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MyProductContract.MyProductContractPresenter presenter) {
        mPresenter = (MyProductPresenter) presenter;
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
    public void getOrderBack(Order order) {
        orderListAdapter.setNewData(order.getOrderList());
        totolQlc.setText(order.getTotalQlc() + "");
        tvYesterdayEarn.setText(order.getYesterdayRevenue() + "");
        totolEarn.setText(order.getTotalRevenue() + "");
    }

    @Override
    public void redeemOrderBack(BaseBack baseBack) {
        if (baseBack.getCode().equals("0")) {
            ToastUtil.displayShortToast(getString(R.string.success));
        }
        getOrderList();
        closeProgressDialog();
    }

    private void showRecleem() {
        View view = View.inflate(this, R.layout.recleem_layout, null);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvNext = view.findViewById(R.id.tvNext);//取消按钮
        View switchWallet = view.findViewById(R.id.switchWallet);
        View llNoWallet = view.findViewById(R.id.llNoWallet);
        TextView tvWalletName = view.findViewById(R.id.tvWalletName);
        TextView tvPrincipal = view.findViewById(R.id.tvPrincipal);
        TextView tvCumulativeEarning = view.findViewById(R.id.tvCumulativeEarning);
        TextView tvWalletAddress = view.findViewById(R.id.tvWalletAddress);
        TextView tvProductName = view.findViewById(R.id.tvProductName);

        tvProductName.setText(currentOrder.getProductName());
        tvPrincipal.setText(currentOrder.getAmount() + " QLC");
        tvCumulativeEarning.setText(currentOrder.getAddRevenue() + " QLC");
        if (Account.INSTANCE.getWallet() == null) {
            switchWallet.setVisibility(View.GONE);
            llNoWallet.setVisibility(View.VISIBLE);
        } else {
            List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            if (neoWallets.size() != 0) {
                for (int i = 0; i < neoWallets.size(); i++) {
                    if (neoWallets.get(i).getAddress().equals(Account.INSTANCE.getWallet().getAddress())) {
                        switchWallet.setVisibility(View.VISIBLE);
                        llNoWallet.setVisibility(View.GONE);
                        tvWalletName.setText(neoWallets.get(i).getName());
                        tvWalletAddress.setText(neoWallets.get(i).getAddress());
                        break;
                    } else {
                        switchWallet.setVisibility(View.GONE);
                        llNoWallet.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                switchWallet.setVisibility(View.GONE);
                llNoWallet.setVisibility(View.VISIBLE);
            }
        }

        //取消或确定按钮监听事件处l
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        Window window = sweetAlertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
                showProgressDialog();
                redeemProduct();

            }
        });
    }

    private void redeemProduct() {
        Map<String, String> buyMap = new HashMap<>();
        buyMap.put("account", ConstantValue.currentUser.getAccount());
        buyMap.put("orderId", currentOrder.getId());
        String orgin = Calendar.getInstance().getTimeInMillis() + "," + ConstantValue.currentUser.getPassword();
        KLog.i("加密前的token为：" + orgin);
        String token = RSAEncrypt.encrypt(orgin, ConstantValue.currentUser.getPubKey());
        buyMap.put("token", token);
        mPresenter.redeemOrder(buyMap);
    }

    @OnClick({R.id.ivBack, R.id.ivHsitory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivHsitory:
                break;
            default:
                break;
        }
    }
}
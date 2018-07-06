package com.stratagile.qlink.ui.activity.mainwallet;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.mainwallet.component.DaggerMainReceiveComponent;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainReceiveContract;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainReceiveModule;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainReceivePresenter;
import com.stratagile.qlink.ui.activity.wallet.QrCodeDetailActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.view.RxQRCode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: $description
 * @date 2018/06/14 09:25:13
 */

public class MainReceiveFragment extends BaseFragment implements MainReceiveContract.View {

    @Inject
    MainReceivePresenter mPresenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_copy)
    Button btCopy;
    private static final String ARG_TYPE = "arg_type";
    private Wallet wallet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recevice_fund_layout, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
        address.setText(wallet.getAddress());
        RxQRCode.builder(wallet.getAddress()).
                backColor(getResources().getColor(com.vondear.rxtools.R.color.white)).
                codeColor(getResources().getColor(com.vondear.rxtools.R.color.black)).
                codeSide(800).
                into(ivAddress);
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerMainReceiveComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .mainReceiveModule(new MainReceiveModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MainReceiveContract.MainReceiveContractPresenter presenter) {
        mPresenter = (MainReceivePresenter) presenter;
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
    public void onDestroyView() {
        super.onDestroyView();
    }
    public static MainReceiveFragment newInstance(String param) {
        MainReceiveFragment fragment = new MainReceiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);

        return fragment;
    }
    @OnClick({R.id.iv_address, R.id.bt_back, R.id.bt_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_address:
                Intent intent = new Intent(getActivity(), QrCodeDetailActivity.class);
                intent.putExtra("content", wallet.getAddress());
                intent.putExtra("title", "public address");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), Pair.create(ivAddress, "qrcode"), Pair.create(cardView, "card"), Pair.create(tvTitle, "title")).toBundle());

                } else {
                    startActivity(intent);
                }
                break;
            case R.id.bt_back:
                getActivity().finish();
                break;
            case R.id.bt_copy:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", address.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.displayShortToast(getString(R.string.copy_success));
                break;
        }
    }
}
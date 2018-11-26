package com.stratagile.qlink.ui.activity.eth;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthWatchComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthWatchContract;
import com.stratagile.qlink.ui.activity.eth.module.EthWatchModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWatchPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.SmoothCheckBox;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/22 14:16:26
 */

public class EthWatchFragment extends BaseFragment implements EthWatchContract.View {

    @Inject
    EthWatchPresenter mPresenter;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;
    @BindView(R.id.btImport)
    Button btImport;
    private ImportViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eth_watch, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);
        return view;
    }

    public static EthWatchFragment newInstance(String param) {
        EthWatchFragment fragment = new EthWatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerEthWatchComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .ethWatchModule(new EthWatchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthWatchContract.EthWatchContractPresenter presenter) {
        mPresenter = (EthWatchPresenter) presenter;
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

    @OnClick(R.id.btImport)
    public void onViewClicked() {
        if (ETHWalletUtils.isETHValidAddress(etAddress.getText().toString())) {
            EthWallet ethWallet = new EthWallet();
            ethWallet.setAddress(etAddress.getText().toString());
            ethWallet.setIsLook(true);
            ethWallet.setName(ETHWalletUtils.generateNewWalletName());
            AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(ethWallet);
            viewModel.walletAddress.postValue(ethWallet.getAddress());
        } else {
            ToastUtil.displayShortToast("Invalid ETH Wallet Address");
        }

    }
}
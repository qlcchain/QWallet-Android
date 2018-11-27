package com.stratagile.qlink.ui.activity.eth;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.data.FullWallet;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthPrivateKeyComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthPrivateKeyContract;
import com.stratagile.qlink.ui.activity.eth.module.EthPrivateKeyModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthPrivateKeyPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.utils.eth.OwnWalletUtils;
import com.stratagile.qlink.utils.eth.WalletStorage;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/05/24 17:49:02
 */

public class EthPrivateKeyFragment extends BaseFragment implements EthPrivateKeyContract.View {

    @Inject
    EthPrivateKeyPresenter mPresenter;

    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.etPrivateKey)
    EditText etPrivateKey;
    @BindView(R.id.btImport)
    TextView btImport;
    private ImportViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eth_private_key, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);
        viewModel.qrCode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (getUserVisibleHint()) {
                    etPrivateKey.setText(s);
                }
            }
        });
        return view;
    }

    public static EthPrivateKeyFragment newInstance(String param) {
        EthPrivateKeyFragment fragment = new EthPrivateKeyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerEthPrivateKeyComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .ethPrivateKeyModule(new EthPrivateKeyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthPrivateKeyContract.EthPrivateKeyContractPresenter presenter) {
        mPresenter = (EthPrivateKeyPresenter) presenter;
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
        if ("".equals(etPrivateKey.getText().toString().trim())) {
            ToastUtil.displayShortToast("please type privatekey");
            return;
        }
        showProgressDialog();
        EthWallet ethWallet = ETHWalletUtils.loadWalletByPrivateKey(etPrivateKey.getText().toString());
        if (ethWallet == null) {
            closeProgressDialog();
            ToastUtil.displayShortToast("import eth wallet error");
            return;
        }
        List<EthWallet> wallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        if (wallets != null && wallets.size() != 0) {
            for (int i = 0; i < wallets.size(); i++) {
                if (wallets.get(i).getAddress().equals(ethWallet.getAddress())) {
                    ToastUtil.displayShortToast("wallet exist");
                    closeProgressDialog();
                    return;
                }
            }
        }
        List<Wallet> wallets1 = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (wallets1 != null && wallets1.size() != 0 ) {
            for (int i = 0; i < wallets1.size(); i++) {
                if (wallets1.get(i).getIsCurrent()) {
                    wallets1.get(i).setIsCurrent(false);
                    AppConfig.getInstance().getDaoSession().getWalletDao().update(wallets1.get(i));
                    break;
                }
            }

        }

        if (wallets != null && wallets.size() != 0) {
            for (int i = 0; i < wallets.size(); i++) {
                if (wallets.get(i).isCurrent()) {
                    wallets.get(i).setCurrent(false);
                    AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallets.get(i));
                    break;
                }
            }
        }

        AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(ethWallet);
        closeProgressDialog();
        viewModel.walletAddress.postValue(ethWallet.getAddress());
    }

}
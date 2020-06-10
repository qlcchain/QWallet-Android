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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.data.FullWallet;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.entity.KLine;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthKeyStroeComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthKeyStroeContract;
import com.stratagile.qlink.ui.activity.eth.module.EthKeyStroeModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthKeyStroePresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.utils.eth.OwnWalletUtils;
import com.stratagile.qlink.utils.eth.WalletStorage;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.acl.Owner;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/05/24 17:48:34
 */

public class EthKeyStroeFragment extends BaseFragment implements EthKeyStroeContract.View {
    @Override
    protected void initDataFromNet() {

    }
    @Inject
    EthKeyStroePresenter mPresenter;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.etKeystore)
    EditText etKeystroe;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btImport)
    TextView btImport;
    private ImportViewModel viewModel;

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eth_key_stroe, null);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);
        viewModel.qrCode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (getUserVisibleHint()) {
                    etKeystroe.setText(s);
                }
            }
        });
        Bundle mBundle = getArguments();
        return view;
    }

    public static EthKeyStroeFragment newInstance(String param) {
        EthKeyStroeFragment fragment = new EthKeyStroeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerEthKeyStroeComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .ethKeyStroeModule(new EthKeyStroeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthKeyStroeContract.EthKeyStroeContractPresenter presenter) {
        mPresenter = (EthKeyStroePresenter) presenter;
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
        if ("".equals(etKeystroe.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.please_type_keystore));
            return;
        }
        if (etPassword.getText().toString().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.please_type_password));
            return;
        }
        showProgressDialog();
        loadWalletByKeystore(etKeystroe.getText().toString(), etPassword.getText().toString());
    }

    /**
     * 通过keystore.json文件导入钱包
     *
     * @param keystore 原json文件
     * @param pwd      json文件密码
     * @return
     */
    public void loadWalletByKeystore(String keystore, String pwd) {
        EthWallet wallet;
        wallet = ETHWalletUtils.loadWalletByKeystore(keystore, pwd);
        if (wallet == null) {
            closeProgressDialog();
            ToastUtil.displayShortToast(getString(R.string.import_wallet_error));
            return;
        }
        KLog.i(wallet.toString());
        List<EthWallet> wallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).getAddress().equals(wallet.getAddress())) {
                ToastUtil.displayShortToast(getString(R.string.wallet_exist));
                closeProgressDialog();
                return;
            }
        }

        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).isCurrent()) {
                wallets.get(i).setCurrent(false);
                AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallets.get(i));
                break;
            }
        }
        AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(wallet);
        closeProgressDialog();
        viewModel.walletAddress.postValue(wallet.getAddress());
    }

}
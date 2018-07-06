package com.stratagile.qlink.ui.activity.eth;

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
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthKeyStroeComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthKeyStroeContract;
import com.stratagile.qlink.ui.activity.eth.module.EthKeyStroeModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthKeyStroePresenter;
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

    @Inject
    EthKeyStroePresenter mPresenter;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.et_keystroe)
    EditText etKeystroe;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_import)
    TextView tvImport;

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eth_key_stroe, null);
        ButterKnife.bind(this, view);
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

    @OnClick(R.id.tv_import)
    public void onViewClicked() {
        if (etKeystroe.getText().toString().equals("")) {
            return;
        }
        if (etPassword.getText().toString().equals("")) {
            return;
        }
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
        Credentials credentials = null;
        WalletFile walletFile = null;
        try {
            walletFile = objectMapper.readValue(keystore, WalletFile.class);

//            WalletFile walletFile = new Gson().fromJson(keystore, WalletFile.class);
            credentials = Credentials.create(Wallet.decrypt(pwd, walletFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
//            ToastUtils.showToast(R.string.load_wallet_by_official_wallet_keystore_input_tip);
            e.printStackTrace();
        }
        KLog.i(credentials.getEcKeyPair().getPrivateKey());
        try {
            OwnWalletUtils.generateWalletFile(pwd, credentials.getEcKeyPair(), new File(AppConfig.getInstance().getFilesDir(), ""), true);
            WalletStorage.getInstance(getActivity()).add(new FullWallet("0x" + walletFile.getAddress(), walletFile.getAddress()), getActivity());
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
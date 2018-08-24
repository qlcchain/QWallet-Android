package com.stratagile.qlink.ui.activity.eth;

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
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthPrivateKeyComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthPrivateKeyContract;
import com.stratagile.qlink.ui.activity.eth.module.EthPrivateKeyModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthPrivateKeyPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.OwnWalletUtils;
import com.stratagile.qlink.utils.eth.WalletStorage;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;

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
    @BindView(R.id.et_private_key)
    EditText etPrivateKey;
    @BindView(R.id.tv_import)
    TextView tvImport;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eth_private_key, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
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

    @OnClick(R.id.tv_import)
    public void onViewClicked() {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(etPrivateKey.getText().toString()));
                try {
                    String address = OwnWalletUtils.generateWalletFile(etPassword.getText().toString(), ecKeyPair, new File(AppConfig.getInstance().getFilesDir(), ""), true);
                    WalletStorage.getInstance(getActivity()).add(new FullWallet("0x" + address, address), getActivity());
                } catch (CipherException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.displayShortToast(getString(R.string.import_success));
                        getActivity().finish();
                    }
                });
            }
        }).start();
    }

}
package com.stratagile.qlink.ui.activity.qlc;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlc.QLCAPI;
import com.stratagile.qlc.utils.QlcUtil;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.ui.activity.eth.ImportViewModel;
import com.stratagile.qlink.ui.activity.qlc.component.DaggerImportQlcPrivateKeyComponent;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcPrivateKeyContract;
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcPrivateKeyModule;
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcPrivateKeyPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/05/21 09:46:08
 */

public class ImportQlcPrivateKeyFragment extends BaseFragment implements ImportQlcPrivateKeyContract.View {

    @Inject
    ImportQlcPrivateKeyPresenter mPresenter;
    @BindView(R.id.etPrivateKey)
    EditText etPrivateKey;
    @BindView(R.id.tvWhatsMnemonic)
    TextView tvWhatsMnemonic;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;
    @BindView(R.id.btImport)
    Button btImport;
    private ImportViewModel viewModel;
    private String scanPri;
    private String scanPub;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import_qlc_private_key, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);
        viewModel.qrCode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (getUserVisibleHint()) {
                    if (s.length() == 128) {
                        scanPri = s.substring(0, 64);
                        scanPub = s.substring(64);
                        if (QlcUtil.privateToPublic(scanPri).equals(scanPub)) {
                            etPrivateKey.setText(s);
                        } else {
                            ToastUtil.displayShortToast("privatekey error");
                        }
                    } else {
                        ToastUtil.displayShortToast("privatekey error");
                    }
                }
            }
        });
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerImportQlcPrivateKeyComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .importQlcPrivateKeyModule(new ImportQlcPrivateKeyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ImportQlcPrivateKeyContract.ImportQlcPrivateKeyContractPresenter presenter) {
        mPresenter = (ImportQlcPrivateKeyPresenter) presenter;
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
        String priKey = etPrivateKey.getText().toString().trim();

        String pubKey = QlcUtil.privateToPublic(priKey).toLowerCase();
        String address =  QlcUtil.publicToAddress(pubKey).toLowerCase();
        QLCAccount qlcAccount = new QLCAccount();
        qlcAccount.setPrivKey(priKey);
        qlcAccount.setPubKey(pubKey);
        qlcAccount.setAddress(address);
        qlcAccount.setIsCurrent(true);
        qlcAccount.setAccountName(QLCAPI.Companion.getQlcWalletName());

        List<QLCAccount> wallets3 = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
        if (wallets3 != null && wallets3.size() != 0) {
            for (int i = 0; i < wallets3.size(); i++) {
                if (wallets3.get(i).getPrivKey().toLowerCase().equals(qlcAccount.getPrivKey())) {
                    ToastUtil.displayShortToast("wallet exist");
                    closeProgressDialog();
                    return;
                }
            }
        }
        AppConfig.instance.getDaoSession().getQLCAccountDao().insert(qlcAccount);
        closeProgressDialog();
        viewModel.walletAddress.postValue(qlcAccount.getAddress());
    }
}
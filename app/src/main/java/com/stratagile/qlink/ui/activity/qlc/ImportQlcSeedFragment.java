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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stratagile.qlc.QLCAPI;
import com.stratagile.qlc.utils.QlcUtil;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.ui.activity.eth.ImportViewModel;
import com.stratagile.qlink.ui.activity.qlc.component.DaggerImportQlcSeedComponent;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcSeedContract;
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcSeedModule;
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcSeedPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qlc.mng.AccountMng;
import qlc.network.QlcException;
import qlc.rpc.AccountRpc;
import qlc.utils.Helper;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/05/21 09:46:27
 */

public class ImportQlcSeedFragment extends BaseFragment implements ImportQlcSeedContract.View {

    @Inject
    ImportQlcSeedPresenter mPresenter;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import_qlc_seed, null);
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


    @Override
    protected void setupFragmentComponent() {
        DaggerImportQlcSeedComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .importQlcSeedModule(new ImportQlcSeedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ImportQlcSeedContract.ImportQlcSeedContractPresenter presenter) {
        mPresenter = (ImportQlcSeedPresenter) presenter;
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
            ToastUtil.displayShortToast(getString(R.string.please_type_seed));
            return;
        }
        if (etPrivateKey.getText().toString().trim().length() != 64) {
            ToastUtil.displayShortToast(getString(R.string.seed_error));
            return;
        }
        showProgressDialog();

        String seed = etPrivateKey.getText().toString().trim();
        JSONObject jsonObject = null;
        try {
            jsonObject = AccountMng.keyPairFromSeed(Helper.hexStringToBytes(seed), 0);
            String priKey = jsonObject.getString("privKey");
            String pubKey = jsonObject.getString("pubKey");
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(seed);
            String mnemonics = AccountRpc.seedToMnemonics(jsonArray);
            String address =  QlcUtil.publicToAddress(pubKey).toLowerCase();
            QLCAccount qlcAccount = new QLCAccount();
            qlcAccount.setPrivKey(priKey.toLowerCase());
            qlcAccount.setMnemonic(mnemonics);
            qlcAccount.setPubKey(pubKey);
            qlcAccount.setAddress(address);
            qlcAccount.setIsCurrent(true);
            qlcAccount.setSeed(seed);
            qlcAccount.setAccountName(QLCAPI.Companion.getQlcWalletName());
            List<QLCAccount> wallets3 = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
            if (wallets3.size() == 0) {
                qlcAccount.setIsAccountSeed(true);
            }

            if (wallets3 != null && wallets3.size() != 0) {
                for (int i = 0; i < wallets3.size(); i++) {
                    if (wallets3.get(i).getAddress().toLowerCase().equals(qlcAccount.getAddress())) {
                        ToastUtil.displayShortToast(getString(R.string.wallet_exist));
                        closeProgressDialog();
                        return;
                    }
                }
            }
            AppConfig.instance.getDaoSession().getQLCAccountDao().insert(qlcAccount);
            closeProgressDialog();
            viewModel.walletAddress.postValue(qlcAccount.getAddress());
        } catch (QlcException e) {
            e.printStackTrace();
            closeProgressDialog();
        }

    }
}
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthMnemonicComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthMnemonicContract;
import com.stratagile.qlink.ui.activity.eth.module.EthMnemonicModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthMnemonicPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.CustomPopWindow;
import com.stratagile.qlink.view.PopWindowUtil;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/22 14:12:37
 */

public class EthMnemonicFragment extends BaseFragment implements EthMnemonicContract.View {

    @Inject
    EthMnemonicPresenter mPresenter;

    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.etMnemonic)
    EditText etMnemonic;
    @BindView(R.id.tvWhatsMnemonic)
    TextView tvWhatsMnemonic;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.llEthType)
    LinearLayout llEthType;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;
    @BindView(R.id.btImport)
    Button btImport;
    @BindView(R.id.tvEthType)
    TextView tvEthType;
    private ImportViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eth_mnemonic, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        viewModel = ViewModelProviders.of(getActivity()).get(ImportViewModel.class);
        return view;
    }

    public static EthMnemonicFragment newInstance(String param) {
        EthMnemonicFragment fragment = new EthMnemonicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerEthMnemonicComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .ethMnemonicModule(new EthMnemonicModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthMnemonicContract.EthMnemonicContractPresenter presenter) {
        mPresenter = (EthMnemonicPresenter) presenter;
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

    @OnClick({R.id.llEthType, R.id.btImport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llEthType:
                PopWindowUtil.showSelectEthTypePopWindow(getActivity(), llEthType, clickListener);
                break;
            case R.id.btImport:
                if ("".equals(etMnemonic.getText().toString())) {
                    ToastUtil.displayShortToast("please type mnemonic");
                    return;
                }
                showProgressDialog();
                Observable.create(new ObservableOnSubscribe<EthWallet>() {
                    @Override
                    public void subscribe(ObservableEmitter<EthWallet> e) throws Exception {
                        EthWallet ethWallet = ETHWalletUtils.importMnemonic(tvEthType.getText().toString(), Arrays.asList(etMnemonic.getText().toString().split(" ")));
                        if (ethWallet == null) {
                            ToastUtil.displayShortToast("import eth wallet error");
                            e.onComplete();
                            return;
                        }
                        if (ethWallet != null) {
                            List<EthWallet> wallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                            for (int i = 0; i < wallets.size(); i++) {
                                if (wallets.get(i).getAddress().equals(ethWallet.getAddress())) {
                                    ToastUtil.displayShortToast("wallet exist");
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
                            AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(ethWallet);
                        }
                        KLog.i(ethWallet.toString());
                        e.onNext(ethWallet);
//                e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<EthWallet>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(EthWallet wallet) {
                                closeProgressDialog();
                                viewModel.walletAddress.postValue(wallet.getAddress());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                closeProgressDialog();
                            }
                        });
                break;
            default:
                break;
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvDefault:
                    tvEthType.setText(ConstantValue.ETH_JAXX_TYPE);
                    break;
                case R.id.tvLedger:
                    tvEthType.setText(ConstantValue.ETH_LEDGER_TYPE);
                    break;
                case R.id.tvCustom:
                    tvEthType.setText(ConstantValue.ETH_CUSTOM_TYPE);
                    break;
                default:
                    break;
            }
            CustomPopWindow.onBackPressed();
        }
    };
}
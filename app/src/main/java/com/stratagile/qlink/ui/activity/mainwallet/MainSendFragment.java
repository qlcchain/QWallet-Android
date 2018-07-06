package com.stratagile.qlink.ui.activity.mainwallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.mainwallet.component.DaggerMainSendComponent;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainSendContract;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainSendModule;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainSendPresenter;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.WalletFragment;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.RxDataTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: $description
 * @date 2018/06/14 09:24:50
 */

public class MainSendFragment extends BaseFragment implements MainSendContract.View {

    @Inject
    MainSendPresenter mPresenter;

    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.paste)
    LinearLayout paste;
    @BindView(R.id.scan)
    LinearLayout scan;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;
    @BindView(R.id.et_qlc_count)
    EditText etQlcCount;
    @BindView(R.id.tv_max_qlc)
    TextView tvMaxQlc;
    @BindView(R.id.cb_qlc)
    CheckBox cbQlc;
    @BindView(R.id.cb_neo)
    CheckBox cbNeo;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_send)
    Button btSend;

    Balance mBalance;

    private boolean isChanged = false;//申明一个flag 来判断是否已经改变

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_fund_layout, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        cbQlc.setChecked(true);
        etQlcCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("".equals(charSequence.toString())) {
                    return;
                }
                if (".".equals(charSequence.toString())) {
                    etQlcCount.setText("");
                    return;
                }
                if ("0".equals(charSequence.toString())) {
                    etQlcCount.setText("");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (isChanged){//必须在修改内容前调用
                        return;
                    }
                    if (editable != null && !"".equals(editable.toString()) && !".".equals(editable.toString())) {
                        int selectionStart;
                        int selectionEnd;
                        selectionStart = etQlcCount.getSelectionStart();
                        selectionEnd = etQlcCount.getSelectionEnd();
                        if (!isOnlyPointNumber(etQlcCount.getText().toString())){
                            if(selectionStart >= 1 && selectionEnd >= selectionStart)
                            {
                                editable.delete(selectionStart - 1, selectionEnd);
                            }
                            isChanged=true;
                            etQlcCount.setText(editable);
                        }
                        float toSendQlcCount = Float.parseFloat(editable.toString());
                        KLog.i(toSendQlcCount);
                        if (toSendQlcCount > Float.parseFloat(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "")) {
                            KLog.i(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                            etQlcCount.setText(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                            isChanged=true;
                            etQlcCount.setSelection((RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "").length());
                        } else {
                            etQlcCount.setSelection(editable.toString().length());
                        }
                        isChanged = false;
                    }
                }catch (Exception e)
                {
                    isChanged=true;
                    etQlcCount.setText("");
                    isChanged = false;
                }
            }
        });
        return view;
    }

    public boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    @Override
    protected void setupFragmentComponent() {
        DaggerMainSendComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .mainSendModule(new MainSendModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MainSendContract.MainSendContractPresenter presenter) {
        mPresenter = (MainSendPresenter) presenter;
    }

    public static MainSendFragment newInstance(String param) {
        MainSendFragment fragment = new MainSendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @see WalletFragment#onGetBalancelSuccess(Balance)
     * @param balance
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refrashNeo(Balance balance) {
        mBalance = balance;
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
    @Override
    public void getScanPermissionSuccess() {
        Intent intent1 = new Intent(getActivity(), ScanQrCodeActivity.class);
        startActivityForResult(intent1, 1);
    }
    /**
     * 校验钱包地址是不是自己的
     */
    private boolean vertifyAddressIsSelf(String address) {
        if (AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress().equals(address)) {
            return true;
        }
        return false;
    }
    private void showSendConfirmDialog(String qlc, String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        btn_cancel.setText(R.string.cancel);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        btn_comfirm.setText(R.string.yes);
        String content = "Are you sure you want to send " + qlc + " QLC to \n" + address + " ?";
        tvContent.setText(content);
        title.setText(getString(R.string.confirm_withdrawal).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                excuteSend(qlc, address);
            }
        });
//        startSpringViewAnimation(view);
        dialog.show();
    }

    private void excuteSend(String qlc, String toaddress) {
        if (toaddress.length() == 34) {
            showProgressDialog();
            List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            Wallet wallet = walletList.get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
            Map<String, Object> infoMap = new HashMap<>();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String uuid1 = uuid.substring(0, 32);
            infoMap.put("recordId", uuid1);
            infoMap.put("type", 1);
            infoMap.put("addressFrom", wallet.getAddress());
            infoMap.put("qlc", qlc);
            infoMap.put("addressTo", toaddress);
            TransactionApi.getInstance().v2TransactionInMain(infoMap, wallet.getAddress(), toaddress, qlc, new SendBackWithTxId() {
                @Override
                public void onSuccess(String txid) {
                    TransactionRecord recordSave = new TransactionRecord();
                    recordSave.setTxid(txid);
                    recordSave.setExChangeId(txid);
                    recordSave.setTransactiomType(1);
                    recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                    AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                    closeProgressDialog();
                    showSendDialog();
//                    setToRoot();
                }

                @Override
                public void onFailure() {
                    ToastUtil.displayShortToast(getString(R.string.send_failure));
                    closeProgressDialog();
                }
            });

        } else {
            ToastUtil.displayShortToast(getString(R.string.wallet_address_error));
        }
    }

    private void showSendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.only_ok_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(getString(R.string.withdrawal_successful).toUpperCase());
        tvContent.setText(R.string.Take_a_rest_Your_transfer_request_is_being_processed);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (address != null) {
                address.setText(data.getStringExtra("result"));
            }
        }
    }
    @OnClick({R.id.paste, R.id.scan, R.id.tv_max_qlc, R.id.bt_back, R.id.bt_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.paste:
                try {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将ClipData内容放到系统剪贴板里。
                    address.setText(cm.getPrimaryClip().getItemAt(0).getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.scan:
                mPresenter.getScanPermission();
                break;
            case R.id.tv_max_qlc:
                etQlcCount.setText(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                break;
            case R.id.bt_back:
                getActivity().finish();
                break;
            case R.id.bt_send:
                if (address.getText().toString().trim().equals("")) {
                    return;
                }
                if (etQlcCount.getText().toString().trim().equals("")) {
                    return;
                }
                if (Float.parseFloat(etQlcCount.getText().toString()) <= 0) {
                    return;
                }

                if (vertifyAddressIsSelf(address.getText().toString())) {
                    ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self));
                    return;
                }

                showSendConfirmDialog(etQlcCount.getText().toString().trim(), address.getText().toString());
                break;
        }
    }
}